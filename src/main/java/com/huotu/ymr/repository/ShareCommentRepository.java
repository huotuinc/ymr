package com.huotu.ymr.repository;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.ShareComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by slt on 2015/12/1.
 */
@Repository
public interface ShareCommentRepository extends JpaRepository<ShareComment,Long> {
    @Query("select c from ShareComment as c where c.share.id=?1 and c.status=?2 and c.id>?3 order by c.id")
    List<ShareComment> findByShareOrderByTime(Long shareId,CommonEnum.ShareCommentStatus status,Long lastId,Pageable pageable);

    @Query("select c from ShareComment as c where c.parentId=0 and c.share.id=?1 and c.status=?2 and c.id>?3")
    List<ShareComment> findComment(Long shareId,CommonEnum.ShareCommentStatus status,Long lastId,Pageable pageable);

    @Query("select c from ShareComment as c where c.share.id=?1 and c.commentId in ?2 and c.status=?3")
    List<ShareComment> findCommentReply(Long shareId,Collection<Long> ids,CommonEnum.ShareCommentStatus status);

    @Transactional
    @Modifying
    @Query("update ShareComment as c set c.status=com.huotu.ymr.common.CommonEnum.ShareCommentStatus.delete where c.commentPath like ?1")
    int deleteComment(String commentPath);

    @Query("select c from ShareComment as c where c.userId=?1 and c.id<?2 order by c.id desc ")
    List<ShareComment> findUserCommentShares(Long userId,Long lastId);

    @Query("select c from ShareComment as c where c.share.ownerId=?1 and c.share.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user and c.id<?2 order by c.id desc ")
    List<ShareComment> findUserBeCommentedShares(Long userId,Long lastId);

    @Query("select c from ShareComment as c where c.share.ownerId=?1 and c.share.ownerType=com.huotu.ymr.common.CommonEnum.UserType.user order by c.id desc ")
    List<ShareComment> findUserBeCommentedShares(Long userId);
}
