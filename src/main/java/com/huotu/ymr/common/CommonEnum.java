package com.huotu.ymr.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.huotu.common.api.ICommonEnum;

/**
 * Created by lgh on 2015/11/27.
 */
public interface CommonEnum {
    enum AppCode implements ICommonEnum {
        /**
         * SUCCESS(1, "操作成功")
         */
        SUCCESS(1, "操作成功");
        private int value;

        private String name;

        AppCode(int value, String name) {
            this.value = value;
            this.name = name;
        }


        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 众筹类型
     */
    enum CrowdFundingType implements ICommonEnum {
        /**
         * booking(0, "预约")
         */
        booking(0, "预约"),
        /**
         * cooperation(1, "合作")
         */
        cooperation(1, "合作"),
        /**
         * subscription(2, "认购")
         */
        subscription(2, "认购");
        private int value;

        private String name;

        CrowdFundingType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 爱美容类型
     */
    enum ShareType implements ICommonEnum {
        /**
         * info(0, "资讯")
         */
        information(0, "资讯"),
        /**
         * group(1, "团购")
         */
        group(1, "团购");
        private int value;

        private String name;

        ShareType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 支付状态
     */
    enum PayType implements ICommonEnum {
        /**
         * paying(0, "支付中")
         */
        paying(0, "支付中"),
        /**
         * payed(1, "支付成功")
         */
        payed(1, "支付成功");
        private int value;

        private String name;

        PayType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 用户级别
     */
    enum UserLevel implements ICommonEnum {
        /**
         * one(0, "LV1")
         */
        one(0, "LV1"),
        /**
         * two(1, "LV2")
         */
        two(1, "LV2"),
        /**
         * three(2, "LV3")
         */
        three(2, "LV3");
        private int value;

        private String name;

        UserLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }


        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
