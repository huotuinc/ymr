package com.huotu.ymr.model.SearchCondition;

import com.huotu.ymr.entity.Manager;
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
     * <option value="1">公司介绍</option>
     *<option value="2">自传故事</option>
     *<option value="3">学院介绍</option>
     <*option value="4">美容知识</option>
     */
    private Integer articleType=-1;

    /**
     * 发布人
     */
    private Manager manager=null;

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
