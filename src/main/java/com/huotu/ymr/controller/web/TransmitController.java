package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareGoods;
import com.huotu.ymr.exception.ShareNotExitsException;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.ShareGoodsRepository;
import com.huotu.ymr.service.ShareService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/transmit")
public class TransmitController {
    @Autowired
    ShareService shareService;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    ShareGoodsRepository shareGoodsRepository;


    /**
     * 进入转发后的爱分享页面
     * @param shareId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shareInfo",method = RequestMethod.GET)
    String showShareInfo(@RequestParam(required = true)Long shareId,Model model) throws Exception{
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            throw new ShareNotExitsException();
        }
        List<ShareGoods> shareGoods=shareGoodsRepository.findByShare(share);
        model.addAttribute("share",share);
        model.addAttribute("shareGoods",shareGoods);
        return "transmit/shareinfo";
    }

}
