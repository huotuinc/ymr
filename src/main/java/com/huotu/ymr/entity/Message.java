package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lgh on 2015/12/10.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 70)
    private String title;

    @Column(length = 300)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedTime;

    /**
     * 用户可以看到的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime;

    /**
     * 无法再看到的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidTime;

    /**
     * 是否删除
     */
    private boolean deleted;

    /**
     * 0:全推送
     * 先忽略，推送重新设计
     */
    private Short messageType;
}
