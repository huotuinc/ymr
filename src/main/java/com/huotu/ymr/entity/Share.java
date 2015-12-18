package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 爱分享
 * Created by lgh on 2015/11/27.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Share implements YmrComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 发帖人类型    0:用户，1：官方(上架)
     */
    private CommonEnum.UserType ownerType;

    /**
     * 标题
     */
    @Column(length = 200)
    private String title;

    /**
     * 类型
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
     * 内容
     */
    @Lob
    private String content;

    /**
     * 置顶
     */
    private Boolean top;
    /**
     * 发帖时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    /**
     * 点赞量
     */
    private Long praiseQuantity = 0L;

    /**
     * 评论量
     */
    private Long commentQuantity = 0L;

    /**
     * 转发量
     */
    private Long relayQuantity = 0L;
    /**
     * 浏览量
     */
    private Long view = 0L;

    /**
     * 启用外链
     * 默认false;
     */
    private Boolean useLink = false;
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
     * 发帖奖励的积分
     */
    private Integer postReward;

    /**
     * 转发奖励的积分
     */
    private Integer relayReward;

    /**
     * 总分
     */
    private Integer score;
    /**
     * 已使用积分
     */
    private Integer usedScore;

    /**
     * 审核状态
     */
    private CommonEnum.CheckType checkStatus;

    /**
     * 审核未通过的原因
     */
    private String reason;
}
