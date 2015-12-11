package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户数据(简单版)
 * Created by slt on 2015/11/26.
 */

@Getter
@Setter
public class AppSimpleUserModel {

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;

}
