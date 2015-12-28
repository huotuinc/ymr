package com.huotu.ymr.service;

import com.huotu.ymr.entity.Login;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by lgh on 2015/12/26.
 */
public interface LoginService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 新建一个可登陆用户
     *
     * @param login    准备新建的可登陆用户
     * @param password 明文密码
     */
    <T extends Login> T newLogin(T login, CharSequence password);

    Login findLoginById(Long id);

    Login saveLogin(Login login);
}
