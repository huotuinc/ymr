package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 众筹项目
 * Created by lgh on 2015/11/27.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class CrowdFunding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 类型
     */
    private CommonEnum.CrowdFundingType crowdFundingType;
    /**
     * 项目名称
     */
    @Column(length = 100)
    private String name;

    /**
     * 可见等级
     */
    private List<CommonEnum.UserLevel> visibleLevel;
    /**
     * 封面图片
     */
    @Column(length = 200)
    private String puctureUrl;

    /**
     * 内容
     */
    @Lob
    private String content;

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
     * 起购金额(预约金额)
     */
    private Double startMoeny;

    /**
     * 目标金额
     */
    private Double toMoeny;


    /**
     * 目前金额
     */
    private Double currentMoeny;
    /**
     * 目标众筹人数 上限人数
     * 只限预约类
     * 如果项目预约成交人数已经到达此值，需要进行退款处理
     */
    private Long toBooking;

    /**
     * 中介费率 0-100
     * 如50 代表50%
     */
    private Integer agencyFeeRate;

    /**
     * 是否推送到爱美容资讯栏目
     * 默认false
     */
    private boolean pushToShare = false;

    /**
     * 目前众筹人数
     */
    private Long currentBooking;

}
