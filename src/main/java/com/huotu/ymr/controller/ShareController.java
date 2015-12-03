package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ShareSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.model.AppShareCommentListModel;
import com.huotu.ymr.model.AppShareInfoModel;
import com.huotu.ymr.model.AppShareListModel;
import com.huotu.ymr.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by lgh on 2015/12/1.
 * Modify by slt on 2015/12/2
 */
@Controller
@RequestMapping("/app")
public class ShareController implements ShareSystem {
    @Autowired
    ShareService shareService;

    @RequestMapping("/searchShareList")
    @Override
    public ApiResult searchShareList(Output<AppShareListModel[]> list, String key, Long lastId) throws Exception {
        if(lastId==null){
            lastId=0L;
        }
        List<Share> shares=shareService.findShareList(key,lastId,10);//todo ÄÚ²¿Âß¼­
        if(shares!=null) {
            AppShareListModel[] appShareListModels = new AppShareListModel[shares.size()];
            for (int i = 0; i < shares.size(); i++) {
                Share share=shares.get(i);
                AppShareListModel appShareListModel=new AppShareListModel();
                appShareListModel.setPId(share.getId());
                appShareListModel.setTitle(share.getTitle());
                appShareListModel.setComment(share.getComment());
                appShareListModel.setContent(share.getContent());
                appShareListModel.setPraise(share.getPraise());
                appShareListModel.setRelay(share.getRelay());
                appShareListModel.setRelayScore(share.getRelayReward());
                appShareListModel.setStatus(share.getStatus());
                appShareListModel.setTime(share.getTime());
                appShareListModel.setTop(share.getTop());
                appShareListModel.setUserHeadUrl(share.getLinkUrl());
                appShareListModels[i]=appShareListModel;
            }
            list.outputData(appShareListModels);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/share")
    @Override
    public ApiResult share(String title, String content) throws Exception {
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/getShareInfo")
    @Override
    public ApiResult getShareInfo(Output<AppShareInfoModel> data, String key, Long lastId) throws Exception {
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/searchShareCommentList")
    @Override
    public ApiResult searchShareCommentList(Output<AppShareCommentListModel> list, Long lastId) throws Exception {
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
