package com.huotu.ymr.service.impl;

import com.huotu.ymr.model.mall.CategoryModel;
import com.huotu.ymr.model.mall.MallGoodModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.service.DataCenterService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
@Service
public class DataCenterServiceImpl implements DataCenterService {


    @Override
    public MallUserModel[] getUserInfoByUniond(String uniond) {
        return null;
    }

    @Override
    public MallUserModel getUserInfoByMobile(String mobile) {
        return null;
    }

    @Override
    public MallUserModel getUserInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public MallUserModel createUser(String accreditInfo) {
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
}
