package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.AppUserInfoModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户系统接口
 * Created by lgh on 2015/11/26.
 */

public interface UserSystem {
    /**
     * 用户登录
     * 存在，直接登录
     * 不存在，创建新的用户
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
     *
     * @param code
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult bindMobile(String code, String phone) throws Exception;

    /**
     * @param data        用户数据
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult modifyPassword(Output<AppUserInfoModel> data, String oldPassword, String newPassword) throws Exception;

}
