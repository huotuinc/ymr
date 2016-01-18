package com.huotu.ymr.service.impl;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.BootConfig;
import com.huotu.ymr.boot.MallBootConfig;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.service.ShareCommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2016/1/14.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
@ActiveProfiles("test")
public class ShareCommentServiceImplTest extends SpringBaseTest {

    @Autowired
    ShareCommentService  shareCommentService;
    @Test
    public void testFindCommentList() throws Exception {
         List<ShareComment> shareCommentList=shareCommentService.findShareComment(252L,0L,10);

        ShareComment shareComment=shareCommentService.findOneShareComment(1L);





    }
}