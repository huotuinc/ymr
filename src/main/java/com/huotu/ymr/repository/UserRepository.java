package com.huotu.ymr.repository;

import com.huotu.ymr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xhk on 2015/12/3.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByToken(String token);
}
