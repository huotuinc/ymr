package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ShareRepository extends JpaRepository<Share, Long>,ClassicsRepository<Share>,JpaSpecificationExecutor<Share> {
    @Query("select s from Share as s where s.id in ?1 and s.id>?2 order by s.id desc ")
    List<Share> getShareByIds(List<Long> shareIds,Long lastId);

    @Query("SELECT s from Share as s where s.ownerId=?1 and s.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user order by s.id desc ")
    List<Share> findMyShares(Long userId);

    @Query("SELECT s from Share as s where s.ownerId=?1 and s.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user and s.id<?2 order by s.id desc ")
    List<Share> findMyShares(Long userId,Long lastId);

}
