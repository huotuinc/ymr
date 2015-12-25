package com.huotu.ymr.service.impl;

import com.huotu.common.base.HttpHelper;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import com.huotu.ymr.common.SysRegex;
import com.huotu.ymr.model.mall.CategoryModel;
import com.huotu.ymr.model.mall.MallGoodModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.DataCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public MallUserModel[] getUserInfoByUniond(String uniond) {
        return null;
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
    public MallUserModel createUser(String unionid, String city, String country, Integer sex, String province, String nickname, String openid) throws IOException{
        String url = commonConfigService.getMallApiServerUrl() + "/weixin/LoginAuthorize";


        Map<String, String> map = new TreeMap<>();
        map.put("timestamp", String.valueOf(new Date().getTime()));
        map.put("appid", appid);
        map.put("customerid",ymrMerchantId);
        map.put("sex",sex+"");
        map.put("nickname",nickname);
        map.put("openid",openid);
        map.put("city",city);
        map.put("country",country);
        map.put("sign", getSign(map));

        String response = HttpHelper.postRequest(url, map);




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
    public Boolean modifyUserInfo() {
        return null;
    }

    private MallUserModel userToModel(User user){
        MallUserModel mallUserModel=new MallUserModel();
        mallUserModel.setHeadUrl(user.getUserImagePath());
        mallUserModel.setIsBindMobile(SysRegex.IsValidMobileNo(user.getMobile()));
        mallUserModel.setMerchantId(user.getMerchant().getId());
        mallUserModel.setName(user.getRealName());
        mallUserModel.setNickName(user.getNickName());
        mallUserModel.setSex("男");//todo
        mallUserModel.setUserName(user.getLoginName());
        mallUserModel.setMobile(user.getMobile());
        return mallUserModel;

    }
    private String getSign(Map<String, String> map) {
        String result = "";
        for (String key : map.keySet()) {
            result += key + "=" + map.get(key).toString() + "&";
        }
        String before=result.substring(0, result.length() - 1) + appsecret;
        return DigestUtils.md5DigestAsHex((result.substring(0, result.length() - 1) + appsecret).getBytes());
    }
}
