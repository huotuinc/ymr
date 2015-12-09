package com.huotu.ymr.controller;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xhk on 2015/12/1.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootConfig.class, MallBootConfig.class, MvcConfig.class})
@Transactional
public class ArticleControllerTest extends SpringBaseTest {

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
        category.setName("自传故事");
        category.setSort(1);
        category = categoryRepository.saveAndFlush(category);
        categories.add(category);

        Category category1 = new Category();
        category1.setPicture("d://b.png");
        category1.setName("学院介绍");
        category1.setSort(2);
        category1 = categoryRepository.saveAndFlush(category1);
        categories.add(category1);

        Category category2 = new Category();
        category2.setPicture("d://c.png");
        category2.setName("美容知识");
        category2.setSort(3);
        category2 = categoryRepository.saveAndFlush(category2);
        categories.add(category2);
        return categories;
    }

    /*
    * 先判断类别表中是否存在数据
    * 然后进行列表数据请求
    * 最后比较取出来的数据与数据库中的数据是否一样
     */
    @Test
    public void testGetCategoryList() throws Exception {
        List<Category> categories = categoryRepository.findAll();
        if (categories.size() < 3) {
            saveCategories();
            categories = categoryRepository.findAll();
        }
        //进行请求
        String result = mockMvc.perform(device.getApi("getCategoryList").build())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list1 = JsonPath.read(result, "$.resultData.list");
        for (int i = 0; i < list1.size(); i++) {
            Assert.assertEquals("进行文章类别的名字断言", categories.get(i).getName(), list1.get(i).get("name"));
            Assert.assertEquals("进行文章类别的图片路径断言", categories.get(i).getPicture(), list1.get(i).get("picture"));
        }
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

    /*
    * 1.判断数据库中是否有3条分类的数据，没有则存入3条
    * 2.判断数据库是否有100条数据，没有则存入100条
    * 3.然后请求lastId为null时的list，即第一页的数据,并判断是否是想要的结果
    * 4.进行第一页后的请求（正常请求），并判断是否是想要的结果
    * 5.请求最后一页的下一页，并判断是否是想要的结果
    * 6.请求不存在的分类下的文章，并判断是否是想要的结果
     */
    @Test
    public void testGetArticleList() throws Exception {

        //进行文章分类的处理
        List<Category> categories = categoryRepository.findAll();
        if (categories == null || categories.size() < 3) {
            saveCategories();
            categories = categoryRepository.findAll();
        }

        //进行文章的存储
        List<Article> articles = articleRepository.findAll();
        if (articles == null || articles.size() < 100) {
            saveArticles(categories);
            articles = articleRepository.findAll();
        }
        //请求存在文章第一页
        String result = mockMvc.perform(device.getApi("getArticleList").param("categoryId", categories.get(0).getId() + "").build())
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        List<Article> articleList = articleService.findArticleListFromLastIdWithNumber(categories.get(0).getId(), articleService.getMaxId() + 1, 10);
        for (int i = 0; i < articleList.size(); i++) {
            Assert.assertEquals("请求存在文章第一页id断言", articleList.get(i).getId().longValue(), Long.parseLong(list.get(i).get("pid") + ""));
            //Assert.assertEquals("请求存在文章第一页time断言",articleList.get(i).getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求存在文章第一页picture断言", articleList.get(i).getPicture(), list.get(i).get("picture"));
        }

        //请求存在文章下页
        String result1 = mockMvc.perform(device.getApi("getArticleList").param("categoryId", categories.get(0).getId() + "").param("lastId", articles.get(50).getId() + "").build())
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result1);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        List<Article> articleList1 = articleService.findArticleListFromLastIdWithNumber(categories.get(0).getId(), articles.get(50).getId(), 10);
        for (int i = 0; i < articleList1.size(); i++) {
            Assert.assertEquals("请求存在文章下页id断言", articleList1.get(i).getId().longValue(), Long.parseLong(list1.get(i).get("pid") + ""));
            //Assert.assertEquals("请求存在文章第二页time断言",articleList1.get(i).getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求存在文章下页picture断言", articleList1.get(i).getPicture(), list1.get(i).get("picture"));
        }

        //请求存在文章最后一篇之后的
        String result2 = mockMvc.perform(device.getApi("getArticleList").param("categoryId", categories.get(0).getId() + "").param("lastId", "0").build())
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result1);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        List<Article> articleList2 = articleService.findArticleListFromLastIdWithNumber(categories.get(0).getId(), 0L, 10);
        Assert.assertEquals("请求存在文章最后一篇之后的获取的数据量断言", articleList2.size(), list2.size());
        for (int i = 0; i < articleList2.size(); i++) {
            Assert.assertEquals("请求存在文章最后一篇之后的id断言", articleList2.get(i).getId().longValue(), Long.parseLong(list2.get(i).get("pid") + ""));
            Assert.assertEquals("请求存在文章最后一篇之后的picture断言", articleList2.get(i).getPicture(), list2.get(i).get("picture"));
        }

        //请求不存在的分类文章
        long maxId = 0;
        for (Category cate : categories) {
            if (cate.getId() > maxId) {
                maxId = cate.getId();
            }
        }
        String result3 = mockMvc.perform(device.getApi("getArticleList").param("categoryId", (maxId + 1) + "").build())
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result2);
        List<HashMap> list3 = JsonPath.read(result3, "$.resultData.list");
        Assert.assertEquals("断言请求不存在的分类文章", 0, list3.size());
    }

    /*
    * 1.如果文章分类小于3则存入三条分类数据
    * 2.如果文章数量少于100条则存入100条数据
    * 3.进行一篇文章的请求,并判断是否是想要的结果
     */
    @Test
    public void testGetArticleInfo() throws Exception {
        //如果文章分类或文章数为0，则进行存储
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
}