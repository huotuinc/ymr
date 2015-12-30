package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Config;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ConfigRepository extends JpaRepository<Config,String>,ClassicsRepository<Config>,JpaSpecificationExecutor<Config> {
}
