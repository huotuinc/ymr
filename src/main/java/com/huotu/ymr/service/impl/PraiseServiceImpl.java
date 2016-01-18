package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Praise;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.exception.UserNotExitsException;
import com.huotu.ymr.model.AppUserSharePraiseModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.PraiseRepository;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.PraiseService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 评论服务
 * Created by slt on 2015/12/1.
 */
@Service
public class PraiseServiceImpl implements PraiseService {
    @Autowired
    PraiseRepository praiseRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    StaticResourceService staticResourceService;
    @Override
    public List<Praise> findPraiseList(User user, Long lastId) throws Exception {
        List<Praise> list=praiseRepository.getPraiseShares(user, lastId);
        return list;
    }

    @Override
    public List<Praise> findBePraisedList(User user, Long lastId) throws Exception {
        if(lastId==0){
            return praiseRepository.getBePraisedShares(user.getId());
        }
        return praiseRepository.getBePraisedShares(user.getId(),lastId);
    }

    @Override
    public AppUserSharePraiseModel getpraiseToModel(Praise praise) throws Exception {
        AppUserSharePraiseModel appUserSharePraiseModel=new AppUserSharePraiseModel();
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(praise.getUser().getId());
        if(Objects.isNull(mallUserModel)){
            throw new UserNotExitsException();
        }
        appUserSharePraiseModel.setTitle(praise.getShare().getTitle());
        appUserSharePraiseModel.setSId(praise.getShare().getId());
        appUserSharePraiseModel.setPId(praise.getId());
        appUserSharePraiseModel.setUserName(mallUserModel.getNickName());
        appUserSharePraiseModel.setUserHeadUrl(mallUserModel.getHeadUrl());
        appUserSharePraiseModel.setShareType(praise.getShare().getShareType());
        appUserSharePraiseModel.setImg(staticResourceService.getResource(praise.getShare().getImg()).toString());
        appUserSharePraiseModel.setIntro(praise.getShare().getIntro());
        appUserSharePraiseModel.setTime(praise.getTime());
        return appUserSharePraiseModel;

    }
}
