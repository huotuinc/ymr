package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * 美投设置项
 * 如全局奖励设置 中介费率 分享出去的web页底部推广
 * Created by lgh on 2015/11/27.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Config {

    /**
     * 关键字
     */
    @Id
    private String key;
    /**
     * 值
     */
    @Lob
    private String value;
}
