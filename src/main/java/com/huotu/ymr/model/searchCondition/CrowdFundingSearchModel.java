package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * 众筹项目查询
 * Created by xhk on 2015/12/14.
 */
@Getter
@Setter
public class CrowdFundingSearchModel {
    /**
     * 文章标题
     */
    private String crowdFundingTitle="";

    /**
     * 全部：-1
     *
     * booking(0, "预约")
     */
    /**
     * cooperation(1, "合作")
     */
    /**
     * subscription(2, "认购")
     */
    private Integer crowdFundingType=-1;


    /**
     * 排序字段 0：发帖时间，1：浏览量，2：转发量
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
}
