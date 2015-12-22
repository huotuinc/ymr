package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 爱分享列表
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareListModel {

    /**
     * ID
     */
    private Long pId;
    /**
     * 用户头像
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

    /**
     * 内容 todo 暂时还没发现需要用到
     */
    private String content;

    /**
     * 置顶
     */
    private Boolean top;

    /**
     * 是否是精品
     */
    private Boolean boutique;
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
     * 点赞者头像列表
     */
    private List<String> headUrls;


}
