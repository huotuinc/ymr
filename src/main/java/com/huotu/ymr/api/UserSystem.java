package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户系统接口
 * Created by lgh on 2015/11/26.
 */

public interface UserSystem {

    /**
     * 系统初始化
     * @param global 全局数据
     * @param user 用户数据
     * @param update 升级数据
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult init(
            Output<AppGlobalModel> global,
            Output<AppUserInfoModel> user,
            Output<AppUpdateModel> update
    ) throws Exception;


    /**
     * 用户登录
     * 微信授权成功
     * <p>
     * 微信授权失败
     * <p>
     * 此接口数据来源为商城数据库
     *
     * @param data    用户数据
     * @param unionId 微信唯一号
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult login(Output<AppUserInfoModel> data,Output<AppSimpleUserModel[]> list, String unionId) throws Exception;


    @RequestMapping(method = RequestMethod.GET)
    ApiResult selectOneUser(Output<AppUserInfoModel> data,Long userId) throws Exception;


    /**
     * 微信授权失败调用的手机号登录
     *
     * @param data  返回的用户数据
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult loginByMobile(Output<AppUserInfoModel> data, String phone,String code) throws Exception;


    /**
     * 获取验证码
     * <p>
     * <b>负责人： </b>
     *
     * @param phone String(11)
     * @param type  场景 0：注册，1：绑定手机
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult sendSMS(String phone, int type) throws Exception;

    /**
     * 绑定手机
     * 前置条件 没有绑定手机
     *
     * @param code  验证码
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult bindMobile(String code, String phone) throws Exception;

    /**
     * 修改手机号
     *
     * @param code  验证码
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult modifyMobile(String code, String phone) throws Exception;

    /**
     * 积分充值到小金库
     * 前置条件：用户当前剩余积分大于0
     * 处理过程: 1.充钱到小金库 2.扣除用户积分 3.记录用户积分流水
     * 要求：3个过程需要同步处理
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult putToCoffers() throws Exception;

    /**
     * 获取积分充值所需的信息
     * 处理过程：根据累计积分和升级到2级所需的积分差额，及金额积分兑换比例 计算出等级升级所需金额
     *
     * @param upgradeMoney 等级升级所需金额
     * @param rate         金额兑换积分比例 0.1 (1元对10积分)
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getScorePutInfo(Output<Double> upgradeMoney, Output<Double> rate) throws Exception;


    /**
     * 进行充值积分
     * 在选择支付方式跳转到支付宝之前，app先请求服务器并获得订单号。
     *
     * @param money 升级所需金额
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult put(Output<String> orderNo, Double money) throws Exception;


    /**
     * 更新用户个人信息
     *
     * @param data
     * @param profileType 1 姓名 2 性别 (值根据商城来定) 3 定位
     * @param profileData 1 String 2 Integer 3 Integer
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult updateUserProfile(Output<AppUserInfoModel> data, @RequestParam(required = true) Integer profileType, Object profileData) throws Exception;


    /**
     * 获取系统消息
     *
     * @param messages
     * @param lastId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult messages(Output<AppMessageModel[]> messages, Long lastId) throws Exception;


    /**
     * 获取众筹列表
     *
     * @param list
     * @param lastId 上一页最后一个Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getMyCrowdFundingList(Output<AppCrowdFundingListModel[]> list, Long lastId) throws Exception;

    /**
     * <p>无需登录</p>
     * app请求设置该设备(imei)的deviceToken
     *
     * @param deviceToken ios deviceToken的16位编码小写；android 极光推送的设备别名
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult updateDeviceToken(String deviceToken) throws Exception;

}
