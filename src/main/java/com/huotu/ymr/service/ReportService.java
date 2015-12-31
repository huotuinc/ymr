package com.huotu.ymr.service;

import com.huotu.ymr.entity.Report;
import com.huotu.ymr.model.searchCondition.ReportSearchModel;
import org.springframework.data.domain.Page;

/**
 * Created by xhk on 2015/12/30.
 * 举报管理
 */
public interface ReportService {
    /**
     * 获取一页举报列表
     * @param reportSearchModel
     * @return
     */
    Page<Report> findReportPage(ReportSearchModel reportSearchModel);
}
