package com.huotu.ymr.service.impl;

import com.alibaba.fastjson.JSON;
import com.huotu.huobanplus.common.Gender;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import com.huotu.ymr.common.HttpHelper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Administrator on 2015/12/22.
 */
@Service
public class DataCenterServiceImpl implements DataCenterService {
    @Autowired
    private UserRestRepository userRestRepository;

    @Autowired
    private GoodsRestRepository goodsRestRepository;

    @Autowired
    private CategoryRestRepository categoryRestRepository;

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
        String response= HttpHelper.postRequest(url,map);
        MallApiResultModel resultModel = JSON.parseObject(response, MallApiResultModel.class);
        if (resultModel.getCode() == 200 && !StringUtils.isEmpty(resultModel.getData().toString())) {
            return Long.parseLong(JsonPath.read(resultModel.getData().toString(), "$.userid").toString());
        }
        return null;
    }


    @Override
    public List<CategoryModel[]> getCategory(Long merchantId) throws IOException {
        List<Category> categories=categoryRestRepository.findByTitleAndCategory(Long.parseLong(ymrMerchantId), new PageRequest(0, Integer.MAX_VALUE)).getContent();
        if(!Objects.isNull(categories)&&!categories.isEmpty()){
            List<CategoryModel>categoryModels=new ArrayList<>();
            for(int i=0;i<categories.size();i++){
                Category category=categories.get(i);
                if(category.getParentId()==0){
                    CategoryModel categoryModel=new CategoryModel();
                    categoryModel.setId(category.getId());
                    categoryModel.setTitle(category.getTitle());

                    //获取二级分类
//                    for(int j=0;j<categories.size();j++){
//                        if(category.){
//                    }

                    categoryModels.add(categoryModel);

                }















            }
        }

        return null;
    }

    @Override
    public MallGoodModel[] getMallGood() {
        return new MallGoodModel[0];
    }

    @Override
    public void bindingMobile(Long userId,String mobile) throws IOException {
        userRestRepository.getOneByPK(userId).setLoginName(mobile);
    }

    @Override
    public void ModifyMobile(Long userId,String mobile) throws IOException{
        userRestRepository.getOneByPK(userId).setLoginName(mobile);
    }

    @Override
    public Boolean rechargeCoffers() {
        return null;
    }

    @Override
    public void modifyUserInfo(Long userId, String data, Integer type) throws IOException {
        User user=userRestRepository.getOneByPK(userId);
        switch (type){
            case 0:
                break;
            case 1:
                break;
            case 2:
                user.setRealName(data);
                break;
            case 3:
                user.setGender(Gender.valueOf(data));
                break;
            case 4:
                user.setLoginName(data);
                break;
            default:
                break;
        }
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
                break;
            case "M":
                mallUserModel.setSex(2);
                break;
            default:
                mallUserModel.setSex(3);
        }
        mallUserModel.setUserName(user.getLoginName());
        if(!Objects.isNull(user.getId())){
            mallUserModel.setUserId(user.getId());
        }
        mallUserModel.setMobile(SysRegex.IsValidMobileNo(user.getLoginName())?user.getLoginName():"");
        return mallUserModel;

    }
    private String getSign(Map<String, String> map) throws UnsupportedEncodingException {
        Map<String, String> resultMap = new TreeMap<String, String>();
        for (Object key : map.keySet()) {
            resultMap.put(key.toString(), map.get(key));
//            log.debug(key + " " + resultMap.get(key));
        }


        StringBuilder strB = new StringBuilder();
        for (String key : resultMap.keySet()) {
            if (!"sign".equals(key) && !StringUtils.isEmpty(resultMap.get(key))) {
                strB.append("&" + key + "=" + resultMap.get(key));
            }
        }

        String toSign = (strB.toString().length() > 0 ? strB.toString().substring(1) : "") + commonConfigService.getAppsecret();
        return DigestUtils.md5DigestAsHex(toSign.getBytes("UTF-8")).toLowerCase();
//        String result = "";
//        for (String key : map.keySet()) {
//            if(!StringUtils.isEmpty(map.get(key)))
//                result += key + "=" + map.get(key) + "&";
//        }
//        String before=result.substring(0, result.length() - 1) + appsecret;
//        return DigestUtils.md5DigestAsHex((result.substring(0, result.length() - 1) + appsecret).getBytes());
    }
}
