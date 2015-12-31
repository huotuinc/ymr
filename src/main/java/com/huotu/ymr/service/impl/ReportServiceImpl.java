package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Report;
import com.huotu.ymr.model.searchCondition.ReportSearchModel;
import com.huotu.ymr.repository.ReportRepository;
import com.huotu.ymr.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by xhk on 2015/12/30.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Override
    public Page<Report> findReportPage(ReportSearchModel reportSearchModel) {

        Sort.Direction direction = reportSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort=new Sort(direction, "time");
        return reportRepository.findAll(new PageRequest(reportSearchModel.getPageNoStr(), 20,sort));
//        return  reportRepository.findAll(new Specification<Report>() {
//            @Override
//            public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb){
//
//                //  predicate = cb.and(cb.like(root.get("to").get("").as(String.class),"%"+articleSearchModel.getArticleTitle()+"%")); //todo 得到用户的name
//
//                CriteriaQuery<Report> criteriaQuery = cb.createQuery(Report.class);
//                Predicate predicate = cb
//                return predicate;
//            }
//        },new PageRequest(reportSearchModel.getPageNoStr(), 20,sort));
    }

}
