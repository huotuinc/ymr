package com.huotu.ymr.service;

import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.model.AppUserShareCommentModel;

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
     * @param shareCommentPath    评论的路径
     * @throws Exception
     */
    void deleteComment(String shareCommentPath) throws Exception;

    /**
     * 获取该用户的评论过的文章
     * @param userId            用户ID
     * @param lastId            最后一条评论的ID
     * @return
     * @throws Exception
     */
    List<ShareComment> findCommentList(Long userId,Long lastId) throws Exception;


    /**
     * 获取我的文章被别人评论的记录
     * @param userId
     * @param lastId
     * @return
     * @throws Exception
     */
    List<ShareComment> findBeCommentedList(Long userId,Long lastId) throws Exception;


    /**
     * 将评论转换为model
     * @return
     * @throws Exception
     */
    AppUserShareCommentModel getCommentToModel(ShareComment shareComment) throws Exception;




}
