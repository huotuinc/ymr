package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * 众筹项目人查询
 * Created by xhk on 2015/12/14.
 */
@Getter
@Setter
public class CrowdFundingPublicSearchModel {
    /**
     * 众筹项目人名字
     */
    private String crowdFundingPublicName="";

    /**
     * 众筹项目Id
     */
    private Long crowdFundingId=1L;

    /**
     *
     * booking(0, "预约")
     */
    /**
     * cooperation(1, "合作")
     */
    /**
     * subscription(2, "认购")
     *
     */
    private Integer crowdFundingType=-1;


    /**
     * 全部：-1
     *
     * cooperation(1, "合作发起人")
     */
    /**
     * subscription(2, "合作人")
     */
    private Integer peopleType=-1;


    /**
     *
     * booking(0, "预约人")
     */
    /**
     * cooperation(1, "合作人")
     */
    /**
     * subscription(2, "认购人")
     */
    private Integer publicType=-1;

    /**
     *
     *可视化类型
     * 通过此来应该如何显示合作人详细
     *
     * 预约人： 0
     *
     * 合作发起人：1
     *
     * 认购人: 2
     *
     * 合作人： 3
     *
     * 合作发起人与合作人： 4
     *
     *预约与合作发起人与认购人：5
     */
    private Integer viewType;


    /**
     * 排序字段 0：发帖时间
     */
    private Integer sort=0;

    /**
     * 排序方式 0：降序|1：升序
     */
    private Integer raSortType = 0;
    /**
     * 发布时间
     */
    private String startTime="";
    /**
     * 结束时间
     */
    private String endTime="";
    /**
     * 指定查询页码
     */
    private Integer pageNoStr = 0;

//    /**
//     * 辅助详情页面的页码显示
//     */
//    private Integer cooPages=0;
}
