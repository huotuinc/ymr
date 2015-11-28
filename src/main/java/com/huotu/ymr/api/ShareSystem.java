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
    ApiResult searchShareList(Output<AppShareListModel> list, String key, Long lastId) throws Exception;

    /**
     * 分享内容
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult share(String title, String content) throws Exception;

    /**
     * 分享详情
     *
     * @param data
     * @param key
     * @param lastId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getShareInfo(Output<AppShareInfoModel> data, String key, Long lastId) throws Exception;


    /**
     * 分享评论列表
     *
     * @param list
     * @param lastId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult searchShareCommentList(Output<AppShareCommentListModel> list, Long lastId) throws Exception;
}
