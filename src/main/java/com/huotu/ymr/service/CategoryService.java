package com.huotu.ymr.service;

import com.huotu.ymr.entity.Category;

/**
 *
 * 文章类型服务
 * Created by xhk on 2015/12/18.
 */
public interface CategoryService {

    /**
     *
     * @param name
     * @return
     */
    Category getArticleTypeByName(String name);
}
