package com.huotu.ymr.repository;

import com.huotu.ymr.entity.MyCrowdFundingFlow;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by xhk on 2015/12/30.
 */
public interface MyCrowdFundingFlowRepository extends JpaRepository<MyCrowdFundingFlow, Long>,ClassicsRepository<MyCrowdFundingFlow>,JpaSpecificationExecutor<MyCrowdFundingFlow> {
}
