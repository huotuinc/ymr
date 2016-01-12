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
public class AppUserSharePraiseModel {

    /**
     * 点赞ID
     */
    private Long pId;

    /**
     * 文章ID
     */
    private Long sId;

    /**
     * 点赞者名称
     */
    private String userName;

    /**
     * 点赞者头像
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

//    /**
//     * 是否是精品
//     */
//    private Boolean boutique;
    /**
     * 点赞时间
     */
    private Date time;
}
