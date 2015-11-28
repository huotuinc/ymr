package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
     * 名称
     */
    @Column(length = 100)
    private String name;
    /**
     * 封面图片
     */
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
     * 起购金额
     */
    private Float startMoeny;

    /**
     * 目标金额
     */
    private Float toMoeny;

    /**
     * 目标众筹人数
     */
    private Long toBooking;

}
