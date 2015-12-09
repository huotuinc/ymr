package com.huotu.ymr.controller;


import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.common.base.RegexHelper;
import com.huotu.ymr.api.UserSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.PublicParameterHolder;
import com.huotu.ymr.entity.ConfigAppVersion;
import com.huotu.ymr.model.AppGlobalModel;
import com.huotu.ymr.model.AppPublicModel;
import com.huotu.ymr.model.AppUpdateModel;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.repository.ConfigAppVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户系统
 * Created by lgh on 2015/11/20.
 */
@Controller
@RequestMapping("/app")
public class UserController implements UserSystem {
    @Autowired
    private AppGlobalModel appGlobalModel;

    @Autowired
    private ConfigAppVersionRepository configAppVersionRepository;

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

    @RequestMapping("/login")
    @Override
    public ApiResult login(Output<AppUserInfoModel> data, Integer unionId) throws Exception {

        return null;
    }



    @RequestMapping("/sendSMS")
    @Override
    public ApiResult sendSMS(String phone, int type, @RequestParam(required = false) Integer codeType) throws Exception {
//        int checkCode=sendPhoneMessage();
//        if(codeType)
//        if (RegexHelper.IsValidMobileNo(phone)) return ApiResult.resultWith(CommonEnum.AppCode.ERROR_MOBILE);//用户错误的手机格式
        return null;
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
