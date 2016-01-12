package com.huotu.ymr.service;

import com.huotu.ymr.entity.Praise;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserSharePraiseModel;

import java.util.List;

/**
 * 点赞管理
 * Created by slt on 2015/12/26.
 */
public interface PraiseService {
    /**
     * 获取用户点赞过的文章
     * @param user          用户
     * @param lastId        最后一条点赞的ID
     * @return
     * @throws Exception
     */
    List<Praise> findPraiseList(User user, Long lastId) throws Exception;

    /**
     * 获取用户被点赞的记录
     * @param user
     * @param lastId
     * @return
     * @throws Exception
     */
    List<Praise> findBePraisedList(User user,Long lastId) throws Exception;


    /**
     * 将点赞实体转换为model
     * @param praise
     * @return
     * @throws Exception
     */
    AppUserSharePraiseModel getpraiseToModel(Praise praise) throws Exception;
}
