package com.huotu.ymr.service;

import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingMoneyRange;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.searchCondition.CrowdFundingPublicSearchModel;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.model.searchCondition.DraftSearchModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by xhk on 2015/12/2.
 */
public interface CrowdFundingService {
    /**
     * 通过文章类型，前一页最后一个id，每页显示的条数
     * 获取一页认购,预约，合作发起人表格
     * @return
     */
    List<CrowdFundingPublic> findCrowdListFromLastIdWithNumber(Long crowdId, Long lastId,int number);

    /**
     * 获取认购表中最大的id值
     * @return
     */
    long getMaxId();

//    /**
//     * 通过关键字和每页条数查找合作发起人的下一页
//     * @return
//     */
//    List<CrowdFundingPublic> searchCooperationgList(String key, Long lastId, Long crowdId, int number);

    /**
     * 获取众筹表中最大的id值
     * @return
     */
    long getCrowdFundingMaxId();

    /**
     * 通过前一页最后一个id，每页显示的条数,关键字key
     * 获取一页众筹表格
     * @return
     */
    List<CrowdFunding> searchCrowdFundingList(String key,Long lastId, int number);

    /**
     * 通过众筹项目crowdId，获得成功的合作项目合作发起者
     * 获取一页众筹表格
     * @return
     */
    List<CrowdFundingPublic> getPublicByCrowdId(Long crowdId);

    /**
     * 通过合作发起者Id，与该合作发起者成功合作的合作者
     * 获取一页众筹表格
     * @return
     */
    List<CrowdFundingBooking> getBookingByPublicId(Long crowdId,Long id);

    /**
     * 后台获取众筹分页
     * 获取一页众筹表格
     * @return
     */
    Page<CrowdFunding> findCrowdFundingPage(CrowdFundingSearchModel searchModel);

    /**
     * 后台获取草稿箱分页
     * 获取一页草稿箱表格
     * @return
     */
    Page<CrowdFunding> findDraftPage(DraftSearchModel draftSearchModel);

    /**
     * 后台获取众筹项目人分页
     * 获取一页众筹项目人表格
     * @return
     */
    Page<CrowdFundingPublic> findPublicPage(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel);

    /**
     * 后台通过合作发起人查找合作人
     * 获取一页合作人表格
     * @return
     */
    List<CrowdFundingBooking> findBookingsByPublic(CrowdFundingPublic crowdFundingPublic,Long crowdId);

    /**
     * 后台获取合作人分页
     * 获取一页合作人表格
     * @return
     */
    Page<CrowdFundingBooking> findBookingPages(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel);

    /**
     * 后台通过项目获取该项目所有的金额范围
     * @return
     */
    List<CrowdFundingMoneyRange> findRangesByCrowdFunding(CrowdFunding crowdFunding);

    /**
     * 后台通过项目删除该项目所有的金额范围
     * @return
     */
    void deleteRangesByCrowdFunding(CrowdFunding crowdFunding);
}
