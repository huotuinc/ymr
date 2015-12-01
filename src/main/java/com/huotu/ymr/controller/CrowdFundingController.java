package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/app")
public class CrowdFundingController implements CrowdFundingSystem {

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
    public ApiResult raiseBooking(Float money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/pay")
    @Override
    public ApiResult pay(Integer type, Float money) throws Exception {
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
    public ApiResult raiseCooperation(Float money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/getRaiseCooperationList")
    @Override
    public ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel> list, String key, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/goCooperation")
    @Override
    public ApiResult goCooperation(Float money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/raiseSubscription")
    @Override
    public ApiResult raiseSubscription(Float money, String name, String phone, String remark) throws Exception {
        return null;
    }

    @RequestMapping("/getSubscriptionList")
    @Override
    public ApiResult getSubscriptionList(Output<AppSubscriptionListModel> list, Long lastId) throws Exception {
        return null;
    }
}
