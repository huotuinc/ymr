package com.huotu.ymr.repository;

import com.huotu.ymr.entity.CrowdFundingMoneyRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface CrowdFundingMoneyRangeRepository extends JpaRepository<CrowdFundingMoneyRange,Long> {
}
