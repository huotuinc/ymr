package com.huotu.ymr.repository;

import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.ymr.boot.BootConfig;
import com.huotu.ymr.boot.MallBootConfig;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.common.HttpHelper;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.model.AppWeiXinAccreditModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.service.DataCenterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/12/3.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
public class ShareCommentRepositoryTest {
    @Autowired
    ShareCommentRepository shareCommentRepository;
    @Autowired
    ShareRepository shareRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    CategoryRestRepository categoryRestRepository;

    @Autowired
    GoodsRestRepository goodsRestRepository;

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

        AppWeiXinAccreditModel appWeiXinAccreditModel=new AppWeiXinAccreditModel();
        appWeiXinAccreditModel.setUnionid(unionid);
        appWeiXinAccreditModel.setSex(sex);
        appWeiXinAccreditModel.setProvince(province);
        appWeiXinAccreditModel.setOpenid(openid);
        appWeiXinAccreditModel.setNickname(nickname);
        appWeiXinAccreditModel.setLanguage(language);
        appWeiXinAccreditModel.setHeadimgurl(headimgurl);
        appWeiXinAccreditModel.setCountry(country);
        appWeiXinAccreditModel.setCity(city);
        dataCenterService.createUser(appWeiXinAccreditModel);





    }
    @Test
    public void getUserInfoTest()throws Exception{
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(3L);
    }

    @Test
    public void getCatListTest()throws Exception{
        Page<Category> categories= categoryRestRepository.findByTitleAndCategory(4471L, new PageRequest(0, 1000));
    }

    @Test
    public void postTest()throws Exception{
        String url ="http://localhost:8080/ymr/manager/testPost";
        Map<String, String> map = new TreeMap<>();
        map.put("nickname","史利挺");
        String response= HttpHelper.postRequest(url,map);

    }
}