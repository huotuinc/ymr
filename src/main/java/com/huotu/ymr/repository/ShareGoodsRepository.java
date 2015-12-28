package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareGoods;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by lgh on 2015/12/28.
 */
public interface ShareGoodsRepository   extends JpaRepository<ShareGoods, Long>,ClassicsRepository<ShareGoods>,JpaSpecificationExecutor<ShareGoods> {
    List<ShareGoods> findByShare(Share share);
}
