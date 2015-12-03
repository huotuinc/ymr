package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.*;
import com.huotu.ymr.service.CrowdFoundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/app")
public class CrowdFundingController implements CrowdFundingSystem {

    @Autowired
    CrowdFoundingService crowdFoundingService;

    @RequestMapping("/getCrowdFundingList")
    @Override
    public ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel> list, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/getCrowFindingInfo")
    @Override
    public ApiResult getCrowFindingInfo(Output<AppCrowdFundingModel> data, Long id) throws Exception {
        return null;
    }

    @RequestMapping("/raiseBooking")
    @Override
    public ApiResult raiseBooking(Double money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/pay")
    @Override
    public ApiResult pay(Integer type, Double money) throws Exception {
        return null;
    }

    @RequestMapping("/callBackWeiXin")
    @Override
    public ApiResult callBackWeiXin() throws Exception {
        return null;
    }

    @RequestMapping("/callBackAlipay")
    @Override
    public ApiResult callBackAlipay() throws Exception {
        return null;
    }

    @RequestMapping("/getBookingList")
    @Override
    public ApiResult getBookingList(Output<AppBookingListModel> list, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/raiseCooperation")
    @Override
    public ApiResult raiseCooperation(Double money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/getRaiseCooperationList")
    @Override
    public ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel> list, String key, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/goCooperation")
    @Override
    public ApiResult goCooperation(Double money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/raiseSubscription")
    @Override
    public ApiResult raiseSubscription(Double money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/getSubscriptionList")
    @Override
    public ApiResult getSubscriptionList(Output<AppSubscriptionListModel[]> list,Long crowdId, Long lastId) throws Exception {

        if(lastId==null){
            lastId=crowdFoundingService.getMaxId()+1;
        }//如果为null则默认第一页
        int number=3; //todo 单页表格的数据个数
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFoundingService.findCrowdListFromLastIdWithNumber(crowdId, lastId, number);
        List<AppSubscriptionListModel> appSubscriptionListModelList=new ArrayList<AppSubscriptionListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppSubscriptionListModel appSubscriptionListModel=new AppSubscriptionListModel();
            appSubscriptionListModel.setTime(crowdFundingPublic.getTime());
            appSubscriptionListModel.setMoney(crowdFundingPublic.getMoney());
            appSubscriptionListModel.setName(crowdFundingPublic.getName());
            appSubscriptionListModel.setStatus(crowdFundingPublic.getStatus());
            appSubscriptionListModel.setPid(crowdFundingPublic.getId());
            appSubscriptionListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appSubscriptionListModelList.add(appSubscriptionListModel);
        }
        list.outputData(appSubscriptionListModelList.toArray(new AppSubscriptionListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
