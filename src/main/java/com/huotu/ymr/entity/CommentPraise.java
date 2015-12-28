package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 点赞表
 * Created by slt on 2015/12/21.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class CommentPraise {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private  Share share;

    /**
     * 文章(帖子)
     */
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private ShareComment comment;


    /**
     * 用户
     */
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private User user;

    /**
     * 状态 -1：为取消点赞，1：为点赞
     */
    private Integer type;
}
