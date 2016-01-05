package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Manager;
import com.huotu.ymr.repository.ManagerRepository;
import com.huotu.ymr.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Created by xhk on 2015/12/29.
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    ManagerRepository managerRepository;

    @Override
    public Manager checkManager(String username, String password) {
        String resultPassword=DigestUtils.md5DigestAsHex(password.getBytes());
        StringBuilder hql=new StringBuilder();
        hql.append("from Manager as manager where manager.loginName=:username " +
                " and manager.password=:resultPassword " );
        List<Manager> managers = managerRepository.queryHql(hql.toString(), query -> {
            //query.setParameter("key","%"+key+"%");
            query.setParameter("loginName", username);
            query.setParameter("resultPassword", resultPassword);
        });
        if(managers.size()==0){
            return null;
        }else {
            return managers.get(0);
        }
    }

    @Override
    public Manager saveManager(String username, String password) {
        String resultPassword=DigestUtils.md5DigestAsHex(password.getBytes());
        Manager manager=new Manager();
        manager.setLoginName(username);
        manager.setPassword(resultPassword);
        manager=managerRepository.saveAndFlush(manager);
        return manager;

    }
}
