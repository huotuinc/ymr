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
public class AppRaiseCooperationListModel {

    private Long pid;
    /**
     * 头像地址
     */
    private String userHeadUrl;

    /**
     * 状态
     * 审核中：0
     * 成功：1
     * 失败：2
     */
    private Integer status;

    /**
     * 时间
     */
    private Date time;

    /**
     * 用户等级
     * /**
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
     * 姓名
     */
    private String name;

    /**
     * 合作人数
     */
    private Long amount;
    /**
     * 提示信息
     * 如 我有50万，找人合作筹募
     */
    private String tip;
}
