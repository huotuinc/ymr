package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
}
