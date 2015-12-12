package com.huotu.ymr.boot;

import org.luffy.lib.libspring.config.RuntimeConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;

/**
 * Created by slt on 2015/12/12.
 * 为了允许本地运行整个webapplication而设置的本地专用配置项
 * @author slt
 */
@Profile("!container")
@Configuration
public class LocalRuntimeConfig extends RuntimeConfig {
    @Override
    public boolean containerEnv() {
        return false;
    }

    @Override
    public String persistenceUnitName() {
        return "hotedu_dev";
    }

    @Override
    public Class<? extends JpaDialect> dialectClass() {
        return EclipseLinkJpaDialect.class;
    }
}
