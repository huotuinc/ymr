package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.CrowdFundingService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by xhk on 2015/12/3.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class CrowdFundingControllerTest extends SpringBaseTest {

    @Autowired
    CrowdFundingService crowdFundingService;

    @Autowired
    CrowdFundingPublicRepository crowdFundingPublicRepository;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;

    @Autowired
    CrowdFundingBookingRepository crowdFundingBookingRepository;

    @Autowired
    CrowdFundingMoneyRangeRepository crowdFundingMoneyRangeRepository;

    @Autowired
    UserRepository userRepository;

    //众筹项目的存储
    public List<CrowdFunding> saveCrowdFunding(){
        List<CrowdFunding> crowdFundings=new ArrayList<CrowdFunding>();
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setName("crowd");
        crowdFunding.setStartTime(new Date());
        crowdFunding.setContent("这个是众筹项目0");
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
        crowdFundings.add(crowdFunding);

        CrowdFunding crowdFunding1=new CrowdFunding();
        crowdFunding1.setName("crowd1");
        crowdFunding1.setStartTime(new Date());
        crowdFunding1.setContent("这个是众筹项目1");
        crowdFunding1=crowdFundingRepository.saveAndFlush(crowdFunding1);
        crowdFundings.add(crowdFunding1);

        return crowdFundings;
    }

    //认购人的存储
    public List<CrowdFundingPublic> saveCrowdFundingPublic(List<CrowdFunding> crowdFundings){
        List<CrowdFundingPublic> crowdFundingPublicList=new ArrayList<CrowdFundingPublic>();

        for(int i=0;i<80;i++) {
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            String name = UUID.randomUUID().toString();
            crowdFundingPublic.setName(name);
            crowdFundingPublic.setCrowdFunding(crowdFundings.get((int) (Math.random() * 2)));
            crowdFundingPublic.setOwnerId(Long.parseLong(i + ""));
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            crowdFundingPublicList.add(crowdFundingPublic);
        }
        return crowdFundingPublicList;
    }

    //存储一个用户
    public User saveOneUser(){
        User user=new User();
        String token=UUID.randomUUID().toString().replace("-", "");
        user.setScore(0);
        user.setToken(token);
        user.setId(1L);//todo 用户信息完善
        user.setUserLevel(CommonEnum.UserLevel.three);
        user=userRepository.saveAndFlush(user);
        return user;
    }

    @Test
    public void testGetCrowdFundingList() throws Exception {

    }

    @Test
    public void testGetCrowFindingInfo() throws Exception {

    }

    @Test
    public void testRaiseBooking() throws Exception {

    }

    @Test
    public void testPay() throws Exception {

    }

    @Test
    public void testCallBackWeiXin() throws Exception {

    }

    @Test
    public void testCallBackAlipay() throws Exception {

    }

    @Test
    public void testGetBookingList() throws Exception {

    }

    @Test
    public void testRaiseCooperation() throws Exception {

    }

    @Test
    public void testGetRaiseCooperationList() throws Exception {

    }

    @Test
    public void testGoCooperation() throws Exception {

    }

    @Test
    public void testRaiseSubscription() throws Exception {

        //进行用户存在判断
        List<User> users=userRepository.findAll();
        if(users.size()<=0){
            saveOneUser();
            users=userRepository.findAll();
        }
        //进行认购项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("认购项目0");
        crowdFunding.setStartTime(new Date());
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是认购项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.subscription);

        //删除认购中与要认购项目同名的项目
        List<CrowdFunding> crowdFundings=crowdFundingRepository.findAll();
        for(CrowdFunding crowF:crowdFundings){
            if(crowF.getName().equals(crowdFunding.getName())){
                crowdFundingRepository.delete(crowF);
            }
        }
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        //正常请求
        mockMvc.perform(get("/app/raiseSubscription")
                .param("money", 50000 + "")
                .param("phone", 13852108585.0 + "")
                .param("remark", "我要认购hahaha!")
                .param("crowdId", crowdFunding.getId() + "")
                .param("userId", users.get(0).getId() + ""))
                .andReturn();

        //获取请求后的认购数据表信息进行断言
        CrowdFunding crowdFCheck=new CrowdFunding();
        crowdFundings=crowdFundingRepository.findAll();
        for(CrowdFunding crowF:crowdFundings){
            if(crowF.getName().equals(crowdFunding.getName())){
                crowdFCheck=crowF;
            }
        }
        Assert.assertSame("提交认购，断言获取的对象与提交的对象是否相同", crowdFunding,crowdFCheck);

//        //认购金额小于起购金额报错
//        String result=mockMvc.perform(get("/app/raiseSubscription")
//                .param("money", 40000 + "")
//                .param("phone",13852108585.0+"")
//                .param("remark", "我要认购hahaha!")
//                .param("crowdId",crowdFunding.getId()+"")
//                .param("userId",users.get(0).getId()+""))
//                .andReturn().getResponse().getErrorMessage();
//        Assert.assertEquals("认购金额小于起购金额报错断言","认购金额小于起购金额",result);
//
//        //认购手机格式错误报错
//        String result1=mockMvc.perform(get("/app/raiseSubscription")
//                .param("money", 50000 + "")
//                .param("phone",138521085.0+"")
//                .param("remark", "我要认购hahaha!")
//                .param("crowdId",crowdFunding.getId()+"")
//                .param("userId",users.get(0).getId()+""))
//                .andReturn().getResponse().getErrorMessage();
//        Assert.assertEquals("认购手机格式错误报错","手机号码格式不正确",result1);
    }


    @Test
    public void testGetSubscriptionList() throws Exception {
        //进行众筹和认购者的存贮
        List<CrowdFunding> crowdFundings=crowdFundingRepository.findAll();
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        if(crowdFundings.size()<2){
            saveCrowdFunding();
            crowdFundings=crowdFundingRepository.findAll();
        }
        if(crowdFundingPublicList.size()<80){
            saveCrowdFundingPublic(crowdFundings);
            crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        }
        //进行认购者列表第一页的请求
        String result=mockMvc.perform(get("/app/getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+""))
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics.size();i++) {
            Assert.assertEquals("请求认购者列表第一页pid断言", crowdFundingPublics.get(i).getOwnerId().longValue(), Long.parseLong(list.get(i).get("pid") + ""));
            Assert.assertEquals("请求认购者列表第一页time断言",crowdFundingPublics.get(i).getTime().getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求认购者列表第一页name断言",crowdFundingPublics.get(i).getName(),list.get(i).get("name"));
        }
        //进行认购者列表下页页的请求
        String result1=mockMvc.perform(get("/app/getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",crowdFundingPublicList.get(40).getId()+""))
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics1=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingPublicList.get(40).getId(), 10);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics1.size();i++) {
            Assert.assertEquals("请求认购者列表下页pid断言", crowdFundingPublics1.get(i).getOwnerId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
            Assert.assertEquals("请求认购者列表下页time断言",crowdFundingPublics1.get(i).getTime().getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求认购者列表下页name断言", crowdFundingPublics1.get(i).getName(), list1.get(i).get("name"));
        }

        //进行认购者列表最后一页的请求
        String result2=mockMvc.perform(get("/app/getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",0+""))
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics2=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(),0L, 10);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics2.size();i++) {
            Assert.assertEquals("请求认购者列表下页pid断言", crowdFundingPublics2.get(i).getOwnerId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
            Assert.assertEquals("请求认购者列表下页time断言",crowdFundingPublics2.get(i).getTime().getTime(),list2.get(i).get("time"));
            Assert.assertEquals("请求认购者列表下页name断言", crowdFundingPublics2.get(i).getName(), list2.get(i).get("name"));
        }

        //进行认购者列表不存在页的请求
        long maxId=0;
        for(CrowdFunding funding:crowdFundings){
            if(funding.getId()>maxId){
                maxId=funding.getId();
            }
        }
        String result3=mockMvc.perform(get("/app/getSubscriptionList").param("crowdId",(maxId+1)+""))
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
            Assert.assertEquals("请求认购者列表下页条数断言", 0, list3.size());

    }
}