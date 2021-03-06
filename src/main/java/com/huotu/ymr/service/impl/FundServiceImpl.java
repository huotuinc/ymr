package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.common.thirdparty.ClientCustomSSL;
import com.huotu.ymr.common.thirdparty.RequestHandler;
import com.huotu.ymr.common.thirdparty.XMLParser;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.Order;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.OrderRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.FundService;
import com.huotu.ymr.service.OrderService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by xhk on 2015/12/24.
 */
@Service
public class FundServiceImpl implements FundService{


    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    UserRepository userRepository;


    @Override
    public boolean refundByUserId(CrowdFunding crowdFunding, Long ownerId) throws Exception {

        String appid=configRepository.findOne(ConfigKey.APPID).getValue();
        String appsecret=configRepository.findOne(ConfigKey.APPSECRET).getValue();
        String partnerkey=configRepository.findOne(ConfigKey.PAYSIGNKEY).getValue();
        String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

        RequestHandler reqHandler = new RequestHandler(
                null, null);

        reqHandler.init(appid, appsecret, partnerkey);

        Order order=orderService.findOneByIds(crowdFunding.getId(), ownerId);
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", configRepository.findOne(ConfigKey.MCHID).getValue());
        packageParams.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        packageParams.put("out_trade_no", order.getOrderNo());
        packageParams.put("out_refund_no", order.getOutOrderNo());
        packageParams.put("total_fee", order.getMoney()+"");
        packageParams.put("refund_fee", order.getMoney()+"");
        packageParams.put("op_user_id", configRepository.findOne(ConfigKey.MCHID).getValue());
        String sign = reqHandler.createSign(packageParams);
        packageParams.put("sign",sign);
        String data=XMLParser.toXml(packageParams);

        String result=ClientCustomSSL.doRefund(createOrderURL, data,staticResourceService.getResource(configRepository.findOne(ConfigKey.CERTIFICATE).getValue()).getPath(),configRepository.findOne(ConfigKey.CERTIFICATEKEY).getValue());
        Map<String,String> map=XMLParser.getMapFromXML(result);

        if(map.get("return_code").equals("SUCCESS")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void increaseIntegralByMoney(Long ownerId, Double money) {
        User user=userRepository.findOne(ownerId);
        //四舍五入
        int score=(int) (Math.rint(money*Double.parseDouble(configRepository.findOne(ConfigKey.MONEY_TO_SCORE).getValue())));
        user.setScore(user.getScore()+score);
        int resultScore=user.getContinuedScore()+score;
        //积分值到了则升等（一级升二级）
        if(resultScore>=Integer.parseInt(configRepository.findOne(ConfigKey.UPGRADE_INTEGRAL).getValue())&&user.getUserLevel()==CommonEnum.UserLevel.one){
            user.setUserLevel(CommonEnum.UserLevel.two);
        }
        user.setContinuedScore(resultScore);
        user=userRepository.saveAndFlush(user);
    }

    @Override
    public User increaseIntegralByScore(User user, Integer score) {
        user.setScore(user.getScore()+score);
        int resultScore=user.getContinuedScore()+score;
        //积分值到了则升等（一级升二级）
        if(resultScore>=Integer.parseInt(configRepository.findOne(ConfigKey.UPGRADE_INTEGRAL).getValue())&&user.getUserLevel()==CommonEnum.UserLevel.one){
            user.setUserLevel(CommonEnum.UserLevel.two);
        }
        user.setContinuedScore(resultScore);
        return user;
    }
}
