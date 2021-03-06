package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 点赞表
 * Created by slt on 2015/12/21.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Praise {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章(帖子)
     */
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private Share share;


    /**
     * 用户
     */
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private User user;

    /**
     * 点赞时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date time;
}
