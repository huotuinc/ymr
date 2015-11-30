package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 众筹金额范围
 * 只限合伙和认购
 * Created by lgh on 2015/11/30.
 */
public class CrowdFundingMoneyRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 类型
     */
    private CommonEnum.CrowdFundingType crowdFundingType;

    /**
     * 金额
     */
    private Float money;
}
