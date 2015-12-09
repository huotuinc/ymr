package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.AppGlobalModel;
import com.huotu.ymr.model.AppUpdateModel;
import com.huotu.ymr.model.AppUserInfoModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户系统接口
 * Created by lgh on 2015/11/26.
 */

public interface UserSystem {

    @RequestMapping(method = RequestMethod.GET)
    ApiResult init(
            Output<AppGlobalModel> global,
            Output<AppUserInfoModel> user,
            Output<AppUpdateModel> update
    ) throws Exception;


    /**
     * 用户登录
     * 微信授权成功
     *
     * 微信授权失败
     *
     * 此接口数据来源为商城数据库
     *
     * @param data    用户数据
     * @param unionId 微信唯一号
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult login(Output<AppUserInfoModel> data, Integer unionId) throws Exception;


    /**
     * 获取验证码
     * <p>
     * <b>负责人： </b>
     *
     * @param phone String(11)
     * @param type  类型 1：绑定手机
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult sendSMS(String phone,
                      int type, @RequestParam(required = false) Integer codeType
    ) throws Exception;

    /**
     * 绑定手机
     * 前置条件 没有绑定手机
     *
     * @param code
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult bindMobile(String code, String phone) throws Exception;

    /**
     * 修改手机号
     *
     * @param code
     * @param phone
     * @return
     * @throws Exception
     */
    ApiResult modifyMobile(String code, String phone) throws  Exception;

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
