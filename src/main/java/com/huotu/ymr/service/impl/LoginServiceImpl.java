package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Login;
import com.huotu.ymr.repository.LoginRepository;
import com.huotu.ymr.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by lgh on 2015/12/26.
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginRepository.findByLoginName(username);
        if (login == null)
            throw new UsernameNotFoundException("无效的用户名。");
        login.setLastLoginDate(LocalDateTime.now());
        return login;
    }

    /**
     * 新建一个可登陆用户
     *
     * @param login    准备新建的可登陆用户
     * @param password 明文密码
     */
    public <T extends Login> T newLogin(T login, CharSequence password) {
        login.setPassword(passwordEncoder.encode(password));
        login.setEnabled(true);
        return loginRepository.save(login);
    }

    @Override
    public Login findLoginById(Long id) {
        return loginRepository.findOne(id);
    }

    @Override
    public Login saveLogin(Login login) {
        return loginRepository.save(login);

    }
}
