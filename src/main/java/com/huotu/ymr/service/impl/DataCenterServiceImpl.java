package com.huotu.ymr.service.impl;

import com.alibaba.fastjson.JSON;
import com.huotu.common.base.HttpHelper;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import com.huotu.ymr.common.SysRegex;
import com.huotu.ymr.model.AppWeiXinAccreditModel;
import com.huotu.ymr.model.mall.CategoryModel;
import com.huotu.ymr.model.mall.MallApiResultModel;
import com.huotu.ymr.model.mall.MallGoodModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.DataCenterService;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2015/12/22.
 */
@Service
public class DataCenterServiceImpl implements DataCenterService {
    @Autowired
    private UserRestRepository userRestRepository;

    @Autowired
    CommonConfigService commonConfigService;
    private String ymrMerchantId;
    private String appsecret;
    private String appid;

    @PostConstruct
    public void init(){
        appsecret =commonConfigService.getAppsecret();
        appid = commonConfigService.getAppId();
        ymrMerchantId=commonConfigService.getYmrMerchantId();
    }


    @Override
    public MallUserModel[] getUserInfoByUniond(String uniond) throws IOException {
        List<User> users=userRestRepository.findByMerchantIdAndUnionId(Long.parseLong(ymrMerchantId),uniond);
        MallUserModel[] mallUserModels=new MallUserModel[users.size()];
        for(int i=0;i<users.size();i++){
            mallUserModels[i]=userToModel(users.get(i));
        }
        return mallUserModels;
    }

    @Override
    public MallUserModel getUserInfoByMobile(String mobile) {
        return null;
    }

    @Override
    public MallUserModel getUserInfoByUserId(Long userId) throws IOException {
        User user=userRestRepository.getOneByPK(userId);
        MallUserModel mallUserModel=userToModel(user);
        return mallUserModel;
    }

    @Override
    public Long createUser(AppWeiXinAccreditModel appWeiXinAccreditModel) throws IOException{
        String url = commonConfigService.getMallApiServerUrl() + "/weixin/LoginAuthorize";
        Map<String, String> map = new TreeMap<>();
        map.put("timestamp", String.valueOf(new Date().getTime()));
        map.put("appid", appid);
        map.put("customerid",ymrMerchantId);
        map.put("sex",appWeiXinAccreditModel.getSex()+"");
        map.put("nickname",appWeiXinAccreditModel.getNickname());
        map.put("openid",appWeiXinAccreditModel.getOpenid());
        map.put("city",appWeiXinAccreditModel.getCity());
        map.put("country",appWeiXinAccreditModel.getCountry());
        map.put("province",appWeiXinAccreditModel.getProvince());
        map.put("headimgurl",appWeiXinAccreditModel.getHeadimgurl());
        map.put("unionid",appWeiXinAccreditModel.getUnionid());
        map.put("sign", getSign(map));
        String response = HttpHelper.postRequest(url, map);
        MallApiResultModel resultModel = JSON.parseObject(response, MallApiResultModel.class);
        if (resultModel.getCode() == 200 && !StringUtils.isEmpty(resultModel.getData().toString())) {
            return Long.parseLong(JsonPath.read(resultModel.getData().toString(), "$.userid").toString());
        }
        return null;
    }


    @Override
    public List<CategoryModel> getCategory() {
        return null;
    }

    @Override
    public MallGoodModel[] getMallGood() {
        return new MallGoodModel[0];
    }

    @Override
    public MallGoodModel bindingMobile() {
        return null;
    }

    @Override
    public MallGoodModel ModifyMobile() {
        return null;
    }

    @Override
    public Boolean rechargeCoffers() {
        return null;
    }

    @Override
    public Boolean modifyUserInfo(Long userId, String data, Integer type) throws IOException {
        User user=userRestRepository.getOneByPK(userId);
        switch (type){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        return null;
    }

    private MallUserModel userToModel(User user){
        MallUserModel mallUserModel=new MallUserModel();
        mallUserModel.setHeadUrl(user.getWeixinImageUrl());
        mallUserModel.setIsBindMobile(SysRegex.IsValidMobileNo(user.getLoginName()));
        mallUserModel.setMerchantId(Long.parseLong(ymrMerchantId));
        mallUserModel.setName(user.getRealName());
        mallUserModel.setNickName(user.getWxNickName());
        switch (user.getGender().getValue()){
            case "F":
                mallUserModel.setSex(1);
            case "M":
                mallUserModel.setSex(2);
            default:
                mallUserModel.setSex(3);
        }
        mallUserModel.setUserName(user.getLoginName());
        if(!Objects.isNull(user.getId())){
            mallUserModel.setUserId(user.getId());
        }
        mallUserModel.setMobile(user.getMobile());
        return mallUserModel;

    }
    private String getSign(Map<String, String> map) {
        String result = "";
        for (String key : map.keySet()) {
            if(!StringUtils.isEmpty(map.get(key)))
                result += key + "=" + map.get(key) + "&";
        }
        String before=result.substring(0, result.length() - 1) + appsecret;
        return DigestUtils.md5DigestAsHex((result.substring(0, result.length() - 1) + appsecret).getBytes());
    }
}
