package com.huotu.ymr.model.backend.share;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;


/**
 * Created by zyw on 2016/1/8.
 */
@Setter
@Getter
public class BackendChargeModel

{
    /**
     * 用户的id
     */

     private long userId;
    /**
     * 用户获得积分的时间
     */
     private Date date;
    /**
     * 用户的昵称
     */
     private String userName;
    /**
     * 用户获得的积分
     */
     private int score;

    /**
     * 用户的等级
     */
    private String level;






 }

