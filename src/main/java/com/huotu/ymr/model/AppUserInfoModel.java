package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
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
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户积分
     */
    private Integer score;
    /**
     * 用户等级
     */
    private CommonEnum.UserLevel userLevel;
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

    /**
     * 是否绑定微信
     */
    private Boolean isBindWeiXin;

    /**
     * 用户token身份标示
     * 32位的UUID
     */
    private String token;

}
