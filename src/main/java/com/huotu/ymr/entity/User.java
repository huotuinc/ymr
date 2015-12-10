package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户
 * Created by lgh on 2015/12/1.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class User {
    /**
     * 用户id 对应商城中的用户Id
     */
    @Id
    private Long id;

    /**
     * 用户级别
     */
    private CommonEnum.UserLevel userLevel;

    /**
     * 用户积分
     * 发帖或转发，或充值获得，可转为商城积分
     */
    private Integer score;

    /**
     * 累计获得积分
     */
    private Integer continuedScore;

    /**
     * 用户token身份标示
     * 32位的UUID
     */
    @Column(length = 32)
    private String token;


}
