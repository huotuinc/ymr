package com.huotu.ymr.service;

import com.huotu.ymr.entity.ShareComment;

import java.util.List;

/**
 * 评论管理
 * Created by slt on 2015/12/3.
 */
public interface ShareCommentService {
    /**
     * 找出某个分享文章下面的所有的评论
     * @param  shareId  爱分享文章ID
     * @param  lastId   最后一条评论ID
     * @param  pageSize 每页显示几条
     * @return          评论列表
     * @throws Exception
     */
    List<ShareComment> findShareComment(Long shareId,Long lastId,Integer pageSize) throws Exception;

    ShareComment saveShareComment(ShareComment shareComment) throws Exception;

    ShareComment findOneShareComment(Long shareCommentId) throws Exception;

    /**
     * 删除评论
     * @param shareCommentId    评论的ID
     * @throws Exception
     */
    void deleteComment(Long shareCommentId) throws Exception;




}
