package com.huotu.ymr.model.web;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 文章网页版
 * Created by xhk on 2015/12/29.
 */
@Getter
@Setter
public class WebArticleModel {
        /**
         * n标题
         */
        private String title;

        /**
         * 图片
         */

        private String picture;

        /**
         * 内容
         */

        private String content;


        /**
         * 时间
         */
        private Date time;

        /**
         * 浏览量
         */
        private Long view;

}
