package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.repository.ShareCommentRepository;
import com.huotu.ymr.repository.ShareRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.ShareCommentService;
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

    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Autowired
    ShareCommentService shareCommentService;

    @Autowired
    UserRepository userRepository;

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
            share.setCheckStatus(1);
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
//                Assert.assertEquals("不置顶是否相�?",shares.get(i-topShares.size()).getId().toString(),id.toString());
//            }
//        }

    }

    @Test
    public void testShare() throws Exception{
        //测试数据
        Calendar calendar=Calendar.getInstance();
        Share share=new Share();
        share.setOwnerId(123L);
        share.setTitle("分享测试文章");
        share.setStatus(true);
        share.setCommentQuantity(2265L);
        share.setPraiseQuantity(44L);
        share=shareRepository.saveAndFlush(share);

        User user=new User();
        user.setId(123456L);
        user.setUserLevel(CommonEnum.UserLevel.one);
        user.setScore(1555);
        user.setToken("123456789");
        user=userRepository.saveAndFlush(user);

        Map<Long,List<ShareComment>> longListMap=new TreeMap<>();
        ShareComment shareComment=new ShareComment();
        shareComment.setShare(share);
        shareComment.setContent("我的一楼");
        shareComment.setParentId(0L);
        calendar.add(Calendar.HOUR,1);
        shareComment.setTime(calendar.getTime());
        shareComment.setCommentName("slt");
        shareComment.setParentName("文章");
        shareComment=shareCommentRepository.saveAndFlush(shareComment);
        shareComment.setCommentPath("|"+shareComment.getId()+"|");
        shareComment=shareCommentRepository.saveAndFlush(shareComment);
        List<ShareComment> list1=new ArrayList<>();
        list1.add(shareComment);
        longListMap.put(shareComment.getId(),list1);


        ShareComment shareComment1=new ShareComment();
        shareComment1.setShare(share);
        shareComment1.setContent("哇靠，二楼被抢了");
        shareComment1.setParentId(shareComment.getId());
        calendar.add(Calendar.HOUR,1);
        shareComment1.setTime(calendar.getTime());
        shareComment.setCommentName("slt2");
        shareComment1.setParentName("第一名姓名");
        shareComment1=shareCommentRepository.saveAndFlush(shareComment1);
        shareComment1.setCommentPath("|"+shareComment.getId()+"|"+shareComment1.getId()+"|");
        shareComment1=shareCommentRepository.saveAndFlush(shareComment1);
        List<ShareComment> list2=longListMap.get(shareComment.getId());
        list1.add(shareComment1);
        longListMap.put(shareComment.getId(), list2);

        ShareComment shareComment3=new ShareComment();
        shareComment3.setShare(share);
        shareComment3.setContent("你这么喜欢抢二楼啊");
        shareComment3.setParentId(shareComment1.getId());
        calendar.add(Calendar.HOUR,1);
        shareComment3.setTime(calendar.getTime());
        shareComment.setCommentName("slt3");
        shareComment3.setParentName("第二名姓名");
        shareComment3=shareCommentRepository.saveAndFlush(shareComment3);
        shareComment3.setCommentPath("|"+shareComment.getId()+"|"+shareComment1.getId()+"|"+shareComment3.getId()+"|");
        shareComment3=shareCommentRepository.saveAndFlush(shareComment1);
        List<ShareComment> list3=longListMap.get(shareComment.getId());
        list3.add(shareComment3);
        longListMap.put(shareComment.getId(), list3);

        ShareComment shareComment4=new ShareComment();
        shareComment4.setShare(share);
        shareComment4.setContent("我是二楼");
        shareComment4.setParentId(0L);
        calendar.add(Calendar.HOUR,1);
        shareComment4.setTime(calendar.getTime());
        shareComment.setCommentName("slt4");
        shareComment4.setParentName("文章");
        shareComment4=shareCommentRepository.saveAndFlush(shareComment4);
        shareComment4.setCommentPath("|"+shareComment4.getId()+"|");
        shareComment4=shareCommentRepository.saveAndFlush(shareComment4);
        List<ShareComment> list4=new ArrayList<>();
        list4.add(shareComment4);
        longListMap.put(shareComment4.getId(), list4);

        String result=mockMvc.perform(get("/app/searchShareCommentList")
                .param("shareId",  share.getId()+"")
                .param("lastId", "0"))
                .andReturn().getResponse().getContentAsString();
        List<Object> resultList= JsonPath.read(result, "$.resultData.list");

        for(int i=0;i<resultList.size();i++){
            List<ShareComment> commentList;
            Map map=(Map)resultList.get(i);
            Integer id=(Integer)map.get("pid");
            if(i==0){
                 commentList=longListMap.get(shareComment.getId());
            }else {
                 commentList=longListMap.get(shareComment4.getId());
            }
            Assert.assertEquals("",commentList.get(0).getId().toString(),id.toString());

        }






    }

    @Test
    public void testGetShareInfo() {

    }

    @Test
    public void testSearchShareCommentList() {

    }


}
