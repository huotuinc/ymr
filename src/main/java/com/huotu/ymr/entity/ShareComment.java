package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 转发评论
 * Created by lgh on 2015/11/27.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class ShareComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属分享
     */
    @ManyToOne
    private Share share;

    /**
     * 评论人Id
     */
    private Long ownerId;
    /**
     * 评论内容
     */
    @Column(length = 400)
    private String content;

    /**
     * 评论时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;


}
