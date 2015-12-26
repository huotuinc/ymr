package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信授权之后返回的数据
 * Created by slt on 2015/12/25.
 */

@Getter
@Setter
public class AppWeiXinAccreditModel {

    /**
     * 微信唯一ID
     */
    String unionid;
    /**
     * openId
     */
    String openid;
    /**
     * 语言
     */
    String language;

    /**
     * 微信昵称
     */
    String nickname;
    /**
     * 微信头像
     */
    String headimgurl;
    /**
     * 性别 2：女，1：男
     */
    Integer sex;
    /**
     * 国家
     */
    String country;
    /**
     * 省份
     */
    String province;
    /**
     * 城市
     */
    String city;

}
