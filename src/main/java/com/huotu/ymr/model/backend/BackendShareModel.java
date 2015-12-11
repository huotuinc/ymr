package com.huotu.ymr.model.backend;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 爱分享后台
 * Created by lgh on 2015/11/26.
 */
@Getter
@Setter
public class BackendShareModel {
    /**
     * 用户头像
     */
    private String userHeadUrl;
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
    private Long praiseQuantity;

    /**
     * 评论量
     */
    private Long commentQuantity;

    /**
     * 转发量
     */
    private Long relayQuantity;
    /**
     * 转发可得积分
     */
    private Integer relayScore;

    /**
     * 爱分享类型 info(0, "资讯")，group(1, "团购")。。。
     */
    private Integer shareType;

}
