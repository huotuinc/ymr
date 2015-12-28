package com.huotu.ymr.boot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by lgh on 2015/12/26.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String loginPage = "/manager/login";
    public static final String loginSuccessURL = "/manager/loginSuccess";
    public static final String loginFailedURL = "/manager/loginFailed";
    public static final String logoutSeccessURL = "/manager/login";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;


    // Since MultiSecurityConfig does not extend GlobalMethodSecurityConfiguration and
    // define an AuthenticationManager, it will try using the globally defined
    // AuthenticationManagerBuilder to create one

    // The @Enable*Security annotations create a global AuthenticationManagerBuilder
    // that can optionally be used for creating an AuthenticationManager that is shared
    // The key to using it is to use the @Autowired annotation
    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Configuration
    public static class YmrWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        /**
//         * 配置忽略的项
//         *
//         * @param web
//         * @throws Exception
//         */
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            super.configure(web);
//        }


        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/app/**", "/resource/**", "/manager/login").permitAll().anyRequest().authenticated()
                    .and()
                    .csrf().disable()
                    .formLogin().loginPage(loginPage).defaultSuccessUrl(loginSuccessURL).failureUrl(loginFailedURL).permitAll()
                    .and()
                    .logout().logoutSuccessUrl(logoutSeccessURL)
                    .and()
                    .httpBasic();
        }
    }
}
