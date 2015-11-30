package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.AppCrowdFundingListModel;
import com.huotu.ymr.model.AppCrowdFundingModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 合伙人系统接口
 * Created by lgh on 2015/11/27.
 */
public interface CrowdFundingSystem {

    /**
     * 获取众筹列表
     *
     * @param list
     * @param lastId
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel> list, Long lastId) throws Exception;

    /**
     * 众筹详情
     *
     * @param data
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getCrowFindingInfo(Output<AppCrowdFundingModel> data, Long id) throws Exception;

    @RequestMapping(method = RequestMethod.POST)
    ApiResult raiseCrowFinding() throws Exception;
}
