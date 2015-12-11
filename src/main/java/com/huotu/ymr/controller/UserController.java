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
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallentity.MallUserBinding;
import com.huotu.ymr.mallrepository.MallUserBindingRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.model.*;
import com.huotu.ymr.repository.ConfigAppVersionRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.repository.VerificationCodeRepository;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.VerificationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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

    @RequestMapping("/init")
    @Override
    public ApiResult init(Output<AppGlobalModel> global, Output<AppUserInfoModel> user, Output<AppUpdateModel> update) throws Exception {
        global.outputData(appGlobalModel);

        AppPublicModel pms = PublicParameterHolder.get();

        update.outputData(versionChecking(pms.getOperation(), pms.getVersion(), pms.getImei()));


        AppUserInfoModel appUserInfoModel = pms.getCurrentUser();
        if (appUserInfoModel == null) {
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_LOGIN_FAIL);
        }

        user.outputData(appUserInfoModel);

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }


    /**
     * 获取该用户数据
     *
     * @param data    用户数据
     * @param unionId 微信唯一号
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, Output<AppSimpleUserModel[]> list, String unionId) throws Exception {
        MallUser mallUser = null;
        Long merchantId = Long.parseLong(commonConfigService.getYmrMerchantId());
        if (unionId == null) {
            return ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        List<MallUserBinding> mallUserBinding = mallUserBindingRepository.findByUnionIdAndMerchantId(unionId, merchantId);//todo 数据中心去取
        if (mallUserBinding.isEmpty()) {
            mallUser = new MallUser();//todo 调用注册接口注册商城的用户
        } else if (mallUserBinding.size() == 1) {
            mallUser = mallUserBinding.get(0).getUserInfo();
            User user = userRepository.findOne(mallUser.getId());
            data.outputData(getAppUserInfoModel(mallUser, user));
        }
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @RequestMapping("/selectOneUser")
    @Override
    public ApiResult selectOneUser(Output<AppUserInfoModel> data, Long userId) throws Exception {
        if (userId == null) {
            ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        MallUser mallUser = mallUserRepository.findOne(userId);
        if (Objects.isNull(mallUser)) {
            ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_NOT_FOUND);
        }
        User user = userRepository.findOne(mallUser.getId());
        data.outputData(getAppUserInfoModel(mallUser, user));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    /**
     * 判断验证码是否正确
     * 不正确：返回信息
     * 正确：
     *
     * @param data  返回的用户数据
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @throws Exception
     */
    @RequestMapping("/loginByMobile")
    @Override
    public ApiResult loginByMobile(Output<AppUserInfoModel> data, String phone, String code) throws Exception {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            ApiResult.resultWith(CommonEnum.AppCode.PARAMETER_ERROR);
        }
        VerificationCode verificationCode = verificationCodeRepository.findByMobileAndType(phone, CommonEnum.VerificationType.reg);
        if (Objects.isNull(verificationCode)) {
            ApiResult.resultWith(CommonEnum.AppCode.ERROR_WRONG_CODE);
        }
        MallUser mallUser = new MallUser();//todo 新建商城用户
        User user = new User();
        data.outputData(getAppUserInfoModel(mallUser, user));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }


    private AppUserInfoModel getAppUserInfoModel(MallUser mallUser, User user) {
        AppUserInfoModel appUserInfoModel = new AppUserInfoModel();
        if (Objects.isNull(user)) {
            user = new User();
            user.setId(mallUser.getId());
            user.setToken(UUID.randomUUID().toString().replace("-", ""));
            user.setUserLevel(CommonEnum.UserLevel.one);
            user = userRepository.save(user);
        }
        appUserInfoModel.setUserId(mallUser.getId());
        appUserInfoModel.setUserName(mallUser.getUsername());
        appUserInfoModel.setHeadUrl(commonConfigService.getResoureServerUrl() + mallUser.getWxHeadUrl());//todo 图片
        appUserInfoModel.setNickName(mallUser.getWxNickName());
        appUserInfoModel.setScore(user.getScore());
        appUserInfoModel.setUserLevel(user.getUserLevel());
        appUserInfoModel.setMerchantId(mallUser.getMerchant().getId());
        appUserInfoModel.setName(mallUser.getRealName());
        appUserInfoModel.setSex(mallUser.getGender());
        appUserInfoModel.setMobile(mallUser.getMobile());
        appUserInfoModel.setIsBindMobile(SysRegex.IsValidMobileNo(mallUser.getUsername()));
        appUserInfoModel.setToken(user.getToken());
        return appUserInfoModel;

    }


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
        return null;
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
