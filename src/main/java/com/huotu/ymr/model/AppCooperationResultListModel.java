package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhk on 2015/12/14.
 */
@Getter
@Setter
public class AppCooperationResultListModel {

    private Long pid;


    /**
     * 头像地址
     */
    private List<AppBookingListModel> bookingListModels=new ArrayList<AppBookingListModel>();
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
