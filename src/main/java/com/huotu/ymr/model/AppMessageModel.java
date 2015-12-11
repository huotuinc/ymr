package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 消息
 * Created by lgh on 2015/12/10.
 */
@Getter
@Setter
public class AppMessageModel {

    private long messageid;
    private long messageOrder;
    private String context;
    private Date date;

}
