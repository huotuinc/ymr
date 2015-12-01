package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ShareSystem;
import com.huotu.ymr.model.AppShareCommentListModel;
import com.huotu.ymr.model.AppShareInfoModel;
import com.huotu.ymr.model.AppShareListModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/app")
public class ShareController implements ShareSystem {

    @RequestMapping("/searchShareList")
    @Override
    public ApiResult searchShareList(Output<AppShareListModel> list, String key, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/share")
    @Override
    public ApiResult share(String title, String content) throws Exception {
        return null;
    }

    @RequestMapping("/getShareInfo")
    @Override
    public ApiResult getShareInfo(Output<AppShareInfoModel> data, String key, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/searchShareCommentList")
    @Override
    public ApiResult searchShareCommentList(Output<AppShareCommentListModel> list, Long lastId) throws Exception {
        return null;
    }
}
