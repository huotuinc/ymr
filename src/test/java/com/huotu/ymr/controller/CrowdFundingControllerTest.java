package com.huotu.ymr.controller;

import com.huotu.common.base.StringHelper;
import com.huotu.ymr.base.Device;
import com.huotu.ymr.base.DeviceType;
import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.BootConfig;
import com.huotu.ymr.boot.MallBootConfig;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.mallentity.MallMerchant;
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallrepository.MallMerchantRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.CrowdFundingService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xhk on 2015/12/3.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
public class CrowdFundingControllerTest extends SpringBaseTest {


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

    @Autowired
    ConfigRepository configRepository;

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

        Config config=new Config(); //todo 注释
        config.setKey("CrowdFundingTip");
        config.setValue("我有A万，找人合作筹募");
        config=configRepository.saveAndFlush(config);

        Config config1=new Config();
        config1.setKey("MoneyToScore");
        config1.setValue("10");
        config1=configRepository.saveAndFlush(config1);

    }

    //用户的等级
    CommonEnum.UserLevel [] userLevels={CommonEnum.UserLevel.one,CommonEnum.UserLevel.two,CommonEnum.UserLevel.three};

    //众筹项目的存储
    public List<CrowdFunding> saveCrowdFunding(){
        List<CrowdFunding> crowdFundings=new ArrayList<CrowdFunding>();
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setName("crowd");
        crowdFunding.setStartTime(new Date());
        crowdFunding.setContent("这个是众筹项目0");
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
        crowdFundings.add(crowdFunding);

        CrowdFunding crowdFunding1=new CrowdFunding();
        crowdFunding1.setName("crowd1");
        crowdFunding1.setStartTime(new Date());
        crowdFunding1.setContent("这个是众筹项目1");
        List<CommonEnum.UserLevel> userLevelList1=new ArrayList<CommonEnum.UserLevel>();
        userLevelList1.add(userLevels[0]);
        userLevelList1.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList1);
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
            crowdFundingPublic.setStatus(1);//都设置为审核通过
            crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            crowdFundingPublicList.add(crowdFundingPublic);
        }
        return crowdFundingPublicList;
    }
    //规律的中文名字存储认购人的存储
    private List<CrowdFundingPublic> saveChineseCrowdFundingPublic(List<CrowdFunding> crowdFundings) {
        List<CrowdFundingPublic> crowdFundingPublicList=new ArrayList<CrowdFundingPublic>();
        String[] chinese={"徐和","和康","徐康"};
        for(int i=0;i<80;i++) {
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            String name = UUID.randomUUID().toString();
            crowdFundingPublic.setName(name+chinese[i%3]);
            crowdFundingPublic.setMoney(50000.00);
            crowdFundingPublic.setStatus(1);//都设置为审核通过
            crowdFundingPublic.setCrowdFunding(crowdFundings.get((int) (Math.random() * 2)));
            crowdFundingPublic.setOwnerId(Long.parseLong(i + ""));
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            crowdFundingPublicList.add(crowdFundingPublic);
        }
        return crowdFundingPublicList;
    }


    //存储一个用户
    public List<User> saveUsers(){
        List<User> userList=new ArrayList<User>();
        for(int i=0;i<5;i++) {
            User user = new User();
            String token = UUID.randomUUID().toString().replace("-", "");
            user.setScore(0);
            user.setToken(token);
            user.setId(Long.parseLong(1+i+""));//todo 用户信息完善
            user.setUserLevel(CommonEnum.UserLevel.three);
            user = userRepository.saveAndFlush(user);
            userList.add(user);
        }
        return userList;
    }

    //通过传入的参数来构建相应数量的带中文众筹项目
    public List<CrowdFunding> saveSeveralCrowdFunding(int number) {
        List<CrowdFunding> crowdFundings=new ArrayList<CrowdFunding>();
        String[] chinese={"徐和","和康","徐康"};
        CommonEnum.CrowdFundingType[]  cFType={CommonEnum.CrowdFundingType.booking,CommonEnum.CrowdFundingType.cooperation,CommonEnum.CrowdFundingType.subscription};
        CommonEnum.CheckType[] checkTypes={CommonEnum.CheckType.audit,CommonEnum.CheckType.close,CommonEnum.CheckType.pass,CommonEnum.CheckType.open,CommonEnum.CheckType.notPass};
        for(int i=0;i<number;i++){
            CrowdFunding crowdFunding=new CrowdFunding();
            String name=UUID.randomUUID().toString();
            crowdFunding.setName(name+chinese[i%3]);
            crowdFunding.setStartTime(new Date());
            List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
            userLevelList.add(userLevels[0]);
            userLevelList.add(userLevels[1]);
            crowdFunding.setVisibleLevel(userLevelList);
            crowdFunding.setTime(new Date());
            crowdFunding.setCheckStatus(checkTypes[i % 5]);
            crowdFunding.setContent("这个是众筹项目" + name);
            crowdFunding.setCrowdFundingType(cFType[i % 3]);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            crowdFundings.add(crowdFunding);
        }
        return crowdFundings;
    }

    /*
    * 1.判断众筹项目是否多余60，否则存入60
    * 2.然后请求lastId为null时的list，即第一页的数据,并判断是否是想要的结果
    * 3.进行第一页后的请求（正常请求），并判断是否是想要的结果
    * 4.请求最后一页的下一页，并判断是否是想要的结果
    * 5.请求不存在的众筹项目，并判断是否是想要的结果
     */
    @Test
    //@Rollback(false)
    public void testGetCrowdFundingList() throws Exception {

        //创建一个用户用户
//        AppPublicModel appPublicModel=new AppPublicModel();
//        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
//        appUserInfoModel.setUserLevel(CommonEnum.UserLevel.three);
//        appPublicModel.setCurrentUser(appUserInfoModel);
//        PublicParameterHolder.put(appPublicModel);
//        appUserInfoModel= PublicParameterHolder.get().getCurrentUser();
        //进行众筹的存贮
        List<CrowdFunding> crowdFundings=crowdFundingRepository.findAll();
        if(crowdFundings.size()<60){
            saveSeveralCrowdFunding(60);
            crowdFundings=crowdFundingRepository.findAll();
        }

        String key="和";
        //进行众筹列表第一页的请求
        String result=mockMvc.perform(device.getApi("getCrowdFundingList").param("key", key).build())
                .andReturn().getResponse().getContentAsString();
        key="和";
        List<CrowdFunding> crowdFunding=crowdFundingService.searchCrowdFundingList(key, crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        System.out.println(result);

        System.out.println(crowdFunding);

        for(int i=0;i<crowdFunding.size();i++) {
//            Assert.assertEquals("请求众筹表第一页pid断言", crowdFunding.get(i).getOwnerId().longValue(), Long.parseLong(list.get(i).get("pid") + ""));
//            Assert.assertEquals("请求众筹表第一页time断言",crowdFunding.get(i).getTime().getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求众筹表第一页name断言",crowdFunding.get(i).getName(),list.get(i).get("title"));
        }

        //进行众筹列表下页页的请求
        String result1=mockMvc.perform(device.getApi("getCrowdFundingList").param("key", key).param("lastId",crowdFundings.get(20).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFunding> crowdFunding1=crowdFundingService.searchCrowdFundingList(key, crowdFundings.get(20).getId(), 10);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        for(int i=0;i<crowdFunding1.size();i++) {
//            Assert.assertEquals("请求众筹表下页pid断言", crowdFunding1.get(i).getOwnerId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
//            Assert.assertEquals("请求众筹表下页time断言",crowdFunding1.get(i).getTime().getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求众筹表下页name断言", crowdFunding1.get(i).getName(), list1.get(i).get("title"));
        }
        System.out.println(result1);

        //进行众筹列表最后一页的请求
        String result2=mockMvc.perform(device.getApi("getCrowdFundingList").param("key", key).param("lastId",0+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFunding> crowdFunding2=crowdFundingService.searchCrowdFundingList(key, 0L, 10);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        for(int i=0;i<crowdFunding2.size();i++) {
//            Assert.assertEquals("请求众筹列表下页pid断言", crowdFunding2.get(i).getOwnerId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
//            Assert.assertEquals("请求众筹表下页time断言",crowdFunding2.get(i).getTime().getTime(),list2.get(i).get("time"));
            Assert.assertEquals("请求众筹表下页name断言", crowdFunding2.get(i).getName(), list2.get(i).get("title"));
        }

        System.out.println(result2);
        key="罗";
        //进行众筹列表不存在页的请求
        long maxId=0;
        for(CrowdFunding funding:crowdFundings){
            if(funding.getId()>maxId){
                maxId=funding.getId();
            }
        }
        String result3=mockMvc.perform(device.getApi("getCrowdFundingList").param("key", key).param("lastId",-1 + "").build())
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
        Assert.assertEquals("请求众筹表下页条数断言", 0, list3.size());
    }


    /*
    * 1.进行众筹项目的条数判断，是否大于2，否则进行存入
    * 2.进行众筹项目信息的请求，并断言是否正确
     */
    @Test
    public void testGetCrowFindingInfo() throws Exception {

        //进行众筹项目的预处理
        List<CrowdFunding> crowdFundings=crowdFundingRepository.findAll();
        if(crowdFundings.size()<2){
            saveCrowdFunding();
            crowdFundings=crowdFundingRepository.findAll();
        }

        //进行众筹项目信息请求
        String result=mockMvc.perform(device.getApi("getCrowFindingInfo").param("id",crowdFundings.get(0).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
        HashMap map = JsonPath.read(result, "$.resultData.data");
        Assert.assertEquals("断言众筹项目的id",crowdFundings.get(0).getId()+"",map.get("pid")+"");
        Assert.assertEquals("断言众筹项目的name",crowdFundings.get(0).getName(),map.get("title"));
        Assert.assertEquals("断言众筹项目的content",crowdFundings.get(0).getContent(),map.get("content"));
    }

    /*
    * 1.进行用户存在判断，不存在存入两个用户
    * 2.存入一个众筹项目
    * 3.进行正常的预约请求，并断言
    * 4.进行用户金额输入错误断言
    * 5.进行手机号码输入错误断言
     */
    @Test
    public void testRaiseBooking() throws Exception {

        //进行用户存在判断
        List<User> users=userRepository.findAll();
        if(users.size()<=0){
            saveUsers();
            users=userRepository.findAll();
        }
        //进行合作项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("预约项目0");
        crowdFunding.setStartTime(new Date());
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是预约项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.booking);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        double lastMoney=50000.00;
        CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
        crowdFundingPublic.setMoney(lastMoney);
        crowdFundingPublic.setStatus(1);//都设置为审核通过
        crowdFundingPublic.setPhone(13852+""+108585+"");
        crowdFundingPublic.setRemark("我要发起预约hahaha!");
        Long userId=mockUser.getId();
        //crowdFundingPublic.setAgencyFee(50000.00 - lastMoney);
        crowdFundingPublic.setName("预约者");
        //正常请求
        String result11=mockMvc.perform(device.getApi("raiseBooking")
                .param("money", 50000 + "")
                .param("name", crowdFundingPublic.getName())
                .param("phone", crowdFundingPublic.getPhone())
                .param("remark", crowdFundingPublic.getRemark())
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();
        System.out.print(result11);

        //获取请求后的合作数据表信息进行断言
        CrowdFundingPublic crowdFCheck=new CrowdFundingPublic();
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        for(CrowdFundingPublic crowF:crowdFundingPublicList){
            if(crowF.getCrowdFunding()!=null) {
                if (crowF.getCrowdFunding().getId() == crowdFunding.getId()&&crowF.getOwnerId()==userId) {
                    crowdFCheck = crowF;
                }
            }
        }

        Assert.assertEquals("提交预约，断言Money", lastMoney+"", (double) crowdFCheck.getMoney()+"");
        Assert.assertEquals("提交预约，断言Phone", crowdFundingPublic.getPhone(), crowdFCheck.getPhone());
        Assert.assertEquals("提交预约，断言Name", crowdFundingPublic.getName(), crowdFCheck.getName());
        Assert.assertEquals("提交预约，断言Remark", crowdFundingPublic.getRemark(), crowdFCheck.getRemark());

        //同一个项目不能多次请求报错
        String result111=mockMvc.perform(device.getApi("raiseBooking")//todo 因为不能重复请求，所以测试只能一个一个测
                .param("money", 40000 + "")
                .param("phone",13852+""+108585+"")
                .param("remark", "我要发起预约hahaha!")
                .param("crowdId",crowdFunding.getId()+"")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception111 = JsonPath.read(result111, "$.systemResultDescription");
        Assert.assertEquals("同一个项目不能多次请求报错","同一个项目不能多次请求",exception111);

        //预约金额小于起购金额报错
        String result=mockMvc.perform(device.getApi("raiseBooking")
                .param("money", 40000 + "")
                .param("phone",13852+""+108585+"")
                .param("remark", "我要发起预约hahaha!")
                .param("crowdId",crowdFunding.getId()+"")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception = JsonPath.read(result, "$.systemResultDescription");
        Assert.assertEquals("预约金额小于起购金额报错断言","认购金额小于起购金额",exception);

        //预约手机格式错误报错
        String result1=mockMvc.perform(device.getApi("raiseBooking")
                .param("money", 50000 + "")
                .param("phone", 1385210 + "aa")
                .param("remark", "我要发起预约hahaha!")
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception1 = JsonPath.read(result1, "$.systemResultDescription");
        Assert.assertEquals("预约手机格式错误报错","手机格式错误",exception1);
 }

    @Test
    public void testCallBackWeiXin() throws Exception {

    }

    @Test
    public void testCallBackAlipay() throws Exception {

    }

    /*
    * 1.判断数据库中是否有2条众筹项目的数据，没有则存入2条
    * 2.判断数据库是否有80条众筹合作者数据，没有则存入80条
    * 3.然后请求lastId为null时的list，即第一页的数据,并判断是否是想要的结果
    * 4.进行第一页后的请求（正常请求），并判断是否是想要的结果
    * 5.请求最后一页的下一页，并判断是否是想要的结果
    * 6.请求不存在的预约列表，并判断是否是想要的结果
     */
    @Test
    @Rollback(false)
    public void testGetBookingList() throws Exception {

        //进行众筹和预约者的存贮
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
        //进行预约者列表第一页的请求
        String result=mockMvc.perform(device.getApi("getBookingList").param("crowdId",crowdFundings.get(0).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics.size();i++) {
            Assert.assertEquals("请求预约者列表第一页pid断言", crowdFundingPublics.get(i).getId().longValue(), Long.parseLong(list.get(i).get("pid") + ""));
            Assert.assertEquals("请求预约者列表第一页time断言",crowdFundingPublics.get(i).getTime().getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求预约者列表第一页name断言",crowdFundingPublics.get(i).getName(),list.get(i).get("name"));
        }
        //进行预约者列表下页页的请求
        String result1=mockMvc.perform(device.getApi("getBookingList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",crowdFundingPublicList.get(40).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics1=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingPublicList.get(40).getId(), 10);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics1.size();i++) {
            Assert.assertEquals("请求预约者列表下页pid断言", crowdFundingPublics1.get(i).getId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
            Assert.assertEquals("请求预约者列表下页time断言",crowdFundingPublics1.get(i).getTime().getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求预约者列表下页name断言", crowdFundingPublics1.get(i).getName(), list1.get(i).get("name"));
        }

        //进行认购者列表最后一页的请求
        String result2=mockMvc.perform(device.getApi("getBookingList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",0+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics2=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(),0L, 10);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics2.size();i++) {
            Assert.assertEquals("请求预约者列表下页pid断言", crowdFundingPublics2.get(i).getId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
            Assert.assertEquals("请求预约者列表下页time断言",crowdFundingPublics2.get(i).getTime().getTime(),list2.get(i).get("time"));
            Assert.assertEquals("请求预约者列表下页name断言", crowdFundingPublics2.get(i).getName(), list2.get(i).get("name"));
        }

        //进行认购者列表不存在页的请求
        long maxId=0;
        for(CrowdFunding funding:crowdFundings){
            if(funding.getId()>maxId){
                maxId=funding.getId();
            }
        }
        String result3=mockMvc.perform(device.getApi("getBookingList").param("crowdId",(maxId+1)+"").build())
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
        Assert.assertEquals("请求预约者列表下页条数断言", 0, list3.size());

    }

    /*
    * 1.进行用户存在的判断，不存在则存入一个用户
    * 2.存储一个合作项目
    * 3.提交发起合作请求
    * 4.进行用户金额输入错误断言
    * 5.进行手机号码输入错误断言
     */
    @Test
    public void testRaiseCooperation() throws Exception {

        //进行用户存在判断
        List<User> users=userRepository.findAll();
        if(users.size()<=0){
            saveUsers();
            users=userRepository.findAll();
        }
        //进行合作项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("合作项目0");
        crowdFunding.setStartTime(new Date());
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是合作项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.booking);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        double lastMoney=50000.00*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
        crowdFundingPublic.setMoney(lastMoney);
        crowdFundingPublic.setPhone(13852+""+108585+"");
        crowdFundingPublic.setStatus(1);//都设置为审核通过
        crowdFundingPublic.setRemark("我要发起合作hahaha!");
        Long userId=mockUser.getId();
        crowdFundingPublic.setAgencyFee(50000.00 - lastMoney);
        crowdFundingPublic.setName("发起合作者");
        //正常请求
        mockMvc.perform(device.getApi("raiseCooperation")
                .param("money", 50000 + "")
                .param("name", crowdFundingPublic.getName())
                .param("phone", crowdFundingPublic.getPhone())
                .param("remark", crowdFundingPublic.getRemark())
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();

        //获取请求后的合作数据表信息进行断言
        CrowdFundingPublic crowdFCheck=new CrowdFundingPublic();
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        for(CrowdFundingPublic crowF:crowdFundingPublicList){
            if(crowF.getCrowdFunding()!=null) {
                if (crowF.getCrowdFunding().getId() == crowdFunding.getId()&&crowF.getOwnerId()==userId) {
                    crowdFCheck = crowF;
                }
            }
        }
        Assert.assertEquals("提交合作，断言Money", lastMoney+"", (double) crowdFCheck.getMoney()+"");
        Assert.assertEquals("提交合作，断言Phone", crowdFundingPublic.getPhone(), crowdFCheck.getPhone());
        Assert.assertEquals("提交合作，断言Name", crowdFundingPublic.getName(), crowdFCheck.getName());
        Assert.assertEquals("提交合作，断言Remark", crowdFundingPublic.getRemark(), crowdFCheck.getRemark());

        //合作金额小于起购金额报错
        String result=mockMvc.perform(device.getApi("raiseCooperation")
                .param("money", 40000 + "")
                .param("phone",13852+""+108585+"")
                .param("remark", "我要合作hahaha!")
                .param("crowdId",crowdFunding.getId()+"")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception = JsonPath.read(result, "$.systemResultDescription");
        Assert.assertEquals("合作金额小于起购金额报错断言","认购金额小于起购金额",exception);

        //合作手机格式错误报错
        String result1=mockMvc.perform(device.getApi("raiseCooperation")
                .param("money", 50000 + "")
                .param("phone", 1385210 + "aa")
                .param("remark", "我要合作hahaha!")
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception1 = JsonPath.read(result1, "$.systemResultDescription");
        Assert.assertEquals("合作手机格式错误报错","手机格式错误",exception1);

    }

    /*
* 1.判断众筹项目是否多于2，否则存入2
* 2.然后请求lastId为null时的list，即第一页的数据,并判断是否是想要的结果
* 3.进行第一页后的请求（正常请求），并判断是否是想要的结果
* 4.请求最后一页的下一页，并判断是否是想要的结果
* 5.请求不存在的发起合作者，并判断是否是想要的结果
 */
    @Test
    @Rollback(false)
    public void testGetRaiseCooperationList() throws Exception {
        //进行众筹和中文认购者的存贮
        List<CrowdFunding> crowdFundings=crowdFundingRepository.findAll();
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        List<CrowdFunding> crowdFundingList1=new ArrayList<CrowdFunding>();
        for(CrowdFunding crowdFunding:crowdFundings){
            if(crowdFunding.getCrowdFundingType()==CommonEnum.CrowdFundingType.cooperation){
                crowdFundingList1.add(crowdFunding);
            }
        }
        if(crowdFundingList1.size()<2){
            saveCrowdFunding();
            crowdFundingList1=crowdFundingRepository.findAll();
        }
            saveChineseCrowdFundingPublic(crowdFundings);
            crowdFundingPublicList=crowdFundingPublicRepository.findAll();


        String key="和";
        //进行合作发起者列表第一页的请求
        String result=mockMvc.perform(device.getApi("getRaiseCooperationList").param("crowdId", crowdFundingList1.get(0).getId() + "").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundingList1.get(0).getId(), crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        System.out.println(result);
        for(int i=0;i<crowdFundingPublics.size();i++) {
            Assert.assertEquals("请求搜索的合作者发起列表第一页pid断言", crowdFundingPublics.get(i).getId().longValue(),Long.parseLong(list.get(i).get("pid") + ""));
           // Assert.assertEquals("请求搜索的合作者列表第一页time断言",crowdFundingPublics.get(i).getTime().getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求搜索的合作者发起列表第一页name断言",crowdFundingPublics.get(i).getName(),list.get(i).get("name"));
        }
        //进行合作发起者列表下页页的请求
        String result1=mockMvc.perform(device.getApi("getRaiseCooperationList").param("crowdId",crowdFundingList1.get(0).getId()+"").param("lastId",crowdFundingPublicList.get(40).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics1=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundingList1.get(0).getId(), crowdFundingPublicList.get(40).getId(), 10);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        System.out.println(result1);
        for(int i=0;i<crowdFundingPublics1.size();i++) {
            Assert.assertEquals("请求搜索的合作发起者列表下页pid断言", crowdFundingPublics1.get(i).getId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
           // Assert.assertEquals("请求搜索的合作者列表下页time断言",crowdFundingPublics1.get(i).getTime().getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求搜索的合作者发起列表下页name断言", crowdFundingPublics1.get(i).getName(), list1.get(i).get("name"));
        }

        //进行认购者列表最后一页的请求
        String result2=mockMvc.perform(device.getApi("getRaiseCooperationList").param("crowdId",crowdFundingList1.get(0).getId()+"").param("lastId",0+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics2=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundingList1.get(0).getId(), 0L, 10);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        System.out.println(result2);
        for(int i=0;i<crowdFundingPublics2.size();i++) {
            Assert.assertEquals("请求搜索的合作发起者列表下页pid断言", crowdFundingPublics2.get(i).getId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
           // Assert.assertEquals("请求搜索的合作者列表下页time断言",crowdFundingPublics2.get(i).getTime().getTime(),list2.get(i).get("time"));
            Assert.assertEquals("请求搜索的合作者发起列表下页name断言", crowdFundingPublics2.get(i).getName(), list2.get(i).get("name"));
        }

        //进行搜索的合作者列表不存在页的请求
        long maxId=0;
        for(CrowdFunding funding:crowdFundingList1){
            if(funding.getId()>maxId){
                maxId=funding.getId();
            }
        }
        key="罗";
        String result3=mockMvc.perform(device.getApi("getRaiseCooperationList")
                .param("crowdId", crowdFundingList1.get(0).getId() + "")
                .param("lastId",crowdFundingList1.get(40).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
        Assert.assertEquals("请求搜索的合作者列表下页条数断言", 0, list3.size());
    }

    /*
   * 1.进行用户存在判断，少于2个用户就新建5个
   * 2.进行众筹项目的存入
   * 3.进行众筹发起合作人的存入
   * 4.进行众筹合作人的存入
   * 5.进行正常的用户合作请求，并断言
    */
    @Test
    @Rollback(false)
    public void testGetCooperationResult() throws Exception {
        List<User> users=userRepository.findAll();
        if(users.size()<=2){
            saveUsers();
            users=userRepository.findAll();
        }
        //进行合作项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("合作项目0");
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding.setStartTime(new Date());
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是合作项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.subscription);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        //进行合作项目发起人存储
        List<CrowdFundingPublic> crowdFundingPublicList=new ArrayList<CrowdFundingPublic>();
        double money=50000.0;
        double lastMoney=money*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingPublic crowdFundingPublic=new CrowdFundingPublic();
        crowdFundingPublic.setAgencyFee(money-lastMoney);
        crowdFundingPublic.setMoney(lastMoney);
        crowdFundingPublic.setStatus(1);//都设置为审核通过
        crowdFundingPublic.setOwnerId(users.get(0).getId());
        crowdFundingPublic.setCrowdFunding(crowdFunding);
        crowdFundingPublic.setStatus(1);
        crowdFundingPublic.setName("我是合作发起者0");
        crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
        crowdFundingPublicList.add(crowdFundingPublic);

        CrowdFundingPublic crowdFundingPublic1=new CrowdFundingPublic();
        crowdFundingPublic1.setAgencyFee(money-lastMoney);
        crowdFundingPublic1.setMoney(lastMoney);
        crowdFundingPublic1.setStatus(1);//都设置为审核通过
        crowdFundingPublic1.setOwnerId(users.get(0).getId());
        crowdFundingPublic1.setCrowdFunding(crowdFunding);
        crowdFundingPublic1.setStatus(1);
        crowdFundingPublic1.setName("我是合作发起者1");
        crowdFundingPublic1=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic1);
        crowdFundingPublicList.add(crowdFundingPublic);

        //进行合作人的存储
        double bookMoney=10000.00*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingBooking crowdFundingBooking = new CrowdFundingBooking();
        crowdFundingBooking.setMoney(bookMoney);
        crowdFundingBooking.setPhone(13852+""+666666 + "");
        crowdFundingBooking.setRemark("我要与合作者hahaha!");
        crowdFundingBooking.setAgencyFee(10000.00 - bookMoney);
        crowdFundingBooking.setOwnerId(users.get(1).getId());
        crowdFundingBooking.setName( "合作者0");
        crowdFundingBooking.setCrowdFunding(crowdFunding);
        crowdFundingBooking.setCrowdFundingPublic(crowdFundingPublic);
        crowdFundingBooking.setStatus(1);
        crowdFundingBooking =crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking);

        CrowdFundingBooking crowdFundingBooking1 = new CrowdFundingBooking();
        crowdFundingBooking1.setMoney(bookMoney);
        crowdFundingBooking1.setPhone(13852+""+666666 + "");
        crowdFundingBooking1.setRemark("我要与合作者hahaha!");
        crowdFundingBooking1.setAgencyFee(10000.00 - bookMoney);
        crowdFundingBooking1.setOwnerId(users.get(1).getId());
        crowdFundingBooking1.setName( "合作者1");
        crowdFundingBooking1.setCrowdFunding(crowdFunding);
        crowdFundingBooking1.setCrowdFundingPublic(crowdFundingPublic1);
        crowdFundingBooking1.setStatus(1);
        crowdFundingBooking =crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking1);

        String result=mockMvc.perform(device.getApi("getCooperationResult")
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
        //List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        Assert.assertEquals("请求发起合作者的数量",crowdFundingPublicList.size(), list.size());
        List<HashMap> list1 =JsonPath.read(list.get(0),"$.bookingListModels");
        Assert.assertEquals("请求合作者的数量", 1,list1.size());


    }


    /*
    * 1.进行用户存在判断，少于2个用户就新建5个
    * 2.进行众筹项目的存入
    * 3.进行众筹发起合作人的存入
    * 4.进行正常的用户合作请求，并断言
     */
    @Test
    @Rollback(false)
    public void testGoCooperation() throws Exception {
//
//        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
//        appUserInfoModel.setUserLevel(CommonEnum.UserLevel.one);
        //进行用户存在判断,不存在则创建5个用户
        List<User> users=userRepository.findAll();
        if(users.size()<=2){
            saveUsers();
            users=userRepository.findAll();
        }
        //进行合作项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("合作项目0");
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding.setStartTime(new Date());
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是合作项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.subscription);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        //进行合作项目发起人存储
        double money=50000.0;
        double lastMoney=money*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingPublic crowdFundingPublic=new CrowdFundingPublic();
        crowdFundingPublic.setAgencyFee(money-lastMoney);
        crowdFundingPublic.setMoney(lastMoney);
        crowdFundingPublic.setStatus(1);//都设置为审核通过
        crowdFundingPublic.setOwnerId(users.get(0).getId());
        crowdFundingPublic.setCrowdFunding(crowdFunding);
        crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);

        double bookMoney=10000.00*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingBooking crowdFundingBooking = new CrowdFundingBooking();
        crowdFundingBooking.setMoney(bookMoney);
        crowdFundingBooking.setPhone(13852+""+666666 + "");
        crowdFundingBooking.setRemark("我要与合作者hahaha!");
        crowdFundingBooking.setAgencyFee(10000.00 - bookMoney);
        //crowdFundingBooking.setOwnerId(users.get(1).getId());
        Long userId=mockUser.getId();
        crowdFundingBooking.setName( "合作者");
        //正常请求
        mockMvc.perform(device.getApi("goCooperation")
                .param("money", 10000.00 + "")
                .param("name",crowdFundingBooking.getName())
                .param("phone", crowdFundingBooking.getPhone())
                .param("remark", crowdFundingBooking.getRemark())
                .param("crowdId", crowdFunding.getId() + "")
                .param("crowdPublicId", crowdFundingPublic.getId() + "")
                .build())
                .andReturn();
        //获取请求后的认购数据表信息进行断言
        CrowdFundingBooking crowdFBCheck=new CrowdFundingBooking();
        List<CrowdFundingBooking> crowdFundingBookings=crowdFundingBookingRepository.findAll();
        //查找存入的合作者信息
        for(CrowdFundingBooking crowF:crowdFundingBookings){
            if(crowF.getCrowdFunding().getId()==crowdFunding.getId()&&crowF.getOwnerId()==userId){
                crowdFBCheck=crowF;
            }
        }
        Assert.assertEquals("提交合作请求，断言Money", bookMoney+"", crowdFBCheck.getMoney()+"");
        Assert.assertEquals("提交合作请求，断言name",crowdFundingBooking.getName(), crowdFBCheck.getName());
        Assert.assertEquals("提交合作请求，断言phone",crowdFundingBooking.getPhone(), crowdFBCheck.getPhone());
        Assert.assertEquals("提交合作请求，断言remark",crowdFundingBooking.getRemark(), crowdFBCheck.getRemark());
        Assert.assertEquals("提交合作请求，断言userId",userId, crowdFBCheck.getOwnerId());
        Assert.assertEquals("提交合作请求，断言crowdId",crowdFunding.getId(), crowdFBCheck.getCrowdFunding().getId());
        Assert.assertEquals("提交合作请求，断言crowdPublicId",crowdFundingPublic.getId(), crowdFBCheck.getCrowdFundingPublic().getId());


        //合作手机格式错误报错
        String result1=mockMvc.perform(device.getApi("goCooperation")
                .param("money", 50000 + "")
                .param("phone", 1385210 + "aa")
                .param("remark", "我要合作hahaha!")
                .param("crowdId", crowdFunding.getId() + "")
                .param("crowdPublicId", crowdFundingPublic.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception1 = JsonPath.read(result1, "$.systemResultDescription");
        Assert.assertEquals("合作手机格式错误报错","手机格式错误",exception1);

    }

    /*
    * 1.进行用户判定，如果不存在用户则创建5个
    * 2.存入一个认购项目
    * 3.进行正常认购请求，并断言
     */
    @Test
    public void testRaiseSubscription() throws Exception {

//        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
//        appUserInfoModel.setUserLevel(CommonEnum.UserLevel.one);
        //进行用户存在判断
        List<User> users=userRepository.findAll();
        if(users.size()<=0){
            saveUsers();
            users=userRepository.findAll();
        }
        //进行认购项目存储
        CrowdFunding crowdFunding=new CrowdFunding();
        crowdFunding.setToMoeny(200000.00);
        crowdFunding.setStartMoeny(50000.00);
        crowdFunding.setName("认购项目0");
        List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
        userLevelList.add(userLevels[0]);
        userLevelList.add(userLevels[1]);
        crowdFunding.setVisibleLevel(userLevelList);
        crowdFunding.setStartTime(new Date());
        crowdFunding.setAgencyFeeRate(10);
        crowdFunding.setContent("这是认购项目0");
        crowdFunding.setCrowdFundingType(CommonEnum.CrowdFundingType.subscription);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);

        double lastMoney=50000.00*(100-crowdFunding.getAgencyFeeRate())/100;
        CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
        crowdFundingPublic.setMoney(lastMoney);
        crowdFundingPublic.setPhone(138521+""+18585+"");
        crowdFundingPublic.setStatus(1);//都设置为审核通过
        crowdFundingPublic.setRemark("我要认购hahaha!");
        crowdFundingPublic.setAgencyFee(50000.00 - lastMoney);
        crowdFundingPublic.setName("认购者");
        Long userId=mockUser.getId();
        //正常请求
        mockMvc.perform(device.getApi("raiseSubscription")
                .param("money", 50000 + "")
                .param("name", crowdFundingPublic.getName())
                .param("phone", crowdFundingPublic.getPhone())
                .param("remark", crowdFundingPublic.getRemark())
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn();

        //获取请求后的认购数据表信息进行断言
        CrowdFundingPublic crowdFCheck=new CrowdFundingPublic();
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingPublicRepository.findAll();
        for(CrowdFundingPublic crowF:crowdFundingPublicList){
            if(crowF.getCrowdFunding()!=null) {
                if (crowF.getCrowdFunding().getId() == crowdFunding.getId()&&crowF.getOwnerId()==userId) {
                    crowdFCheck = crowF;
                }
            }
        }
        Assert.assertEquals("提交认购，断言Money", lastMoney+"", (double) crowdFCheck.getMoney()+"");
        Assert.assertEquals("提交认购，断言Phone", crowdFundingPublic.getPhone(), crowdFCheck.getPhone());
        Assert.assertEquals("提交认购，断言Name", crowdFundingPublic.getName(), crowdFCheck.getName());
        Assert.assertEquals("提交认购，断言Remark", crowdFundingPublic.getRemark(), crowdFCheck.getRemark());

        //认购金额小于认购金额报错
        String result=mockMvc.perform(device.getApi("raiseSubscription")
                .param("money", 40000 + "")
                .param("phone",13852+""+108585+"")
                .param("remark", "我要认购hahaha!")
                .param("crowdId",crowdFunding.getId()+"")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception = JsonPath.read(result, "$.systemResultDescription");
        Assert.assertEquals("认购金额小于起购金额报错断言","认购金额小于起购金额",exception);

        //合作手机格式错误报错
        String result1=mockMvc.perform(device.getApi("raiseSubscription")
                .param("money", 50000 + "")
                .param("phone", 1385210 + "aa")
                .param("remark", "我要认购hahaha!")
                .param("crowdId", crowdFunding.getId() + "")
                .build())
                .andReturn().getResponse().getContentAsString();
        String exception1 = JsonPath.read(result1, "$.systemResultDescription");
        Assert.assertEquals("认购手机格式错误报错","手机格式错误",exception1);
}


    /*
* 1.判断数据库中是否有2条众筹项目的数据，没有则存入2条
* 2.判断数据库是否有80条众筹合作者数据，没有则存入80条
* 3.然后请求lastId为null时的list，即第一页的数据,并判断是否是想要的结果
* 4.进行第一页后的请求（正常请求），并判断是否是想要的结果
* 5.请求最后一页的下一页，并判断是否是想要的结果
* 6.请求不存在的认购列表，并判断是否是想要的结果
 */
    @Test
    @Rollback(false)
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
        String result=mockMvc.perform(device.getApi("getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingService.getMaxId() + 1, 10);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics.size();i++) {
            Assert.assertEquals("请求认购者列表第一页pid断言", crowdFundingPublics.get(i).getId().longValue(), Long.parseLong(list.get(i).get("pid") + ""));
            Assert.assertEquals("请求认购者列表第一页time断言",crowdFundingPublics.get(i).getTime().getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求认购者列表第一页name断言",crowdFundingPublics.get(i).getName(),list.get(i).get("name"));
        }
        //进行认购者列表下页页的请求
        String result1=mockMvc.perform(device.getApi("getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",crowdFundingPublicList.get(40).getId()+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics1=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(), crowdFundingPublicList.get(40).getId(), 10);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics1.size();i++) {
            Assert.assertEquals("请求认购者列表下页pid断言", crowdFundingPublics1.get(i).getId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
            Assert.assertEquals("请求认购者列表下页time断言",crowdFundingPublics1.get(i).getTime().getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求认购者列表下页name断言", crowdFundingPublics1.get(i).getName(), list1.get(i).get("name"));
        }

        //进行认购者列表最后一页的请求
        String result2=mockMvc.perform(device.getApi("getSubscriptionList").param("crowdId",crowdFundings.get(0).getId()+"").param("lastId",0+"").build())
                .andReturn().getResponse().getContentAsString();
        List<CrowdFundingPublic> crowdFundingPublics2=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdFundings.get(0).getId(),0L, 10);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        for(int i=0;i<crowdFundingPublics2.size();i++) {
            Assert.assertEquals("请求认购者列表下页pid断言", crowdFundingPublics2.get(i).getId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
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
        String result3=mockMvc.perform(device.getApi("getSubscriptionList").param("crowdId",(maxId+1)+"").build())
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
            Assert.assertEquals("请求认购者列表下页条数断言", 0, list3.size());

    }

    //存入50草稿箱用来进行后台测试
    @Test
    //@Rollback(false)
    public void saveDraft() {
        int number=50;
        List<CrowdFunding> crowdFundings=new ArrayList<CrowdFunding>();
        String[] chinese={"徐和","和康","徐康"};
        CommonEnum.CrowdFundingType[]  cFType={CommonEnum.CrowdFundingType.booking,CommonEnum.CrowdFundingType.cooperation,CommonEnum.CrowdFundingType.subscription};
        //CommonEnum.CheckType[] checkTypes={CommonEnum.CheckType.audit,CommonEnum.CheckType.close,CommonEnum.CheckType.pass,CommonEnum.CheckType.open,CommonEnum.CheckType.notPass};
        for(int i=0;i<number;i++){
            CrowdFunding crowdFunding=new CrowdFunding();
            String name=UUID.randomUUID().toString();
            crowdFunding.setName(name+chinese[i%3]);
            crowdFunding.setStartTime(new Date());
            List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
            userLevelList.add(userLevels[0]);
            userLevelList.add(userLevels[1]);
            crowdFunding.setVisibleLevel(userLevelList);
            crowdFunding.setTime(new Date());
            crowdFunding.setCheckStatus(CommonEnum.CheckType.draft);
            crowdFunding.setContent("这个是众筹项目" + name);
            crowdFunding.setCrowdFundingType(cFType[i%3]);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            crowdFundings.add(crowdFunding);
        }
    }

    //详情测试数据存入
    @Test
    @Rollback(false)
    public void detailTest() {
        int number =2;
        List<CrowdFunding> crowdFundings=new ArrayList<CrowdFunding>();
        String[] chinese={"徐和","和康","徐康"};
        CommonEnum.CrowdFundingType[]  cFType={CommonEnum.CrowdFundingType.subscription,CommonEnum.CrowdFundingType.cooperation};
        CommonEnum.CheckType[] checkTypes={CommonEnum.CheckType.audit,CommonEnum.CheckType.close,CommonEnum.CheckType.pass,CommonEnum.CheckType.open,CommonEnum.CheckType.notPass};
        for(int i=0;i<number;i++){
            CrowdFunding crowdFunding=new CrowdFunding();
            String name=UUID.randomUUID().toString();
            crowdFunding.setName(name+chinese[i]);
            crowdFunding.setStartTime(new Date());
            List<CommonEnum.UserLevel> userLevelList=new ArrayList<CommonEnum.UserLevel>();
            userLevelList.add(userLevels[0]);
            userLevelList.add(userLevels[1]);
            crowdFunding.setVisibleLevel(userLevelList);
            crowdFunding.setTime(new Date());
            crowdFunding.setCheckStatus(checkTypes[i%5]);
            crowdFunding.setContent("这个是众筹项目" + name);
            crowdFunding.setCrowdFundingType(cFType[i%3]);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            crowdFundings.add(crowdFunding);
        }

        List<CrowdFundingPublic> crowdFundingPublicList=new ArrayList<CrowdFundingPublic>();

        for(int i=0;i<40;i++) {
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            String name = UUID.randomUUID().toString();
            crowdFundingPublic.setName(name);
            crowdFundingPublic.setCrowdFunding(crowdFundings.get(0));
            crowdFundingPublic.setOwnerId(Long.parseLong(i + ""));
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublic.setStatus(i%3);//都设置为审核通过
            crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            crowdFundingPublicList.add(crowdFundingPublic);
        }
        //进行合作项目发起人存储
        List<CrowdFundingPublic> crowdFundingPublicList1=new ArrayList<CrowdFundingPublic>();

        for(int i=0;i<10;i++) {
            double money = 50000.0;
            double lastMoney = money * (100 - 10) / 100;
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            crowdFundingPublic.setAgencyFee(money - lastMoney);
            crowdFundingPublic.setMoney(lastMoney);
            crowdFundingPublic.setStatus(i%3);//都设置为审核通过
            //crowdFundingPublic.setOwnerId(users.get(0).getId());
            crowdFundingPublic.setCrowdFunding(crowdFundings.get(1));
            crowdFundingPublic.setStatus(1);
            crowdFundingPublic.setName(UUID.randomUUID().toString());
            crowdFundingPublic = crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            crowdFundingPublicList1.add(crowdFundingPublic);
        }

        //进行合作人的存储
        for(int i=0;i<50;i++) {
            double bookMoney = 10000.00 * (100 - 10) / 100;
            CrowdFundingBooking crowdFundingBooking = new CrowdFundingBooking();
            crowdFundingBooking.setMoney(bookMoney);
            crowdFundingBooking.setPhone(13852 + "" + 666666 + "");
            crowdFundingBooking.setRemark("我要与合作者hahaha!");
            crowdFundingBooking.setAgencyFee(10000.00 - bookMoney);
            //crowdFundingBooking.setOwnerId(users.get(1).getId());
            crowdFundingBooking.setName("合作者0");
            crowdFundingBooking.setName(UUID.randomUUID().toString());
            crowdFundingBooking.setCrowdFunding(crowdFundings.get(1));
            crowdFundingBooking.setCrowdFundingPublic(crowdFundingPublicList1.get(i%10));
            crowdFundingBooking.setStatus(0);
            crowdFundingBooking = crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking);
        }


    }

    @Test
    @Rollback(false)
    public  void testSetConfig(){
        Config config=new Config();
        config.setKey(ConfigKey.GLOBALAGENCYFEE);
        config.setValue("0");
        config=configRepository.saveAndFlush(config);

        Config config1=new Config();
        config1.setKey(ConfigKey.APPID);
        config1.setValue("1111111111111");
        config1=configRepository.saveAndFlush(config1);
        Config config2=new Config();
        config2.setKey(ConfigKey.APPSECRET);
        config2.setValue("22222222222222222");
        config2=configRepository.saveAndFlush(config2);
        Config config3=new Config();
        config3.setKey(ConfigKey.MCHID);
        config3.setValue("3333333333333");
        config3=configRepository.saveAndFlush(config);


    }

}