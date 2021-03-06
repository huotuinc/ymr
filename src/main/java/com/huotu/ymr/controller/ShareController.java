package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ShareSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.common.StringHelper;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.exception.ShareNotExitsException;
import com.huotu.ymr.exception.UserNotExitsException;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.model.AppShareCommentListModel;
import com.huotu.ymr.model.AppShareInfoModel;
import com.huotu.ymr.model.AppShareListModel;
import com.huotu.ymr.model.AppShareReplyModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lgh on 2015/12/1.
 * Modify by slt on 2015/12/2
 */
@Controller
@RequestMapping("/app")
public class ShareController implements ShareSystem {
    @Autowired
    ShareService shareService;

    @Autowired
    ShareCommentService shareCommentService;

    @Autowired
    CommonConfigService commonConfigService;
    @Autowired
    ConfigRepository configRepository;

    @Autowired
    MallUserRepository mallUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    PraiseRepository praiseRepository;

    @Autowired
    CommentPraiseRepository commentPraiseRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    private Environment environment;

    @Autowired
    private ShareRunningRepository shareRunningRepository;


    @RequestMapping("/searchShareList")
    @Override
    public ApiResult searchShareList(Output<AppShareListModel[]> list, String key, Long lastId,
                                     @RequestParam(required = true)Long userId) throws Exception {
        if(lastId==null){
            lastId=0L;
        }
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }

        List<Share> shares=shareService.findAppShareList(key, lastId, 3);//todo
        if(shares!=null) {
            AppShareListModel[] appShareListModels = new AppShareListModel[shares.size()];
            for (int i = 0; i < shares.size(); i++) {
                Share share=shares.get(i);
                AppShareListModel appShareListModel=shareService.shareToListModel(share,user);
                appShareListModels[i]=appShareListModel;
            }
            list.outputData(appShareListModels);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/addShare")
    @Override
    public ApiResult addShare(String title, String content,String imgUrl,Long userId) throws Exception {
        Config userGT=configRepository.findOne(ConfigKey.USER_TRANSMIT);//todo
        Config userPT=configRepository.findOne(ConfigKey.USER_POST);
        Config userTT=configRepository.findOne(ConfigKey.USER_TOTAL);

        if(title==null||content==null||imgUrl==null||userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        User user=userRepository.getOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.freeze){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_FREEZE);
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.notalk){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_NOTALK);
        }

        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);// todo 最后通过数据中心返回用户信息
        if(Objects.isNull(mallUserModel)){
            throw new UserNotExitsException();
        }
        Share share=new Share();
        share.setOwnerId(userId);
        share.setName(mallUserModel.getNickName());
        share.setOwnerType(CommonEnum.UserType.user);
        share.setTitle(title);
        share.setShareType(CommonEnum.ShareType.userShare);
        share.setImg(imgUrl);
        share.setContent(content);
        share.setIntro(StringHelper.getText(content,100));
        share.setTop(false);
        share.setTime(new Date());
        share.setPostReward(Integer.parseInt(userPT.getValue()));
        share.setRelayReward(Integer.parseInt(userGT.getValue()));
        share.setScore(Integer.parseInt(userTT.getValue()));
        share.setUsedScore(0);
        share.setCheckStatus(CommonEnum.CheckType.audit);
        share.setReason("");
        share.setBoutique(false);
        shareService.saveShare(share);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/getShareInfo")
    @Override
    public ApiResult getShareInfo(Output<AppShareInfoModel> data,Long shareId) throws Exception {
        if(shareId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SHARE_NOT_FOUND);
        }
        //浏览量加+1
        share.setView(share.getView()+1);
        shareService.saveShare(share);
        AppShareInfoModel shareInfoModel=new AppShareInfoModel();
        shareInfoModel.setTitle(share.getTitle());
        shareInfoModel.setTime(share.getTime());
        shareInfoModel.setSId(share.getId());
        shareInfoModel.setImg(staticResourceService.getResource(share.getImg()).toString());
        shareInfoModel.setContent(share.getContent());
        shareInfoModel.setPraiseQuantity(share.getPraiseQuantity());
        shareInfoModel.setRelayReward(share.getRelayReward());
        shareInfoModel.setRelayQuantity(share.getRelayQuantity());
        shareInfoModel.setTotalIntegral(share.getScore());
        shareInfoModel.setUseIntegral(share.getUsedScore());
        shareInfoModel.setTransmitUrl(staticResourceService.getResource("/transmit/shareInfo?shareId="+share.getId()).toString());
        data.outputData(shareInfoModel);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping("/setTransmitShare")
    public ApiResult setTransmitShare(@RequestParam(required = true)Long userId,
                                      @RequestParam(required = true)Long shareId) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            throw new ShareNotExitsException();
        }
        //检查用户是否转发过文章
        List<ShareRunning> shareRunnings=shareRunningRepository.findByUserIdAndShare(userId,share);
        //用户加积分
        if(shareRunnings.isEmpty()){//如果用户没有转发过该文章
            Integer remainScore=share.getScore()-share.getUsedScore();
            Integer award=Integer.parseInt(configRepository.findOne(ConfigKey.USER_TRANSMIT).getValue());
            if(remainScore>0){//如果文章还有积分可获取
                if(award>remainScore){//如果用户获取积分的值大于文章剩余的值
                    award=remainScore;//增加剩余的值
                }

                user.setScore(user.getScore()+award);
                user.setContinuedScore(user.getContinuedScore()+award);
                share.setUsedScore(share.getUsedScore()+award);
                userRepository.save(user);
                shareService.saveShare(share);
            }
        }
        ShareRunning shareRunning=new ShareRunning();
        shareRunning.setUserId(userId);
        shareRunning.setShare(share);
        shareRunning.setTime(new Date());
        if(shareRunnings.isEmpty()){
            shareRunning.setIntegral(share.getRelayReward());
        }else {
            shareRunning.setIntegral(0);
        }
        shareRunningRepository.save(shareRunning);
        share.setRelayQuantity(share.getRelayQuantity()+1);
        shareService.saveShare(share);
        return  ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

    }

    @RequestMapping("/clickPraise")
    @Override
    public ApiResult clickPraise(Output<Long> data,Long shareId, Long userId) throws Exception {
        //数据有效性检查
        if(shareId==null||userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        Share share=shareService.findOneShare(shareId);
        User user=userRepository.findOne(userId);
        if(Objects.isNull(share)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);

        }
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SHARE_NOT_FOUND);
        }
        //往数据库中插入一条点赞记录
        Praise praise=praiseRepository.findByShareAndUser(share,user);//todo 根据用户和文章找出的点赞记录
        //如果没有点过赞
        if(Objects.isNull(praise)){
            praise=new Praise();
            praise.setShare(share);
            praise.setUser(user);
            praise.setTime(new Date());
            praiseRepository.save(praise);
            //帖子点赞量修改
            share.setPraiseQuantity(share.getPraiseQuantity()+1);
            shareService.saveShare(share);
        }
        data.outputData(share.getPraiseQuantity());
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/clickCommentPraise")
    @Override
    public ApiResult clickCommentPraise(Output<Integer>data,Long commentId, Long userId) throws Exception {
        //数据有效性检查
        if(commentId==null||userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        ShareComment comment=shareCommentService.findOneShareComment(commentId);
        User user=userRepository.findOne(userId);
        if(Objects.isNull(comment)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_COMMENT_NOT_FOUND);

        }
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SHARE_NOT_FOUND);
        }
        CommentPraise commentPraise=commentPraiseRepository.findByCommentAndUser(comment, user);//todo 根据用户和评论找出的点赞记录
        //如果没有点过赞
        if(Objects.isNull(commentPraise)){
            commentPraise=new CommentPraise();
            commentPraise.setComment(comment);
            commentPraise.setUser(user);
            commentPraiseRepository.save(commentPraise);
            //帖子点赞量修改
            comment.setPraiseQuantity(comment.getPraiseQuantity()+1);
            shareCommentService.saveShareComment(comment);
        }
        data.outputData(comment.getPraiseQuantity());
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

    }

    @RequestMapping("/searchShareCommentList")
    @Override
    public ApiResult searchShareCommentList(Output<AppShareCommentListModel[]> list,Long shareId,Long lastId,
                                            @RequestParam(required = true)Long userId) throws Exception {
        if(lastId==null){
            lastId=0L;
        }
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
         List<ShareComment> shareComments=shareCommentService.findShareComment(shareId,lastId,3);
        if(!Objects.isNull(shareComments)&&!shareComments.isEmpty()){
            List<AppShareCommentListModel> appShareCommentListModels=new ArrayList<>();
            for(int i=0;i<shareComments.size();i++){
                ShareComment shareComment=shareComments.get(i);
                AppShareCommentListModel appShareCommentListModel=new AppShareCommentListModel();
                if(shareComment.getParentId()==0){
                    CommentPraise commentPraise=commentPraiseRepository.findByCommentAndUser(shareComment,user);
                    appShareCommentListModel.setPid(shareComment.getId());
                    appShareCommentListModel.setName(shareComment.getCommentName());
                    appShareCommentListModel.setLevel(shareComment.getLevel());
                    appShareCommentListModel.setCommentUserId(shareComment.getUserId());
                    appShareCommentListModel.setUserHeadUrl(shareComment.getHeadUrl());
                    appShareCommentListModel.setContent(shareComment.getContent());
                    appShareCommentListModel.setTime(shareComment.getTime());
                    appShareCommentListModel.setCommentQuantity(shareComment.getCommentQuantity());
                    appShareCommentListModel.setPraiseQuantity(shareComment.getPraiseQuantity());
                    appShareCommentListModel.setPraise(!Objects.isNull(commentPraise));
                    //开始查找该评论下的回复
                    List<AppShareReplyModel> appShareReplyModels=new ArrayList<>();
                    for(int j=0;j<shareComments.size();j++){
                        ShareComment reply=shareComments.get(j);
                        if(reply.getParentId()!=0){
                            Long parentId=Long.parseLong(reply.getCommentPath().split("\\|")[1]);
                            if(parentId.equals(shareComment.getId())){
                                AppShareReplyModel appShareReplyModel=new AppShareReplyModel();
                                appShareReplyModel.setRid(reply.getId());
                                appShareReplyModel.setReplyId(reply.getUserId());
                                appShareReplyModel.setUserId(reply.getUserId());
                                ShareComment comment=shareCommentService.findOneShareComment(reply.getParentId());
                                appShareReplyModel.setToReplyId(Objects.isNull(comment)?0:comment.getUserId());//todo
                                appShareReplyModel.setReplyName(reply.getCommentName());
                                appShareReplyModel.setToReplyName(reply.getParentName());
                                appShareReplyModel.setContent(reply.getContent());
                                appShareReplyModels.add(appShareReplyModel);

                            }
                        }
                    }
                    //评论查找完毕
                    appShareCommentListModel.setReplyModels(appShareReplyModels);
                    appShareCommentListModels.add(appShareCommentListModel);
                }

            }

            list.outputData(appShareCommentListModels.toArray(new AppShareCommentListModel[appShareCommentListModels.size()]));
        }else {
            list.outputData(null);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/addReply")
    public ApiResult addReply(Long userId, Long parentId,Long commentId, String content) throws Exception {
        if(userId==null||parentId==null||content==null||commentId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
        if(Objects.isNull(mallUserModel)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        if(commentId!=0&&Objects.isNull(shareCommentService.findOneShareComment(commentId))){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_COMMENT_NOT_FOUND);
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.freeze){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_FREEZE);
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.notalk){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_NOTALK);
        }
        ShareComment shareComment=shareCommentService.findOneShareComment(parentId);
        if(Objects.isNull(shareComment)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_COMMENT_NOT_FOUND);
        }
        ShareComment reply=new ShareComment();
        reply.setShare(shareComment.getShare());
        reply.setUserId(userId);
        reply.setCommentId(commentId);
        reply.setCommentName(mallUserModel.getNickName());
        reply.setLevel(user.getUserLevel());
        reply.setHeadUrl(mallUserModel.getHeadUrl());
        reply.setContent(content);
        reply.setTime(new Date());
        reply.setStatus(CommonEnum.ShareCommentStatus.normal);
        reply.setParentId(shareComment.getId());
        reply.setParentName(shareComment.getCommentName());
        reply=shareCommentService.saveShareComment(reply);
        reply.setCommentPath(shareComment.getCommentPath()+reply.getId()+"|");
        shareCommentService.saveShareComment(reply);

        //评论量+1
        Share share=shareService.findOneShare(shareComment.getShare().getId());
        share.setCommentQuantity(share.getCommentQuantity()+1);
        shareService.saveShare(share);
        //todo 返回值需要讨论
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/deleteComment")
    public ApiResult deleteComment(Long commentId, Long userId) throws Exception {
        if(commentId==null||userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        ShareComment comment=shareCommentService.findOneShareComment(commentId);
        User user=userRepository.findOne(userId);
        if(Objects.isNull(comment)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_COMMENT_NOT_FOUND);
        }
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        if(userId.equals(comment.getUserId())){
            shareCommentService.deleteComment(comment.getCommentPath());
        }

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/addComment")
    public ApiResult addComment(@RequestParam(required = true)Long userId, Long shareId, String content) throws Exception {
        if(userId==null||shareId==null||content==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
//        User user=userRepository.findOne(userId);
        MallUserModel mallUserModel = dataCenterService.getUserInfoByUserId(userId);
        if (Objects.isNull(mallUserModel)) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        User user = userRepository.findOne(mallUserModel.getUserId());
        if (Objects.isNull(user)) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.freeze){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_FREEZE);
        }
        if(user.getUserStatus()== CommonEnum.UserStatus.notalk){
            return ApiResult.resultWith(CommonEnum.AppCode.USER_BE_NOTALK);
        }
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SHARE_NOT_FOUND);

        }
        ShareComment shareComment=new ShareComment();
        shareComment.setShare(share);
        shareComment.setUserId(mallUserModel.getUserId());
        shareComment.setCommentName(mallUserModel.getNickName());
        shareComment.setLevel(user.getUserLevel());
        shareComment.setHeadUrl(mallUserModel.getHeadUrl());
//        shareComment.setHeadUrl(staticResourceService.getResource(mallUserModel.getHeadUrl()).toString());//todo
        shareComment.setContent(content);
        shareComment.setStatus(CommonEnum.ShareCommentStatus.normal);
        shareComment.setTime(new Date());
        shareComment.setParentId(0L);
        shareComment.setCommentId(0L);
        shareComment.setParentName("");
        shareComment=shareCommentService.saveShareComment(shareComment);
        shareComment.setCommentPath("|"+shareComment.getId()+"|");
        shareCommentService.saveShareComment(shareComment);
        //文章评论量+1
        share.setCommentQuantity(share.getCommentQuantity() + 1);
        shareService.saveShare(share);


        //todo 返回值需要讨论
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
