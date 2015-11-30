package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 预约或合作发起人、 众筹认购人
 * Created by lgh on 2015/11/28.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class CrowdFundingPublic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属众筹
     */
    @ManyToOne
    private CrowdFunding crowdFunding;

    /**
     * 发起人
     */
    private Long ownerId;
    /**
     * 发起人头像
     */
    private String userHeadUrl;
    /**
     * 发起人姓名
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 购买金额
     */
    private Float money;

    /**
     * 发起时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;


}
