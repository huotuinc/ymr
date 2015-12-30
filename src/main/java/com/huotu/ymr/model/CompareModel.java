package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by xhk on 2015/12/29.
 * 用来存储得到的合作发起者与合作者的信息
 * 来进行排序
 */
@Getter
@Setter
public class CompareModel {


    /**
     * 头像地址
     */
    private String headUrl;


    /**
     * 合作时间
     */
    private Date time;
}
