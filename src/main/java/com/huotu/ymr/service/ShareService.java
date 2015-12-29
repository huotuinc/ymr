package com.huotu.ymr.service;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppShareListModel;
import com.huotu.ymr.model.searchCondition.ShareSearchModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分享管理
 * Created by slt on 2015/12/2.
 */
public interface ShareService {

    /**
     * 查找出已经通过审核的爱分享文章
     * @param key       搜索关键字
     * @param lastId    最后一条文章ID，第一次lastId为0
     * @param pageSize  每次显示的文章数量
     * @return
     * @throws Exception
     */
    List<Share> findAppShareList(String key,Long lastId,int pageSize) throws Exception;


    /**
     * 获取我发布的文章(帖子)
     * @param userId    用户的ID
     * @param lastId    最后一条文章的ID
     * @return
     * @throws Exception
     */
    List<Share> findMyIssueShareList(Long userId,Long lastId) throws Exception;


    Share findOneShare(Long shareId) throws Exception;

    Share saveShare(Share share) throws Exception;

    Page<Share> findPcShareList(ShareSearchModel shareSearchModel) throws Exception;

    /**
     * share实体转model
     * @param share
     * @return
     * @throws Exception
     */
    AppShareListModel shareToListModel(Share share,User user) throws Exception;


}
