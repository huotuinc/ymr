package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 我的众筹流水
 * Created by Administrator on 2015/12/30.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class MyCrowdFundingFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private User owner;

    /**
     * 所属项目
     */
    private CrowdFunding crowdFunding;

    /**
     * 所属角色
     * 预约人、合作发起人、认购人：0
     * 合作人：1
     */
    private Integer role;




}
