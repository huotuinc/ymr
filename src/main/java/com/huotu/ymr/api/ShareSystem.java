package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.AppShareCommentListModel;
import com.huotu.ymr.model.AppShareInfoModel;
import com.huotu.ymr.model.AppShareListModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 爱分享系统接口
 * Created by lgh on 2015/11/27.
 */
public interface ShareSystem {

    /**
     * 爱分享列表
     *
     * @param list
     * @param key    搜索关键字 按标题搜索
     * @param lastId 上一页的最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult searchShareList(Output<AppShareListModel[]> list, String key, Long lastId) throws Exception;

    /**
     * 添加爱分享内容
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult addShare(String title, String content,String imgUrl,Long userId) throws Exception;

    /**
     * 获取爱分享详情
     *
     * @param data      爱分享详情
     * @param shareId   爱分享文章ID
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getShareInfo(Output<AppShareInfoModel> data, Long shareId) throws Exception;

    /**
     * 点赞或取消点赞
     * @param shareId   文章ID
     * @param userId    用户ID
     * @param type      类型，-1:取消点赞，1：点赞
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult clickPraise(Long shareId,Long userId,Integer type) throws Exception;


    /**
     * 爱分享评论列表

     * @param list      输出给app的评论列表
     * @param shareId   爱分享文章ID
     * @param lastId    最后一条评论的id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult searchShareCommentList(Output<AppShareCommentListModel[]> list,Long shareId,Long lastId) throws Exception;

    /**
     * 添加评论
     * @param userId    用户ID
     * @param shareId   文章ID
     * @param content   评论内容
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult addComment(Long userId,Long shareId,String content) throws Exception;

    /**
     * 添加回复
     * @param userId    用户ID
     * @param parentId  被评论的ID
     * @param content   回复内容
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult addReply(Long userId,Long parentId,String content) throws Exception;

}
