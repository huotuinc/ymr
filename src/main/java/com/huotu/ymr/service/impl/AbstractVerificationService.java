/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.SysRegex;
import com.huotu.ymr.entity.VerificationCode;
import com.huotu.ymr.exception.InterrelatedException;
import com.huotu.ymr.repository.VerificationCodeRepository;
import com.huotu.ymr.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author CJ
 */
public abstract class AbstractVerificationService implements VerificationService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    /**
     * 允许间隔60
     */
    private int gapSeconds = 60;

    @Transactional
    public void sendCode(String mobile, VerificationProject project, String code, Date currentDate, CommonEnum.VerificationType type)
            throws IllegalStateException, IllegalArgumentException, NoSuchMethodException, InterrelatedException {
        if (!SysRegex.IsValidMobileNo(mobile)) {
            throw new IllegalArgumentException("号码不对");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByMobileAndType(mobile, type);
        if (verificationCode != null) {
            //刚刚发送过
            if (currentDate.getTime() - verificationCode.getSendTime().getTime() < gapSeconds * 1000) {
                throw new IllegalStateException("刚刚发过");
            }
        } else {
            verificationCode = new VerificationCode();
            verificationCode.setMobile(mobile);
            verificationCode.setType(type);
        }
        verificationCode.setSendTime(currentDate);
        verificationCode.setCode(code);
        verificationCode = verificationCodeRepository.save(verificationCode);

        doSend(project, verificationCode);
    }

    protected abstract void doSend(VerificationProject project, VerificationCode code) throws InterrelatedException;

    @Transactional
    public boolean verifyCode(String mobile, VerificationProject project, String code, Date currentDate, CommonEnum.VerificationType type) throws IllegalArgumentException {
        if (!SysRegex.IsValidMobileNo(mobile)) {
            throw new IllegalArgumentException("号码不对");
        }
        List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeAndSendTimeGreaterThan(mobile, type, new Date(currentDate.getTime() - gapSeconds * 1000));
        for (VerificationCode verificationCode : codeList) {
            if (verificationCode.getCode().equals(code))
                return true;
        }
        return false;
    }

    public int getGapSeconds() {
        return gapSeconds;
    }

    public void setGapSeconds(int gapSeconds) {
        this.gapSeconds = gapSeconds;
    }
}
