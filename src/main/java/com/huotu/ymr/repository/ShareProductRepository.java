package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareProduct;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ShareProductRepository extends JpaRepository<ShareProduct, Long>,ClassicsRepository<ShareProduct> ,JpaSpecificationExecutor<ShareProduct> {
    List<ShareProduct> findByShare(Share share);

}
