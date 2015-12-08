package com.huotu.ymr.controller;

import com.alipay.util.AlipayNotify;
import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.CrowdFundingSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.PublicParameterHolder;
import com.huotu.ymr.common.thirdparty.WeixinUtils;
import com.huotu.ymr.common.thirdparty.XMLParser;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.*;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/app")
public class CrowdFundingController implements CrowdFundingSystem {

    private static final Log log = LogFactory.getLog(CrowdFundingController.class);


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
    @Override
    public ApiResult getCrowdFundingList(Output<AppCrowdFundingListModel[]> list, Long lastId) throws Exception {

        AppUserInfoModel appUserInfoModel=PublicParameterHolder.get().getCurrentUser();
        int number=10; //todo 每页条数
        if(lastId==null){
            lastId=crowdFundingService.getCrowdFundingMaxId()+1;
        }//如果为null则默认第一页
        List<CrowdFunding> crowdFundings=crowdFundingService.searchCrowdFundingList(lastId,number,appUserInfoModel.getUserLevel());
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
    public ApiResult raiseBooking(Output<String> orderNo,Output<Double> fee,Output<String> wxCallbackUrl,Output<String> alipayCallbackUrl,Double money, String name, String phone, String remark,Long crowdId,Long userId) throws Exception {

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

            //TODO 支付中的订单
        }
    }



    @RequestMapping("/callBackWeiXin")
    @Override
    @ResponseBody
    public AppWeixinResultModel callBackWeiXin(HttpServletRequest request) throws Exception {
        log.info("微信回调中");

        AppWeixinResultModel result = new AppWeixinResultModel();

        String data = null;
        Map<String, String> map = null;
        try {
            try (BufferedReader reader = request.getReader()) {
                StringBuffer stringBuffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    stringBuffer.append(line);
                    line = reader.readLine();
                }

                log.info(stringBuffer.toString());
                data = stringBuffer.toString();
            }

            map = XMLParser.getMapFromXML(data);
        } catch (Exception ex) {
            log.error("解析xml数据失败");
            result.setReturn_code("FAIL");
            result.setReturn_msg("解析xml数据失败");
            return result;
        }

        String return_code = map.get("return_code").toString();
        if ("SUCCESS".equals(return_code)) {

            String sign = map.get("sign") != null ? map.get("sign").toString() : null;
            if (sign != null && sign.equals(WeixinUtils.getSign(map))) {

                String total_fee = map.get("total_fee").toString();
                String transaction_id = map.get("transaction_id").toString();
                String out_trade_no = map.get("out_trade_no").toString();

                float money = Float.parseFloat(total_fee) / 100;

                //doPay(out_trade_no, money, transaction_id, CommonEnum.PurchaseSource.WEIXIN);


                result.setReturn_code("SUCCESS");
                result.setReturn_msg("");
                return result;
            } else {
                result.setReturn_code("FAIL");
                result.setReturn_msg("签名失败");
                return result;
            }
        } else {
            result.setReturn_code("FAIL");
            result.setReturn_msg("返回值错误");
            return result;
        }
    }

    @RequestMapping("/callBackAlipay")
    @Override
    @ResponseBody
    public String callBackAlipay(HttpServletRequest request) throws Exception {
        //去掉 sign 和 sign_type 两个参数,将其他参数按照字母顺序升序排列,再把所有 数组值以“&”字符连接起来:
        //再用自己的私钥和 支付宝的公钥 进行校验
        //调用支付宝校验 确定是否支付宝发送
        //https://mapi.alipay.com/gateway.do?service=notify_verify&partner=2088002396 712354&notify_id=
        //该URI 需要返回true
        log.info("支付宝来电");


        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
            log.info(name + ":" + valueStr);
        }
//商户订单号
        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号

        String trade_no = request.getParameter("trade_no");

        //交易状态
        String trade_status = request.getParameter("trade_status");

        boolean verifyed = AlipayNotify.verify(params);
        log.info(verifyed);
        if (verifyed) {


//请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            float total_fee = Float.parseFloat(request.getParameter("total_fee"));

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //doPay(out_trade_no, total_fee, trade_no, CommonEnum.PurchaseSource.ALIPAY);

                //注意：
                //该种交易状态只在两种情况下出现
                //1、开通了普通即时到账，买家付款成功后。
                //2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //doPay(out_trade_no, total_fee, trade_no, CommonEnum.PurchaseSource.ALIPAY);
                //注意：
                //该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
            }

            return "success";

        } else {
            return "fail";
        }
    }

//    /**
//     * @param orderNo        订单号
//     * @param money          金额
//     * @param outOrderNo     支付宝或微信的外部交易号
//     * @param purchaseSource 购买来源
//     */
//    @Transactional
//    private void doPay(String orderNo, float money, String outOrderNo, CommonEnum.PurchaseSource purchaseSource) {
//
//        Date date = new Date();
//        UserPurchaseOrder userPurchaseOrder = userPurchaseOrderRepository.findOne(orderNo);
//        if (userPurchaseOrder == null) {
//            userPurchaseOrder = new UserPurchaseOrder();
//            userPurchaseOrder.setMoney(money);
//            userPurchaseOrder.setOrderNo(orderNo);
//            userPurchaseOrder.setOutOrderNo(outOrderNo);
//            userPurchaseOrder.setSource(purchaseSource);
//            userPurchaseOrder.setStatus(CommonEnum.OrderStatus.deliverying);
//            userPurchaseOrder.setPayTime(date);
//            userPurchaseOrderRepository.save(userPurchaseOrder);
//        } else if (userPurchaseOrder.getStatus().equals(CommonEnum.OrderStatus.deliverying)
//                && userPurchaseOrder.getProductId() != null && userPurchaseOrder.getUser() != null) {
//            userPurchaseOrder.setMoney(money);
//            userPurchaseOrder.setOrderNo(orderNo);
//            userPurchaseOrder.setOutOrderNo(outOrderNo);
//            userPurchaseOrder.setSource(purchaseSource);
//            userPurchaseOrder.setStatus(CommonEnum.OrderStatus.deliveryed);
//            userPurchaseOrder.setPayTime(date);
//            userPurchaseOrderRepository.save(userPurchaseOrder);
//
//            if (userPurchaseOrder.getProductType().equals(CommonEnum.ProductType.MEAL)) {
//                FlowConfig flowConfig = flowConfigRepository.findOne(userPurchaseOrder.getProductId());
//                float flow = flowConfig.getMeal();
//
//                //记录用户流量
//                FlowDetail flowDetailResult = new FlowDetail();
//                flowDetailResult.setUser(userPurchaseOrder.getUser());
//                flowDetailResult.setType(CommonEnum.FlowType.BUY_MEAL);
//                flowDetailResult.setInOrOut(CommonEnum.InOrOutType.IN);
//                flowDetailResult.setFlow(flow);
//                flowDetailResult.setToSourceId(orderNo);
//                flowDetailResult.setCurrentFlow(userPurchaseOrder.getUser().getRemainFlow() + flow);
//                flowDetailResult.setOperateTime(date);
//                flowDetailResult.setRemark("");
//                flowDetailRepository.save(flowDetailResult);
//
//                //更新用户流量
//                userService.addFlow(userPurchaseOrder.getUser(), flow);
//            }
//
//        }
//    }

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
            appRaiseCooperationListModel.setPid(crowdFundingPublic.getId());
            appRaiseCooperationListModel.setAmount(crowdFundingPublic.getAmount());
            String tip="我有"+crowdFundingPublic.getMoney()/10000+"万，找人合作筹募";//todo 以什么为单位 待定
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
