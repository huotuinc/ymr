package com.huotu.ymr.model.backend.user;

import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by xhk on 2016/1/15.
 * 用户举报显示的信息
 */
@Getter
@Setter
public class ReportShowModel {

    /**
     * 举报id
     * reportId
     *
     */
    private Long id;

    /**
     * 举报的时间
     */

    private Date time;

    /**
     * 被举报的用户
     */
    private User to;

    /**
     * 是否已经处理
     *
     * 已经处理：1
     * 未处理：0
     */
    private Integer hasSolved;
    /**
     * 评论内容
     */
    private ShareComment shareComment;
    /**
     * 被举报用户微信昵称
     *
     */
    private String wXName;

    /**
     * 被举报用户手机
     */
    private String phone;
//    /**
//     *
//     *总条目数
//     */
//    private Long totalElements=0L;
//
//    /**
//     *
//     *总页数
//     */
//    private Integer totalPages=0;
//
//    /**
//     *
//     *当前页数
//     */
//    private Integer number=0;
}
