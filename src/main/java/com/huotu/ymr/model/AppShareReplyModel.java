package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lgh on 2015/11/27.
 */
@Getter
@Setter
public class AppShareReplyModel {

    /**
     * 回复ID，也就是评论ID
     */
    private Long rid;

    /**
     * 回复人姓名
     */
    private String replyName;

    /**
     * 回复人ID
     */
    private Long replyId;

    /**
     * 回复人ID
     */
    private Long userId;

    /**
     * 被回复人姓名
     */
    private String toReplyName;

    /**
     * 回复内容
     */
    private String content;





}
