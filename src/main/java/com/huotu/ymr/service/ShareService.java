package com.huotu.ymr.service;

import com.huotu.ymr.entity.Share;

import java.util.List;

/**
 * 分享管理
 * Created by slt on 2015/12/2.
 */
public interface ShareService {

    List<Share> findShareList(String key,Long lastId,int pageSize) throws Exception;

    Share findOneShare(Long shareId) throws Exception;


}
