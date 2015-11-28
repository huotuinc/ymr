package com.huotu.ymr.common;


import com.huotu.ymr.model.AppPublicModel;

/**
 * Created by lgh on 2015/11/12.
 */
public class PublicParameterHolder {

    private static final ThreadLocal<AppPublicModel> models = new ThreadLocal<>();

    public static AppPublicModel get() {
        return models.get();
    }

    public static void put(AppPublicModel model) {
        models.set(model);
    }
}
