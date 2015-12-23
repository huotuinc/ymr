package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ShareSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.model.AppShareCommentListModel;
import com.huotu.ymr.model.AppShareInfoModel;
import com.huotu.ymr.model.AppShareListModel;
import com.huotu.ymr.model.AppShareReplyModel;
import com.huotu.ymr.repository.CommentPraiseRepository;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.PraiseRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.ShareCommentService;
import com.huotu.ymr.service.ShareService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @RequestMapping("/searchShareList")
    @Override
    public ApiResult searchShareList(Output<AppShareListModel[]> list, String key, Long lastId) throws Exception {
        if(lastId==null){
            lastId=0L;
        }
        List<Share> shares=shareService.findAppShareList(key, lastId, 10);//todo
        if(shares!=null) {
            AppShareListModel[] appShareListModels = new AppShareListModel[shares.size()];
            for (int i = 0; i < shares.size(); i++) {
                Share share=shares.get(i);
                AppShareListModel appShareListModel=new AppShareListModel();
                appShareListModel.setPId(share.getId());
                appShareListModel.setTitle(share.getTitle());
                appShareListModel.setImg(staticResourceService.getResource(share.getImg()).toString());//todo
                appShareListModel.setIntro(share.getIntro());
                appShareListModel.setCommentQuantity(share.getCommentQuantity());
                appShareListModel.setContent(share.getContent());
                appShareListModel.setShareType(share.getShareType());
                appShareListModel.setPraiseQuantity(share.getPraiseQuantity());
                appShareListModel.setRelayQuantity(share.getRelayQuantity());
                appShareListModel.setRelayScore(share.getRelayReward());
                appShareListModel.setTime(share.getTime());
                appShareListModel.setTop(share.getTop());
                appShareListModel.setUserHeadUrl("http://cdn.duitang.com/uploads/item/201402/11/20140211190918_VcMBs.thumb.224_0.jpeg");//todo
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
        MallUser user=mallUserRepository.findOne(userId);// todo 最后通过数据中心返回用户信息
        Share share=new Share();
        share.setOwnerId(userId);
        share.setName(user.getWxNickName());
        share.setOwnerType(CommonEnum.UserType.user);
        share.setTitle(title);
        share.setShareType(CommonEnum.ShareType.userShare);
        share.setImg(imgUrl);
        share.setContent(content);
        share.setIntro("");
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
        AppShareInfoModel shareInfoModel=new AppShareInfoModel();
        shareInfoModel.setTitle(share.getTitle());
        shareInfoModel.setTime(share.getTime());
        shareInfoModel.setImg(staticResourceService.getResource(share.getImg()).toString());
        shareInfoModel.setContent(share.getContent());
        shareInfoModel.setPraiseQuantity(share.getPraiseQuantity());
        shareInfoModel.setRelayReward(share.getRelayReward());
        shareInfoModel.setRelayQuantity(share.getRelayQuantity());
        shareInfoModel.setTotalIntegral(share.getScore());
        shareInfoModel.setUseIntegral(share.getUsedScore());
        data.outputData(shareInfoModel);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
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
    public ApiResult searchShareCommentList(Output<AppShareCommentListModel[]> list,Long shareId,Long lastId) throws Exception {
        if(lastId==null){
            lastId=0L;
        }
        Map<Long,List<ShareComment>> shareComments=shareCommentService.findShareComment(shareId,lastId,10);
        if(shareComments!=null&&shareComments.size()!=0){
            AppShareCommentListModel[] appShareCommentListModels=new AppShareCommentListModel[shareComments.size()];
            int n=0;


            for (Map.Entry<Long, List<ShareComment>> entry : shareComments.entrySet()) {
                List<ShareComment> commentList=entry.getValue();
                if(commentList==null||commentList.size()==0){
                    continue;
                }
                AppShareCommentListModel appShareCommentListModel=new AppShareCommentListModel();
                ShareComment shareComment=commentList.get(0);
                appShareCommentListModel.setPid(shareComment.getId());
                appShareCommentListModel.setLevel(shareComment.getLevel());
                appShareCommentListModel.setName(shareComment.getCommentName());//todo
                appShareCommentListModel.setContent(shareComment.getContent());
                appShareCommentListModel.setUserHeadUrl(shareComment.getHeadUrl());//todo
                appShareCommentListModel.setCommentQuantity(shareComment.getCommentQuantity());
                appShareCommentListModel.setPraiseQuantity(shareComment.getPraiseQuantity());
                appShareCommentListModel.setTime(shareComment.getTime());

                AppShareReplyModel[] appShareReplyModels=new AppShareReplyModel[commentList.size()-1];
                for(int i=0;i<commentList.size()-1;i++){
                    AppShareReplyModel appShareReplyModel=new AppShareReplyModel();
                    ShareComment reply=commentList.get(i+1);
                    appShareReplyModel.setRid(reply.getId());
                    appShareReplyModel.setReplyName(reply.getCommentName());//todo
                    appShareReplyModel.setContent(reply.getContent());
                    appShareReplyModel.setToReplyName(reply.getParentName());
                    appShareReplyModels[i]=appShareReplyModel;
                }
                appShareCommentListModel.setReplyModels(appShareReplyModels);
                appShareCommentListModels[n]=appShareCommentListModel;
                n++;
            }
            list.outputData(appShareCommentListModels);
        }else {
            list.outputData(null);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/addReply")
    public ApiResult addReply(Long userId, Long parentId, String content) throws Exception {
        if(userId==null||parentId==null||content==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUser mallUser=mallUserRepository.findOne(userId);
        if(Objects.isNull(mallUser)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        User user=userRepository.findOne(mallUser.getId());
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        ShareComment shareComment=shareCommentService.findOneShareComment(parentId);
        if(Objects.isNull(shareComment)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_COMMENT_NOT_FOUND);
        }
        ShareComment reply=new ShareComment();
        reply.setShare(shareComment.getShare());
        reply.setUserId(mallUser.getId());
        reply.setCommentName(mallUser.getWxNickName());
//        reply.setLevel(user.getUserLevel());
//        reply.setHeadUrl(commonConfigService.getResoureServerUrl()+mallUser.getWxHeadUrl());//todo
        reply.setContent(content);
        reply.setTime(new Date());
        reply.setParentId(shareComment.getId());
        reply.setParentName(shareComment.getCommentName());
        reply=shareCommentService.saveShareComment(reply);
        reply.setCommentPath(shareComment.getCommentPath()+reply.getId()+"|");
        shareCommentService.saveShareComment(reply);
        //todo 返回值需要讨论
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
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
            shareCommentService.deleteComment(commentId);
        }

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping(value = "/addComment")
    public ApiResult addComment(Long userId, Long shareId, String content) throws Exception {
        if(userId==null||shareId==null||content==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUser mallUser=mallUserRepository.findOne(userId);
        if(Objects.isNull(mallUser)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        User user=userRepository.findOne(mallUser.getId());
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SHARE_NOT_FOUND);

        }
        ShareComment shareComment=new ShareComment();
        shareComment.setShare(share);
        shareComment.setUserId(mallUser.getId());
        shareComment.setCommentName(mallUser.getWxNickName());
        shareComment.setLevel(user.getUserLevel());
        shareComment.setHeadUrl(staticResourceService.getResource(mallUser.getWxHeadUrl()).toString());//todo
        shareComment.setContent(content);
        shareComment.setTime(new Date());
        shareComment.setParentId(0L);
        shareComment.setParentName("");
        shareComment=shareCommentService.saveShareComment(shareComment);
        shareComment.setCommentPath("|"+shareComment.getId()+"|");
        shareCommentService.saveShareComment(shareComment);
        //todo 返回值需要讨论
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
