package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lgh on 2015/11/30.
 */
@Getter
@Setter
public class AppRaiseCooperationListModel {

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
     * 合作人数
     */
    private Long amount;
    /**
     * 提示信息
     * 如 我有50万，找人合作筹募
     */
    private String tip;
}
