package com.huotu.ymr.model.SearchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * 爱分享后台查询条件
 * Created by slt on 2015/12/9.
 */
@Getter
@Setter
public class ShareSearchModel {
    /**
     * 爱分享文章标题
     */
    private String shareTitle;

    /**
     * 爱分享类型 info(0, "资讯")，group(1, "团购")。。。
     */
    private Integer shareType;

    /**
     * 排序字段 0：发帖时间，1：浏览量，2：转发量，3：点赞量，4：评论量
     */
    private Integer sort;

    /**
     * 排序方式 0：降序|1：升序
     */
    private Integer raSortType = 0;
    /**
     * 发布时间
     */
    private String txtBeginPaytime;
    /**
     * 结束时间
     */
    private String txtEndPaytime;
    /**
     * 指定查询页码
     */
    private Integer pageNoStr = 0;
}
