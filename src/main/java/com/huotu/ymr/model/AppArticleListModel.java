package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 文章列表
 * Created by lgh on 2015/11/26.
 */

@Getter
@Setter
public class AppArticleListModel {
    private Long pId;


    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */

    private String picture;

    /**
     * 简介
     */
    private String summary;

    /**
     * 浏览量
     */
    private Long view;
}
