package com.huotu.ymr.model.backend.config;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 微信配置参数
 * Created by xhk on 2015/12/25.
 */
@Getter
@Setter
public class WeiXinConfigModel {

    /**
     * 微信支付商户号（MCHID）
     */
    private String mchid="";

    /**
     * appid
     */
    private String appid="";

    /**
     * AppSecret
     */
    private String appSecret="";

    /**
     * 签名密钥(paySignKey)
     */
    private String paySignKey="";

    /**
     * 证书路径
     */
    private String celPath="";

    /**
     * 证书密钥
     */
    private String celKey="";
}
