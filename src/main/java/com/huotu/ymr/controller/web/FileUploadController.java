package com.huotu.ymr.controller.web;

import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 富文本图片上传
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ajaxEditorFileUpload",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel fileUploadUeImage(MultipartHttpServletRequest request) throws Exception {
        ResultModel result=new ResultModel();
        MultipartFile file=request.getFile("imgFile");
        //取得扩展名
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String fileName = StaticResourceService.SHSRES_IMG + UUID.randomUUID().toString() + "."+fileExt;

        URI uri =staticResourceService.uploadResource(fileName, file.getInputStream());
        result.setCode(1);
        result.setError(0);
        result.setMessage("上传成功!");
        result.setUrl(uri.toString());
        return result;
    }


    /**
     * 帖子封面图片上传
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/UploadImg")
    @ResponseBody
    public ResultModel UploadImg(@RequestParam(value = "shareImage")MultipartFile shareImage,HttpServletResponse response) throws Exception{
        ResultModel resultModel=new ResultModel();
//        MultipartFile file=request.getFile("shareImage");
        //文件格式判断
        if (ImageIO.read(shareImage.getInputStream()) == null) {
            resultModel.setCode(0);
            resultModel.setMessage("请上传图片文件！");
            return resultModel;
        }
        if (shareImage.getSize() == 0) {
            resultModel.setCode(0);
            resultModel.setMessage("请上传图片！");
            return resultModel;
        }
        //保存图片
        String fileName = StaticResourceService.SHSRES_IMG + UUID.randomUUID().toString() + ".png";
        URI uri=staticResourceService.uploadResource(fileName, shareImage.getInputStream());
        response.setHeader("X-frame-Options", "SAMEORIGIN");
        resultModel.setCode(1);
        resultModel.setMessage(uri.toString());
        return resultModel;

    }
}
