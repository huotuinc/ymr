package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Article;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.ManagerRepository;
import com.huotu.ymr.service.ArticleService;
import com.huotu.ymr.service.CategoryService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xhk on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ConfigManagerController {

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    CategoryService categoryService;



    /**
     * 跳转到编辑退款页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/configRefund", method = RequestMethod.GET)
    public String configRefund(Article article, Model model) throws Exception {
        model.addAttribute("articleTypes", CommonEnum.ArticleType.values());
        return "manager/config/alterConfig"; //todo 返回上传状态
    }




}
