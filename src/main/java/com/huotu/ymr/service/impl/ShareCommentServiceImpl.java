package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.repository.ShareCommentRepository;
import com.huotu.ymr.service.ShareCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务
 * Created by slt on 2015/12/1.
 */
@Service
public class ShareCommentServiceImpl implements ShareCommentService {
    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Override
    public List<ShareComment> findShareComment(Long shareId,Long lastId,Integer pageSize) throws Exception {
        List<ShareComment> list= shareCommentRepository.findByShareOrderByTime(shareId, lastId, new PageRequest(0,pageSize));
//        Map<Long,List<ShareComment>> map=new TreeMap<Long,List<ShareComment>>(new Comparator<Long>() {
//            @Override
//            public int compare(Long o1, Long o2) {
//                return o2-o1>0?1:-1;
//            }
//        });
//        //先把评论放到map中去
//        for(ShareComment sc:list){
//            if(sc.getParentId()==0L){
//                List<ShareComment> comments=new ArrayList<>();
//                comments.add(sc);
//                map.put(sc.getId(),comments);
//            }
//        }
//
//        //再把回复放到map中去，回复放到对应的评论中去
//        for(ShareComment sc:list){
//            if(sc.getParentId()!=0L){
//                //获取父
//                String commentIdStr=sc.getCommentPath().split("\\|")[1];
//                if(!StringUtils.isEmpty(commentIdStr)){
//                    Long commentId=Long.parseLong(commentIdStr);
//                    //获取父评论的回复列表
//                    List<ShareComment> shareComments=map.get(commentId);
//                    if(Objects.isNull(shareComments)){
//                        shareComments=new ArrayList<>();
//                    }
//                    shareComments.add(sc);
//                    map.put(commentId,shareComments);
//                }
//            }
//        }
        return list;
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
