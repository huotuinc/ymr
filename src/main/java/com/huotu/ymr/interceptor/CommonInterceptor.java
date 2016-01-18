/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.interceptor;


import com.alibaba.fastjson.JSON;
import com.huotu.ymr.common.PublicParameterHolder;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppPublicModel;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * app通用监听 sign验证
 */
public class CommonInterceptor implements HandlerInterceptor {

    private static final Log log = LogFactory.getLog(CommonInterceptor.class);

    @Autowired
    private CommonConfigService commonConfigService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    private UserService userService;


    /**
     * 该方法只有返回true，后面的Controller才会被继续执行
     * 逻辑处理：首先验证签名是否有效如果未通过签名验证则返回app错误信息。
     *          签名验证通过之后更新AppPublicModel的信息
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sign = request.getParameter("sign");
        if (sign == null || !sign.equals(getSign(request))) {
            PhysicalApiResult result = new PhysicalApiResult();
            result.setSystemResultCode(1);
            result.setResultCode(50601);
            result.setResultDescription("系统请求失败");
            PrintWriter out = null;
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                out = response.getWriter();
                out.append(JSON.toJSONString(result));
            } finally {
                if (out != null) {
                    out.close();
                }
            }

            return false;
        }

        AppPublicModel appPublicModel = initPublicParam(request);

        PublicParameterHolder.put(appPublicModel);

        return true;
    }

    private AppPublicModel initPublicParam(HttpServletRequest request) throws IOException {

        AppPublicModel model = new AppPublicModel();
        String sign = StringUtils.isEmpty(request.getParameter("sign")) ? "" : request.getParameter("sign");
        long timestamp = StringUtils.isEmpty(request.getParameter("timestamp")) ? 0 : Long.parseLong(request.getParameter("timestamp"));
        String version = StringUtils.isEmpty(request.getParameter("version")) ? "" : request.getParameter("version");
        String operation = StringUtils.isEmpty(request.getParameter("operation")) ? "" : request.getParameter("operation");
        String imei = StringUtils.isEmpty(request.getParameter("imei")) ? "" : request.getParameter("imei");
        String token = StringUtils.isEmpty(request.getParameter("token")) ? "" : request.getParameter("token");
        if (!StringUtils.isEmpty(token)) {
            //根据token查找User
            User user = userRepository.findByToken(token);
            if (user != null) {
                AppUserInfoModel appUserInfoModel = new AppUserInfoModel();
                appUserInfoModel.setUserId(user.getId());
                appUserInfoModel.setUserLevel(user.getUserLevel());
                appUserInfoModel.setScore(user.getScore());
                appUserInfoModel.setToken(token);
                //根据UserId去数据中心获取该用户的其他相关信息，包括微信头像，昵称等
                MallUserModel mallUserModel = dataCenterService.getUserInfoByUserId(user.getId());
                if (mallUserModel != null) {
                    appUserInfoModel.setName(mallUserModel.getName());
                    appUserInfoModel.setNickName(mallUserModel.getNickName());
                    appUserInfoModel.setHeadUrl(mallUserModel.getHeadUrl());
                    appUserInfoModel.setSex(mallUserModel.getSex());
                    appUserInfoModel.setMerchantId(mallUserModel.getMerchantId());
                    appUserInfoModel.setIsBindMobile(mallUserModel.getIsBindMobile());
                    appUserInfoModel.setUserName(mallUserModel.getUserName());
                    appUserInfoModel.setMobile(mallUserModel.getMobile());
                    model.setCurrentUser(appUserInfoModel);
                }
            }
        }
        model.setSign(sign);
        model.setTimestamp(timestamp);
        model.setVersion(version);
        model.setOperation(operation);
        model.setImei(imei);
        return model;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        logger.debug("==============执行顺序: 2、postHandle================");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.debug("==============执行顺序: 3、afterCompletion================");

    }

    private String getSign(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> resultMap = new TreeMap<String, String>();
        resultMap.put("appSecret", commonConfigService.getAppKeySecret());
        Map map = request.getParameterMap();
        for (Object key : map.keySet()) {
            resultMap.put(key.toString(), request.getParameter(key.toString()));
        }

        StringBuilder strB = new StringBuilder();
        resultMap.keySet().stream().filter(key -> !"sign".equals(key)).forEach(key -> strB.append(resultMap.get(key)));
//        System.out.println(strB.toString());
        return DigestUtils.md5DigestAsHex(strB.toString().getBytes("UTF-8")).toLowerCase();
    }


}
