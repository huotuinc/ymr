package com.huotu.ymr.boot;

import com.huotu.ymr.model.AppGlobalModel;
import org.luffy.lib.libspring.data.ClassicsRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by lgh on 2015/12/8.
 */
@Configuration
@ImportResource(value = "classpath:spring-jpa.xml")
@EnableJpaRepositories(value = "com.huotu.ymr.repository"
        , repositoryFactoryBeanClass = ClassicsRepositoryFactoryBean.class)
public class BootConfig {
        @Bean
        public AppGlobalModel appGlobalModel() {
                return new AppGlobalModel();
        }

}
