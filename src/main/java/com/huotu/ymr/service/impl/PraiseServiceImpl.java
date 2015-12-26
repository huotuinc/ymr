package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Praise;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserSharePraiseModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.PraiseRepository;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.PraiseService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public AppUserSharePraiseModel getpraiseToModel(Praise praise) throws Exception {
        AppUserSharePraiseModel appUserSharePraiseModel=new AppUserSharePraiseModel();
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(praise.getUser().getId());
        appUserSharePraiseModel.setTitle(praise.getShare().getTitle());
        appUserSharePraiseModel.setPId(praise.getShare().getId());
        appUserSharePraiseModel.setUserHeadUrl(staticResourceService.getResource(mallUserModel.getHeadUrl()).toString());
        appUserSharePraiseModel.setShareType(praise.getShare().getShareType());
        appUserSharePraiseModel.setImg(staticResourceService.getResource(praise.getShare().getImg()).toString());
        appUserSharePraiseModel.setIntro(praise.getShare().getIntro());
        appUserSharePraiseModel.setTop(praise.getShare().getTop());
        appUserSharePraiseModel.setBoutique(praise.getShare().getBoutique());
        appUserSharePraiseModel.setTime(praise.getShare().getTime());
        return appUserSharePraiseModel;

    }
}
