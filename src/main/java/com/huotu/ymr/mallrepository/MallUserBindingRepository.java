package com.huotu.ymr.mallrepository;


import com.huotu.ymr.mallentity.MallUserBinding;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by lgh on 2015/12/8.
 */
public interface MallUserBindingRepository extends JpaRepository<MallUserBinding,Long>,ClassicsRepository<MallUserBinding> {
    List<MallUserBinding> findByUnionIdAndMerchantId(String unionId,Long merchantId);
}
