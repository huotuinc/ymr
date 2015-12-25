/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/17.
 */
public class HttpHelper {
    /**
     * http post请求
     *
     * @param url   请求的URL
     * @param data  参数
     * @return
     * @throws IOException
     */
    public static String postRequest(String url, Map<String, String> data) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        BasicNameValuePair[] basicNameValuePairs = new BasicNameValuePair[data.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            basicNameValuePairs[i] = new BasicNameValuePair(entry.getKey(), entry.getValue());
            i++;
        }
        post.setEntity(
                EntityBuilder.create()
                        .setContentEncoding("UTF-8")
                        .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                        .setParameters(basicNameValuePairs)
                        .build()
        );
        HttpResponse resultData = httpClient.execute(post);

        InputStream inputStream = resultData.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));


        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        return stringBuffer.toString();

    }

    public static String getRequest(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        return stringBuffer.toString();
    }


}
