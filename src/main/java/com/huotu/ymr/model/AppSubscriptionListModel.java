package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/30.
 */
@Getter
@Setter
public class AppSubscriptionListModel {

    private Long pid;
    /**
     * 头像地址
     */
    private String userHeadUrl;
    /**
     * 姓名
     */
    private String name;

    /**
    * 用户等级
     * * /**
     * one(0, "LV1")
     */
    /**
     * two(1, "LV2")
     */
    /**
     * three(2, "LV3")
     */

    private CommonEnum.UserLevel level;
    /**
     * 时间
     */
    private Date time;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 金额
     */
    private Double money;
}
