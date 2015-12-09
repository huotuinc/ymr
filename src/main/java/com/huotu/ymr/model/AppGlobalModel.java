package com.huotu.ymr.model;

import com.huotu.ymr.service.CommonConfigService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    private CommonConfigService commonConfigService;

    //todo run
    @Autowired
    public void init() {

    }

    //todo run
    @PostConstruct
    public void afterConstruct() {
        customerServicePhone = commonConfigService.getCustomerServicePhone();

    }
}
