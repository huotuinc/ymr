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

    @Test
    public void testHttpPost() throws Exception{
        String city = "Shaoxing";
        String country = "CN";
        String headimgurl = "http://wx.qlogo.cn/mmopen/F8RjDus4R6AiausgyKibkrJp0q5nwwCFeichj0613Xtib01YRemLttgWw1MHmvItoHRCVf1SNZftzfGmiaIQJBiciaI1NyRmcr4SU6e/0";
        String language = "zh_CN";
        String nickname = "Luohaibo";
        String openid = "ow92RuL4PiGnKAikOV7iDFgvp92A";
        String province = "Zhejiang";
        int sex = 1;
        String unionid = "o76SuuMeJjOp0Tnsr2AQnPD_0RKs";




    }
}