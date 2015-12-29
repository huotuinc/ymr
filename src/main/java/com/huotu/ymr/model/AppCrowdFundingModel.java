package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

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
     * 众筹状态
     * 未开始：0
     * 成功：1
     * 失败：2
     * 进行中：3
     */
    private CommonEnum.CrowdStatus partnerStatue;
    /**
     * 项目标题
     */
    private String title;
    /**
     * 时间
     */
    private Date time;

    /**
     * 开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;


    /**
     * 结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
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
    /**
     * 项目参与者的头像路径
     * 现默认10个
     */
    private List<String> peopleHeadUrl;
}
