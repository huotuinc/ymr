package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.model.searchCondition.UserSearchModel;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户管理
 * Created by slt on 2015/12/1.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public AppUserInfoModel getAppUserInfoModel(User user, MallUserModel mallUserModel) throws Exception {
        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
        appUserInfoModel.setUserId(mallUserModel.getUserId());
        appUserInfoModel.setUserName(mallUserModel.getUserName());
        appUserInfoModel.setHeadUrl(mallUserModel.getHeadUrl());
        appUserInfoModel.setNickName(mallUserModel.getNickName());
        appUserInfoModel.setScore(user.getScore());
        appUserInfoModel.setUserLevel(user.getUserLevel());
        appUserInfoModel.setMerchantId(mallUserModel.getMerchantId());
        appUserInfoModel.setName(mallUserModel.getName());
        appUserInfoModel.setSex(mallUserModel.getSex());
        appUserInfoModel.setMobile(mallUserModel.getMobile());
        appUserInfoModel.setIsBindMobile(mallUserModel.getIsBindMobile());
        appUserInfoModel.setToken(user.getToken());
        return appUserInfoModel;
    }

    @Override
    public User getUser(Long userId) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            user=new User();
            user.setId(userId);
            user.setUserLevel(CommonEnum.UserLevel.one);
            user.setScore(0);
            user.setContinuedScore(0);

        }
        //创建一个新的token
        String token= UUID.randomUUID().toString().replaceAll("-","");
        user.setToken(token);
        userRepository.save(user);
        return user;
    }

    @Override
    public Page<User> findPcUserList(UserSearchModel userSearchModel) {
        //排序
        Sort sort;
        Sort.Direction direction = userSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (userSearchModel.getSort()) {
            case 1:
                //积分
                sort = new Sort(direction, "score");
                break;
            case 2:
                //等级
                sort = new Sort(direction, "userLevel");
                break;
            default:
                sort = new Sort(direction, "id");
                break;
        }
        return userRepository.findAll(new PageRequest(0,10));
//        return  userRepository.findAll(new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb){
//                /**
//                 * 前提条件:
//                 *
//                 */
//                Predicate predicate = cb.and(
//                        cb.equal(
//                                root.get("ownerType").as(CommonEnum.UserType.class),
//                                EnumHelper.getEnumType(CommonEnum.UserType.class,
//                                        shareSearchModel.getOwnerType())),
//                        cb.notEqual(root.get("checkStatus").as(CommonEnum.CheckType.class),
//                                CommonEnum.CheckType.delete)
//                );
//
//                if(!StringUtils.isEmpty(userSearchModel.getName()))
//
//
//                //检查状态搜索(主要用于草稿箱)
//                if(shareSearchModel.getCheckType()==-1){
//                    predicate=cb.and(predicate,
//                            cb.notEqual(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft));
//
//                }else {
//                    predicate=cb.and(predicate,
//                            cb.equal(root.get("checkStatus").as(CommonEnum.CheckType.class),
//                                    EnumHelper.getEnumType(CommonEnum.CheckType.class,shareSearchModel.getCheckType())));
//                }
//                //加入标题模糊搜索
//                if (!StringUtils.isEmpty(shareSearchModel.getShareTitle())){
//                    predicate = cb.and(predicate,cb.like(root.get("title").as(String.class),"%"+shareSearchModel.getShareTitle()+"%"));
//                }
//                //加入类型搜索(资讯，团购等)
//                if(shareSearchModel.getShareType()!=-1){
//                    predicate = cb.and(predicate,cb.equal(root.get("shareType").as(CommonEnum.ShareType.class), EnumHelper.getEnumType(CommonEnum.ShareType.class,shareSearchModel.getShareType())));
//                }
//                return predicate;
//            }
//        },new PageRequest(shareSearchModel.getPageNoStr(), 20,sort));
    }
}
