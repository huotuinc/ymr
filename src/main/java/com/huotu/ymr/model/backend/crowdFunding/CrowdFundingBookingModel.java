package com.huotu.ymr.model.backend.crowdFunding;

import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingPublic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * 获取合作人的信息
 * Created by xhk on 2015/12/15.
 */
@Getter
@Setter
public class CrowdFundingBookingModel {

    /**
     *
     *所属项目
     */
   private CrowdFundingPublic crowdFundingPublic;

    /**
     *
     *项目人
     */
   private CrowdFundingPublic person;

    /**
     *
     *合作发起人下的合作人
     */
    private List<CrowdFundingBooking> booking;

    /**
     *
     *项目人类型
     *
     * 预约人： 0
     *
     * 合作发起人：1
     *
     * 认购人: 2
     *
     * 合作人： 3
     *
     * 合作发起人与合作人： 4
     *
     *预约与合作发起人与认购人：5
     */
    private Integer peopleType;



    /**
     *
     *总条目数
     */
    private Long totalElements=0L;

    /**
     *
     *总页数
     */
    private Integer totalPages=0;

    /**
     *
     *当前页数
     */
    private Integer number=0;

//    /**
//     * 审核状态 1成功2失败
//     * 默认0
//     */
//    private Integer status=0;


}
