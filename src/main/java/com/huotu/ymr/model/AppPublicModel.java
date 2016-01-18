package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 公共参数部分
 * Created by lgh on 2015/11/26.
 */

@Getter
@Setter
public class AppPublicModel {
    /**
     * 用户UserModel
     */
    private AppUserInfoModel currentUser;
    /**
     * 平台1、YMR2015AD   （android）    2、YMR2015AP   （ios)
     */
    private String operation;
    /**
     * 版本号
     */
    private String version;
    /**
     * 设备号 设备必须确保唯一 也必须确保每次发送给服务端都是一致的
     */
    private String imei;
    /**
     * 验证签名
     */
    private String sign;

    /**
     * 毫秒--utc
     */
    private long timestamp;

    /**
     * 用户token
     */
    private String token;
}
