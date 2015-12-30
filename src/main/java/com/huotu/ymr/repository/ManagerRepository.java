package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Manager;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by xhk on 2015/12/18.
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long>,ClassicsRepository<Manager>,JpaSpecificationExecutor<Manager> {
}
