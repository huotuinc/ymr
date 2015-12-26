package com.huotu.ymr.model;

import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.repository.ConfigRepository;
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

    private String appKey;//todo 注释补全

    private String partnerId;

    private String payType;

    private String payTypeName;

    private String notify;

    private String webPagePay;


    @Autowired
    private CommonConfigService commonConfigService;

    @Autowired
    private ConfigRepository configRepository;

    //todo run
    @Autowired
    public void init() {

        //todo 将其他参数也取出来
        appId=configRepository.findOne(ConfigKey.APPID).getValue();
    }

    //todo run
    @PostConstruct
    public void afterConstruct() {
        customerServicePhone = commonConfigService.getCustomerServicePhone();

    }
}
