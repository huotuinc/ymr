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
import com.huotu.huobanplus.sis.common.PublicParameterHolder;
import com.huotu.huobanplus.sis.model.app.AppSisPublicModel;
import com.huotu.huobanplus.sis.service.CommonConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private String appSecret = "";//

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        logger.debug("==============执行顺序: 0、preHandle================");

        String sign = request.getParameter("sign");

        //logger.info("sign:" + getSign(request));
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

        AppSisPublicModel appPublicModel = initPublicParam(request);
        PublicParameterHolder.put(appPublicModel);

        return true;
    }

    private AppSisPublicModel initPublicParam(HttpServletRequest request) {

        AppSisPublicModel model = new AppSisPublicModel();
        String sign = StringUtils.isEmpty(request.getParameter("sign")) ? "" : request.getParameter("sign");
        long timestamp = StringUtils.isEmpty(request.getParameter("timestamp")) ? 0 : Long.parseLong(request.getParameter("timestamp"));
        String appid = StringUtils.isEmpty(request.getParameter("appid")) ? "" : request.getParameter("appid");
        String version = StringUtils.isEmpty(request.getParameter("version")) ? "" : request.getParameter("version");
        String operation = StringUtils.isEmpty(request.getParameter("operation")) ? "" : request.getParameter("operation");

        model.setSign(sign);
        model.setTimestamp(timestamp);
        model.setAppid(appid);
        model.setVersion(version);
        model.setOperation(operation);

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
        Map map = request.getParameterMap();
        for (Object key : map.keySet()) {
            resultMap.put(key.toString().toLowerCase(), request.getParameter(key.toString()));
        }

        StringBuilder strB = new StringBuilder();

        resultMap.keySet().stream().filter(key -> !"sign".equals(key)).forEach(key -> strB.append("&" + key + "=" + resultMap.get(key)));

        String result = strB.toString().substring(1) + commonConfigService.getAppKeySecret();

        log.info(result);
        return DigestUtils.md5DigestAsHex(result.getBytes("UTF-8")).toLowerCase();
    }


}
