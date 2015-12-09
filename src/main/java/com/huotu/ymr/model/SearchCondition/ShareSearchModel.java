package com.huotu.ymr.model.SearchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/12/9.
 */
@Getter
@Setter
public class ShareSearchModel {
    /**
     * 爱分享文章标题
     */
    private String shareTitle;

    private String txtShipName;//收货人
    private String txtShipMobile;//收货人手机号
    private Integer ddlShipStatus = -1;//0：未发货|1：已发货|2：部分发货|3：部分退货|4：已退货
    private Integer ddlOrderByField = 0;//0：按下单时间排序|1：按支付时间排序|2：按订单金额排序
    private Integer ddlPayStatus = -1;//0：未支付|1：已支付|2：已支付至担保方|3：部分付款|4：部分退款|5：全额退款
    private Integer raSortType = 0;//0：降序|1：升序
    private String txtBeginPaytime;//支付时间查询范围-起始
    private String txtEndPaytime;//支付时间查询范围-结束
    private Integer pageNoStr = 0;//指定查询页码
}
