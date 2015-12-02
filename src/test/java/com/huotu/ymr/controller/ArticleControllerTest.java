package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.entity.Article;
import com.huotu.ymr.entity.Category;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.CategoryRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by xhk on 2015/12/1.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class ArticleControllerTest extends SpringBaseTest  {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleService articleService;


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }


    //存储文章类别方法
    public List<Category> saveCategories(){
        List<Category> categories=new ArrayList<Category>();
        //先进行文章类别的存贮
        Category category=new Category();
        category.setPicture("d://a.png");
        category.setName("自传故事");
        category.setSort(1);
        category=categoryRepository.saveAndFlush(category);
        categories.add(category);

        Category category1=new Category();
        category1.setPicture("d://b.png");
        category1.setName("学院介绍");
        category1.setSort(2);
        category1=categoryRepository.saveAndFlush(category1);
        categories.add(category1);

        Category category2=new Category();
        category2.setPicture("d://c.png");
        category2.setName("美容知识");
        category2.setSort(3);
        category2=categoryRepository.saveAndFlush(category2);
        categories.add(category2);
        return categories;
    }
    @Test
    public void testGetCategoryList() throws Exception {
        List<Category> categories=saveCategories();
        //进行请求
        String result=mockMvc.perform(get("/article/getCategoryList"))
                .andReturn().getResponse().getContentAsString();
        List<HashMap> list1 = JsonPath.read(result, "$.resultData.list");
        for(int i=0;i<list1.size();i++){
            Assert.assertEquals("进行文章类别的名字断言", categories.get(i).getName(), list1.get(i).get("name"));
            Assert.assertEquals("进行文章类别的图片路径断言",categories.get(i).getPicture(),list1.get(i).get("picture"));
        }
    }

    //存储文章方法
    public List saveArticles( List<Category> categories){
        List<Article>  articles=new ArrayList<Article>();

        for(int i=0;i<30;i++) {
            Article article = new Article();
            String title= UUID.randomUUID().toString();
            article.setTitle(title);
            article.setPicture(i+title);
            article.setTime(new Date());
            article.setCategory(categories.get((int)(Math.random()*3)));
            article=articleRepository.saveAndFlush(article);
        }
        return articles;
    }

    @Test
    public void testGetArticleList() throws Exception {

        //进行文章分类的处理
        List<Category> categories=categoryRepository.findAll();
        if(categories==null||categories.size()<3){
            saveCategories();
            categories=categoryRepository.findAll();
        }

        //进行文章的存储
        List<Article>  articles=articleRepository.findAll();
        if(articles==null||articles.size()<30){
            saveArticles(categories);
        }
        //请求存在文章第一页
        String result=mockMvc.perform(get("/article/getArticleList").param("categoryId","20"))
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result);
        List<HashMap> list = JsonPath.read(result, "$.resultData.list");
        List<Article> articleList=articleService.findArticleListFromlastIdWithNumber(20,articleService.getMaxId()+1,3);
        for(int i=0;i<articleList.size();i++) {
            Assert.assertEquals("请求存在文章第一页id断言",articleList.get(i).getId().longValue(),Long.parseLong(list.get(i).get("pid") + ""));
            //Assert.assertEquals("请求存在文章第一页time断言",articleList.get(i).getTime(),list.get(i).get("time"));
            Assert.assertEquals("请求存在文章第一页picture断言",articleList.get(i).getPicture(),list.get(i).get("picture"));
        }

        //请求存在文章第二页
        String result1=mockMvc.perform(get("/article/getArticleList").param("categoryId","20").param("lastId","38"))
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result1);
        List<HashMap> list1 = JsonPath.read(result1, "$.resultData.list");
        List<Article> articleList1=articleService.findArticleListFromlastIdWithNumber(20,38L,3);
        for(int i=0;i<articleList1.size();i++) {
            Assert.assertEquals("请求存在文章第二页id断言",articleList1.get(i).getId().longValue(),Long.parseLong(list1.get(i).get("pid")+""));
            //Assert.assertEquals("请求存在文章第二页time断言",articleList1.get(i).getTime(),list1.get(i).get("time"));
            Assert.assertEquals("请求存在文章第二页picture断言",articleList1.get(i).getPicture(),list1.get(i).get("picture"));
        }
        //请求不存在的分类文章
        String result2=mockMvc.perform(get("/article/getArticleList").param("categoryId","2"))
                .andReturn().getResponse().getContentAsString();
        //System.out.println(result2);
        List<HashMap> list2 = JsonPath.read(result2, "$.resultData.list");
        Assert.assertEquals("断言请求不存在的分类文章",0,list2.size());
    }

    @Test
    public void testGetArticleInfo() throws Exception {
        //如果文章分类或文章数为0，则进行存储
        List<Article> articleList=articleRepository.findAll();
        List<Category> categories=categoryRepository.findAll();
        if(categories.size()==0){
            saveCategories();
            categories=categoryRepository.findAll();
        }
        if(articleList.size()==0){
            saveArticles(categories);
        }
        String result=mockMvc.perform(get("/article/getArticleInfo").param("id",articleList.get(0).getId().toString()))
                .andReturn().getResponse().getContentAsString();
       // System.out.println(result);
        HashMap map = JsonPath.read(result, "$.resultData.data");
        Assert.assertEquals("断言请求文章id",articleList.get(0).getTitle(),map.get("title"));
    }
}