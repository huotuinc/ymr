package com.huotu.ymr.model.searchCondition;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 2016/1/10.
 */
@Setter
@Getter
public class ChargeSearchModel
{

    /**
     * �û���
     */
    private String userName="";

    /**
     * �û��ȼ� userLevel 0:һ����1:������2������,-1:ȫ��
     */
    private Integer userLevel=-1;



    /**
     * �����ֶ� 0�����(ID)��1�����֣�2���ȼ�
     */
    private Integer sort=0;

    /**
     * ����ʽ 0������|1������
     */
    private Integer raSortType = 0;
    //    /**
    //     * ����ʱ��
    //     */
    private String startTime="";
    //    /**
    //     * ����ʱ��
    //     */
      private String endTime="";
    /**
     * ָ����ѯҳ��
     */
    private Integer pageNoStr = 0;

    private String articleTitle="";

    private  int totalScoreRecords;

    private Integer scoreFlowType=-1;

    private long userID;


}
