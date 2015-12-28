package com.huotu.ymr.boot;

import com.huotu.huobanplus.sdk.common.CommonClientSpringConfig;
import org.luffy.lib.libspring.data.ClassicsRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by lgh on 2015/12/8.
 */
@Configuration
@ImportResource(value = "classpath:spring-jpa-mall.xml")
//@Import(value = CommonClientSpringConfig.class)
@EnableJpaRepositories(value = {"com.huotu.ymr.mallrepository"}
        , entityManagerFactoryRef = "mallEntityManagerFactory"
        , transactionManagerRef = "mallTransactionManager"
        , repositoryFactoryBeanClass = ClassicsRepositoryFactoryBean.class)
public class MallBootConfig {
}
