package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Device;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by slt on 2016/1/11.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device,String>,ClassicsRepository<Device>,JpaSpecificationExecutor<Device> {
}
