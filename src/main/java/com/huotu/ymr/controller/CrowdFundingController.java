package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.exception.CrowdException;
import com.huotu.ymr.model.*;
import com.huotu.ymr.repository.CrowdFundingBookingRepository;
import com.huotu.ymr.repository.CrowdFundingPublicRepository;
import com.huotu.ymr.repository.CrowdFundingRepository;
import com.huotu.ymr.repository.UserRepository;
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
    CrowdFundingService crowdFundingService;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;

    @Autowired
    CrowdFundingPublicRepository crowdFundingPublicRepository;

    @Autowired
    CrowdFundingBookingRepository crowdFundingBookingRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/getCrowdFundingList")
    @Override    //todo 传入参数缺少一个用户id，或者用户级别
    public ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel[]> list, Long lastId) throws Exception {//todo 通过用户等级来显示可见的众筹项目
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getCrowdFundingMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFunding> crowdFundings=crowdFundingService.searchCrowdFundingList(lastId,number);
        List<AppCrowdFundingListModel> appCrowdFundingListModels=new ArrayList<AppCrowdFundingListModel>();
        for(CrowdFunding crowdFunding:crowdFundings){
            AppCrowdFundingListModel appCrowdFundingListModel=new AppCrowdFundingListModel();
            appCrowdFundingListModel.setStartMoeny(crowdFunding.getStartMoeny());
            appCrowdFundingListModel.setToMoeny(crowdFunding.getToMoeny());
            appCrowdFundingListModel.setTitle(crowdFunding.getName());
            //appCrowdFundingListModel.setCurrentBooking(); //todo 是否增加一个字段
            //appCrowdFundingListModel.setCurrentMoeny(); //todo 是否增加一个字段
            //appCrowdFundingListModel.setPId(crowdFunding.getId());//todo 什么id
            appCrowdFundingListModel.setPuctureUrl(crowdFunding.getPuctureUrl());
            appCrowdFundingListModel.setSummary(crowdFunding.getContent());
            //appCrowdFundingListModel.setTime();//todo 什么时间，应该有起始和终止时间

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
        //appCrowdFundingModel.setCurrentBooking(); //TODO 是否增加一个字段记录当前预约人数
        //appCrowdFundingModel.setCurrentMoeny();  //TODO 是否增加一个字段记录当前money
        appCrowdFundingModel.setPId(crowdFunding.getId()); //TODO 什么id 这个名字不是personId么，怎么感觉是众筹id.认为是众筹id
        appCrowdFundingModel.setTime(crowdFunding.getEndTime()); //todo 什么时间 认为是截止时间
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
    public ApiResult raiseBooking(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception {

        //todo 支付之后才能下面的操作，暂时缺少支付操作
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        if(userId==null){
            throw new CrowdException("用户请求非法");
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new CrowdException("用户不存在");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (RegexHelper.IsValidMobileNo(phone)) {
            throw new CrowdException("手机号码格式不正确");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new CrowdException("认购金额小于起购金额");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
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
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取合作发起人人信息，存入合作发起表中
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
        }
    }

    @RequestMapping("/pay")
    @Override
    public ApiResult pay(Integer type, Double money) throws Exception {
        return null;
    }

    @RequestMapping("/callBackWeiXin")
    @Override
    public ApiResult callBackWeiXin() throws Exception {
        return null;
    }

    @RequestMapping("/callBackAlipay")
    @Override
    public ApiResult callBackAlipay() throws Exception {
        return null;
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
            appBookingListModel.setPid(crowdFundingPublic.getOwnerId()); //todo pid是什么id
            appBookingListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appBookingListModels.add(appBookingListModel);
        }
        list.outputData(appBookingListModels.toArray(new AppBookingListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/raiseCooperation")
    @Override
    public ApiResult raiseCooperation(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception {
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        if(userId==null){
            throw new CrowdException("用户请求非法");
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new CrowdException("用户不存在");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (RegexHelper.IsValidMobileNo(phone)) {
            throw new CrowdException("手机号码格式不正确");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new CrowdException("认购金额小于起购金额");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
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
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取合作发起人人信息，存入合作发起表中
            crowdFundingPublic=crowdFundingPublicRepository.saveAndFlush(crowdFundingPublic);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

        }
    }

    @RequestMapping("/getRaiseCooperationList")
    @Override
    public ApiResult getRaiseCooperationList(Output<AppRaiseCooperationListModel[]> list, String key, Long lastId,Long crowdId) throws Exception {
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFundingPublic> crowdFundingPublicList=crowdFundingService.searchCooperationgList(key,lastId,crowdId,number);
        List<AppRaiseCooperationListModel> appRaiseCooperationListModels=new ArrayList<AppRaiseCooperationListModel>();
        for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
            AppRaiseCooperationListModel appRaiseCooperationListModel=new AppRaiseCooperationListModel();
            appRaiseCooperationListModel.setName(crowdFundingPublic.getName());
            appRaiseCooperationListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appRaiseCooperationListModel.setPid(crowdFundingPublic.getOwnerId());//todo pid是什么id
            //appRaiseCooperationListModel.setAmount(crowdFundingPublic.getAmount);// todo 合作人数是否在实体类中加一个字段
            String tip="我有"+crowdFundingPublic.getMoney()/10000+"万，找人合作筹募";//todo 以什么为单位
            appRaiseCooperationListModel.setTip(tip);
            appRaiseCooperationListModels.add(appRaiseCooperationListModel);
        }
        list.outputData(appRaiseCooperationListModels.toArray(new AppRaiseCooperationListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/goCooperation")
    @Override
    public ApiResult goCooperation(Double money, String name, String phone, String remark,Long crowdId,Long crowdPublicId,Long userId) throws Exception {
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        CrowdFundingPublic crowdFundingPublic=new CrowdFundingPublic();
        User user=new User();
        if(userId==null){
            throw new CrowdException("用户请求非法");
        }else{
            user=userRepository.findOne(userId);
        }
        if(crowdPublicId==null){
            throw new CrowdException("所要合作的合作人身份不合法");
        }else{
            crowdFundingPublic=crowdFundingPublicRepository.findOne(crowdPublicId);
        }
        if(crowdFundingPublic==null){
            throw new CrowdException("发起人用户不存在");
        }else if(crowdFunding==null){
            throw new CrowdException("众筹项目不存在");
        }else if (RegexHelper.IsValidMobileNo(phone)) {
            throw new CrowdException("手机号码格式不正确");
        }
        else {
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
    public ApiResult raiseSubscription(Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception {
        CrowdFunding crowdFunding= crowdFundingRepository.findOne(crowdId);
        double startMoeny=crowdFunding.getStartMoeny();
        User user=new User();
        if(userId==null){
            throw new CrowdException("用户请求非法");
        }else{
            user=userRepository.findOne(userId);
        }
        if(user==null){
            throw new CrowdException("用户不存在");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER);
        }else if (RegexHelper.IsValidMobileNo(phone)) {
            throw new CrowdException("手机号码格式不正确");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);
        }else if(startMoeny>money){
            throw new CrowdException("认购金额小于起购金额");
            //return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MONEY);
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
            //crowdFundingPublic.setUserHeadUrl(user.getHeadUrl());//todo 获取认购人信息，存入认购表中
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
            appSubscriptionListModel.setPid(crowdFundingPublic.getOwnerId()); //todo pid是什么id
            appSubscriptionListModel.setUserHeadUrl(crowdFundingPublic.getUserHeadUrl());
            appSubscriptionListModelList.add(appSubscriptionListModel);
        }
        list.outputData(appSubscriptionListModelList.toArray(new AppSubscriptionListModel[crowdFundingPublicList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
