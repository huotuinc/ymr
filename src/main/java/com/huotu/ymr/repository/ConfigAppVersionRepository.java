package com.huotu.ymr.repository;

import com.huotu.ymr.entity.ConfigAppVersion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/9.
 */
@Repository
public interface ConfigAppVersionRepository extends JpaRepository<ConfigAppVersion, Long> {
    /**
     * 获取最新版本
     * @return
     */
    ConfigAppVersion findTopByOrderByIdDesc();

    ConfigAppVersion findTopByVersionNo(String versionNo);
}
