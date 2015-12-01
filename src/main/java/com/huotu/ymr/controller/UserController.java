package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.model.AppUserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ViewResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
        return null;
    }

    @RequestMapping("/bindMobile")
    @Override
    public ApiResult bindMobile(String code, String phone) throws Exception {
        return null;
    }

//    @Autowired
//    private ViewResolver viewResolver;

//    @RequestMapping("/index")
//    public String index(Locale locale, Model model) throws Exception {
//        model.addAttribute("a", "ni hao");
//        System.out.println("enter");
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("a", "hao");
////        return new  ModelAndView("test", map);
//        return "test";
////        return viewResolver.resolveViewName("test",null);
//    }
}
