package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/28.
 */
@Getter
@Setter
public class AppCrowdFundingModel {

    private Long pId;

    /**
     * 众筹类型
     */
    private CommonEnum.CrowdFundingType type;

    /**
     * 项目标题
     */
    private String title;
    /**
     * 时间
     */
    private Date time;
    /**
     * 介绍
     */
    private String content;
    /**
     * 目标金额
     */
    private Double toMoeny;

    /**
     * 目前金额
     */
    private Double currentMoeny;
    /**
     * 起购金额
     */
    private Double startMoeny;

    /**
     * 目标众筹人数
     */
    private Long toBooking;
    /**
     * 目前众筹人数
     */
    private Long currentBooking;
}
