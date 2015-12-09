/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.base;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.mallentity.MallMerchant;
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallrepository.MallMerchantRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.UUID;


/**
 * Created by lgh on 2015/8/28.
 */

public class SpringBaseTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void createMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    protected String createToken() {

        return UUID.randomUUID().toString().replace("-", "");
    }


    protected MallMerchant generateMerchant(MallMerchantRepository merchantRepository) {
        String token = createToken();
        MallMerchant merchant = new MallMerchant();
        merchant.setPassword("");
        merchant.setMobile("");
        merchant.setName(UUID.randomUUID().toString());
        merchant.setNickName("伙伴商城abc");
        merchant = merchantRepository.saveAndFlush(merchant);


        return merchant;
    }

    protected MallUser generateMallUser(String mockUserName, String mockUserPassword, MallUserRepository mockMallUserRepository
            , MallMerchant mockMerchant) {
        MallUser mallUser = new MallUser();
        mallUser.setMobile("13845765123");
        mallUser.setMerchant(mockMerchant);
        mallUser.setPassword(mockUserPassword);
        mallUser.setRealName("realname");
        mallUser.setRegTime(new Date());
        mallUser.setType(0);
        mallUser.setUserFace("");
        mallUser.setUsername(mockUserName);
        mallUser.setWxHeadUrl("");
        mallUser.setWxNickName("wxnickname");
        return mockMallUserRepository.saveAndFlush(mallUser);
    }

    protected User generateUserWithToken(MallUser mockMallUser, UserRepository mockUserRepository) {
        User user = new User();
        user.setId(mockMallUser.getId());
        user.setScore(0);
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        user.setUserLevel(CommonEnum.UserLevel.one);
        return mockUserRepository.saveAndFlush(user);
    }
}
