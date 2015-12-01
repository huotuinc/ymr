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
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发帖人
     */
    private Long ownerId;

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
     * 浏览量
     */
    private Long view;

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
     * 状态
     * true开启 false 关闭
     */
    private Boolean status;

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


}
