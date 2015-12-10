package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 举报评论
 * Created by lgh on 2015/12/10.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 举报人
     */
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private User owner;

    /**
     * 被举报者
     */
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private User to;

    /**
     * 评论内容
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private ShareComment shareComment;

    /**
     * 时间
     */
    private Date time;


}
