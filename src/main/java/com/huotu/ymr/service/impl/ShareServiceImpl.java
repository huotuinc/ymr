package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.repository.ShareRepository;
import com.huotu.ymr.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享文章管理
 * Created by slt on 2015/12/1.
 */
@Service
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareRepository shareRepository;
    @Override
    public List<Share> findAppShareList(String key,Long lastId,int pageSize) throws Exception{
        StringBuilder hql = new StringBuilder();
        //查找的记录是否足够
        boolean isEnough=true;
        //判断查找置顶的分享还是普通的分享，true为置顶
        Boolean findWho=true;
        hql.append("select s from Share as s where s.status=true and s.checkStatus=1 ");
        if(!StringUtils.isEmpty(key)){
            hql.append(" and s.title like :key ");
        }

        if(lastId==0){//没有lastId(从第一页开始查)
            hql.append("order by s.top desc,s.id desc");

        }else {//从lastId开始查
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

        //top开始没找全的
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

    @Override
    public Share findOneShare(Long shareId) throws Exception {
        return shareRepository.findOne(shareId);
    }

    @Override
    public Share addShare(Share share) throws Exception {
        return shareRepository.save(share);
    }

    @Override
    public Page<Share> findPcShareList(String key, String keyType, Integer pageNo, Integer pageSize) throws Exception {
        return  shareRepository.findAll(new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(key)||key==null) {
                    return null;
                }
                return cb.like(root.get("title").as(String.class),"%"+key+"%");
            }
        },new PageRequest(pageNo, pageSize));
    }
}
