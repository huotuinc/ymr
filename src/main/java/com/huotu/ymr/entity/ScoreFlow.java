package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 积分流水
 * Created by lgh on 2015/12/9.
 */
public class ScoreFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
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
    private Date time;
}
