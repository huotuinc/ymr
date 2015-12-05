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
     * @param crowdId 众筹项目id
     * @param userId 预约用户id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseBooking(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception;

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
    ApiResult pay(Integer type, Double money) throws Exception;

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
     * @param crowdId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getBookingList(Output<AppBookingListModel[]> list, Long lastId,Long crowdId) throws Exception;

    /**
     * 发起合作
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @param crowdId 众筹项目id
     * @param userId 认购人id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseCooperation(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception;


    /**
     * 搜索合作发起人列表
     *
     * @param list
     * @param key    搜索关键字
     * @param lastId 上一页最后一个Id
     * @param crowdId  众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel[]> list, String key, Long lastId,Long crowdId) throws Exception;

    /**
     * 合作
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @param crowdId 众筹id
     * @param crowdPublicId 合作发起人id
     * @param userId 合作者的id
     * @returnid
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult goCooperation(Double money, String name, String phone, String remark,Long crowdId,Long crowdPublicId,Long userId) throws Exception;


    /**
     * 发起认购
     *
     * @param money  金额
     * @param name   姓名
     * @param phone  联系电话
     * @param remark 备注
     * @param crowdId 众筹项目id
     * @param userId 认购人id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseSubscription(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception;

    /**
     * 获取认购列表
     *
     * @param list
     * @param crowdId 所属的众筹项目Id
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getSubscriptionList(Output<AppSubscriptionListModel[]> list, Long crowdId,Long lastId) throws Exception;


}
