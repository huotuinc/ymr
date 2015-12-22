package com.huotu.ymr.repository;

import com.huotu.ymr.entity.*;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by slt on 2015/12/22.
 */
public interface CommentPraiseRepository extends JpaRepository<CommentPraise, Long> ,ClassicsRepository<CommentPraise>,JpaSpecificationExecutor<CommentPraise> {
    CommentPraise findByCommentAndUser(ShareComment comment, User user);
    List<CommentPraise> findByComment(ShareComment comment);
    List<CommentPraise> findByUser(User user);
}
