package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Article;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareGoods;
import com.huotu.ymr.exception.ShareNotExitsException;
import com.huotu.ymr.model.web.WebArticleModel;
import com.huotu.ymr.model.web.WebCrowdModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.CrowdFundingRepository;
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

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;


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

    /**
     *进入转发后的文章页面
     * @param articleId
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "articleInfo",method = RequestMethod.GET)
    public String getArticleInfo(@RequestParam(required = true)Long articleId,Model model) throws Exception {
        Article article=articleRepository.findOne(articleId);
        WebArticleModel articleModel=new WebArticleModel();
        articleModel.setContent(article.getContent());
        articleModel.setTitle(article.getTitle());
        articleModel.setTime(article.getTime());
        model.addAttribute("articleModel",articleModel);
        return "transmit/navv";
    }
    /**
     *进入转发后的项目页面
     * @param crowdId 众筹项目id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "crowdInfo",method = RequestMethod.GET)
    public String getCrowdInfo(@RequestParam(required = true)Long crowdId,Model model) throws Exception {
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        WebCrowdModel webCrowdModel=new WebCrowdModel();
        webCrowdModel.setContent(crowdFunding.getContent());
        webCrowdModel.setEndTime(crowdFunding.getEndTime());
        webCrowdModel.setStartMoeny(crowdFunding.getStartMoeny());
        webCrowdModel.setTitle(crowdFunding.getName());
        webCrowdModel.setToMoeny(crowdFunding.getToMoeny());
        model.addAttribute("crowdModel",webCrowdModel);
        return "transmit/zc";
    }

}
