package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 预约、合作发起人、 众筹认购人
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
    private Double money;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 发起时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 中介费
     */
    private Double agencyFee;

    /**
     * 支付流水号
     */
    @Column(length = 100)
    private String orderNo;

    /**
     * 审核状态 1成功2失败
     * 默认0
     */
    private Integer status;
    /**
     * 合作人数
     */
    private Long amount;
}
