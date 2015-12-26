package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by slt on 2015/12/1.
 */
@Repository
public interface ShareCommentRepository extends JpaRepository<ShareComment,Long> {
    @Query("select c from ShareComment as c where c.share.id=?1 and c.id>?2 order by c.id")
    List<ShareComment> findByShareOrderByTime(Long shareId,Long lastId,Pageable pageable);

    @Query("delete from ShareComment as c where c.commentPath like ?1")
    void deleteComment(String commentPath);

    @Query("select c from ShareComment as c where c.userId=?1 and c.id<?2 order by c.id desc ")
    List<ShareComment> findUserCommentShares(Long userId,Long lastId);
}
