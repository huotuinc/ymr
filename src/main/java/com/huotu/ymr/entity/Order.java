package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单
 * 预约订单 或 充值订单
 * Created by lgh on 2015/12/10.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Order {

    /**
     * 订单号
     */
    @Id
    @Column(length = 100)
    private String orderNo;

    /**
     * 订单金额
     */
    private Double money;

    /**
     * 对应的积分
     */
    private Integer score;

    /**
     * 付款的用户
     */
    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.MERGE})
    private User user;

    /**
     * 下单时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 订单状态
     */
    private CommonEnum.PayType payType;

    /**
     * 外部流水号
     */
    @Column(length = 100)
    private String outOrderNo;

    /**
     * 支付回调时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;


}
