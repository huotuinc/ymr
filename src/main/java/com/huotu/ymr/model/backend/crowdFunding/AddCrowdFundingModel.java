package com.huotu.ymr.model.backend.crowdFunding;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 众筹项目的添加model
 * Created by xhk on 2015/12/19.
 */
@Setter
@Getter
public class AddCrowdFundingModel {


    /**
     * 众筹id
     *
     */
    private Long id;

    /**
     * 类型
     */
    private CommonEnum.CrowdFundingType crowdFundingType;

    /**
     * 文章草稿还是发布
     *
     * 草稿：1
     *
     * 发布：0
     */
    private Integer isDraft;
//    /**
//     * 预约类：0
//     *
//     * 合伙众筹类：1
//     *
//     * 认购类：2
//     *
//     */
//    private Integer crowdFunding=0;

    /**
     *
     * 可见等级
     *
     * 可见：1
     *
     * 不可见：0
     */
    private Integer lev1=0;

    private Integer lev2=0;

    private Integer lev3=0;

    /**
     * 项目名字
     */
    private String name="";

    /**
     * 内容
     */
    private String content="";

    /**
     * 开始时间
     */
    private String startDate="";

    /**
     * 结束时间
     */
    private String endDate="";

    /**
     * 目标金额
     */
    private Double limitMoney;

    /**
     * 上限人数
     *
     */
    private Long limitPeople;

    /**
     * 起购金额
     *
     */
    private Double startMoney;

    /**
     * 封面地址
     */
    private  String picture;

    /**
     * 中介费全局设置
     *
     * globle:1
     *
     * single:0
     */
    private Integer globleOrSingle=1;

    /**
     * 单独中介费设置
     */
    private Integer singleSet;



}
