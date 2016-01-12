package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareInfoModel {
    /**
     * 爱分享文章ID
     */
    private Long sId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图片
     */
    private String img;

    /**
     * 内容
     */
    private String content;
    /**
     * 时间
     */
    private Date time;
    /**
     * 点赞数量
     */
    private Long praiseQuantity;


    /**
     * 转发量
     */
    private Long relayQuantity;


    /**
     * 转发积分
     */
    private Integer relayReward;

    /**
     * 总共可用积分
     */
    private Integer totalIntegral;

    /**
     * 已分享积分
     */
    private Integer useIntegral;

    /**
     * 转发出去的URL
     */
    private String transmitUrl;

}
