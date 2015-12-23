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
     * 发帖人（管理员或用户）
     */
    private Long ownerId;
    /**
     * 发帖人名称
     */
    private String name;
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
    private String content;

    /**
     * 是否启用外链
     */
    private Boolean useLink;

    /**
     * 外链地址
     */
    private String linkUrl;

    /**
     * 开启推荐产品
     * <p>
     * true开启 false 关闭 (默认)
     * 开启后将关联商城商品，在爱分享详情页的末尾显示相关联商品
     */
    private Boolean enabledRecommendProduct = false;

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
    private Integer shareType;

    /**
     * 审核类型
     */
    private String  checkType;

    /**
     * 发布人类型
     */
    private String  userType="官方";

    /**
     * 是否是精品
     */
    private Boolean boutique;

}
