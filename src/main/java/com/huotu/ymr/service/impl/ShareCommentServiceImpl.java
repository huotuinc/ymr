package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.repository.ShareCommentRepository;
import com.huotu.ymr.service.ShareCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 评论服务
 * Created by slt on 2015/12/1.
 */
@Service
public class ShareCommentServiceImpl implements ShareCommentService {
    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Override
    public Map<Long,List<ShareComment>> findShareComment(Long shareId,Long lastId,Integer pageSize) throws Exception {
        List<ShareComment> list= shareCommentRepository.findByShareOrderByTime(shareId, lastId, new PageRequest(0,pageSize));
        Map<Long,List<ShareComment>> map=new TreeMap<Long,List<ShareComment>>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2-o1>0?1:-1;
            }
        });
        for(ShareComment sc:list){
            if(sc.getParentId()==0L){
                List<ShareComment> comments=new ArrayList<>();
                comments.add(sc);
                map.put(sc.getId(),comments);
            }else {
                String commentIdStr=sc.getCommentPath().split("\\|")[1];
                if(!StringUtils.isEmpty(commentIdStr)){
                    Long commentId=Long.parseLong(commentIdStr);
                    List<ShareComment> shareComments=map.get(commentId);
                    shareComments.add(sc);
                    map.put(commentId,shareComments);
                }
            }
        }
        return map;
    }

    @Override
    public ShareComment saveShareComment(ShareComment shareComment) throws Exception {
        return shareCommentRepository.save(shareComment);
    }

    @Override
    public ShareComment findOneShareComment(Long shareCommentId) throws Exception {
        return shareCommentRepository.findOne(shareCommentId);
    }

    @Override
    public void deleteComment(Long shareCommentId) throws Exception {
        shareCommentRepository.deleteComment("|"+shareCommentId+"|");
    }
}
