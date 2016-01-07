package com.huotu.ymr.model;

import com.huotu.ymr.service.CommonConfigService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by lgh on 2015/12/9.
 */
@Getter
@Setter
public class AppGlobalModel {
    /**
     * 客服电话号码
     */
    private String customerServicePhone;

    /**
     * 支付配置信息
     */
    /**
     * 公众账号ID
     */
    private String appId;

    private String appKey;//APPSECRET

    private String partnerId; //支付对象

    private String payType;//todo 注释补全 支付类型

    private String payTypeName;//todo 注释补全

    private String notify;//回调地址

    private String webPagePay;//todo 注释补全


    @Autowired
    private CommonConfigService commonConfigService;


    //todo run
    @Autowired
    public void init() {

        //todo 将其他参数也取出来
        //appId=configRepository.findOne(ConfigKey.APPID).getValue();
        //appKey=configRepository.findOne(ConfigKey.APPSECRET).getValue();
        //notify="http://192.168.1.41:8080/ymr/pay/callBackWeiXin";
        //payType= CommonEnum.PurchaseSource.WEIXIN; //todo 支付类型
    }

    //todo run
    @PostConstruct
    public void afterConstruct() {
        customerServicePhone = commonConfigService.getCustomerServicePhone();

    }
}
