package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/30.
 */
@Getter
@Setter
public class AppCrowdFundingListModel {
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
