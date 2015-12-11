package com.huotu.ymr.model.backend;

import lombok.Getter;
import lombok.Setter;

/**
 * 后台爱分享model
 * Created by slt on 2015/11/27.
 */
@Getter
@Setter
public class bbackendShareModel {
    /**
     * 用户头像
     */
    private String userHeadUrl="";
       /**
     * 标题
     */
    private String title="";

    /**
     * 图片
     */
    private String img="";

    /**
     * 简介
     */
    private String intro="";

    /**
     * 内容
     */
    private String content="";

    /**
     * 置顶
     */
    private Boolean top=true;


    /**
     * 转发可得积分
     */
    private Integer relayScore=0;

    /**
     * 类型   0：资讯，1：团购，2：众筹
     */
    private Integer shareType=0;
}
