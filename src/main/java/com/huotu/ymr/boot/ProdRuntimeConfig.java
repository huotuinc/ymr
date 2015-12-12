package com.huotu.ymr.boot;

import org.luffy.lib.libspring.config.RuntimeConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;

/**
 * Created by slt
 * 生产时的配置
 * @author slt
 */
@Profile("container")
@Configuration
public class ProdRuntimeConfig extends RuntimeConfig {
    @Override
    public boolean containerEnv() {
        return true;
    }

    @Override
    public String persistenceUnitName() {
        return "hotedu";
    }

    @Override
    public Class<? extends JpaDialect> dialectClass() {
        return EclipseLinkJpaDialect.class;
    }
}
