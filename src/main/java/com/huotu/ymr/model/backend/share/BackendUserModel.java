package com.huotu.ymr.model.backend.share;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户后台
 * Created by slt on 2015/11/30.
 */
@Getter
@Setter
public class BackendUserModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 等级
     */
    private String level;

    /**
     * 拥有的积分
     */
    private Integer score;

    /**
     * 用户状态 0:正常，1：禁言，2：冻结
     */
    private Integer userStatus;

}
