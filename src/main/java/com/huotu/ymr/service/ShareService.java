package com.huotu.ymr.service;

import com.huotu.ymr.entity.Share;

import java.util.List;

/**
 * 分享管理
 * Created by slt on 2015/12/2.
 */
public interface ShareService {

    /**
     * 查找出已经通过审核的爱分享文章
     * @param key       搜索关键字
     * @param lastId    最后一条文章ID
     * @param pageSize  每次显示的文章数量
     * @return
     * @throws Exception
     */
    List<Share> findShareList(String key,Long lastId,int pageSize) throws Exception;

    Share findOneShare(Long shareId) throws Exception;


}
