package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Cacheable(value = false)
/**
 * 美投设置项
 * 如全局奖励设置 中介费率 分享出去的web页底部推广
 * CrowdFundingTip = 我有A万，找人合作筹募
 * MoneyToScore = 10 (1元10积分)
 * GlobalAgencyFee 全局中介费
 *
 *
 * GlobalTransmit=0      官方转发积分，默认为0
 * GlobalTotal=-1        官方配置总积分，默认为-1 表示无限制
 * UserTransmit=0        用户转发积分，默认为0
 * UserTotal=0           用户总奖励积分，默认为0
 * UserPost=0            用户发帖奖励的积分，默认为0
 * BoottomGeneralize    底部推广富文本
 *
 */
public class Config {

    /**
     * 关键字
     */
    @Id
    @Column(length = 100,nullable = false,name = "PRKEY")
    private String key;
    /**
     * 值
     */
    @Lob
    private String value;
}
