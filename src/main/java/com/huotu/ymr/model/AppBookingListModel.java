package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lgh on 2015/11/30.
 */
@Getter
@Setter
public class AppBookingListModel {

    private Long pid;
    /**
     * 头像地址
     */
    private String userHeadUrl;
    /**
     * 姓名
     */
    private String name;

    /**
     * 用户等级
     */
    private CommonEnum.UserLevel level;
    /**
     * 时间
     */
    private Date time;

    /**
     * 手机
     */
    private String phone;
    /**
     * 状态
     * 审核中：0
     * 成功：1
     * 失败：2
     */
    private Integer status ;

}
