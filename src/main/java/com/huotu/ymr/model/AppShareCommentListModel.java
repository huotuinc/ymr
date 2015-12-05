package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareCommentListModel {

    /**
     * 评论ID
     */
    private Long pid;
    /**
     * 评论人
     */
    private String name;

    /**
     * 评论人等级
     */
    private CommonEnum.UserLevel level;


    /**
     * 头像地址
     */
    private String userHeadUrl;
    /**
     * 评论内容
     */

    private String content;

    /**
     * 评论时间
     */
    private Date time;

    /**
     * 被评论数量
     */
    private Integer commentQuantity;

    /**
     * 被点赞数量
     */
    private Integer praiseQuantity;

    /**
     * 回复内容
     */
    private AppShareReplyModel[] replyModels;


}
