package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.common.PublicParameterHolder;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.exception.*;
import com.huotu.ymr.model.*;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.CrowdFundingMoneyRangeService;
import com.huotu.ymr.service.CrowdFundingService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/app")
public class CrowdFundingController implements CrowdFundingSystem {



    @Autowired
    CommonConfigService commonConfigService;

    @Autowired
    CrowdFundingService crowdFundingService;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;

    @Autowired
    CrowdFundingPublicRepository crowdFundingPublicRepository;

    @Autowired
    CrowdFundingBookingRepository crowdFundingBookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    CrowdFundingMoneyRangeService crowdFundingMoneyRangeService;

    @Autowired
    MyCrowdFundingFlowRepository myCrowdFundingFlowRepository;

    @RequestMapping("/getCrowdFundingList")
    @Override
    public ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel[]> list,String key, Long lastId) throws Exception {

        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getCrowdFundingMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFunding> crowdFundings=crowdFundingService.searchCrowdFundingList(key,lastId,number);
        List<AppCrowdFundingListModel> appCrowdFundingListModels=new ArrayList<AppCrowdFundingListModel>();
        for(CrowdFunding crowdFunding:crowdFundings){
            AppCrowdFundingListModel appCrowdFundingListModel=new AppCrowdFundingListModel();
            appCrowdFundingListModel.setStartMoeny(crowdFunding.getStartMoeny());
            appCrowdFundingListModel.setToMoeny(crowdFunding.getToMoeny());
            appCrowdFundingListModel.setTitle(crowdFunding.getName());
            appCrowdFundingListModel.setTime(new Date());
            appCrowdFundingListModel.setType(crowdFunding.getCrowdFundingType());
            appCrowdFundingListModel.setCurrentBooking(crowdFunding.getCurrentBooking());
            appCrowdFundingListModel.setCurrentMoeny(crowdFunding.getCurrentMoeny());
            appCrowdFundingListModel.setPId(crowdFunding.getId());

            if(crowdFunding.getPuctureUrl()==null){
                appCrowdFundingListModel.setPuctureUrl(null);
            }else {
                URI uri = staticResourceService.getResource(crowdFunding.getPuctureUrl());
                appCrowdFundingListModel.setPuctureUrl(uri.toString());
            }
            //String path=uri.getPath();


            appCrowdFundingListModel.setSummary(crowdFunding.getContent());
            appCrowdFundingListModel.setStartTime(crowdFunding.getStartTime());
            appCrowdFundingListModel.setEndTime(crowdFunding.getEndTime());
            appCrowdFundingListModels.add(appCrowdFundingListModel);
        }
        list.outputData(appCrowdFundingListModels.toArray(new AppCrowdFundingListModel[crowdFundings.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/getCrowFindingInfo")
    @Override
    public ApiResult getCrowFindingInfo(Output<AppCrowdFundingModel> data, Long id) throws Exception {
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(id);
        Date date=new Date();
        AppCrowdFundingModel appCrowdFundingModel=new AppCrowdFundingModel();
        appCrowdFundingModel.setContent(crowdFunding.getContent());
        appCrowdFundingModel.setTotalBooking(crowdFunding.getTotalBooking());
        appCrowdFundingModel.setCurrentBooking(crowdFunding.getCurrentBooking());
        appCrowdFundingModel.setCurrentMoeny(crowdFunding.getCurrentMoeny());
        appCrowdFundingModel.setPId(crowdFunding.getId());
        appCrowdFundingModel.setTime(date);


        //返回众筹状态
        if(crowdFunding.getCrowdFundingType()==CommonEnum.CrowdFundingType.booking){
            if(crowdFunding.getStartTime().before(date)){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.notstarted);
            }else if(crowdFunding.getCurrentBooking()>=crowdFunding.getToBooking()){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.success);
            }else if(crowdFunding.getCurrentBooking()<crowdFunding.getToBooking()&&crowdFunding.getEndTime().before(date)){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.fail);
            }else if(crowdFunding.getCurrentBooking()<crowdFunding.getToBooking()){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.running);
            }
        }else{
            if(crowdFunding.getStartTime().before(date)){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.notstarted);
            }else if(crowdFunding.getCurrentMoeny()>=crowdFunding.getToMoeny()){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.success);
            }else if(crowdFunding.getCurrentMoeny()<crowdFunding.getToMoeny()&&crowdFunding.getEndTime().before(date)){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.fail);
            }else if(crowdFunding.getCurrentMoeny()<crowdFunding.getToMoeny()){
                appCrowdFundingModel.setPartnerStatue(CommonEnum.CrowdStatus.running);
            }
        }

        //项目的参与者头像
        if(crowdFunding.getCrowdFundingType()==CommonEnum.CrowdFundingType.cooperation&& appCrowdFundingModel.getPartnerStatue()==CommonEnum.CrowdStatus.success){
            //合作者与合作发起者按时间排序
            List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findSuccessCrowdsFromLastIdWithNumber(crowdFunding.getId(), crowdFundingService.getMaxId(), 10);
            List<CrowdFundingBooking> crowdFundingBookingList=crowdFundingService.findBookingFromLastIdWithNumber(crowdFunding.getId(),crowdFundingService.getBookingMaxId(),10);
            CompareModel[] compares=new CompareModel[crowdFundingBookingList.size()+crowdFundingPublicList.size()];
            int count=0;
            for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
                CompareModel compareModel=new CompareModel();
                compareModel.setHeadUrl(crowdFundingPublic.getUserHeadUrl());
                compareModel.setTime(crowdFundingPublic.getTime());
                compares[count]=compareModel;
                count++;
            }
            for(CrowdFundingBooking crowdFundingBooking:crowdFundingBookingList){
                CompareModel compareModel=new CompareModel();
                compareModel.setHeadUrl(crowdFundingBooking.getUserHeadUrl());
                compareModel.setTime(crowdFundingBooking.getTime());
                compares[count]=compareModel;
                count++;
            }
            //冒泡排序
            for(int i=0;i<compares.length;i++){
                for(int j=i+1;j<compares.length;j++){
                    if(compares[j].getTime().after(compares[j].getTime())){
                        CompareModel temp=compares[j];
                        compares[j]=compares[i];
                        compares[i]=temp;
                    }
                }
            }
            List<String> urls=new ArrayList<String>();
            if(compares.length<=10){
                for(int i=0;i<compares.length;i++){
                    if(compares[i].getHeadUrl()==null){
                        urls.add(null);// todo 理论上用户初始化就有一个默认头像，一定是有的
                    }else{
                        urls.add(staticResourceService.getResource(compares[i].getHeadUrl()).toString()); //todo 用户头像
                    }
                }
            }else{
                for(int i=0;i<10;i++){
                    if(compares[i].getHeadUrl()==null){
                        urls.add(null);// todo 理论上用户初始化就有一个默认头像，一定是有的
                    }else{
                        urls.add(staticResourceService.getResource(compares[i].getHeadUrl()).toString()); //todo 用户头像
                    }
                }
            }
            appCrowdFundingModel.setPeopleHeadUrl(urls);

        }else if(crowdFunding.getCrowdFundingType()==CommonEnum.CrowdFundingType.subscription&& appCrowdFundingModel.getPartnerStatue()==CommonEnum.CrowdStatus.success){
            List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findSuccessCrowdsFromLastIdWithNumber(crowdFunding.getId(),crowdFundingService.getMaxId()+1,10);
            List<String> url=new ArrayList<String>();
            for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
                if(crowdFundingPublic.getUserHeadUrl()==null){
                    url.add(null);
                }else{
                    url.add(staticResourceService.getResource(crowdFundingPublic.getUserHeadUrl()).toString()); //todo 用户头像
                }
                url.add("http://ymr.com/head.png"); //todo 删除
            }
            appCrowdFundingModel.setPeopleHeadUrl(url);
        }
        appCrowdFundingModel.setEndTime(crowdFunding.getEndTime());
        appCrowdFundingModel.setStartTime(crowdFunding.getStartTime());
        appCrowdFundingModel.setStartMoeny(crowdFunding.getStartMoeny());
        appCrowdFundingModel.setTitle(crowdFunding.getName());
        appCrowdFundingModel.setToBooking(crowdFunding.getToBooking());
        appCrowdFundingModel.setToMoeny(crowdFunding.getToMoeny());
        appCrowdFundingModel.setStartMoeny(crowdFunding.getStartMoeny());
        appCrowdFundingModel.setType(crowdFunding.getCrowdFundingType());
        if(crowdFunding.getView()==null) {
            crowdFunding.setView(0L);
        }else{
            Long view=crowdFunding.getView();
            crowdFunding.setView(view+1);
            crowdFunding= crowdFundingRepository.saveAndFlush(crowdFunding);
        }
        data.outputData(appCrowdFundingModel);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/raiseBooking")
    @Override
    public ApiResult raiseBooking(Output<String> orderNo,Output<Double> fee,Output<String> wxCallbackUrl,Output<String> alipayCallbackUrl,Double money, String name, String phone, String remark,Long crowdId) throws Exception {

        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        Long userId=appUserInfoModel.getUserId();
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        CrowdFundingPublic crowdFundingBooking=crowdFundingService.findPublicByCFAndUserId(crowdId,userId);
        if(crowdFundingBooking!=null){
            throw new HaveRaisedException();
        }
        if(userId==null){
            throw new UserRequestIllegalException();
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new UserNotExitsException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (!RegexHelper.IsValidMobileNo(phone)) {
            throw new PhoneFormatErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new AmountErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
        }else if(!crowdFunding.getVisibleLevel().contains(appUserInfoModel.getUserLevel())){
            throw new UserLevelErrorException();
        }else {

            double rate=Double.parseDouble(configRepository.findOne(ConfigKey.MONEY_TO_SCORE).getValue());
            Date date=new Date();
            Order order=new Order();
            order.setMoney(money);
            order.setTime(date);
            order.setScore((int)(money*rate));
            order.setUser(user);
            order.setPayType(CommonEnum.PayType.paying);
            order.setOrderNo(date.getTime()+""+userId);//todo 订单号怎么定
            order=orderRepository.saveAndFlush(order);

            orderNo.outputData(order.getOrderNo());
            fee.outputData(money);

            //TODO 商家支付账号
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();  //todo 这个用户什么时候存到众筹的预约表
            crowdFundingPublic.setOwnerId(userId);
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublic.setPhone(phone);
            crowdFundingPublic.setRemark(remark);
            crowdFundingPublic.setName(name);
            crowdFundingPublic.setCrowdFunding(crowdFunding);
            crowdFundingPublic.setStatus(0);
            crowdFundingPublic.setMoney(money);
            crowdFundingPublic.setOrderNo(order.getOrderNo());

            MyCrowdFundingFlow myCrowdFundingFlow=new MyCrowdFundingFlow();
            myCrowdFundingFlow.setCrowdFunding(crowdFunding);
            myCrowdFundingFlow.setOwner(user);
            myCrowdFundingFlow.setRole(0);
            myCrowdFundingFlow =myCrowdFundingFlowRepository.saveAndFlush(myCrowdFundingFlow);

            crowdFunding.setTotalBooking(crowdFunding.getTotalBooking()+1);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取合作发起人信息，存入合作发起表中

            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            //TODO 支付中的订单

            String wxUrl= commonConfigService.getWebUrl()+"/pay/callBackWeiXin";
            String aliUrl=commonConfigService.getWebUrl()+"/pay/callBackAlipay";
            wxCallbackUrl.outputData(wxUrl);
            alipayCallbackUrl.outputData(aliUrl);

            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
        }
    }




    @RequestMapping("/getBookingList")
    @Override
    public ApiResult getBookingList(Output<AppBookingListModel[]> list, Long lastId,Long crowdId) throws Exception {
        if(lastId==null){
            lastId=crowdFundingService.getMaxId()+1;
        }//如果为null则默认第一页
        int number=10; //todo 单页表格的数据个数
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdId, lastId, number);
        List<AppBookingListModel> appBookingListModels=new ArrayList<AppBookingListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppBookingListModel appBookingListModel=new AppBookingListModel();
            appBookingListModel.setTime(crowdFundingPublic.getTime());
            appBookingListModel.setName(crowdFundingPublic.getName());
            appBookingListModel.setPid(crowdFundingPublic.getId());
            appBookingListModel.setPhone(crowdFundingPublic.getPhone());
            appBookingListModel.setStatus(crowdFundingPublic.getStatus());
            appBookingListModel.setLevel(CommonEnum.UserLevel.one); //todo 删除
            //appBookingListModel.setLevel(userRepository.findOne(crowdFundingPublic.getOwnerId()).getUserLevel());//todo 用户等级
            //appBookingListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl()); //todo 用户头像
            appBookingListModels.add(appBookingListModel);
        }
        list.outputData(appBookingListModels.toArray(new AppBookingListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/raiseCooperation")
    @Override
    public ApiResult raiseCooperation(Double money, String name, String phone, String remark,Long crowdId) throws Exception {
        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        Long userId=appUserInfoModel.getUserId();
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        CrowdFundingPublic crowdFundingCoo=crowdFundingService.findPublicByCFAndUserId(crowdId,userId);
        CrowdFundingBooking crowdFundingBooking=crowdFundingService.findBookingByCFAndUserId(crowdId,userId);
        if(crowdFundingCoo!=null||crowdFundingBooking!=null){
            throw new HaveRaisedException();
        }
        if(userId==null){
            throw new UserRequestIllegalException();
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new UserNotExitsException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (!RegexHelper.IsValidMobileNo(phone)) {
            throw new PhoneFormatErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new AmountErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
        }else if(!crowdFunding.getVisibleLevel().contains(appUserInfoModel.getUserLevel())){
            throw new UserLevelErrorException();
        }else {
            double lastMoney=money*(100-crowdFunding.getAgencyFeeRate())/100;
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            crowdFundingPublic.setOwnerId(userId);
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublic.setMoney(lastMoney);
            crowdFundingPublic.setPhone(phone);
            crowdFundingPublic.setRemark(remark);
            crowdFundingPublic.setAgencyFee(money-lastMoney);
            crowdFundingPublic.setName(name);
            crowdFundingPublic.setCrowdFunding(crowdFunding);
            crowdFundingPublic.setStatus(0);

            MyCrowdFundingFlow myCrowdFundingFlow=new MyCrowdFundingFlow();
            myCrowdFundingFlow.setCrowdFunding(crowdFunding);
            myCrowdFundingFlow.setOwner(user);
            myCrowdFundingFlow.setRole(0);
            myCrowdFundingFlow =myCrowdFundingFlowRepository.saveAndFlush(myCrowdFundingFlow);

            crowdFunding.setTotalBooking(crowdFunding.getTotalBooking()+1);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取合作发起人人信息，存入合作发起表中
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

        }
    }

    @RequestMapping("/getCooperationResult")
    @Override
    public ApiResult getCooperationResult(Output<AppCooperationResultListModel[]> list, Long crowdId) throws Exception {
        //CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdId);
        List<CrowdFundingPublic> crowdFundingPublics=crowdFundingService.getPublicByCrowdId(crowdId);
        List<AppCooperationResultListModel> appCooperationResultListModels=new ArrayList<AppCooperationResultListModel>();
        for(CrowdFundingPublic fundingPublic:crowdFundingPublics){
            List<CrowdFundingBooking> crowdFundingBookings=crowdFundingService.getBookingByPublicId(crowdId,fundingPublic.getId());
            AppCooperationResultListModel appCooperationResultListModel=new AppCooperationResultListModel();
            appCooperationResultListModel.setName(fundingPublic.getName());
            //appCooperationResultListModel.setUserHeadUrl(fundingPublic.getUserHeadUrl()); //todo 用户头像
            appCooperationResultListModel.setAmount(fundingPublic.getAmount());
            for(CrowdFundingBooking crowdFundingBooking:crowdFundingBookings){
                AppBookingListModel appBookingListModel=new AppBookingListModel();
                appBookingListModel.setName(crowdFundingBooking.getName());
                //appBookingListModel.setUserHeadUrl(crowdFundingBooking.getUserHeadUrl()); //todo 用户头像
                appCooperationResultListModel.getBookingListModels().add(appBookingListModel);
            }
            appCooperationResultListModels.add(appCooperationResultListModel);
        }
        list.outputData(appCooperationResultListModels.toArray(new AppCooperationResultListModel[crowdFundingPublics.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/getRaiseCooperationList")  //TODO 是用户的请求还是管理员的请求
    @Override
    public ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel[]> list, Long lastId,Long crowdId) throws Exception {
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdId,lastId,number);
        List<AppRaiseCooperationListModel> appRaiseCooperationListModels=new ArrayList<AppRaiseCooperationListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppRaiseCooperationListModel appRaiseCooperationListModel=new AppRaiseCooperationListModel();
            appRaiseCooperationListModel.setName(crowdFundingPublic.getName());
            //appRaiseCooperationListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl()); //todo 用户头像
            appRaiseCooperationListModel.setPid(crowdFundingPublic.getId());
            appRaiseCooperationListModel.setAmount(crowdFundingPublic.getAmount());
            appRaiseCooperationListModel.setTime(crowdFundingPublic.getTime());
            appRaiseCooperationListModel.setLevel(CommonEnum.UserLevel.three);//todo 删除
                    //appRaiseCooperationListModel.setLevel(userRepository.findOne(crowdFundingPublic.getOwnerId()).getUserLevel()); //todo 用户等级
                    appRaiseCooperationListModel.setStatus(crowdFundingPublic.getStatus());
            String tip=configRepository.findOne(ConfigKey.CROWDFUDINGTIP).getValue();
           tip =tip.replace("A",crowdFundingPublic.getMoney()/10000+"");//todo 全局变量
            appRaiseCooperationListModel.setTip(tip);
            appRaiseCooperationListModels.add(appRaiseCooperationListModel);
        }
        list.outputData(appRaiseCooperationListModels.toArray(new AppRaiseCooperationListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/goCooperation")
    @Override
    public ApiResult goCooperation(Double money, String name, String phone, String remark,Long crowdId,Long crowdPublicId) throws Exception {
        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        Long userId=appUserInfoModel.getUserId();
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        CrowdFundingPublic crowdFundingPublic=new CrowdFundingPublic();
        User user=new User();

        CrowdFundingBooking crowdFundingBook=crowdFundingService.findBookingByCFAndUserId(crowdId,userId);
        CrowdFundingPublic crowdFundingPublic1=crowdFundingService.findPublicByCFAndUserId(crowdId,userId);
        if(crowdFundingBook!=null||crowdFundingPublic1!=null){
            throw new HaveRaisedException();
        }
        if(money==null){
            money=0.0;
        }
        if(crowdPublicId==null){
            throw new CrowdFundingPublicIllegalException();
        }else{
            crowdFundingPublic=crowdFundingPublicRepository.findOne(crowdPublicId);
        }
        if(userId==null){
            throw new UserRequestIllegalException();
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new UserNotExitsException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (!RegexHelper.IsValidMobileNo(phone)) {
            throw new PhoneFormatErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(!crowdFunding.getVisibleLevel().contains(appUserInfoModel.getUserLevel())) {
            throw new UserLevelErrorException();
        }else if(crowdFundingPublic==null){
            throw new CrowdFundingPublicIllegalException();
        }else if(crowdFunding==null){
            throw new CrowdFundingNotExitsException();
        } else {
            double lastMoney=money*(100-crowdFunding.getAgencyFeeRate())/100;
            CrowdFundingBooking crowdFundingBooking = new CrowdFundingBooking();
            crowdFundingBooking.setName(name);
            crowdFundingBooking.setMoney(lastMoney);
            crowdFundingBooking.setCrowdFunding(crowdFunding);
            crowdFundingBooking.setCrowdFundingPublic(crowdFundingPublic);
            crowdFundingBooking.setPhone(phone);
            crowdFundingBooking.setRemark(remark);
            crowdFundingBooking.setTime(new Date());
            crowdFundingBooking.setOwnerId(userId);
            crowdFundingBooking.setAgencyFee(money-lastMoney);

            MyCrowdFundingFlow myCrowdFundingFlow=new MyCrowdFundingFlow();
            myCrowdFundingFlow.setCrowdFunding(crowdFunding);
            myCrowdFundingFlow.setOwner(user);
            myCrowdFundingFlow.setRole(1);
            myCrowdFundingFlow =myCrowdFundingFlowRepository.saveAndFlush(myCrowdFundingFlow);

            crowdFundingPublic.setAmount(crowdFundingPublic.getAmount()+1);
            crowdFunding.setTotalBooking(crowdFunding.getTotalBooking()+1);
            crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取认购人信息，存入认购表中
            crowdFundingBooking=crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

        }
    }

    @RequestMapping("/raiseSubscription")
    @Override
    public ApiResult raiseSubscription(Double money, String name, String phone, String remark,Long crowdId) throws Exception {
        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        Long userId=appUserInfoModel.getUserId();
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        CrowdFundingPublic crowdFundingSub=crowdFundingService.findPublicByCFAndUserId(crowdId,userId);
        if(crowdFundingSub!=null){
            throw new HaveRaisedException();
        }
        if(userId==null){
            throw new UserRequestIllegalException();
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new UserNotExitsException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (!RegexHelper.IsValidMobileNo(phone)) {
            throw new PhoneFormatErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new AmountErrorException();
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
        }else if(!crowdFunding.getVisibleLevel().contains(appUserInfoModel.getUserLevel())){
            throw new UserLevelErrorException();
        }else{
            double lastMoney=money*(100-crowdFunding.getAgencyFeeRate())/100;
            CrowdFundingPublic crowdFundingPublic = new CrowdFundingPublic();
            crowdFundingPublic.setOwnerId(userId);
            crowdFundingPublic.setTime(new Date());
            crowdFundingPublic.setMoney(lastMoney);
            crowdFundingPublic.setPhone(phone);
            crowdFundingPublic.setRemark(remark);
            crowdFundingPublic.setAgencyFee(money-lastMoney);
            crowdFundingPublic.setName(name);
            crowdFundingPublic.setCrowdFunding(crowdFunding);
            crowdFundingPublic.setStatus(0);

            MyCrowdFundingFlow myCrowdFundingFlow=new MyCrowdFundingFlow();
            myCrowdFundingFlow.setCrowdFunding(crowdFunding);
            myCrowdFundingFlow.setOwner(user);
            myCrowdFundingFlow.setRole(0);
            myCrowdFundingFlow =myCrowdFundingFlowRepository.saveAndFlush(myCrowdFundingFlow);

            crowdFundingPublic.setAmount(crowdFundingPublic.getAmount()+1);
            crowdFunding.setTotalBooking(crowdFunding.getTotalBooking()+1);
//            /crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取认购人信息，存入认购表中
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

        }
    }

    @RequestMapping("/getSubscriptionList")
    @Override
    public ApiResult getSubscriptionList(Output<AppSubscriptionListModel[]> list,Long crowdId, Long lastId) throws Exception {

        if(lastId==null){
            lastId=crowdFundingService.getMaxId()+1;
        }//如果为null则默认第一页
        int number=10; //todo 单页表格的数据个数
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findCrowdListFromLastIdWithNumber(crowdId, lastId, number);
        List<AppSubscriptionListModel> appSubscriptionListModelList=new ArrayList<AppSubscriptionListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppSubscriptionListModel appSubscriptionListModel=new AppSubscriptionListModel();
            appSubscriptionListModel.setTime(crowdFundingPublic.getTime());
            appSubscriptionListModel.setMoney(crowdFundingPublic.getMoney());
            appSubscriptionListModel.setName(crowdFundingPublic.getName());
            appSubscriptionListModel.setLevel(CommonEnum.UserLevel.two); //todo 删除
            //appSubscriptionListModel.setLevel(userRepository.findOne(crowdFundingPublic.getOwnerId()).getUserLevel()); //todo 用户等级
            appSubscriptionListModel.setStatus(crowdFundingPublic.getStatus());
            appSubscriptionListModel.setPid(crowdFundingPublic.getId());
            //appSubscriptionListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());//todo 用户头像
            appSubscriptionListModelList.add(appSubscriptionListModel);
        }
        list.outputData(appSubscriptionListModelList.toArray(new AppSubscriptionListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
