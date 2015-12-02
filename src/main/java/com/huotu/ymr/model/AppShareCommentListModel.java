package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareCommentListModel {

    private Long pid;
    /**
     * 评论人
     */
    private String name;
    /**
     * 头像地址
     */
    private String userHeadUrl;
    /**
     * 评论内容
     */

    private String content;

    /**
     * 评论时间
     */
    private Date time;
}
