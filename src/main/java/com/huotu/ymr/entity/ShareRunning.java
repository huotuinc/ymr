package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章分享流水
 * Created by lgh on 2015/11/27.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class ShareRunning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分享人ID
     */
    private Long userId;

    /**
     * 分享人名称
     */
    private String name;

    /**
     * 文章(帖子)
     */
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private Share share;

    /**
     * 分享的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 获得的积分
     */
    private Integer integral;

}
