package com.huotu.ymr.model;

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
     * 封面图片
     */
    private String puctureUrl;
    /**
     * 项目标题
     */
    private String title;
    /**
     * 时间
     */
    private Date time;
    /**
     * 简介
     */
    private String summary;
    /**
     * 目标金额
     */
    private Float toMoeny;

    /**
     * 目前金额
     */
    private Float currentMoeny;

    /**
     * 起购金额
     */
    private Float startMoeny;

    /**
     * 目标众筹人数
     */
    private Long toBooking;
    /**
     * 目前众筹人数
     */
    private Long currentBooking;
}
