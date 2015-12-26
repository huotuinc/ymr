package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Praise;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.User;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xhk on 2015/12/10.
 */
public interface PraiseRepository extends JpaRepository<Praise, Long> ,ClassicsRepository<Praise>,JpaSpecificationExecutor<Praise> {
    Praise findByShareAndUser(Share share,User user);
    List<Praise> findByShare(Share share);
    List<Praise> findByUser(User user);

    @Query("select p from Praise as p where p.user=?1 and p.id<?2 order by p.id desc ")
    List<Praise> getPraiseShares(User user,Long lastId);
}
