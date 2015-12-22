package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ResourceSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Base64;
import java.util.UUID;

/**
 * Created by slt on 2015/12/12.
 */
@Controller
@RequestMapping("/app")
public class ImageUploadController implements ResourceSystem {
    @Autowired
    StaticResourceService staticResourceService;
    @Override
    @RequestMapping(value = "/uploadHandUrl")
    public ApiResult uploadHandUrl(Long userId, @RequestParam Object profileData) throws Exception {
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/uploadShareImg")
    public ApiResult uploadShareImg(Output<String> data,@RequestParam Object profileData) throws Exception {
        byte[] headPic = Base64.getMimeDecoder().decode(profileData.toString());
        ByteArrayInputStream in=new ByteArrayInputStream(headPic);
        String fileName = StaticResourceService.SHSRES_IMG + UUID.randomUUID().toString() + ".png";
        URI uri=staticResourceService.uploadResource(fileName, in);
        data.outputData(uri.toString());
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
