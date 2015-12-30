package com.huotu.ymr.repository;

import com.huotu.ymr.entity.ShareRunning;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xhk on 2015/12/10.
 */
public interface ShareRunningRepository extends JpaRepository<ShareRunning, Long> ,ClassicsRepository<ShareRunning>,JpaSpecificationExecutor<ShareRunning> {
    @Query("select sr from ShareRunning as sr where sr.share.ownerId=?1 and sr.share.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user and sr.id<?2 order by sr.id desc ")
    List<ShareRunning> findByUserId(Long userId,Long lastId);

    @Query("select sr from ShareRunning as sr where sr.share.ownerId=?1 and sr.share.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user order by sr.id desc ")
    List<ShareRunning> findByUserId(Long userId);

}
