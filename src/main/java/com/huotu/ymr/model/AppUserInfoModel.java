package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户数据
 * Created by lgh on 2015/11/26.
 */

@Getter
@Setter
public class AppUserInfoModel {

    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户积分
     */
    private Integer score;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 是否绑定手机号
     */
    private boolean isBindMobile;
}
