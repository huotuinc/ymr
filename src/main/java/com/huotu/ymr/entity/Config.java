package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 美投设置项
 * 如全局奖励设置 中介费率 分享出去的web页底部推广
 * Created by lgh on 2015/11/27.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)

/**
 * CrowdFundingTip = 我有A万，找人合作筹募
 * MoneyToScore = 10 (1元10积分)
 */
public class Config {

    /**
     * 关键字
     */
    @Id
    @Column(length = 100)
    private String key;
    /**
     * 值
     */
    @Lob
    private String value;
}
