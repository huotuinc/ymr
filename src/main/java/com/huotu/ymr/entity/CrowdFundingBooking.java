package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 众筹预约人或（合作者）
 * Created by lgh on 2015/11/28.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class CrowdFundingBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属众筹
     */
    @ManyToOne
    private CrowdFunding crowdFunding;
    /**
     * 所属发起人
     */
    @ManyToOne
    private CrowdFundingPublic crowdFundingPublic;

    /**
     * 预约人
     */
    private Long ownerId;
    /**
     * 预约人头像
     */
    private String userHeadUrl;
    /**
     * 预约人姓名
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 预约金额
     */
    private Float money;

    /**
     * 预约时间
     */
    private Date time;
}
