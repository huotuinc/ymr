package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 文章转发
 * Created by slt on 2015/11/27.
 */
@Getter
@Setter
public class AppUserShareModel {

    /**
     * 文章ID
     */
    private Long pId;

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
     * 发帖时间
     */
    private Date time;

    /**
     * 可获得的积分
     */
    private Integer integral;

    /**
     * 审核状态
     */
    private CommonEnum.CheckType checkType;


}
