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
public class AppUserShareRunningModel {

    /**
     * 文章ID
     */
    private Long pId;

    /**
     * 用户的ID
     */
    private Long userId;

    /**
     * 转发者姓名
     */
    private String name;

    /**
     * 转发者头像
     */
    private String headUrl;

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


}
