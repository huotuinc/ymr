package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 美投设置项
 * 如全局奖励设置
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
    private String value;
}
