package com.huotu.ymr.repository;

import com.huotu.ymr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by xhk on 2015/12/3.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByToken(String token);
    @Query("select u.pushingToken from User as u where u.pushingToken is not null")
    Set<String> findAllPushToken();
}
