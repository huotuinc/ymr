package com.huotu.ymr.repository;

import com.huotu.ymr.entity.ShareComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgh on 2015/12/1.
 */
@Repository
public interface ShareCommentRepository extends JpaRepository<ShareComment,Long> {
}
