package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * 后台用户查询条件
 * Created by slt on 2015/12/9.
 */
@Getter
@Setter
public class UserSearchModel {
    /**
     * 用户名
     */
    private String name="";

    /**
     * 用户等级 userLevel 0:一级，1:二级，2：三级,-1:全部
     */
    private Integer userLevel=-1;

    /**
     * 电话
     */
    private String mobile="";

    /**
     * 排序字段 0：序号(ID)，1：积分，2：等级
     */
    private Integer sort=0;

    /**
     * 排序方式 0：降序|1：升序
     */
    private Integer raSortType = 0;

    /**
     * 指定查询页码
     */
    private Integer pageNoStr = 0;

}
