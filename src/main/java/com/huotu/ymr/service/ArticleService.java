package com.huotu.ymr.service;

import com.huotu.ymr.entity.Article;

import java.util.List;

/**
 * 文章管理
 * Created by xhk on 2015/12/1.
 */
public interface ArticleService {
    /**
     * 通过文章类型，前一页最后一个id，每页显示的条数
     * 获取一页文章
     * @return
     */
    List<Article> findArticleListFromlastIdWithNumber(Integer categoryId, Long lastId,int number);

    /**
     * 获取w文章表中最大的id值
     * @return
     */
    long getMaxId();
}
