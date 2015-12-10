/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.service;

/**
 * 通用变量定义
 * Created by lgh on 2015/9/23.
 */
public interface CommonConfigService {

    /**
     * 资源地址
     * @return
     */
    String getResoureServerUrl();

    /**
     * 商城服务api地址
     * @return
     */
    String getMallApiServerUrl();

    /**
     * 获取当前网站的地址
     * @return
     */
    String getWebUrl();

    /**
     * app加密的密钥
     * @return
     */
    String getAppKeySecret();

    /**
     * 客服电话
     * @return
     */
    String getCustomerServicePhone();

}
