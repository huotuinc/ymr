package com.huotu.ymr.model.mall;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/12/22.
 */
@Getter
@Setter
public class MallUserModel {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户名 (手机号或随机码)
     */
    private String userName;

    /**
     * 用户头像
     */
    private String headUrl;

    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 所属商家Id
     */
    private Long merchantId;
    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 是否绑定手机号
     */
    private Boolean isBindMobile;

}
