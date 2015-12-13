package com.huotu.ymr.model.backend.share;

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
     * ID
     */
    private Long id;
    /**
     * 标题
     */
    private String shareTitle;

    /**
     * 图片
     */
    private String shareImg;

    /**
     * 内容
     */
    private String shareContent;

    /**
     * 置顶
     */
    private Boolean top;

    /**
     * 发帖时间
     */
    private Date time;

    /**
     * 浏览量
     */
    private Long view;

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
     * 全局配置转发积分
     */
    private Integer overallTransmit;

    /**
     * 全局配置总积分
     */
    private Integer overallTotal;

    /**
     * 单独配置转发积分
     */
    private Integer aloneTransmit;

    /**
     * 单独配置总积分
     */
    private Integer aloneTotal;

    /**
     * 爱分享类型 info(0, "资讯")，group(1, "团购")。。。
     */
    private Integer shareType=0;

    /**
     * 审核类型
     */
    private String  checkType="审核中";

    /**
     * 发布人类型
     */
    private String  userType="官方";

}
