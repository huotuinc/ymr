package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.common.*;
import com.huotu.ymr.entity.ConfigAppVersion;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.entity.VerificationCode;
import com.huotu.ymr.exception.InterrelatedException;
import com.huotu.ymr.mallrepository.MallUserBindingRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.model.*;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.ConfigAppVersionRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.repository.VerificationCodeRepository;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.StaticResourceService;
import com.huotu.ymr.service.VerificationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

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
    private MallUserRepository mallUserRepository;

    @Autowired
    private AppGlobalModel appGlobalModel;

    @Autowired
    private ConfigAppVersionRepository configAppVersionRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private MallUserBindingRepository mallUserBindingRepository;

    @Autowired
    private CommonConfigService commonConfigService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private StaticResourceService staticResourceService;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    private Environment environment;


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
     *
     * @param data    用户数据
     * @param unionId 微信唯一号
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, String unionId,String accreditInfo) throws Exception {
        if (environment.acceptsProfiles("test") || environment.acceptsProfiles("development")){
            //测试用
            User usertest=userRepository.findOne(1234L);
            String tokentest=UUID.randomUUID().toString().replaceAll("-","");
            AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
            appUserInfoModel.setUserId(usertest.getId());
            appUserInfoModel.setUserName("小开开专属测试号");
            appUserInfoModel.setHeadUrl("http://cdn.duitang.com/uploads/item/201402/11/20140211190918_VcMBs.thumb.224_0.jpeg");
            appUserInfoModel.setNickName("小开开");
            appUserInfoModel.setScore(usertest.getScore());
            appUserInfoModel.setUserLevel(usertest.getUserLevel());
            appUserInfoModel.setMerchantId(456L);
            appUserInfoModel.setName("方开金");
            appUserInfoModel.setSex("男");
            appUserInfoModel.setMobile("13588741728");
            appUserInfoModel.setIsBindMobile(false);
            appUserInfoModel.setToken(tokentest);
            usertest.setToken(tokentest);
            userRepository.save(usertest);
            data.outputData(appUserInfoModel);
        }
        if(environment.acceptsProfiles("prod")){
            if (unionId == null) {
                return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
            }
            MallUserModel mallUserModel = dataCenterService.getUserInfoByUniond(unionId)[0];//todo 调用数据中心
            if(Objects.isNull(mallUserModel)){
                mallUserModel=dataCenterService.createUser(accreditInfo);
            }
            User user=getUser(mallUserModel.getUserId());
            AppUserInfoModel appUserInfoModel=getAppUserInfoModel(user,mallUserModel);
            data.outputData(appUserInfoModel);
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/changeUser")
    @Override
    public ApiResult changeUser(Output<AppUserInfoModel[]> list, String unionId) throws Exception {
        if (unionId == null) {
            ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUserModel[] mallUserModels=dataCenterService.getUserInfoByUniond(unionId);
        AppUserInfoModel[] appUserInfoModels=new AppUserInfoModel[mallUserModels.length];
        for(int i=0;i<mallUserModels.length;i++){
            User user=getUser(mallUserModels[i].getUserId());
            AppUserInfoModel appUserInfoModel=getAppUserInfoModel(user,mallUserModels[i]);
            appUserInfoModels[i]=appUserInfoModel;
        }
        list.outputData(appUserInfoModels);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    /**
     * 根据userId，返回User对象，如果没有则创建一个
     * @param userId
     * @return
     * @throws Exception
     */
    private User getUser(Long userId) throws Exception{
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            user=new User();
            user.setId(userId);
            user.setUserLevel(CommonEnum.UserLevel.one);
            user.setScore(0);
            user.setContinuedScore(0);

        }
        //创建一个新的token
        String token=UUID.randomUUID().toString().replaceAll("-","");
        user.setToken(token);
        userRepository.save(user);
        return user;

    }

    /**
     * 根据User和MallUserModel返回AppUserInfoModel
     * @param user
     * @param mallUserModel
     * @return
     * @throws Exception
     */
    private AppUserInfoModel getAppUserInfoModel(User user,MallUserModel mallUserModel)throws Exception{
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
    public ApiResult bindMobile(String code, String phone) throws Exception {
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(phone)){
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        VerificationCode verificationCode=verificationCodeRepository.findByMobileAndType(phone, CommonEnum.VerificationType.bind);
        if(!code.equals(verificationCode.getCode())){
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_CODE);
        }
        //检查是否已经绑定了手机号

        //开始绑定手机号

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/modifyMobile")
    @Override
    public ApiResult modifyMobile(String code, String phone) throws Exception {
        return null;
    }

    @RequestMapping("/putToCoffers")
    @Override
    public ApiResult putToCoffers() throws Exception {
        return null;
    }

    @RequestMapping("/getScorePutInfo")
    @Override
    public ApiResult getScorePutInfo(Output<Double> upgradeMoney, Output<Double> rate) throws Exception {
        return null;
    }

    @RequestMapping("/put")
    @Override
    public ApiResult put(Output<String> orderNo, Double money) throws Exception {
        return null;
    }

    @RequestMapping("/updateUserProfile")
    @Override
    public ApiResult updateUserProfile(Output<AppUserInfoModel> data, @RequestParam(required = true) Integer profileType, Object profileData) throws Exception {
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
