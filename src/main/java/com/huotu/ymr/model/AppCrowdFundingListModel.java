package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
     * 众筹类型
     */
    private CommonEnum.CrowdFundingType type;
    /**
     * 项目状态
     *  audit(0, "审核中")
     * pass(1, "通过")
     *close(3, "关闭")
     */
    private CommonEnum.CheckType status;

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
     * 预约金额
     */
    private Double booingMoeny;

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
     * 开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;


    /**
     * 结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
}
