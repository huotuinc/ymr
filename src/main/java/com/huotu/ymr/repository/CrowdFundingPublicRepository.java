package com.huotu.ymr.repository;

import com.huotu.ymr.entity.CrowdFundingPublic;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface CrowdFundingPublicRepository extends JpaRepository<CrowdFundingPublic, Long>,ClassicsRepository<CrowdFundingPublic> {
}
