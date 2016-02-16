package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 2016/1/10.
 */
@Setter
@Getter
public class ChargeSearchModel
{

    /**
     * 用户名
     */
    private String userName="";

    /**
     * 用户等级 userLevel 0:一级，1:二级，2：三级,-1:全部
     */
    private Integer userLevel=-1;



    /**
     * 排序字段 0：序号(ID)，1：积分，2：等级
     */
    private Integer sort=0;

    /**
     * 排序方式 0：降序|1：升序
     */
    private Integer raSortType = 0;
    //    /**
    //     * 发布时间
    //     */
    private String startTime="";
    //    /**
    //     * 结束时间
    //     */
      private String endTime="";
    /**
     * 指定查询页码
     */
    private Integer pageNoStr = 0;

    private String articleTitle="";

    private  int totalScoreRecords;

    private Integer scoreFlowType=-1;

    private long userID;


}
