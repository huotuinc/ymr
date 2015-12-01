/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.base;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


public class DeviceRequestBuilder {

    private final String api;
    private final HttpMethod method;
    private final Map<String, String> parameters = new HashMap();

    public DeviceRequestBuilder(String api, HttpMethod method) {
        super();
        this.api = api;
        this.method = method;
    }

    public DeviceRequestBuilder param(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public MockHttpServletRequestBuilder build() throws UnsupportedEncodingException {
        MockHttpServletRequestBuilder url;
        if (method == HttpMethod.GET) {
            url = MockMvcRequestBuilders.get("/app/" + api);
        } else if (method == HttpMethod.POST) {
            url = MockMvcRequestBuilders.post("/app/" + api);
        } else
            throw new IllegalArgumentException("无效的请求" + method);

        Map<String, String> toSign = new HashMap();

        toSign.putAll(parameters);
        toSign.put("appSecret", "1165a8d240b29af3f418b8d10599d0da");

        TreeSet<String> keys = new TreeSet();

        keys.addAll(toSign.keySet());

        StringBuilder buffer = new StringBuilder();
        for (String name : keys) {
            buffer.append(toSign.get(name));
        }

        String sign = DigestUtils.md5DigestAsHex(buffer.toString().getBytes("UTF-8"));

        url.param("sign", sign.toLowerCase());
        for (String name : parameters.keySet()) {
            url.param(name, parameters.get(name));
        }

        return url;
    }

//MockHttpServletRequestBuilder

}
