package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.common.*;
import com.huotu.ymr.entity.ConfigAppVersion;
import com.huotu.ymr.exception.InterrelatedException;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.model.*;
import com.huotu.ymr.repository.ConfigAppVersionRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.VerificationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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
    private MallUserRepository mallUserRepository;

    @Autowired
    private AppGlobalModel appGlobalModel;

    @Autowired
    private ConfigAppVersionRepository configAppVersionRepository;

    @Autowired
    private VerificationService verificationService;

    @RequestMapping("/init")
    @Override
    public ApiResult init(Output<AppGlobalModel> global, Output<AppUserInfoModel> user, Output<AppUpdateModel> update) throws Exception {
        global.outputData(appGlobalModel);

        AppPublicModel pms = PublicParameterHolder.get();

        update.outputData(versionChecking(pms.getOperation(), pms.getVersion(), pms.getImei()));

        AppUserInfoModel appUserInfoModel = pms.getCurrentUser();
        if (appUserInfoModel == null)
            return ApiResult.resultWith(CommonEnum.AppCode.ERROR_USER_LOGIN_FAIL);


        user.outputData(appUserInfoModel);

        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }


    /**
     * 根据unionId判断商城中是否有该用户
     *      没有：1.添加一个该用户
     *           2.在美投用户表中查看是否有该用户
     *                 2.1.有：不插入
     *                     没有：插入该用户
     *      有： 1.在美投用户表中查看是否有该用户
     *                 1.1.有：不插入
     *                 1.2.没有：插入该用户
     *  获取该用户数据
     * @param data    用户数据
     * @param unionId 微信唯一号
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, Integer unionId) throws Exception {



        return null;
    }

    /**
     * 判断验证码是否正确
     *      不正确：返回信息
     *     正确：
     * @param data  返回的用户数据
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @throws Exception
     */
    @RequestMapping("/phoneLogin")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, String phone, String code) throws Exception {
        return null;
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

    @Override
    public ApiResult putToCoffers() throws Exception {
        return null;
    }

    @Override
    public ApiResult getScorePutInfo(Output<Double> upgradeMoney, Output<Double> rate) throws Exception {
        return null;
    }

    @Override
    public ApiResult put(Output<String> orderNo, Double money) throws Exception {
        return null;
    }

    @Override
    public ApiResult updateUserProfile(Output<AppUserInfoModel> data, @RequestParam(required = true) Integer profileType, Object profileData) throws Exception {
        return null;
    }

    @Override
    public ApiResult messages(Output<AppMessageModel[]> messages, Long lastId) throws Exception {
        return null;
    }

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
