package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户系统接口
 * Created by lgh on 2015/11/26.
 */

public interface ResourceSystem {

    /**
     * 上传头像
     * @param userId    用户ID
     * @param data      图片数据
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult uploadHandUrl(Long userId,@RequestParam Object data) throws Exception;

    /**
     * 上传爱分享文章的图片
     * @param profileData      图片数据
     * @param data             用于展示的绝对路径
     * @param relativeUrl      用于上传的相对路径
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    ApiResult uploadShareImg(Output<String> data,Output<String> relativeUrl, @RequestParam Object profileData) throws Exception;
}
