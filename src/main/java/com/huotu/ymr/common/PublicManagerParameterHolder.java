package com.huotu.ymr.common;

import com.huotu.ymr.model.manager.MngPublicModel;

/**
 * Created by lgh on 2015/12/18.
 */
public class PublicManagerParameterHolder {

    private static final ThreadLocal<MngPublicModel> models = new ThreadLocal<>();

    public static MngPublicModel get() {

        return models.get();
    }

    public static void put(MngPublicModel model) {
        models.set(model);
    }
}
