package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 介绍性的文章
 * Created by lgh on 2015/11/26.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分类
     */
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private Category category;

    /**
     * 标题
     */
    @Column(length = 200)
    private String title;

    /**
     * 图片
     */
    @Column(length = 200)
    private String picture;

    /**
     * 内容
     */
    @Lob
    private String content;

    /**
     * 启用外链
     * 默认false;
     */
    private Boolean useLink = false;
    /**
     * 外链地址
     */
    private String linkUrl;
    /**
     * 简介
     */
    @Column(length = 200)
    private String summary;
    /**
     * 时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 浏览量
     */
    private Long view;
}
