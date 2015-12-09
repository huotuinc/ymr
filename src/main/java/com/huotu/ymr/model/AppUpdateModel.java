package com.huotu.ymr.model;

import com.huotu.ymr.common.CommonEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lgh on 2015/12/9.
 */
@Getter
@Setter
public class AppUpdateModel {
    private String updateMD5;
    private String updateUrl;
    private String updateTips;
    private CommonEnum.VersionUpdateType updateType;
}
