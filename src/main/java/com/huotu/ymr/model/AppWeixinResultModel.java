package com.huotu.ymr.model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by xhk on 2015/7/30.
 */
public class AppWeixinResultModel {

    @XmlElement
    private String return_code;
    @XmlElement
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
