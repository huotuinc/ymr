package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/26.
 */
@Repository
public interface LoginRepository extends JpaRepository<Login,Long> {

    Login findByLoginName(String loginName);


}
