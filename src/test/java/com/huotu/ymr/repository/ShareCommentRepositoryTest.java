package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Share;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2015/12/3.
 */
public class ShareCommentRepositoryTest {
    @Autowired
    ShareCommentRepository shareCommentRepository;
    @Autowired
    ShareRepository shareRepository;

    @Test
    public void testFindByParentId() throws Exception {
        Share share=new Share();
        share.setOwnerId(123L);
        share.setEnabledRecommendProduct(true);
        share.setTitle("123456");
        share=shareRepository.saveAndFlush(share);



//        List<ShareComment> list=shareCommentRepository.findByParentId(0L);

    }
}