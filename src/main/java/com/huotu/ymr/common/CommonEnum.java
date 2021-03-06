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
        /**
         * ERROR_USER_NOT_FOUND(2001,"用户不存在")
         */
        ERROR_USER_NOT_FOUND(2001,"用户不存在"),

        /**
         * USER_BE_FREEZE(2101,"用户已被冻结，请联系客服")
         */
        USER_BE_FREEZE(2101,"用户已被冻结，无法进行任何操作，请联系客服"),

        /**
         * USER_BE_NOTALK(2102,"您已被禁言，无法发表任何言论")
         */
        USER_BE_NOTALK(2102,"您已被禁言，无法发表任何言论"),

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
         *  ERROR_MOBILE_ALREADY_BINDING(53004, "手机号已绑定")
         */
        ERROR_MOBILE_ALREADY_BINDING(53004, "手机号已绑定"),
        /**
         *  ERROR_MOBILE_NOT_BINDING(53005, "手机号未绑定")
         */
        ERROR_MOBILE_NOT_BINDING(53005, "手机号未绑定"),
        /**
         * ERROR_WRONG_VERIFICATION_INTERVAL(53014, "验证码发送间隔为90秒")
         */
        ERROR_WRONG_VERIFICATION_INTERVAL(53014, "验证码发送间隔为90秒"),
        /**
         * ERROR_SEND_MESSAGE_FAIL(55001, "短信发送通道不稳定，请重新尝试")
         */
        ERROR_SEND_MESSAGE_FAIL(55001, "短信发送通道不稳定，请重新尝试"),

        /**
         * ERROR_WRONG_CODE(55002,"验证码错误")
         */
        ERROR_WRONG_CODE(55002,"验证码错误"),

        /**
         * ERROR_SHARE_NOT_FOUND(60001,"文章不存在")
         */
        ERROR_SHARE_NOT_FOUND(60001,"文章不存在"),

        /**
         * ERROR_COMMENT_NOT_FOUND(60002,"评论不存在")
         */
        ERROR_COMMENT_NOT_FOUND(60002,"评论不存在"),

        /**
         * ERROR_INTEGRAL_INSUFFICIENT(70001,"积分不足")
         */
        ERROR_INTEGRAL_INSUFFICIENT(70001,"积分不足"),

        /**
         * ERROR_EXTRACT_FIAL(70002,"提现失败")
         */
        ERROR_EXTRACT_FIAL(70002,"提现失败");


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
     * 文章类型
     */
    enum ArticleType implements ICommonEnum {
        /**
         * company(0, "公司介绍"),
         */
        company(0, "公司介绍"),
        /**
         *  autobiography(1, "自传故事"),
         */
        autobiography(1, "自传故事"),
        /**
         * college(2,"学院介绍"),
         */
        college(2,"学院介绍"),
        /**
         *  hairdressing(3,"美容知识");
         */
        hairdressing(3,"美容知识");
        private int value;

        private String name;

        ArticleType(int value, String name) {
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
        payed(1, "支付成功"),

        /**
         * refund(2, "被退款")
         */
        refunded(2, "被退款");


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
     * 用户状态
     */
    enum UserStatus implements ICommonEnum {
        /**
         * normal(0, "正常")
         */
        normal(0, "正常"),
        /**
         * notalk(1, "禁言")
         */
        notalk(1, "禁言"),
        /**
         * freeze(2, "冻结")
         */
        freeze(2, "冻结");
        private int value;

        private String name;

        UserStatus(int value, String name) {
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
     * 评论状态
     */
    enum ShareCommentStatus implements ICommonEnum {
        /**
         * normal(0, "正常")
         */
        normal(0, "正常"),
        /**
         * delete(1, "删除")
         */
        delete(1, "删除");
        private int value;

        private String name;

        ShareCommentStatus(int value, String name) {
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
        admin_put(5, "管理员充值");


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


    enum AppUserType implements ICommonEnum{
        /**
         * customer(0, "客户")
         */
        customer(0, "客户"),
        /**
         * stylist(1, "设计师")
         */
        stylist(1, "设计师");

        private int value;

        private String name;

        AppUserType(int value, String name) {
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
     * 项目状态
     */
    enum CrowdStatus implements ICommonEnum {
        /**
         * notstarted(0, "未开始"),
         */
        notstarted(0, "未开始"),
        /**
         * success(1, "成功"),
         */
        success(1, "成功"),
        /**
         * fail(2, "失败"),
         */
        fail(2, "失败"),
        /**
         * running(3, "进行中");
         */
        running(3, "进行中");

        private int value;

        private String name;

        CrowdStatus(int value, String name) {
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
         * audit(0,"审核中")
         */
        audit(0, "审核中"),
        /**
         * pass(1, "通过")
         */
        pass(1, "通过"),
        /**
         * notPass(2, "未通过")
         */
        notPass(2, "未通过"),
        close(3, "关闭"),
        delete(4, "删除"),
        open(5,"开启"),
        draft(6,"草稿");
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




    enum VerificationType implements ICommonEnum {


        reg(0, "注册"),

        bind(1,"绑定手机");



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


    /**
     * 推送消息类型
     */
    enum PushMessageType implements ICommonEnum {
        /**
         * 1:版本更新
         * <p>携带数据为版本更新摘要</p>
         */
        Version(0, "版本更新"),
        /**
         * 2:消息提醒
         * <p>提示阅读消息，携带数据为空</p>
         */
        RemindMessage(1, "消息提醒"),
        /**
         * 3:通知
         * <p>携带数据为纯文本消息</p>
         */
        Notify(2, "通知");

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

        PushMessageType(int value, String name) {
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

    enum PurchaseSource implements ICommonEnum{
        /**
         * 用户支付类型
         */
        WEIXIN(0, "微信支付"),

        ALIPAY(1, "支付宝支付");

        private int value;

        private String name;

        PurchaseSource(int value,String name){
            this.value=value;
            this.name=name;

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
