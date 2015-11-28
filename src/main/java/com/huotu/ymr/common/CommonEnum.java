package com.huotu.ymr.common;

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
         * partner(1, "合伙")
         */
        partner(1, "合伙"),
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
}