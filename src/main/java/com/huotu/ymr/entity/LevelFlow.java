package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 级别变更流水
 * Created by lgh on 2015/12/10.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class LevelFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 级别变更类别
     */
    private CommonEnum.LevelFlowType levelFlowType;
    /**
     * 用户
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User user;

    /**
     * 原级别
     */
    private CommonEnum.UserLevel sourceLevel;

    /**
     * 目标级别
     */
    private CommonEnum.UserLevel toLevel;

    /**
     * 操作日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

}
