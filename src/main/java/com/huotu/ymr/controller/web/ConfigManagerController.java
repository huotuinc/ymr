package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.entity.Config;
import com.huotu.ymr.model.backend.config.WeiXinConfigModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.ManagerRepository;
import com.huotu.ymr.service.ArticleService;
import com.huotu.ymr.service.CategoryService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    ConfigRepository configRepository;

    /**
     * 商家微信配置信息添加与修改
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveWeiXinConfig",method = RequestMethod.POST)
    public String saveWeiXinConfig(WeiXinConfigModel weiXinConfigModel, Model model,HttpServletRequest request) throws Exception {

        Config config=new Config();
        config.setKey(ConfigKey.MCHID);
        config.setValue(weiXinConfigModel.getMchid());
        config=configRepository.saveAndFlush(config);

        Config config1=new Config();
        config1.setKey(ConfigKey.APPID);
        config1.setValue(weiXinConfigModel.getAppid());
        config1=configRepository.saveAndFlush(config1);

        Config config2=new Config();
        config2.setKey(ConfigKey.APPSECRET);
        config2.setValue(weiXinConfigModel.getAppSecret());
        config2=configRepository.saveAndFlush(config2);

        Config config3=new Config();
//        URI uri=staticResourceService.getResource(weiXinConfigModel.getCelPath());
//        String celPath=uri.toURL().toString();
        config3.setKey(ConfigKey.CERTIFICATE); //todo 路径相对
        config3.setValue(weiXinConfigModel.getCelPath());
        config3=configRepository.saveAndFlush(config3);

        Config config4=new Config();
        config4.setKey(ConfigKey.PAYSIGNKEY);
        config4.setValue(weiXinConfigModel.getPaySignKey());
        config4=configRepository.saveAndFlush(config4);

        Config config5=new Config();
        config5.setKey(ConfigKey.CERTIFICATEKEY);
        config5.setValue(weiXinConfigModel.getCelKey());
        config5=configRepository.saveAndFlush(config5);

        return "manager/config/alterConfig";
    }

    /**
     *商家微信跳转
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/configRefund",method = RequestMethod.GET)
    public String configRefund(WeiXinConfigModel weiXinConfigModel, Model model) throws Exception {
       Config config= configRepository.findOne(ConfigKey.MCHID);
        if(config!=null) {
            weiXinConfigModel.setMchid(config.getValue());
        }
        Config config1= configRepository.findOne(ConfigKey.APPID);
        if(config1!=null) {
            weiXinConfigModel.setAppid(config1.getValue());
        }
        Config config2= configRepository.findOne(ConfigKey.APPSECRET);
        if(config2!=null) {
            weiXinConfigModel.setAppSecret(config2.getValue());
        }
        Config config3= configRepository.findOne(ConfigKey.CERTIFICATE);
        if(config3!=null) {
            weiXinConfigModel.setCelPath(config3.getValue());
        }
        Config config4= configRepository.findOne(ConfigKey.PAYSIGNKEY);
        if(config4!=null) {
            weiXinConfigModel.setPaySignKey(config4.getValue());
        }
        Config config5= configRepository.findOne(ConfigKey.CERTIFICATEKEY);
        if(config5!=null) {
            weiXinConfigModel.setCelKey(config5.getValue());
        }

        return "manager/config/alterConfig";
    }


}
