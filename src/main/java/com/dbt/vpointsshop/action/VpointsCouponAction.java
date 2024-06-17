package com.dbt.vpointsshop.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.log.service.VpsOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ReadExcel;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsCouponBatch;
import com.dbt.vpointsshop.service.CouponService;
import com.dbt.vpointsshop.service.VpointsGoodsService;

@Controller
@RequestMapping("/vpointsCoupon")
public class VpointsCouponAction extends BaseAction{
    @Autowired
    private VpointsGoodsService goodService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private VpsOperationLogService logService;
    /**
     * 查询批次
     * @param model
     * @param pageParam
     * @param queryParam
     * @param session
     * @return
     */
    @RequestMapping("/getBatchList")
    public String getBatchList(Model model, String pageParam,String queryParam,HttpSession session){
        PageOrderInfo info = new PageOrderInfo(pageParam);
        SysUserBasis user=getUserBasis(session);
        VpointsCouponBatch bean=new VpointsCouponBatch(queryParam);
//		if(StringUtils.isNotEmpty(companyKey)&&StringUtils.isEmpty(bean.getCompanyKey())){
//			bean.setCompanyKey(companyKey);
//		}else{
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
        int countAll=0;
        List<VpointsCouponBatch> batchList=null;
        try {
            batchList = couponService.getBatchList(info,bean);
            countAll = couponService.getBatchCount(info, bean);
        } catch (Exception e) {
            model.addAttribute("errorMsg","查询异常");
            e.printStackTrace();
        }
        model.addAttribute("batchList", batchList);
        model.addAttribute("showCount", countAll);
        model.addAttribute("startIndex", info.getStartCount());
        model.addAttribute("countPerPage", info.getPagePerCount());
        model.addAttribute("currentPage", info.getCurrentPage());
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("pageParam", pageParam);
        return "vpointsGoods/showBatchList";
    }
    /**
     * 新增批次
     * @param model
     * @param session
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/addBatch")
    public String addBatch(Model model,HttpSession session,
            @RequestParam("batchFile") MultipartFile batchFile, VpointsCouponBatch batch){
        SysUserBasis user=getUserBasis(session);
        String userKey=user.getUserKey();
        HashMap map = new HashMap<String,String>();
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(batch.getCompanyKey())){
//			batch.setCompanyKey(companyKey);
//		}
        batch.fillFields(userKey);
        String batchKey=UUIDTools.getInstance().getUUID();
        batch.setBatchKey(batchKey);
        String logMsg = user.getUserName() + "(" + user.getPhoneNum()  + ")";
        String result = "addSuccess";
        try {
            List<String> list=checkFile(batchFile);
            if(list==null||list.size()==0){
                result = "addFalse";
                model.addAttribute("refresh", result);
                map.put("errMsg", "新增失败");
                return JSON.toJSONString(map);
            //	return "forward:/vpointsCoupon/getBatchList.do";
            }
            int count=list.size();
            batch.setBatchCount(count);
            result=couponService.addBatch(batch);
            result=couponService.addCoupon(batchKey,list,userKey);
        } catch (Exception e) {
            result = "addFalse";
            map.put("errMsg", "新增失败");
            logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1, "", logMsg + ", 电子券批次新增失败");
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1, "", logMsg + ", 电子券批次新增成功");
        model.addAttribute("refresh", result);
    //	return "forward:/vpointsCoupon/getBatchList.do";
        map.put("errMsg", "新增成功");
        return JSON.toJSONString(map);
    }
    /**
     * 详情
     * @param model
     * @param pageParam
     * @param queryParam
     * @return
     */
    @RequestMapping("/getBatchDetail")
    public String getBatchDetail(Model model,String batchKey,HttpSession session){
        PageOrderInfo info = new PageOrderInfo(null);
        VpointsCouponBatch bean=new VpointsCouponBatch();
                bean.setBatchKey(batchKey);
                    try {
                        bean = couponService.getBatchList(info,bean).get(0);
                    } catch (Exception e) {
                        model.addAttribute("errorMsg","查询异常");
                        e.printStackTrace();
                    }
//					SysUserBasis user=getUserBasis(session);
//					String companyKey=user.getCompanyKey();
//					if(StringUtils.isEmpty(companyKey)){
//						List<CompanyInfo> companyList=goodService.getCompanyList();
//						model.addAttribute("companyList", companyList);
//					}
                model.addAttribute("bean", bean);
        return "vpointsGoods/editBatch";
    }

    /**
     * 详情
     * @param model
     * @param 
     * @param 
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBatchDetailMsg")
    public String getBatchDetailMsg(Model model,String batchKey,HttpSession session){
        PageOrderInfo info = new PageOrderInfo(null);
        VpointsCouponBatch bean=new VpointsCouponBatch();
        bean.setBatchKey(batchKey);
        try {
            bean = couponService.getBatchList(info,bean).get(0);
        } catch (Exception e) {
            model.addAttribute("errorMsg","查询异常");
            e.printStackTrace();
            return null;
        }
     /*   model.addAttribute("bean", bean);*/
        return JSON.toJSONString(bean);
    }
    /**
     * 更新批次
     * @param model
     * @param session
     * @param batchFile
     * @param batch
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateBatch")
    public String updateBatch(Model model,HttpSession session,
            @RequestParam("batchFile") MultipartFile batchFile,VpointsCouponBatch batch){
        SysUserBasis user=getUserBasis(session);
        String userKey=user.getUserKey();
        HashMap map = new HashMap<String,String>();
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(batch.getCompanyKey())){
//			batch.setCompanyKey(companyKey);
//		}
        String logMsg = user.getUserName() + "(" + user.getPhoneNum()  + ")";
        batch.setUpdateTime(DateUtil.getDateTime());
        String result="addSuccess";
        try {
            List<String> list=checkFile(batchFile);
            if(list!=null&&list.size()>0){
                int count=list.size();
                batch.setBatchCount(batch.getBatchCount()+count);
                result=couponService.addCoupon(batch.getBatchKey(),list,userKey);
            }
            result=couponService.updateBatch(batch);
        } catch (Exception e) {
            result = "addFalse";
            map.put("errMsg", "修改失败");
            logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, "", logMsg + ", 电子券批次更新失败");
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, "", logMsg + ", 电子券批次更新成功");
        model.addAttribute("refresh", result);
        map.put("errMsg", "修改成功");
        return JSON.toJSONString(map);
       // return "forward:/vpointsCoupon/getBatchList.do";
    }
    /**
     * 新增跳转
     * @return
     */
    @RequestMapping("/batchAdd")
    public String batchAdd(Model model,HttpSession session){
//		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(companyKey)){
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
        return "vpointsGoods/addBatch";
    }
    /**
     * 删除批次
     * @param goodsId
     * @return
     */
    @RequestMapping("/delBatch")
    public String delBatch(HttpSession session,String batchKey,Model model){
        String result = "deleteSuccess";
        SysUserBasis user=getUserBasis(session);
        String logMsg = user.getUserName() + "(" + user.getPhoneNum()  + ")";
        try {
            result=couponService.delBatch(batchKey);
        } catch (Exception e) {
            result = "deleteFalse";
            e.printStackTrace();
            logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3, "", logMsg + ", 电子券批次删除失败");
            return "forward:/vpointsCoupon/getBatchList.do";
        }
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3, "", logMsg + ", 电子券批次删除成功");
        model.addAttribute("refresh", result);
        return "forward:/vpointsCoupon/getBatchList.do";
    }
    /**
     * excel解析
     * @param batchFile
     * @return
     * @throws IOException
     */
    public List<String> checkFile(MultipartFile batchFile) throws IOException{
        String importfileFileName = batchFile.getOriginalFilename();
        InputStream input = batchFile.getInputStream();
        String content="";
        if(input.available()>0){
            List<String> list=new ArrayList<String>();
            List<List<Object>> readExcel = ReadExcel.readExcel(input, importfileFileName.substring(importfileFileName.lastIndexOf(".") + 1));
            for (int i = 0; i < readExcel.size(); i++) {
                Object cell = readExcel.get(i).get(0);
                if(cell!=null){
                    content=cell.toString();
                }
                list.add(content);
            }
            return list;
        }
        return null;
    }
}
