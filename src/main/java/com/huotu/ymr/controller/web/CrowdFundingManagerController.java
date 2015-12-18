package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.backend.crowdFunding.CrowdFundingBookingModel;
import com.huotu.ymr.model.backend.crowdFunding.CrowdFundingPeopleModel;
import com.huotu.ymr.model.backend.crowdFunding.Msg;
import com.huotu.ymr.model.searchCondition.CrowdFundingPublicSearchModel;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.model.searchCondition.DraftSearchModel;
import com.huotu.ymr.repository.CrowdFundingBookingRepository;
import com.huotu.ymr.repository.CrowdFundingPublicRepository;
import com.huotu.ymr.repository.CrowdFundingRepository;
import com.huotu.ymr.service.CrowdFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class CrowdFundingManagerController {

    @Autowired
    CrowdFundingService crowdFundingService;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;

    @Autowired
    CrowdFundingPublicRepository crowdFundingPulicRepository;

    @Autowired
    CrowdFundingBookingRepository crowdFundingBookingRepository;

    /**
     * 获取众筹列表
     * @param crowdFundingSearchModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/getCrowdFundingList",method = RequestMethod.GET)
    public String getCrowdFundingList(CrowdFundingSearchModel crowdFundingSearchModel,Model model) throws Exception {

        Page<CrowdFunding> crowdFundingPages=crowdFundingService.findCrowdFundingPage(crowdFundingSearchModel);
        model.addAttribute("allcrowdFundingList", crowdFundingPages);//文章列表

        model.addAttribute("pageNoStr",crowdFundingSearchModel.getPageNoStr());//当前页数
        return "manager/crowdfunding/crowdFundingList";
    }

    /**
     * 跳转到相对应的众筹详细的项目人信息
     * @param crowdFundingsId 前台传过来的众筹id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/jumpToCFDetail",method = RequestMethod.GET)
    public String jumpToCFDetail(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel,Long crowdFundingsId,Model model) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
       // CrowdFundingPublicSearchModel crowdFundingPublicSearchModel=new CrowdFundingPublicSearchModel();
        crowdFundingPublicSearchModel.setCrowdFundingId(crowdFundingsId);
        if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.booking ){
            crowdFundingPublicSearchModel.setPublicType(0);
            Page<CrowdFundingPublic> pages = crowdFundingService.findPublicPage(crowdFundingPublicSearchModel);

            CrowdFundingPeopleModel crowdFundingPeopleModel=new CrowdFundingPeopleModel();
            crowdFundingPeopleModel.setTotalPages(pages.getTotalPages());
            crowdFundingPeopleModel.setNumber(pages.getNumber());
            crowdFundingPeopleModel.setPeople(pages.getContent());
            crowdFundingPeopleModel.setPeopleType(0);
            crowdFundingPublicSearchModel.setViewType(0);

            crowdFundingPeopleModel.setTotalElements(pages.getTotalElements());
            //crowdFundingPeopleModel.setCrowdFundingPublic(crowdFunding);
            model.addAttribute("allCrowdFundingPublicList", crowdFundingPeopleModel);

            crowdFundingPublicSearchModel.setCrowdFundingType(0);
            crowdFundingPublicSearchModel.setPublicType(0);
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.subscription){
            crowdFundingPublicSearchModel.setPublicType(2);
            Page<CrowdFundingPublic> pages = crowdFundingService.findPublicPage(crowdFundingPublicSearchModel);
            CrowdFundingPeopleModel crowdFundingPeopleModel=new CrowdFundingPeopleModel();
            crowdFundingPeopleModel.setTotalPages(pages.getTotalPages());
            crowdFundingPeopleModel.setNumber(pages.getNumber());
            crowdFundingPeopleModel.setPeople(pages.getContent());
            crowdFundingPeopleModel.setPeopleType(2);
            crowdFundingPublicSearchModel.setViewType(2);

            crowdFundingPeopleModel.setTotalElements(pages.getTotalElements());
            //crowdFundingPeopleModel.setCrowdFundingPublic(crowdFunding);
            model.addAttribute("allCrowdFundingPublicList", crowdFundingPeopleModel);

            crowdFundingPublicSearchModel.setCrowdFundingType(2);
            crowdFundingPublicSearchModel.setPublicType(2);
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.cooperation){
            crowdFundingPublicSearchModel.setPublicType(1);
            Page<CrowdFundingPublic> pages=crowdFundingService.findPublicPage(crowdFundingPublicSearchModel);
            List<CrowdFundingPublic> crowdFundingPublicList=pages.getContent();
            List<CrowdFundingBookingModel> crowdFundingBookingModels=new ArrayList<CrowdFundingBookingModel>();
            //List<CrowdFundingBooking> crowdFundingBookings=new ArrayList<CrowdFundingBooking>();
            for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
                List<CrowdFundingBooking> crowdFundingBookings=crowdFundingService.findBookingsByPublic(crowdFundingPublic,crowdFundingPublicSearchModel.getCrowdFundingId());
                //CrowdFundingCooModel cooListModel=new CrowdFundingCooModel();
                CrowdFundingBookingModel bookingListM=new CrowdFundingBookingModel();
                //bookingListModel.setCrowdFundingPublic(crowdFundingPublic);
                //cooListModel.setPeople(pages.getContent());
                //cooListModel.setBooking(crowdFundingBookings);
                //cooListModel.setPeopleType(3);
                bookingListM.setBooking(crowdFundingBookings);
                bookingListM.setPeopleType(1);
                bookingListM.setPerson(crowdFundingPublic);
                bookingListM.setTotalElements(pages.getTotalElements());
                bookingListM.setTotalPages(pages.getTotalPages());
                bookingListM.setNumber(pages.getNumber());
                crowdFundingBookingModels.add(bookingListM);
            }
            crowdFundingPublicSearchModel.setViewType(4);

            model.addAttribute("allCrowdFundingPublicList", crowdFundingBookingModels);
            crowdFundingPublicSearchModel.setCrowdFundingType(1);
            crowdFundingPublicSearchModel.setPublicType(1);
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
        }
        model.addAttribute("crowdFundingTitle",crowdFunding.getName());
        model.addAttribute("crowFundingStart",crowdFunding.getStartTime());
        model.addAttribute("crowFundingEnd",crowdFunding.getEndTime());
        model.addAttribute("currentBooking",crowdFunding.getCurrentBooking());



        model.addAttribute("content",crowdFunding.getContent()); //todo 信息过滤图片
        model.addAttribute("pageNoStr",1);//当前页数
        return "manager/crowdfunding/crowdFundingPublicList";
    }


    /**
     * 审核众筹信息
     * @param crowdFundingsId 前台传过来的众筹id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/auditCrowdFunding",method = RequestMethod.GET)
    public String auditCrowdFunding(Long crowdFundingsId,Model model) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        model.addAttribute("crowdFunding", crowdFunding);
        return "manager/crowdfunding/crowdFundingDetail";
    }

    /**
     * 关闭众筹
     * @param crowdFundingsId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/closeCrowdFunding",method = RequestMethod.POST)
    @ResponseBody
    public Msg closeCrowdFunding(Long crowdFundingsId) throws Exception {

        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        crowdFunding.setCheckStatus(CommonEnum.CheckType.close);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;

    }

    /**
     * 开启众筹
     * @param crowdFundingsId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/openCrowdFunding",method = RequestMethod.POST)
    @ResponseBody
    public Msg openCrowdFunding(Long crowdFundingsId) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        crowdFunding.setCheckStatus(CommonEnum.CheckType.open);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }

    @RequestMapping("/getCrowdFundingBookingList")
    public String getCrowdFundingBookingList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingBookingList";
    }

    @RequestMapping("/getCrowdFundingCooperationList")
    public String getCrowdFundingCooperationList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingCooperationList";
    }
    @RequestMapping("/getCrowdFundingSubscriptionList")
    public String getCrowdFundingSubscriptionList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingSubscriptionList";
    }


    @RequestMapping("/addCrowd")
    public String addCrowd(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/addCrowd";
    }

    /**
     * 获取草稿箱列表
     * @param draftSearchModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/getDraftList",method = RequestMethod.GET)
    public String getDraftList(DraftSearchModel draftSearchModel,Model model) throws Exception {

        Page<CrowdFunding> draftPages=crowdFundingService.findDraftPage(draftSearchModel);
        model.addAttribute("allDraftList", draftPages);//文章列表
        model.addAttribute("pageNoStr",draftSearchModel.getPageNoStr());//当前页数
        return "manager/crowdfunding/draftsList";
    }

    /**
     * 删除草稿
     * @param draftId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/delDraft",method = RequestMethod.POST)
    @ResponseBody
    public Msg delDraft(Long draftId) throws Exception {

        crowdFundingRepository.delete(draftId);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;

    }

    /**
     * 上架并启用草稿
     * @param draftId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/putawayDraft",method = RequestMethod.POST)
    @ResponseBody
    public Msg putawayDraft(Long draftId) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(draftId);
        crowdFunding.setCheckStatus(CommonEnum.CheckType.pass);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }


    /**
     * 获取项目人列表
     * @param crowdFundingPublicSearchModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/getCrowdFundingPublicList",method = RequestMethod.GET)
    public String getCrowdFundingPublicList(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel,Model model) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingPublicSearchModel.getCrowdFundingId());

        if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.booking ){
            crowdFundingPublicSearchModel.setCrowdFundingType(0);
            crowdFundingPublicSearchModel.setPublicType(0);
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.subscription){
            crowdFundingPublicSearchModel.setCrowdFundingType(2);
            crowdFundingPublicSearchModel.setPublicType(2);
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.cooperation){
            crowdFundingPublicSearchModel.setCrowdFundingType(1);
            crowdFundingPublicSearchModel.setPublicType(1);
        }

        //如果请求合作发起人等public人
        if((crowdFundingPublicSearchModel.getPublicType()==1&&crowdFundingPublicSearchModel.getPeopleType()==1)
                ||crowdFundingPublicSearchModel.getPublicType()==0
                ||crowdFundingPublicSearchModel.getPublicType()==2) {
            Page<CrowdFundingPublic> pages = crowdFundingService.findPublicPage(crowdFundingPublicSearchModel);
            CrowdFundingPeopleModel bookingListM=new CrowdFundingPeopleModel();
            bookingListM.setPeople(pages.getContent());
            bookingListM.setPeopleType(1);
            crowdFundingPublicSearchModel.setViewType(1);

            bookingListM.setTotalElements(pages.getTotalElements());
            bookingListM.setTotalPages(pages.getTotalPages());
            bookingListM.setNumber(pages.getNumber());

            model.addAttribute("allCrowdFundingPublicList", bookingListM);
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());
        }
        //如果是请求合作者全部，才遍历合作发起人下的合作人
        if( crowdFundingPublicSearchModel.getPublicType()==1&&crowdFundingPublicSearchModel.getPeopleType()==-1){
            Page<CrowdFundingPublic> pages=crowdFundingService.findPublicPage(crowdFundingPublicSearchModel);
            List<CrowdFundingPublic> crowdFundingPublicList=pages.getContent();
            List<CrowdFundingBookingModel> crowdFundingBookingModels=new ArrayList<CrowdFundingBookingModel>();
            //List<CrowdFundingBooking> crowdFundingBookings=new ArrayList<CrowdFundingBooking>();
            for(CrowdFundingPublic crowdFundingPublic:crowdFundingPublicList){
                List<CrowdFundingBooking> crowdFundingBookings=crowdFundingService.findBookingsByPublic(crowdFundingPublic,crowdFundingPublicSearchModel.getCrowdFundingId());
                CrowdFundingBookingModel bookingListM=new CrowdFundingBookingModel();
                //CrowdFundingCooModel cooListModel=new CrowdFundingCooModel();
               // bookingListModel.setCrowdFundingPublic(crowdFundingPublic);
                bookingListM.setPeopleType(3);

                //cooListModel.setPeople(pages.getContent());
                bookingListM.setBooking(crowdFundingBookings);
                bookingListM.setTotalElements(pages.getTotalElements());
                bookingListM.setTotalPages(pages.getTotalPages());
                bookingListM.setPeopleType(1);
                bookingListM.setPerson(crowdFundingPublic);
                bookingListM.setNumber(pages.getNumber());
                crowdFundingBookingModels.add(bookingListM);
            }


            crowdFundingPublicSearchModel.setViewType(4);

            model.addAttribute("allCrowdFundingPublicList", crowdFundingBookingModels);
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());

        }
        //如果是请求合作人，则直接得到合作人；
        if(crowdFundingPublicSearchModel.getPublicType()==1&&crowdFundingPublicSearchModel.getPeopleType()==2) {
            Page<CrowdFundingBooking> pages = crowdFundingService.findBookingPages(crowdFundingPublicSearchModel);
            CrowdFundingPeopleModel bookingListM=new CrowdFundingPeopleModel();
            bookingListM.setPeople(pages.getContent());
            bookingListM.setPeopleType(3);
            crowdFundingPublicSearchModel.setViewType(3);

            bookingListM.setTotalElements(pages.getTotalElements());
            bookingListM.setTotalPages(pages.getTotalPages());
            bookingListM.setNumber(pages.getNumber());

            model.addAttribute("allCrowdFundingPublicList", bookingListM);
            //crowdFundingPublicSearchModel.setCooPages(pages.getTotalPages());
            model.addAttribute("totalElements",pages.getTotalElements());
            model.addAttribute("number",pages.getNumber());
            model.addAttribute("totalPages",pages.getTotalPages());
        }
        model.addAttribute("crowdFundingTitle",crowdFunding.getName());
        model.addAttribute("crowFundingStart",crowdFunding.getStartTime());
        model.addAttribute("crowFundingEnd",crowdFunding.getEndTime());
        model.addAttribute("currentBooking",crowdFunding.getCurrentBooking());
        model.addAttribute("content",crowdFunding.getContent()); //todo 信息过滤图片

        model.addAttribute("pageNoStr",crowdFundingPublicSearchModel.getPageNoStr());//当前页数
        return "manager/crowdfunding/crowdFundingPublicList";

    }


    /**
     * 回访项目成功
     * @param userId 前台传过来的项目人id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/successCrowdFunding",method = RequestMethod.POST)
    @ResponseBody
    public Msg successCrowdFunding(Long userId,Integer booking) throws Exception {

        if(booking==0){
            CrowdFundingPublic crowdFundingPublic=crowdFundingPulicRepository.findOne(userId);
            crowdFundingPublic.setStatus(1);
            crowdFundingPublic=crowdFundingPulicRepository.saveAndFlush(crowdFundingPublic);
        }else if(booking==1){
            CrowdFundingBooking crowdFundingBooking=crowdFundingBookingRepository.findOne(userId);
            crowdFundingBooking.setStatus(1);
            crowdFundingBooking=crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking);
        }
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }

    /**
     * 回访项目失败
     * @param userId 前台传过来的项目人id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/failCrowdFunding",method = RequestMethod.POST)
    @ResponseBody
    public Msg failCrowdFunding(Long userId,Integer booking) throws Exception {

        if(booking==0){
            CrowdFundingPublic crowdFundingPublic=crowdFundingPulicRepository.findOne(userId);
            crowdFundingPublic.setStatus(2);
            crowdFundingPublic=crowdFundingPulicRepository.saveAndFlush(crowdFundingPublic);
        }else if(booking==1){
            CrowdFundingBooking crowdFundingBooking=crowdFundingBookingRepository.findOne(userId);
            crowdFundingBooking.setStatus(2);
            crowdFundingBooking=crowdFundingBookingRepository.saveAndFlush(crowdFundingBooking);
        }
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }


}
