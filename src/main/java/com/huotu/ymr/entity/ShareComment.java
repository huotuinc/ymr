package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
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
    private Long userId;

    /**
     * 评论人名称
     */
    private String commentName;

    /**
     * 评论人等级
     */
    private CommonEnum.UserLevel level;

    /**
     * 评论人头像
     */
    private String headUrl;
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
     * 被回复的评论ID,如果为0则是评论文章
     */
    private Long parentId;

    /**
     * 所属评论的ID,如果为0，则没有
     */
    private Long commentId;

    /**
     * 被评论人的姓名，如果为空则是评论文章
     */
    private String parentName;

    /**
     * 被点赞的数量
     */
    private Integer praiseQuantity=0;

    /**
     * 被回复的数量
     */
    private Integer commentQuantity=0;

    /**
     * 评论路径
     */
    private String commentPath;

    /**
     *  评论状态
     */
    private CommonEnum.ShareCommentStatus status;


}
