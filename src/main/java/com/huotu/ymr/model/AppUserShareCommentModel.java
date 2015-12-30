package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 爱分享列表
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppUserShareCommentModel {

    /**
     * 文章ID
     */
    private Long pId;

    /**
     * 评论ID
     */
    private Long cId;

    /**
     * 评论者用户头像
     */
    private String userHeadUrl;

    /**
     * 标题
     */
    private String title;

    /**
     * 爱分享文章类型
     */
    private CommonEnum.ShareType shareType;

    /**
     * 图片
     */
    private String img;

    /**
     * 简介
     */
    private String intro;

//    /**
//     * 置顶
//     */
//    private Boolean top;
//
//    /**
//     * 是否是精品
//     */
//    private Boolean boutique;
//    /**
//     * 发帖时间
//     */
//    private Date time;

    /**
     * 被评论人的ID
     */
    private Long commentUserId;

    /**
     * 被评论人名字
     */
    private String commentName;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 评论内容
     */
    private String commentComment;

}
