package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.repository.ShareRepository;
import com.huotu.ymr.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * �������¹���
 * Created by slt on 2015/12/1.
 */
@Service
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareRepository shareRepository;
    @Override
    public List<Share> findShareList(String key,Long lastId,int pageSize) throws Exception{
        StringBuilder hql = new StringBuilder();
        //���ҵļ�¼�Ƿ��㹻
        boolean isEnough=true;
        //�жϲ����ö��ķ�������ͨ�ķ���trueΪ�ö�
        Boolean findWho=true;
        hql.append("select s from Share as s where s.status=true ");
        if(!StringUtils.isEmpty(key)){
            hql.append(" and s.title like :key ");
        }

        if(lastId==0){//û��lastId(�ӵ�һҳ��ʼ��)
            hql.append("order by s.top desc,s.id desc");

        }else {//��lastId��ʼ��
            hql.append(" and s.top=:isTop ");
            hql.append(" and s.id<:lastId ");
            hql.append("order by s.id desc");
            Share share=shareRepository.findOne(lastId);
            if(share!=null){
                if(!share.getTop()){
                    findWho=false;
                }
            }
        }
        final boolean finalFindWho=findWho;
        List list = shareRepository.queryHql(hql.toString(),query -> {
            if(!StringUtils.isEmpty(key)){
                query.setParameter("key","%"+key+"%");
            }
            if(lastId!=0){
                query.setParameter("lastId", lastId);
                query.setParameter("isTop",finalFindWho);
            }
            query.setMaxResults(pageSize);
        });

        //top��ʼû��ȫ��
        if(lastId!=0 && findWho && list.size()<pageSize){
            String otherHql=hql.toString().replaceAll("and s.id<:lastId"," ");
            Share data=(Share)list.get(list.size() - 1);
            List otherList = shareRepository.queryHql(otherHql,query -> {
                if(!StringUtils.isEmpty(key)){
                    query.setParameter("key","%"+key+"%");
                }
                    query.setParameter("isTop",false);
                query.setMaxResults(pageSize-list.size());
            });
            list.addAll(otherList);
        }

        List<Share> shares= new ArrayList<>();
        list.forEach(object -> {
            Object data = (Object) object;
            if (data!=null){
                shares.add((Share)data);
            }
        });
        return shares;
    }
}
