package com.huotu.ymr.service.impl;

import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import com.huotu.ymr.entity.Report;
import com.huotu.ymr.model.searchCondition.ReportSearchModel;
import com.huotu.ymr.repository.ReportRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;


/**
 * Created by xhk on 2015/12/30.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    @Override
    public Page<Report> findReportPage(ReportSearchModel reportSearchModel) throws IOException {


        Sort.Direction direction = reportSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort=new Sort(direction, "time");
        //Pageable pageable= (Pageable) new PageRequest(page,pageSize);
        //Page<com.huotu.huobanplus.common.entity.User> pages=userRestRepository.findByWxNickName("%"+reportSearchModel.getName()+"%",pageable);

        return  reportRepository.findAll(new Specification<Report>() {
            @Override
            public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb){

                if(reportSearchModel.getContent()!=null) {
                    Predicate predicate = cb.like(root.get("shareComment").get("content").as(String.class), "%" + reportSearchModel.getContent() + "%");
                    return predicate;
                }else {
                    Predicate predicate = cb.like(root.get("shareComment").get("content").as(String.class), "%%");
                    return predicate;
                }
            }
        },new PageRequest(reportSearchModel.getPageNoStr(), 20,sort));
    }

}
