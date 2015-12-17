package com.huotu.ymr.boot;

import com.huotu.common.api.ApiResultHandler;
import com.huotu.common.api.OutputHandler;
import com.huotu.ymr.interceptor.AppHandlerExceptionResolver;
import com.huotu.ymr.interceptor.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

/**
 * Created by lgh on 2015/11/30.
 */

@Configuration
@EnableWebMvc
@ComponentScan(value = {"com.huotu.ymr.service.impl,com.huotu.ymr.controller"})
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Bean
    CommonInterceptor commonInterceptor() {
        return new CommonInterceptor();
    }

    @Autowired
    private CommonInterceptor commonInterceptor;


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/app/*");
    }

    /**
     * 设置控制器方法参数化输出
     *
     * @param argumentResolvers
     */

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new OutputHandler());
    }

    /**
     * 监听 控制器的ApiResult返回值
     *
     * @param returnValueHandlers
     */

    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new ApiResultHandler());
    }


    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new AppHandlerExceptionResolver());

    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
        registry.viewResolver(viewResolver());

        registry.jsp();
    }

    /**
     * for upload
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        SpringTemplateEngine engine = new SpringTemplateEngine();
        ServletContextTemplateResolver rootTemplateResolver = new ServletContextTemplateResolver();
        rootTemplateResolver.setPrefix("/");
        rootTemplateResolver.setCacheable(false);
        rootTemplateResolver.setSuffix(".html");
        rootTemplateResolver.setCharacterEncoding("UTF-8");

        engine.setTemplateResolver(rootTemplateResolver);
        resolver.setTemplateEngine(engine);
//        resolver.setOrder(99);
        resolver.setOrder(2147483647 + 10);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

}
