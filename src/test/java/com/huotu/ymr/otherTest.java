package com.huotu.ymr;

import org.junit.Test;

/**
 * Created by Administrator on 2016/1/31.
 */
public class otherTest {
    @Test
    public void otherTest(){
        String s="[name]可以";
        System.out.print(s.replaceAll("\\[name\\]","替换"));
    }
}
