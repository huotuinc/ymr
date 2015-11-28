package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 合伙人系统接口
 * Created by lgh on 2015/11/27.
 */
public interface CrowdFundingSystem {

    @RequestMapping(method = RequestMethod.GET)
    ApiResult getList(Output<>) throws Exception;
}
