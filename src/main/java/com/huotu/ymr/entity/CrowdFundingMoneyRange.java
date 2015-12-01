package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 众筹金额范围
 * 只限合作和认购
 * Created by lgh on 2015/11/30.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
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
    private Double money;
}
