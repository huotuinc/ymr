package com.huotu.ymr.controller.web;

import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.UUID;

/**
 * Created by Administrator on 2015/12/14.
 */
@Controller
@RequestMapping("/manager")
public class FileUploadController {

    @Autowired
    StaticResourceService staticResourceService;
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    @RequestMapping(value="/ajaxEditorFileUpload",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel fileUploadUeImage(String imgsrc) throws Exception {
        ResultModel result=new ResultModel();
        //去掉多余字符
        imgsrc = imgsrc.substring(22);
        byte[] bytes1 = decoder.decodeBuffer(imgsrc);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        //获取图片的名字
        String fileName = StaticResourceService.SHSRES_IMG + UUID.randomUUID().toString() + ".png";
        URI uri =staticResourceService.uploadResource(fileName, bais);
        result.setCode(1);
        result.setMessage(uri.getPath());
        return result;
    }
}
