package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 众筹合作者
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
     * 合作者
     */
    private Long ownerId;
    /**
     * 合作者头像
     */
    private String userHeadUrl;
    /**
     * 合作者姓名
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 金额
     */
    private Float money;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 中介费
     */
    private Float agencyFee;

    /**
     * 状态 1成功2失败
     * 默认0
     */
    private Integer status;
}
