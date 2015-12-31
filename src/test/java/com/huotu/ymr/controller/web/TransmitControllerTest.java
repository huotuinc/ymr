package com.huotu.ymr.controller.web;

import com.huotu.common.base.StringHelper;
import com.huotu.ymr.base.Device;
import com.huotu.ymr.base.DeviceType;
import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.BootConfig;
import com.huotu.ymr.boot.MallBootConfig;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.entity.Article;
import com.huotu.ymr.entity.Category;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.mallentity.MallMerchant;
import com.huotu.ymr.mallentity.MallUser;
import com.huotu.ymr.mallrepository.MallMerchantRepository;
import com.huotu.ymr.mallrepository.MallUserRepository;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.CategoryRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.ArticleService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xhk on 2015/12/30.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
@ActiveProfiles("test")
public class TransmitControllerTest extends SpringBaseTest {

    private Device device;
    private String mockUserName;
    private String mockUserPassword;
    private User mockUser;
    private MallUser mockMallUser;
    private MallMerchant mockMerchant;

    @Autowired
    private UserRepository mockUserRepository;
    @Autowired
    private MallUserRepository mockMallUserRepository;
    @Autowired
    private MallMerchantRepository mockMallMerchantRepository;


    @Autowired
    ArticleService articleService;


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Before
    public void init() {
        device = Device.newDevice(DeviceType.Android);
        Random random = new Random();
        mockUserName = StringHelper.randomNo(random, 12);
        mockUserPassword = UUID.randomUUID().toString().replace("-", "");

        mockMerchant = generateMerchant(mockMallMerchantRepository);

        mockMallUser = generateMallUser(mockUserName, mockUserPassword, mockMallUserRepository, mockMerchant);
        mockUser = generateUserWithToken(mockMallUser, mockUserRepository);

        device.setToken(mockUser.getToken());
    }
    //存储文章类别方法
    public List<Category> saveCategories() {
        List<Category> categories = new ArrayList<Category>();
        //先进行文章类别的存贮
        Category category = new Category();
        category.setPicture("d://a.png");
        category.setName("公司介绍");
        category.setSort(1);
        category = categoryRepository.saveAndFlush(category);
        categories.add(category);

        Category category1 = new Category();
        category1.setPicture("d://b.png");
        category1.setName("自传故事");
        category1.setSort(2);
        category1 = categoryRepository.saveAndFlush(category1);
        categories.add(category1);

        Category category2 = new Category();
        category2.setPicture("d://c.png");
        category2.setName("学院介绍");
        category2.setSort(3);
        category2 = categoryRepository.saveAndFlush(category2);
        categories.add(category2);

        Category category3 = new Category();
        category3.setPicture("d://d.png");
        category3.setName("美容知识");
        category3.setSort(4);
        category3 = categoryRepository.saveAndFlush(category3);
        categories.add(category3);
        return categories;
    }

    //存储文章方法
    public List saveArticles(List<Category> categories) {
        List<Article> articles = new ArrayList<Article>();

        for (int i = 0; i < 100; i++) {
            Article article = new Article();
            String title = UUID.randomUUID().toString();
            article.setTitle(title);
            article.setPicture(i + title);
            article.setTime(new Date());
            article.setCategory(categories.get((int) (Math.random() * 3)));
            article = articleRepository.saveAndFlush(article);
        }
        return articles;
    }

    @Test
    public void testShowShareInfo() throws Exception {

    }

    @Test
    public void testGetArticleInfo() throws Exception {
        List<Article> articleList = articleRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        if (categories == null || categories.size() < 3) {
            saveCategories();
            categories = categoryRepository.findAll();
        }
        if (articleList == null || articleList.size() < 100) {
            saveArticles(categories);
            articleList = articleRepository.findAll();
        }
        String result = mockMvc.perform(device.getApi("getArticleInfo").param("id", articleList.get(0).getId().toString()).build())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
        HashMap map = JsonPath.read(result, "$.resultData.data");
        Assert.assertEquals("断言请求文章id", articleList.get(0).getTitle(), map.get("title"));
    }



    @Test
    public void testGetCrowdInfo() throws Exception {

    }
}