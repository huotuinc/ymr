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
     * 标题
     */
    private String title;

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
    private Long praise;


}
