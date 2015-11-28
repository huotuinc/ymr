/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.interceptor;

/**
 * 更低级别的api响应
 * @author CJ
 */
public class PhysicalApiResult {

    /**
     *
     系统状态返回：1，成功;0，失败
     */
    private int systemResultCode;
    /**
     *
     成功/失败描述
     */
    private String systemResultDescription;
    /**
     *
     逻辑状态返回 ：1成功,0 失败
     */
    private int resultCode;

    /**
     * 逻辑状态描述
     */

    private String resultDescription;
    /**
     * 返回具体数据
     */
    private Object resultData;


    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public int getSystemResultCode() {
        return systemResultCode;
    }

    public void setSystemResultCode(int systemResultCode) {
        this.systemResultCode = systemResultCode;
    }

    public String getSystemResultDescription() {
        return systemResultDescription;
    }

    public void setSystemResultDescription(String systemResultDescription) {
        this.systemResultDescription = systemResultDescription;
    }
}