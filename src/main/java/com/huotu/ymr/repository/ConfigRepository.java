package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Config;
import com.huotu.ymr.entity.CrowdFunding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ConfigRepository extends JpaRepository<Config,String>,JpaSpecificationExecutor<CrowdFunding> {
}
