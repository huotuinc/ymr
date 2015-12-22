package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * 文章后台查询条件
 * Created by xhk on 2015/12/9.
 */
@Getter
@Setter
public class ArticleSearchModel {
    /**
     * 文章标题
     */
    private String articleTitle="";

    /**
     * 众筹类型
     * <ul>
     *     <li>公司介绍</li>
     *     <li>自传故事</li>
     *     <li>学院介绍</li>
     *     <li>美容知识</li>
     * </ul>
     */
    private Integer articleType=-1;

    /**
     * 发布人
     */
    private String managerName;

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
