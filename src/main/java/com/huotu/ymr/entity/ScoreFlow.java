package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分流水
 * Created by lgh on 2015/12/9.
 */
@Entity
@Setter
@Getter
@Cacheable(value = false)
@Table(name = "scoreFlow")
public class ScoreFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
    @OneToOne
    private User user;

    /**
     * 类型
     */
    private CommonEnum.ScoreFlowType scoreFlowType;

    /**
     * 积分 (可负值,根据类型情况不同)
     */
    private Integer score;


    /**
     * 当前积分（操作后的积分）
     */
    private Integer currentScore;

    /**
     * 时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
}
