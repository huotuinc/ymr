package com.huotu.ymr.model.backend.article;

import lombok.Getter;
import lombok.Setter;

/**
 * 后台文章添加
 * Created by xhk on 2015/12/12.
 */
@Getter
@Setter
public class ArticleCUDModel {

    /**
     * 文章id
     */
    private Long articleID;

    /**
     * 文章标题
     */
    private String articleTitle;

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
     * 外链状态，是否启用外链，0：不启用；1：启用
     */
    private Integer urlStatus;

    /**
     * 外链地址
     */
    private String linkUrl;

    /**
     * 文章内容
     */
    private String content;


    /**
     * 文章封面
     */
    private byte[] picture;



}
