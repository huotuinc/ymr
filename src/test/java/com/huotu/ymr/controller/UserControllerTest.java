package com.huotu.ymr.controller;

import com.huotu.common.base.StringHelper;
import com.huotu.ymr.base.Device;
import com.huotu.ymr.base.DeviceType;
import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.BootConfig;
import com.huotu.ymr.boot.MallBootConfig;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.mallentity.MallMerchant;
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallrepository.MallMerchantRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.ManagerService;
import com.huotu.ymr.service.ShareService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by lgh on 2015/12/1.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
public class UserControllerTest extends SpringBaseTest {
    @Autowired
    private WebApplicationContext context;

    private Device device;
    private String mockUserName;
    private String mockUserPassword;
    private User mockUser;
    private MallUser mockMallUser;
    private MallMerchant mockMerchant;

    @Autowired
    private UserRepository mockUserRepository;
    @Autowired
    private MallUserRepository mockMallUserRepository;
    @Autowired
    private MallMerchantRepository mockMallMerchantRepository;

    @Autowired
    private ShareService shareService;

    @Autowired
    private PraiseRepository praiseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ManagerService managerService;


    @Before
    public void init() {
        device = Device.newDevice(DeviceType.Android);
        Random random = new Random();
        mockUserName = StringHelper.randomNo(random, 12);
        mockUserPassword = UUID.randomUUID().toString().replace("-", "");

        mockMerchant = generateMerchant(mockMallMerchantRepository);

        mockMallUser = generateMallUser(mockUserName, mockUserPassword, mockMallUserRepository, mockMerchant);
        mockUser = generateUserWithToken(mockMallUser, mockUserRepository);

        device.setToken(mockUser.getToken());
    }


    @Test
    public void testLogin() throws Exception {

    }

    @Test
    public void testSendSMS() throws Exception {
        String result=mockMvc.perform(device.getApi("sendSMS")
                .param("phone",  "18067253205")
                .param("type", "0").build())
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    public void testBindMobile() throws Exception {

    }

    @Test
    public void testGetPraiseList() throws Exception {
        Share share=new Share();
        share.setTime(new Date());
        share.setTitle("测试1");
        share=shareService.saveShare(share);


        Praise praise=new Praise();
        praise.setUser(mockUser);
        praise.setShare(share);
        praise=praiseRepository.save(praise);


        String result=mockMvc.perform(device.getApi("getPraiseList")
                .param("userId",  mockUser.getId()+"")
                .param("lastId", "0").build())
                .andReturn().getResponse().getContentAsString();

    }




    @Test
    public void testgetPraiseList() throws Exception {
        Share share=new Share();
        share.setTime(new Date());
        share.setTitle("测试1");
        share=shareService.saveShare(share);


        Praise praise=new Praise();
        praise.setUser(mockUser);
        praise.setShare(share);
        praise=praiseRepository.save(praise);


        String result=mockMvc.perform(device.getApi("getPraiseList")
                .param("userId",  mockUser.getId()+"")
                .param("lastId", "0").build())
                .andReturn().getResponse().getContentAsString();

    }
    @Test
    @Rollback(false)
    public void saveReports(){
        Report report=new Report();
        ShareComment shareComment=new ShareComment();
        shareComment.setCommentName("评论1");
        shareComment.setContent("这是评论1！");
        shareComment=shareCommentRepository.saveAndFlush(shareComment);
        User user=userRepository.findOne(1043429L);

        for(int i=0;i<50;i++) {
            Report report1=new Report();
            report1.setOwner(user);
            report1.setShareComment(shareComment);
            report1.setTime(new Date());
            report1.setHasSolved(0);
            report1.setTo(user);
            report1.getTo().setUserStatus(CommonEnum.UserStatus.normal);
            report1 = reportRepository.saveAndFlush(report1);
        }
    }
    @Test
    @Rollback(false)
    public void saveOneLogin(){
       Manager manager=new Manager();
        manager.setLoginName("3");
        String password = "123456";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        manager.setPassword(hashedPassword);
        //manager.setManagerField();
        managerRepository.saveAndFlush(manager);


    }

}