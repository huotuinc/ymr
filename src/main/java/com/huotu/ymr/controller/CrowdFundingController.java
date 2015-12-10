package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.PublicParameterHolder;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.exception.*;
import com.huotu.ymr.model.*;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.CrowdFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
            appCrowdFundingListModel.setCurrentBooking(crowdFunding.getCurrentBooking());
            appCrowdFundingListModel.setCurrentMoeny(crowdFunding.getCurrentMoeny());
            appCrowdFundingListModel.setPId(crowdFunding.getId());
            appCrowdFundingListModel.setPuctureUrl(crowdFunding.getPuctureUrl());
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
        AppCrowdFundingModel appCrowdFundingModel=new AppCrowdFundingModel();
        appCrowdFundingModel.setContent(crowdFunding.getContent());
        appCrowdFundingModel.setCurrentBooking(crowdFunding.getCurrentBooking());
        appCrowdFundingModel.setCurrentMoeny(crowdFunding.getCurrentMoeny());
        appCrowdFundingModel.setPId(crowdFunding.getId());
        appCrowdFundingModel.setEndTime(crowdFunding.getEndTime());
        appCrowdFundingModel.setStartMoeny(crowdFunding.getStartMoeny());
        appCrowdFundingModel.setTitle(crowdFunding.getName());
        appCrowdFundingModel.setToBooking(crowdFunding.getToBooking());
        appCrowdFundingModel.setToMoeny(crowdFunding.getToMoeny());
        appCrowdFundingModel.setStartMoeny(crowdFunding.getStartMoeny());
        appCrowdFundingModel.setType(crowdFunding.getCrowdFundingType());
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

            double rate=Double.parseDouble(configRepository.findOne("MoneyToScore").getValue());
            Order order=new Order();
            order.setMoney(money);
            order.setTime(new Date());
            order.setScore((int)(money*rate));
            order.setUser(user);
            order.setPayType(CommonEnum.PayType.paying);
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
            crowdFundingPublic.setOrderNo(order.getOrderNo());
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
            appBookingListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
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
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取合作发起人人信息，存入合作发起表中
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

        }
    }

    @RequestMapping("/getRaiseCooperationList")  //TODO 是用户的请求还是管理员的请求
    @Override
    public ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel[]> list, Long lastId,Long crowdId) throws Exception {
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.findCrowdListFromLastIdWithNumber(lastId,crowdId,number);
        List<AppRaiseCooperationListModel> appRaiseCooperationListModels=new ArrayList<AppRaiseCooperationListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppRaiseCooperationListModel appRaiseCooperationListModel=new AppRaiseCooperationListModel();
            appRaiseCooperationListModel.setName(crowdFundingPublic.getName());
            appRaiseCooperationListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appRaiseCooperationListModel.setPid(crowdFundingPublic.getId());
            appRaiseCooperationListModel.setAmount(crowdFundingPublic.getAmount());
            String tip=configRepository.findOne("CrowdFundingTip").getValue();
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
            appSubscriptionListModel.setStatus(crowdFundingPublic.getStatus());
            appSubscriptionListModel.setPid(crowdFundingPublic.getId());
            appSubscriptionListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appSubscriptionListModelList.add(appSubscriptionListModel);
        }
        list.outputData(appSubscriptionListModelList.toArray(new AppSubscriptionListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
