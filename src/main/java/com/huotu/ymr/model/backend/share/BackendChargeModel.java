package com.huotu.ymr.model.backend.share;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;


/**
 * Created by zyw on 2016/1/8.
 */
@Setter
@Getter
public class BackendChargeModel

{
    /**
     * �û���id
     */

     private long userId;
    /**
     * �û���û��ֵ�ʱ��
     */
     private Date date;
    /**
     * �û����ǳ�
     */
     private String userName;
    /**
     * �û���õĻ���
     */
     private int score;

    /**
     * �û��ĵȼ�
     */
    private String level;






 }

