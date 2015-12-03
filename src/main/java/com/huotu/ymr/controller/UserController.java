package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.model.AppUserInfoModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lgh on 2015/11/20.
 */
@Controller
@RequestMapping("/app")
public class UserController implements UserSystem {

    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, Integer unionId) throws Exception {

        return null;
    }

    @RequestMapping("/sendSMS")
    @Override
    public ApiResult sendSMS(String phone, int type, @RequestParam(required = false) Integer codeType) throws Exception {
//        int checkCode=sendPhoneMessage();
//        if(codeType)
//        if (RegexHelper.IsValidMobileNo(phone)) return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);//用户错误的手机格式
        return null;
    }

    @RequestMapping("/bindMobile")
    @Override
    public ApiResult bindMobile(String code, String phone) throws Exception {
        return null;
    }
}
