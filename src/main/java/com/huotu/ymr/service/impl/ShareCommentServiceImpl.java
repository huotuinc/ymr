package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.exception.UserNotExitsException;
import com.huotu.ymr.model.AppUserShareCommentModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.ShareCommentRepository;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.ShareCommentService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 评论服务
 * Created by slt on 2015/12/1.
 */
@Service
public class ShareCommentServiceImpl implements ShareCommentService {
    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    StaticResourceService staticResourceService;

    @Override
    public List<ShareComment> findShareComment(Long shareId,Long lastId,Integer pageSize) throws Exception {
        List<ShareComment> listComment=shareCommentRepository.findComment(shareId, CommonEnum.ShareCommentStatus.normal, lastId, new PageRequest(0,pageSize));
        if(!listComment.isEmpty()){
            List<Long> commentIds= new ArrayList<>();
            for(ShareComment s:listComment){
                commentIds.add(s.getId());
            }
            List<ShareComment> listReply=shareCommentRepository.findCommentReply(shareId,commentIds, CommonEnum.ShareCommentStatus.normal);
            listComment.addAll(listReply);
        }
        return listComment;
    }

    @Override
    public ShareComment saveShareComment(ShareComment shareComment) throws Exception {
        return shareCommentRepository.save(shareComment);
    }

    @Override
    public ShareComment findOneShareComment(Long shareCommentId) throws Exception {
        return shareCommentRepository.findOne(shareCommentId);
    }

    @Override
    public void deleteComment(String shareCommentPath) throws Exception {
        shareCommentRepository.deleteComment(shareCommentPath+"%");
    }

    @Override
    public List<ShareComment> findCommentList(Long userId,Long lastId) throws Exception {
        List<ShareComment> list=shareCommentRepository.findUserCommentShares(userId,lastId);
        return list;
    }

    @Override
    public List<ShareComment> findBeCommentedList(Long userId, Long lastId) throws Exception {
        if(lastId==0){
            return shareCommentRepository.findUserBeCommentedShares(userId);
        }
        return shareCommentRepository.findUserBeCommentedShares(userId,lastId);
    }

    @Override
    public AppUserShareCommentModel getCommentToModel(ShareComment shareComment) throws Exception {
        AppUserShareCommentModel appUserShareCommentModel=new AppUserShareCommentModel();
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(shareComment.getUserId());
        if(Objects.isNull(mallUserModel)){
            throw new UserNotExitsException();
        }
        appUserShareCommentModel.setPId(shareComment.getShare().getId());
        appUserShareCommentModel.setCId(shareComment.getId());
        appUserShareCommentModel.setUserHeadUrl(mallUserModel.getHeadUrl());
        appUserShareCommentModel.setUserName(mallUserModel.getNickName());
        appUserShareCommentModel.setTitle(shareComment.getShare().getTitle());
        appUserShareCommentModel.setShareType(shareComment.getShare().getShareType());
        appUserShareCommentModel.setImg(staticResourceService.getResource(shareComment.getShare().getImg()).toString());
        appUserShareCommentModel.setIntro(shareComment.getShare().getIntro());
//        appUserShareCommentModel.setTop(shareComment.getShare().getTop());
//        appUserShareCommentModel.setBoutique(shareComment.getShare().getBoutique());
//        appUserShareCommentModel.setTime(shareComment.getShare().getTime());
        appUserShareCommentModel.setCommentUserId(shareComment.getParentId());
        appUserShareCommentModel.setCommentName(shareComment.getCommentName());
        appUserShareCommentModel.setCommentTime(shareComment.getTime());
        appUserShareCommentModel.setCommentComment(shareComment.getContent());
        return appUserShareCommentModel;

    }
}
