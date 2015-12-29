package com.huotu.ymr.model.web;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 众筹转发页面
 * Created by xhk on 2015/12/29.
 */
@Getter
@Setter
public class WebCrowdModel {
    /**
     * 项目标题
     */
    private String title;

    /**
     * 结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    /**
     * 介绍
     */
    private String content;
    /**
     * 目标金额
     */
    private Double toMoeny;


    /**
     * 起购金额
     */
    private Double startMoeny;


}
