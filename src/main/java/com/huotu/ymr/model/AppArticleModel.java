package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 文章
 * Created by lgh on 2015/11/26.
 */
@Getter
@Setter
public class AppArticleModel {
    /**
     * n标题
     */
    private String title;

    /**
     * 图片
     */

    private String picture;

    /**
     * 内容
     */

    private String content;


    /**
     * 时间
     */
    private Date time;

    /**
     * 浏览量
     */
    private Long view;
}
