package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.repository.ShareRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by lgh on 2015/12/1.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class ShareControllerTest extends SpringBaseTest {

    @Autowired
    ShareRepository shareRepository;

    @Before
    public void init() throws Exception {

    }

    @Test
    public void testSearchShareList() throws Exception{
        Random random=new Random();
        Calendar calendar=Calendar.getInstance();
        int zhiding=0;
        Share share;
        String lastId="";
        List<Share> shares=new ArrayList<>();
        List<Share> topShares=new ArrayList<>();
        List<Share> keyShares=new ArrayList<>();
        for(int i=0;i<20;i++){
            int ran=random.nextInt(3);
            share=new Share();
            if(ran==0){
                share.setTop(true);
                zhiding++;
            }else {
                share.setTop(false);
            }
            calendar.add(Calendar.HOUR,1);
            share.setTime(calendar.getTime());
            share.setStatus(true);
            share.setTitle(ran==0?"置顶文章":"不是置顶文章"+i);
            Share sharetag=shareRepository.saveAndFlush(share);
            if(ran==0){
                topShares.add(sharetag);
            }else {
                shares.add(sharetag);
            }
            if(zhiding==3){
                lastId=sharetag.getId()+"";
            }
        }
        //对置顶的分享进行排序
        topShares.sort(new Comparator<Share>() {
            @Override
            public int compare(Share o2, Share o1) {
                return o1.getTime().getTime()>o2.getTime().getTime()?1:-1;
            }
        });
        //对不置顶的文章进行排序
        shares.sort(new Comparator<Share>() {
            @Override
            public int compare(Share o2, Share o1) {
                return o1.getTime().getTime()>o2.getTime().getTime()?1:-1;
            }
        });

        String result=mockMvc.perform(get("/app/searchShareList")
                .param("key",  "")
                .param("lastId", ""))
                .andReturn().getResponse().getContentAsString();



        List<Object> resultList= JsonPath.read(result, "$.resultData.list");
        //关键字和分页测试

        Assert.assertEquals("数量",10,resultList.size());
        for(int i=0;i<resultList.size();i++){
            Map first=(Map)resultList.get(i);
            Object id=first.get("pid");
            if(i<topShares.size()){
                Assert.assertEquals("置顶是否相同",topShares.get(i).getId().toString(),id.toString());
            }else {
                Assert.assertEquals("不置顶是否相同",shares.get(i-topShares.size()).getId().toString(),id.toString());
            }
        }

        //关键字和分页测试
        String result2 = mockMvc.perform(get("/app/searchShareList")
                .param("key", "置顶")
                .param("lastId", lastId))
                .andReturn().getResponse().getContentAsString();
        List<Object> resultList2= JsonPath.read(result2, "$.resultData.list");

//        for(int i=0;i<resultList2.size();i++){
//
//            Map first=(Map)resultList2.get(i);
//            Object id=first.get("pid");
//            if(i<topShares.size()){
//                Assert.assertEquals("置顶是否相同",topShares.get(i).getId().toString(),id.toString());
//            }else {
//                Assert.assertEquals("不置顶是否相同",shares.get(i-topShares.size()).getId().toString(),id.toString());
//            }
//        }

    }

    @Test
    public void testShare() {

    }

    @Test
    public void testGetShareInfo() {

    }

    @Test
    public void testSearchShareCommentList() {

    }


}
