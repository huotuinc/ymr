package com.huotu.ymr;

import org.junit.Test;

/**
 * Created by Administrator on 2015/12/5.
 */
public class otherTest {
    @Test
    public void test1(){
        String s="|1|22|3|5|88|";
        String[] k=s.split("\\|");

        for(int i=0;i<k.length;i++){
            System.out.println("数字"+k[i]);
        }

    }

}
