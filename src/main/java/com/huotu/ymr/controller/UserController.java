package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.common.*;
import com.huotu.ymr.entity.*;
import com.huotu.ymr.exception.InterrelatedException;
import com.huotu.ymr.exception.UserNotExitsException;
import com.huotu.ymr.model.*;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * 用户系统
 * Created by lgh on 2015/11/20.
 */
@Controller
@RequestMapping("/app")
public class UserController implements UserSystem {
    private static Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppGlobalModel appGlobalModel;

    @Autowired
    private ConfigAppVersionRepository configAppVersionRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    private Environment environment;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ShareService shareService;

    @Autowired
    ShareCommentService shareCommentService;

    @Autowired
    PraiseRepository praiseRepository;

    @Autowired
    PraiseService praiseService;

    @Autowired
    ShareRunningRepository shareRunningRepository;


    @RequestMapping("/init")
    @Override
     public ApiResult init(Output<AppGlobalModel> global, Output<AppUserInfoModel> user, Output<AppUpdateModel> update) throws Exception {
        global.outputData(appGlobalModel);

        AppPublicModel pms = PublicParameterHolder.get();

        update.outputData(versionChecking(pms.getOperation(), pms.getVersion(), pms.getImei()));


        AppUserInfoModel appUserInfoModel = pms.getCurrentUser();
        user.outputData(appUserInfoModel);
//        if (appUserInfoModel == null) {
//            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_LOGIN_FAIL);
//        }
        user.outputData(appUserInfoModel);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }


    /**
     * 获取该用户数据
     * 通过unionId去商城获取用户数据
     *
     *      1.一条数据，判断本地数据库中User是否存在，如果存在则登录成功，如果不存在则创建一个User,修改token,最后返回该User信息
     *      2.0条数据，请求商城创建一个MallUser,本地数据库创建一个User,修改token,最后返回该User信息
     *
     * @throws Exception
     */
    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data,
                           String unionid,
                           String headimgurl,
                           String city,
                           String country,
                           Integer sex,
                           String province,
                           String nickname ,
                           String openid) throws Exception {
            AppUserInfoModel appUserInfoModel;
            MallUserModel mallUserModel=null;
//            MallUserModel mallUserModel = dataCenterService.getUserInfoByUniond(unionid)[0];//todo 调用数据中心
            if(Objects.isNull(mallUserModel)){
                AppWeiXinAccreditModel appWeiXinAccreditModel=new AppWeiXinAccreditModel();
                appWeiXinAccreditModel.setUnionid(unionid);
                appWeiXinAccreditModel.setOpenid(openid);
                appWeiXinAccreditModel.setNickname(nickname);
                appWeiXinAccreditModel.setHeadimgurl(headimgurl);
                appWeiXinAccreditModel.setSex(sex);
                appWeiXinAccreditModel.setCountry(country);
                appWeiXinAccreditModel.setCity(city);
                appWeiXinAccreditModel.setProvince(province);
                Long userId=dataCenterService.createUser(appWeiXinAccreditModel);
                if(userId==null){
                    throw new Exception("商城创建用户失败");
                }
                User user=userService.getUser(userId);
                mallUserModel=dataCenterService.getUserInfoByUserId(userId);
                mallUserModel.setSex(sex);
                appUserInfoModel=userService.getAppUserInfoModel(user, mallUserModel);
            }else {
                User user=userService.getUser(mallUserModel.getUserId());
                appUserInfoModel=userService.getAppUserInfoModel(user,mallUserModel);
            }
            data.outputData(appUserInfoModel);

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/changeUser")
    @Override
    public ApiResult changeUser(Output<AppUserInfoModel[]> list, String unionId) throws Exception {
        if (unionId == null) {
            ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUserModel[] mallUserModels=dataCenterService.getUserInfoByUniond(unionId);//todo
        AppUserInfoModel[] appUserInfoModels=new AppUserInfoModel[mallUserModels.length];
        for(int i=0;i<mallUserModels.length;i++){
            User user=userService.getUser(mallUserModels[i].getUserId());
            AppUserInfoModel appUserInfoModel=userService.getAppUserInfoModel(user,mallUserModels[i]);
            appUserInfoModels[i]=appUserInfoModel;
        }
        list.outputData(appUserInfoModels);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

//    /**
//     * 根据userId，返回User对象，如果没有则创建一个
//     * @param userId
//     * @return
//     * @throws Exception
//     */
//    private User getUser(Long userId) throws Exception{
//        User user=userRepository.findOne(userId);
//        if(Objects.isNull(user)){
//            user=new User();
//            user.setId(userId);
//            user.setUserLevel(CommonEnum.UserLevel.one);
//            user.setScore(0);
//            user.setContinuedScore(0);
//
//        }
//        //创建一个新的token
//        String token=UUID.randomUUID().toString().replaceAll("-","");
//        user.setToken(token);
//        userRepository.save(user);
//        return user;
//
//    }

//    /**
//     * 根据User和MallUserModel返回AppUserInfoModel
//     * @param user
//     * @param mallUserModel
//     * @return
//     * @throws Exception
//     */
//    private AppUserInfoModel getAppUserInfoModel(User user,MallUserModel mallUserModel)throws Exception{
//        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
//        appUserInfoModel.setUserId(mallUserModel.getUserId());
//        appUserInfoModel.setUserName(mallUserModel.getUserName());
//        appUserInfoModel.setHeadUrl(mallUserModel.getHeadUrl());
//        appUserInfoModel.setNickName(mallUserModel.getNickName());
//        appUserInfoModel.setScore(user.getScore());
//        appUserInfoModel.setUserLevel(user.getUserLevel());
//        appUserInfoModel.setMerchantId(mallUserModel.getMerchantId());
//        appUserInfoModel.setName(mallUserModel.getName());
//        appUserInfoModel.setSex(mallUserModel.getSex());
//        appUserInfoModel.setMobile(mallUserModel.getMobile());
//        appUserInfoModel.setIsBindMobile(mallUserModel.getIsBindMobile());
//        appUserInfoModel.setToken(user.getToken());
//        return appUserInfoModel;
//    }

//    /**
//     * 判断验证码是否正确
//     * 不正确：返回信息
//     * 正确：
//     *
//     * @param data  返回的用户数据
//     * @param phone 手机号
//     * @param code  验证码
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/loginByMobile")
//    @Override
//    public ApiResult loginByMobile(Output<AppUserInfoModel> data, String phone, String code) throws Exception {
//        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
//            ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
//        }
//        VerificationCode verificationCode = verificationCodeRepository.findByMobileAndType(phone, CommonEnum.VerificationType.reg);
//        if (Objects.isNull(verificationCode)) {
//            ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_CODE);
//        }
//        MallUser mallUser = new MallUser();//todo 调用数据中心新建商城用户
//        User user = new User();
////        data.outputData(getAppUserInfoModel(mallUser, user));
//        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
//    }


//    private AppUserInfoModel getAppUserInfoModel(MallUser mallUser, User user) throws URISyntaxException {
//        AppUserInfoModel appUserInfoModel = new AppUserInfoModel();
//        if (Objects.isNull(user)) {
//            user = new User();
//            user.setId(mallUser.getId());
//            user.setToken(UUID.randomUUID().toString().replace("-", ""));
//            user.setUserLevel(CommonEnum.UserLevel.one);
//            user = userRepository.save(user);
//        }
//        appUserInfoModel.setUserId(mallUser.getId());
//        appUserInfoModel.setUserName(mallUser.getUsername());
////        appUserInfoModel.setHeadUrl(staticResourceService.getResource("").toString());//todo 图片
//        appUserInfoModel.setHeadUrl(staticResourceService.getResource(mallUser.getWxHeadUrl()).toString());
//        appUserInfoModel.setNickName(mallUser.getWxNickName());
//        appUserInfoModel.setScore(user.getScore());
//        appUserInfoModel.setUserLevel(user.getUserLevel());
//        appUserInfoModel.setMerchantId(mallUser.getMerchant().getId());
//        appUserInfoModel.setName(mallUser.getRealName());
//        appUserInfoModel.setSex(mallUser.getGender());
//        appUserInfoModel.setMobile(mallUser.getMobile());
//        appUserInfoModel.setIsBindMobile(SysRegex.IsValidMobileNo(mallUser.getUsername()));
//        appUserInfoModel.setToken(user.getToken());
//        return appUserInfoModel;
//
//    }


    @RequestMapping("/sendSMS")
    @Override
    public ApiResult sendSMS(String phone, int type) throws Exception {
        CommonEnum.VerificationType verificationType = EnumHelper.getEnumType(CommonEnum.VerificationType.class, type);
        Date date = new Date();
        // **********************************************************
        // 发送短信前处理
        if (!SysRegex.IsValidMobileNo(phone)) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_MOBILE);
        }

        Random rnd = new Random();
        String code = StringHelper.RandomNum(rnd, 4);

        try {
            verificationService.sendCode(phone, VerificationService.VerificationProject.fanmore, code, date, verificationType);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
        } catch (IllegalStateException ex) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_VERIFICATION_INTERVAL);
        } catch (IllegalArgumentException ex) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_MOBILE);
        } catch (NoSuchMethodException ex) {
            //发送类别不受支持！
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SEND_MESSAGE_FAIL);
        } catch (InterrelatedException ex) {
            //第三方错误！
            log.error("短信发送失败", ex);
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_SEND_MESSAGE_FAIL);
        }
    }

    @RequestMapping("/bindMobile")
    @Override
    public ApiResult bindMobile(String code, String phone,Long currentDate,Long userId) throws Exception {
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(phone)||userId==null||currentDate==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeAndSendTimeGreaterThan(
                phone, CommonEnum.VerificationType.bind, new Date(currentDate - 60 * 1000));
        for (VerificationCode verificationCode : codeList) {
            if (!verificationCode.getCode().equals(code))
                return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_CODE);
        }
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
        //检查是否已经绑定了手机号
        if(mallUserModel.getIsBindMobile()){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE_ALREADY_BINDING);
        }
        //todo 开始绑定手机号
        dataCenterService.bindingMobile();

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/modifyMobile")
    @Override
    public ApiResult modifyMobile(String code, String phone,Long currentDate,Long userId) throws Exception {
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(phone)||userId==null||currentDate==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeAndSendTimeGreaterThan(
                phone, CommonEnum.VerificationType.bind, new Date(currentDate - 60 * 1000));
        for (VerificationCode verificationCode : codeList) {
            if (!verificationCode.getCode().equals(code))
                return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_CODE);
        }
//        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
//        if(SysRegex.IsValidMobileNo(mallUserModel.getUserName()))
        //todo 修改绑定手机
        dataCenterService.modifyUserInfo();
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/putToCoffers")
    @Override
    public ApiResult putToCoffers(Long userId) throws Exception {
        if(userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        Double scoreToGold=Double.parseDouble(configRepository.findOne(ConfigKey.SCORE_TO_GOLD).getValue());
        Integer score=user.getScore();
        if(score<=0||scoreToGold*score>1){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_INTEGRAL_INSUFFICIENT);
        }
        //可以提现的金币数量
        Double golds=score*scoreToGold;
        //todo 充值小金库
        boolean flag=dataCenterService.rechargeCoffers();
        if(flag){
            //改变积分数值
            userRepository.save(user);
            return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
        }else {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_EXTRACT_FIAL);
        }

    }

    @RequestMapping("/getScorePutInfo")
    @Override
    public ApiResult getScorePutInfo(Output<Double> upgradeMoney, Output<Double> rate,Long userId) throws Exception {
        if(userId==null){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        //一级升二级所需积分
        Config upT=configRepository.findOne(ConfigKey.UPGRADE_INTEGRAL);
        //用户实际所需的积分
        Integer realNeedIntegral=Integer.parseInt(upT.getValue())-user.getContinuedScore();
        //钱转积分的比例
        Double mts=Double.parseDouble(configRepository.findOne(ConfigKey.MONEY_TO_SCORE).getValue());
        if(realNeedIntegral>0){
            //取整
            Double realNeedMoney=Math.ceil(realNeedIntegral/mts);
            upgradeMoney.outputData(realNeedMoney);
        }else {
            upgradeMoney.outputData(0.0);
        }
        rate.outputData(mts);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/put")
    @Override
    public ApiResult put(Output<String> orderNo, Double money) throws Exception {
        return null;
    }

    @RequestMapping("/updateUserProfile")
    @Override
    public ApiResult updateUserProfile(Output<AppUserInfoModel> data,
                                       @RequestParam(required = true)Long userId,
                                       @RequestParam(required = true)Integer profileType,
                                       @RequestParam(required = true)Object profileData) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
        if(Objects.isNull(user.getId())){
            throw new UserNotExitsException();
        }
        switch (profileType){
            case 0:
                //上传头像
                break;
            case 1:
                //昵称修改
                String nickName=(String)profileData;
                break;
            case 2:
                //姓名修改
                String realName=(String)profileData;
                break;
            case 3:
                //性别修改
                String sex=(String)profileData;
                break;
            case 4:
                //联系电话
                String phone=(String)profileData;
                break;
            case 5:
                //定位
                break;
        }

        return null;

    }

    @RequestMapping("/messages")
    @Override
    public ApiResult messages(Output<AppMessageModel[]> messages, Long lastId) throws Exception {
        return null;
    }

    @RequestMapping("/getMyCrowdFundingList")
    @Override
    public ApiResult getMyCrowdFundingList(Output<AppCrowdFundingListModel[]> list, Long lastId) throws Exception {

        return null;
    }

    @RequestMapping("/getPraiseList")
    @Override
    public ApiResult getPraiseList(Output<AppUserSharePraiseModel[]> list,
                                   @RequestParam(required = true)Long userId,
                                   @RequestParam(required = true)Long lastId) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        List<Praise> praises=praiseService.findPraiseList(user, lastId);
        if(!praises.isEmpty()){
            AppUserSharePraiseModel[] appUserSharePraiseModels=new AppUserSharePraiseModel[praises.size()];

            for(int i=0;i<praises.size();i++){
                appUserSharePraiseModels[i]=praiseService.getpraiseToModel((praises.get(i)));
            }
            list.outputData(appUserSharePraiseModels);
        }else {
            list.outputData(null);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
    @RequestMapping("/getCommentList")
    @Override
    public ApiResult getCommentList(Output<AppUserShareCommentModel[]> list,
                                    @RequestParam(required = true)Long userId,
                                    @RequestParam(required = true)Long lastId) throws Exception {

        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        List<ShareComment> shareComments=shareCommentService.findCommentList(userId, lastId);
        if(!shareComments.isEmpty()){
            AppUserShareCommentModel[] appUserShareCommentModels=new AppUserShareCommentModel[shareComments.size()];

            for(int i=0;i<shareComments.size();i++){
                appUserShareCommentModels[i]=shareCommentService.getCommentToModel(shareComments.get(i));
            }
            list.outputData(appUserShareCommentModels);
        }else {
            list.outputData(null);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/getUserShareList")
    @Override
    public ApiResult getUserShareList(Output<AppUserShareRunningModel[]> list,
                                      @RequestParam(required = true)Long userId,
                                      @RequestParam(required = true)Long lastId) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            throw new UserNotExitsException();
        }
        List<ShareRunning> shareRunnings=shareRunningRepository.findByUserId(userId,lastId);
        if(!shareRunnings.isEmpty()){
            AppUserShareRunningModel[] appUserShareRunningModels=new AppUserShareRunningModel[shareRunnings.size()];

            for(int i=0;i<shareRunnings.size();i++){
                AppUserShareRunningModel appUserShareRunningModel=new AppUserShareRunningModel();
                ShareRunning shareRunning=shareRunnings.get(i);
                appUserShareRunningModel.setPId(shareRunning.getShare().getId());
                appUserShareRunningModel.setTitle(shareRunning.getShare().getTitle());
                appUserShareRunningModel.setShareType(shareRunning.getShare().getShareType());
                appUserShareRunningModel.setImg(shareRunning.getShare().getImg());
                appUserShareRunningModel.setIntro(shareRunning.getShare().getIntro());
                appUserShareRunningModel.setTime(shareRunning.getShare().getTime());
                appUserShareRunningModel.setIntegral(shareRunning.getIntegral());
                appUserShareRunningModels[i]=appUserShareRunningModel;
            }
            list.outputData(appUserShareRunningModels);
        }else {
            list.outputData(null);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/updateDeviceToken")
    @Override
    public ApiResult updateDeviceToken(String deviceToken) throws Exception {
        return null;
    }

    private AppUpdateModel versionChecking(String opertion, String version, String imei) {
        AppUpdateModel result = new AppUpdateModel();

        if (opertion.equals("YMR2015AD")) {

            ConfigAppVersion newestVersionModel = configAppVersionRepository.findTopByOrderByIdDesc();

            if (!RegexHelper.IsVersion(version)) // 版本不正确强制整包更新
            {
                result.setUpdateType(CommonEnum.VersionUpdateType.NO);
                result.setUpdateUrl("");
                result.setUpdateTips("");
                result.setUpdateMD5("");
                return result;
            }

            // 获取当前App版本
            ConfigAppVersion currentVersionModel = configAppVersionRepository.findTopByVersionNo(version);

            // 版本不正确强制整包更新
            if (currentVersionModel == null) {
                result.setUpdateType(CommonEnum.VersionUpdateType.NO);
                result.setUpdateUrl("");
                result.setUpdateTips("");
                result.setUpdateMD5("");
                return result;
            }
            // 当前版本跟最新版本一样，无需更新
            if (currentVersionModel.getVersionNo().equals(newestVersionModel.getVersionNo())) {
                result.setUpdateType(CommonEnum.VersionUpdateType.NO);
                result.setUpdateUrl("");
                result.setUpdateTips("");
                result.setUpdateMD5("");
                return result;
            }
            // 当前版本存在致命错误 强制整包更新
            if (currentVersionModel.isBigError()) {
                // 整包更新
                result.setUpdateType(CommonEnum.VersionUpdateType.FORCE_WHOLE);
                result.setUpdateUrl(newestVersionModel.getDescription());
                result.setUpdateTips(newestVersionModel.getDescription());
                result.setUpdateMD5(newestVersionModel.getMd5value());
                return result;

            }

            // 设置默认的更新模式 增量更新 增量包地址

            result.setUpdateType(CommonEnum.VersionUpdateType.INCREMENT);
            result.setUpdateUrl(currentVersionModel.getDifferenceFileUrl());
            result.setUpdateTips(newestVersionModel.getDescription());
            result.setUpdateMD5(currentVersionModel.getMd5value());

            // 获取全局 强制客户端更新（针对用户所有版本）

            Boolean isAppForcedUpdating = false;

            // 检查版本跨度 如2.1.6 版本前2位变化则整包更新 否则只增量更新
            Boolean isBigChange = true;// 1.2.1 版本修改
            if (currentVersionModel.getVersionNo().substring(0, currentVersionModel.getVersionNo().lastIndexOf(".") + 1) != newestVersionModel.getVersionNo()
                    .substring(0, newestVersionModel.getVersionNo().lastIndexOf(".") + 1))
                isBigChange = true;

            if (isAppForcedUpdating && isBigChange)// 强制整包
            {
                result.setUpdateType(CommonEnum.VersionUpdateType.FORCE_WHOLE);
                result.setUpdateUrl(newestVersionModel.getSourceFileUrl());
                result.setUpdateMD5(newestVersionModel.getMd5value());
            } else if (isAppForcedUpdating && !isBigChange)// 强制增量
            {
                result.setUpdateType(CommonEnum.VersionUpdateType.FORCE_INCREMENT);
                result.setUpdateUrl(currentVersionModel.getDifferenceFileUrl());
                result.setUpdateMD5(currentVersionModel.getMd5value());
            } else if (!isAppForcedUpdating && isBigChange)// 整包
            {
                result.setUpdateType(CommonEnum.VersionUpdateType.WHOLE);
                result.setUpdateUrl(newestVersionModel.getSourceFileUrl());
                result.setUpdateMD5(newestVersionModel.getMd5value());
            }

            return result;
        } else {
            result.setUpdateType(CommonEnum.VersionUpdateType.NO);
            result.setUpdateUrl("");
            result.setUpdateTips("");
            result.setUpdateMD5("");
            return result;
        }

    }
}
