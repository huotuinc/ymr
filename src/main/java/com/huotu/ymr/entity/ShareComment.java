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
public class ShareComment implements YmrReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属分享
     */
    @ManyToOne
    private Share share;

    /**
     * 评论人
     */
    private User user;

//    /**
//     * 评论人姓名
//     */
//    private String commentName;
//
//    /**
//     * 评论人等级
//     */
//    private Integer level;
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

    /**
     * 被回复的评论ID,如果为空则是评论文章
     */
    private Long parentId;

    /**
     * 被评论人的姓名，如果为空则是评论文章
     */
    private String parentName;

    /**
     * 被点赞的数量
     */
    private Integer praiseQuantity;

    /**
     * 被回复的数量
     */
    private Integer commentQuantity;

    /**
     * 评论路径
     */
    private String commentPath;


}
