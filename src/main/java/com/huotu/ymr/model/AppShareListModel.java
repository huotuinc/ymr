package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 爱分享列表
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareListModel {

    private Long pId;
    /**
     * 用户头像
     */
    private String userHeadUrl;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String img;

    /**
     * 简介
     */
    private String intro;

    /**
     * 内容
     */
    private String content;

    /**
     * 置顶
     */
    private Boolean top;
    /**
     * 发帖时间
     */
    private Date time;

    /**
     * 点赞量
     */
    private Long praise;

    /**
     * 评论量
     */
    private Long comment;

    /**
     * 转发量
     */
    private Long relay;
    /**
     * 转发可得积分
     */
    private Integer relayScore;
}
