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
        SUCCESS(1, "操作成功"),
        /**
         * PARAMETER_ERROR(1001,"参数错误")
         */
        PARAMETER_ERROR(1001, "参数错误"),
        SYSTEM_BAD_REQUEST_50601(50601, "系统请求失败,code 50601"),//sign校验失败
        /**
         * ERROR_USER_LOGIN_FAIL(56000, "用户登录失败")
         */
        ERROR_USER_LOGIN_FAIL(56000, "用户登录失败"),
        /**
         * ERROR_USER_TOKEN_FAIL(56001, "用户登录失效，需要重新登录")
         */
        ERROR_USER_TOKEN_FAIL(56001, "你已经在其他设备登录，需要重新登录"),
        /**
         * ERROR_WRONG_MOBILE(53003, "不合法的手机号")
         */
        ERROR_WRONG_MOBILE(53003, "不合法的手机号"),
        /**
         * ERROR_WRONG_VERIFICATION_INTERVAL(53014, "验证码发送间隔为90秒")
         */
        ERROR_WRONG_VERIFICATION_INTERVAL(53014, "验证码发送间隔为90秒"),
        /**
         * ERROR_SEND_MESSAGE_FAIL(55001, "短信发送通道不稳定，请重新尝试")
         */
        ERROR_SEND_MESSAGE_FAIL(55001, "短信发送通道不稳定，请重新尝试");


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
        group(1, "团购"),
        /**
         * crowdFunding(2,"众筹项目")
         */
        crowdFunding(2,"众筹项目"),
        /**
         *  userShare(3,"用户分享")
         */
        userShare(3,"用户分享");
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

    /**
     * 0 无更新 1增量更新 2 整包更新 3 强制增量更新 4 强制整包更新
     */
    enum VersionUpdateType implements ICommonEnum {
        NO(0, "无更新"),

        INCREMENT(1, "增量更新"),

        WHOLE(2, "整包更新"),

        FORCE_INCREMENT(3, "强制增量更新"),

        FORCE_WHOLE(4, "强制整包更新");


        private int value;

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

        private String name;

        VersionUpdateType(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /**
     * 积分流水类型
     */
    enum ScoreFlowType implements ICommonEnum {

        user_pay(0, "用户支付"),
        user_put(1, "用户充值"),
        post(2, "发布爱分享"),
        turn(3, "转发爱分享"),
        extracted(4, "提取积分"),
        admin_put(5,"管理员充值");


        private int value;

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

        private String name;

        ScoreFlowType(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /**
     * 用户类型
     */
    enum UserType implements ICommonEnum {
        /**
         * user(0, "用户")
         */
        user(0, "用户"),
        /**
         * merchant(1, "官方")
         */
        official(1, "官方");
        private int value;

        private String name;

        UserType(int value, String name) {
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
     * 审核类型
     */
    enum CheckType implements ICommonEnum {
        /**
         * notPass(0, "未通过")
         */
        notPass(0, "未通过"),
        /**
         * pass(1, "通过")
         */
        pass(1, "通过"),
        /**
         * audit(2,"审核中")
         */
        audit(2, "审核中");
        private int value;

        private String name;

        CheckType(int value, String name) {
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


    enum CodeType implements ICommonEnum{
        /**
         * text(0, "文本")
         */
        text(0, "文本"),
        /**
         * voice(1, "语音")
         */
        voice(1, "语音");

        private int value;

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

        private String name;

        CodeType(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    enum VerificationType implements ICommonEnum {


        reg(0, "注册");

        private int value;

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

        private String name;

        VerificationType(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }



    enum LevelFlowType implements ICommonEnum {
        /**
         * 用户积分达到一定分数后升级
         */
        auto(0, "自动升级"),

        put(1, "充值升级"),
        admin_change(2, "管理员改变升级");
        private int value;

        private String name;

        LevelFlowType(int value, String name) {
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
