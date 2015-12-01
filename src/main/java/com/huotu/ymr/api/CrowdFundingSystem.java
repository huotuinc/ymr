package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 合伙人系统接口
 * Created by lgh on 2015/11/27.
 */
public interface CrowdFundingSystem {

    /**
     * 获取众筹列表
     *
     * @param list
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel> list, Long lastId) throws Exception;

    /**
     * 众筹详情
     *
     * @param data
     * @param id   众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getCrowFindingInfo(Output<AppCrowdFundingModel> data, Long id) throws Exception;

    /**
     * 发起预约
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseBooking(Float money, String name, String phone, String remark) throws Exception;

    /**
     * 支付金额
     * 支持支付宝和微信
     * 记录一条支付订单
     *
     * @param type  类型 1支付宝2微信
     * @param money 金额
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult pay(Integer type, Float money) throws Exception;

    /**
     * 支付回调(微信) todo 待完善
     * 确认支付订单 记录支付流水
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult callBackWeiXin() throws Exception;


    /**
     * 支付回调(支付宝)  todo 待完善
     * 确认支付订单 记录支付流水
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult callBackAlipay() throws Exception;

    /**
     * 获取预约人列表
     *
     * @param list
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getBookingList(Output<AppBookingListModel> list, Long lastId) throws Exception;

    /**
     * 发起合作
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseCooperation(Float money, String name, String phone, String remark) throws Exception;


    /**
     * 搜索合作发起人列表
     *
     * @param list
     * @param key    搜索关键字
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel> list, String key, Long lastId) throws Exception;

    /**
     * 合作
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult goCooperation(Float money, String name, String phone, String remark) throws Exception;


    /**
     * 发起认购
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseSubscription(Float money, String name, String phone, String remark) throws Exception;

    /**
     * 获取认购列表
     *
     * @param list
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getSubscriptionList(Output<AppSubscriptionListModel> list, Long lastId) throws Exception;


}
