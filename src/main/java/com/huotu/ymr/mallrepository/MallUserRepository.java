package com.huotu.ymr.mallrepository;

import com.huotu.ymr.mallentity.MallMerchant;
import com.huotu.ymr.mallentity.MallUser;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lgh on 2015/12/8.
 */
public interface MallUserRepository extends JpaRepository<MallUser, Long>, ClassicsRepository<MallUser> {
//    MallUser findByMerchant(MallMerchant merchant);

}
