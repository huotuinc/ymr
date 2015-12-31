package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xhk on 2015/12/30.
 *
 * 举报管理
 */
@Getter
@Setter
public class ReportSearchModel {


    /**
     * 被举报人姓名
     */
    private String name;

    /**
     * 排序字段 0：发帖时间，1：浏览量，2：转发量
     */
    private Integer sort=0;

    /**
     * 排序方式 0：降序|1：升序
     */
    private Integer raSortType = 0;
    /**
     * 指定查询页码
     */
    private Integer pageNoStr = 0;



}
