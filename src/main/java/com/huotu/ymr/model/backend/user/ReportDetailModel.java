package com.huotu.ymr.model.backend.user;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by xhk on 2015/12/31.
 * 举报具体信息
 */
@Getter
@Setter
public class ReportDetailModel {

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 被举报时间
     */
    private Date time;

    /**
     * 用户等级
     */
    private CommonEnum.UserLevel level;

    /**
     * 被举报用户手机
     */
    private String phone;

    /**
     * 被举报者的评论
     */
    private String content;

}
