package com.huotu.ymr.controller;

import com.alipay.util.AlipayNotify;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.thirdparty.WeixinUtils;
import com.huotu.ymr.common.thirdparty.XMLParser;
import com.huotu.ymr.entity.Order;
import com.huotu.ymr.model.AppWeixinResultModel;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.OrderRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.CrowdFundingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/10.
 */
@Controller
@RequestMapping("/pay")
public class PayCallBackController {

    private static final Log log = LogFactory.getLog(CrowdFundingController.class);

    @Autowired
    CrowdFundingService crowdFundingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ConfigRepository configRepository;

    @RequestMapping("/callBackWeiXin")
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

                float money = Float.parseFloat(total_fee) / 100;  //todo 这个钱为什么要除以100 float

               if(doPay(out_trade_no, money, transaction_id, CommonEnum.PurchaseSource.WEIXIN)){
                   result.setReturn_code("SUCCESS");
                   result.setReturn_msg("");
                   return result;
               }else {
                   result.setReturn_code("FAIL");
                   result.setReturn_msg("签名失败");
                   return result;
               }

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
            float total_fee = Float.parseFloat(request.getParameter("total_fee"));

            if (trade_status.equals("TRADE_FINISHED")) {

            } else if (trade_status.equals("TRADE_SUCCESS")) {

                if(doPay(out_trade_no, total_fee, trade_no, CommonEnum.PurchaseSource.ALIPAY)){
                    return "success";
                }else{
                    return "fail";
                }
            }
            return "success";

        } else {
            return "fail";
        }
    }

    /**
     * @param orderNo        订单号
     * @param money          金额
     * @param outOrderNo     支付宝或微信的外部交易号
     * @param purchaseSource 购买来源
     */
    @Transactional
    private boolean doPay(String orderNo, float money, String outOrderNo, CommonEnum.PurchaseSource purchaseSource) {
        Date date = new Date();
        Order userOrder = orderRepository.findOne(orderNo);
        double rate=Double.parseDouble(configRepository.findOne("MoneyToScore").getValue());
        if (userOrder == null) { //如果订单不存在
           return false;
        } else if (userOrder.getPayType().equals(CommonEnum.PayType.paying)  //如果用户订单存在，不为空，并且它为正在支付状态，并且它的所属用户存在
                && userOrder.getOrderNo() != null && userOrder.getUser() != null) {
            userOrder.setMoney(Double.parseDouble(money + ""));
            userOrder.setOrderNo(orderNo);
            userOrder.setOutOrderNo(outOrderNo);
            userOrder.setPayType(CommonEnum.PayType.payed);
            userOrder.setOutOrderNo(outOrderNo);
            userOrder.setPurchaseSource(purchaseSource);
            userOrder.setScore((int) (rate * money));
            userOrder.setPayTime(date);
            userOrder = orderRepository.saveAndFlush(userOrder);

            return true;
        }else if(userOrder.getPayType().equals(CommonEnum.PayType.payed)) { //如果已经支付完成了
            return true;
        }
        return false;
    }

}
