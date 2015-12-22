package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.PublicManagerParameterHolder;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingMoneyRange;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.backend.crowdFunding.AddCrowdFundingModel;
import com.huotu.ymr.model.backend.crowdFunding.CrowdFundingBookingModel;
import com.huotu.ymr.model.backend.crowdFunding.CrowdFundingPeopleModel;
import com.huotu.ymr.model.backend.crowdFunding.Msg;
import com.huotu.ymr.model.manager.MngPublicModel;
import com.huotu.ymr.model.searchCondition.CrowdFundingPublicSearchModel;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.model.searchCondition.DraftSearchModel;
import com.huotu.ymr.repository.*;
import com.huotu.ymr.service.CrowdFundingService;
import com.huotu.ymr.service.StaticResourceService;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    CrowdFundingMoneyRangeRepository crowdFundingMoneyRangeRepository;


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
     * 跳转到审核众筹信息
     * @param addCrowdFundingModel 前台传过来的众筹参数
     * @param id 项目id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/jumpToAuditCrowdFunding",method = RequestMethod.GET)
    public String jumpToAuditCrowdFunding(AddCrowdFundingModel addCrowdFundingModel,Long id,Model model) throws Exception {
        CrowdFunding crowdFunding = crowdFundingRepository.findOne(id);
        URI uri=staticResourceService.getResource(crowdFunding.getPuctureUrl());
        //crowdFunding.setPuctureUrl(uri.toString());
        addCrowdFundingModel.setPicture(uri.toString());
        addCrowdFundingModel.setContent(crowdFunding.getContent());
        addCrowdFundingModel.setName(crowdFunding.getName());

        if(crowdFunding.getCheckStatus()==CommonEnum.CheckType.draft) {
            addCrowdFundingModel.setIsDraft(1);
        }
        else if(crowdFunding.getCheckStatus()==CommonEnum.CheckType.audit){
            addCrowdFundingModel.setIsDraft(0);
        }

        addCrowdFundingModel.setCrowdFundingType(crowdFunding.getCrowdFundingType());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        addCrowdFundingModel.setEndDate(sdf.format(crowdFunding.getEndTime()));
        addCrowdFundingModel.setStartDate(sdf.format(crowdFunding.getStartTime()));
        addCrowdFundingModel.setSingleSet(crowdFunding.getAgencyFeeRate());
        addCrowdFundingModel.setLimitMoney(crowdFunding.getToMoeny());

        List<CommonEnum.UserLevel> userLevels=crowdFunding.getVisibleLevel();
        for(CommonEnum.UserLevel userL:userLevels){
            if(userL== CommonEnum.UserLevel.one){
                addCrowdFundingModel.setLev1(1);
            }
            if(userL==CommonEnum.UserLevel.two){
                addCrowdFundingModel.setLev2(1);
            }
            if(userL==CommonEnum.UserLevel.three){
                addCrowdFundingModel.setLev3(1);
            }
        }
        addCrowdFundingModel.setId(crowdFunding.getId());
        addCrowdFundingModel.setLimitPeople(crowdFunding.getToBooking());

        if(crowdFunding.getCrowdFundingType()!=CommonEnum.CrowdFundingType.booking) {
            if (crowdFunding.getAgencyFeeRate() == Integer.parseInt(configRepository.findOne("GlobalAgencyFee").getValue())) {
                addCrowdFundingModel.setGlobleOrSingle(1);
            } else {
                addCrowdFundingModel.setGlobleOrSingle(0);
                addCrowdFundingModel.setSingleSet(crowdFunding.getAgencyFeeRate());
            }
        }

        if((crowdFunding.getCheckStatus()==CommonEnum.CheckType.draft&&crowdFunding.getCrowdFundingType()!=CommonEnum.CrowdFundingType.booking)
                ||(crowdFunding.getCheckStatus()==CommonEnum.CheckType.audit&&crowdFunding.getCrowdFundingType()!=CommonEnum.CrowdFundingType.booking)) {
            List<CrowdFundingMoneyRange> crowdFundingMoneyRanges=crowdFundingService.findRangesByCrowdFunding(crowdFunding);
            List<Double> fees=new ArrayList<Double>();
            for(CrowdFundingMoneyRange crowdFundingMoneyRange:crowdFundingMoneyRanges){
                fees.add(crowdFundingMoneyRange.getMoney());
            }
            model.addAttribute("fees",fees.toArray(new Double[fees.size()]));
        }

        model.addAttribute("crowdTypes", CommonEnum.CrowdFundingType.values());
        return "manager/crowdfunding/addCrowd"; //todo 返回上传状态
    }
        /**
         * 添加与审核众筹信息
         * @param addCrowdFundingModel 前台传过来的众筹参数
         * @param model
         * @return
         * @throws Exception
         */
    @RequestMapping(value="/auditCrowdFunding",method = RequestMethod.POST)
    public String auditCrowdFunding(AddCrowdFundingModel addCrowdFundingModel,Model model,HttpServletRequest request) throws Exception {

        String result="redirect:getCrowdFundingList";
        MngPublicModel mpm = PublicManagerParameterHolder.get();
        String contextPath = request.getContextPath();
        //int isDraft=Integer.parseInt(request.getParameter("isDraft"));
        //新增
        if (addCrowdFundingModel.getId() == null) {
            Uri uri = new Uri(addCrowdFundingModel.getPicture());
            String imgPath = uri.getPath().substring(uri.getPath().indexOf(contextPath) + contextPath.length());
            //addCrowdFundingModel.setPicture(imgPath);
           CrowdFunding crowdFunding=new CrowdFunding();

            crowdFunding.setPuctureUrl(imgPath);
            if(addCrowdFundingModel.getIsDraft()==0){
                crowdFunding.setCheckStatus(CommonEnum.CheckType.audit);
                result ="redirect:getCrowdFundingList";
            }else if(addCrowdFundingModel.getIsDraft()==1){
                crowdFunding.setCheckStatus(CommonEnum.CheckType.draft);
                result ="redirect:getDraftList";
            }

            List<CommonEnum.UserLevel> userLevels=new ArrayList<CommonEnum.UserLevel>();
            if(addCrowdFundingModel.getLev1()==1){
                userLevels.add(CommonEnum.UserLevel.one);
            }
            if(addCrowdFundingModel.getLev2()==1){
                userLevels.add(CommonEnum.UserLevel.two);
            }
            if(addCrowdFundingModel.getLev3()==1){
                userLevels.add(CommonEnum.UserLevel.three);
            }

            crowdFunding.setVisibleLevel(userLevels);
            crowdFunding.setCrowdFundingType(addCrowdFundingModel.getCrowdFundingType());
            crowdFunding.setTime(new Date());
            crowdFunding.setName(addCrowdFundingModel.getName());
            crowdFunding.setToBooking(addCrowdFundingModel.getLimitPeople());
            crowdFunding.setToMoeny(addCrowdFundingModel.getLimitMoney());
            crowdFunding.setContent(addCrowdFundingModel.getContent());

            if(addCrowdFundingModel.getCrowdFundingType()!=CommonEnum.CrowdFundingType.booking) {

                if (addCrowdFundingModel.getGlobleOrSingle() == 1) {
                    crowdFunding.setAgencyFeeRate(Integer.parseInt(configRepository.findOne("GlobalAgencyFee").getValue()));//todo 数据库中存入全局
                } else if (addCrowdFundingModel.getGlobleOrSingle() == 0) {
                    crowdFunding.setAgencyFeeRate(addCrowdFundingModel.getSingleSet());
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            date = sdf.parse(addCrowdFundingModel.getEndDate());
            crowdFunding.setEndTime(date);

            date = sdf.parse(addCrowdFundingModel.getStartDate());
            crowdFunding.setStartTime(date);
            //crowdFunding.setManager(managerRepository.findOne(mpm.getManager().getId()));//todo 发布者
            crowdFunding.setContent(addCrowdFundingModel.getContent());
            crowdFunding=crowdFundingRepository.save(crowdFunding);


            if(crowdFunding.getCrowdFundingType()!=CommonEnum.CrowdFundingType.booking) {
                String[] feeButton = request.getParameterValues("feeButton");
                for (int i = 0; i < feeButton.length; i++) {
                    CrowdFundingMoneyRange crowdFundingMoneyRange = new CrowdFundingMoneyRange();
                    crowdFundingMoneyRange.setMoney(Double.parseDouble(feeButton[i]));
                    crowdFundingMoneyRange.setCrowdFundingType(addCrowdFundingModel.getCrowdFundingType());
                    crowdFundingMoneyRange.setCrowdFunding(crowdFunding);
                    crowdFundingMoneyRange = crowdFundingMoneyRangeRepository.save(crowdFundingMoneyRange);
                }
            }


            //修改
        } else {
            CrowdFunding modifyCrowdFunding = crowdFundingRepository.findOne(addCrowdFundingModel.getId());
            //URI uri=staticResourceService.getResource(article.getPicture());
           if(addCrowdFundingModel.getIsDraft()==0){
               modifyCrowdFunding.setCheckStatus(CommonEnum.CheckType.pass);
        }else if(addCrowdFundingModel.getIsDraft()==1){
               modifyCrowdFunding.setCheckStatus(CommonEnum.CheckType.draft);
            result ="redirect:getDraftList";
        }
            modifyCrowdFunding=crowdFundingRepository.save(modifyCrowdFunding);
        }
        return result;
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
    /**
     * 删除众筹
     * @param crowdFundingsId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/delCrowdFunding",method = RequestMethod.POST)
    @ResponseBody
    public Msg delCrowdFunding(Long crowdFundingsId) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        crowdFundingRepository.delete(crowdFunding);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }


    /**
     * 跳转到添加页面
     * @param addCrowdFundingModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/jumpToAddCrowd")
    public String jumpToAddCrowd(AddCrowdFundingModel addCrowdFundingModel,Model model) {
        //addCrowdFundingModel.setCrowdFunding(0);
        addCrowdFundingModel.setLev1(1);
        addCrowdFundingModel.setLev2(1);
        addCrowdFundingModel.setLev3(0);
        addCrowdFundingModel.setGlobleOrSingle(1);
        model.addAttribute("crowdTypes", CommonEnum.CrowdFundingType.values());
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
