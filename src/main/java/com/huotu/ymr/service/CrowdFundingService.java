package com.huotu.ymr.service;

import com.huotu.ymr.entity.CrowdFundingPublic;

import java.util.List;

/**
 * Created by xhk on 2015/12/2.
 */
public interface CrowdFundingService {
    /**
     * 通过文章类型，前一页最后一个id，每页显示的条数
     * 获取一页认购表格
     * @return
     */
    List<CrowdFundingPublic> findCrowdListFromLastIdWithNumber(Long crowdId, Long lastId,int number);

    /**
     * 获取认购表中最大的id值
     * @return
     */
    long getMaxId();

    /**
     * 通过关键字和每页条数查找合作发起人的下一页
     * @return
     */
    List<CrowdFundingPublic> searchCooperationgList(String key, Long lastId, Long crowdId, int number);


}
