/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 上午11:21:52 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.enterprise.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.core.io.FileTypeUtil;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/skuInfo")
@Slf4j
public class SkuInfoAction extends BaseAction{

	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private VcodeActivityService vcodeActivityService;

	/**
	 * SKU列表
	 */
	@RequestMapping("/showSkuInfoList")
	public String showSkuInfoList(HttpSession session,String queryParam, String pageParam, Model model) {
		try {
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            SkuInfo queryBean = new SkuInfo(queryParam);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<SkuInfo> resultList = skuInfoService.queryForLst(queryBean, pageInfo);
			int countResult = skuInfoService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam",queryParam );
            model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
		    log.error("SKU查询失败", ex);
		}
		return "skuinfo/showSkuInfoList";
	}
	
	/**
	 * 跳转sku新增页面
	 */
	@RequestMapping("/showSkuInfoAdd")
	public String showSkuInfoAdd(HttpSession session, Model model){
        SysUserBasis currentUser = this.getUserBasis(session);
		log.info("Obtaining basic user information showSkuInfoAdd->{}",JSON.toJSONString(currentUser));
		String skuChannelInfos = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
				.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.SKUCHANNEL);
		if(StringUtils.isNotBlank(skuChannelInfos)){
			List<SkuInfo.SkuChannelInfo> skuChannelInfoList = new ArrayList<>();
			List<String> skuChannels = Arrays.asList(skuChannelInfos.split(","));
			for(String s : skuChannels){
				SkuInfo.SkuChannelInfo channelInfo = new SkuInfo.SkuChannelInfo();
				channelInfo.setSkuChannelCode(s.split(":")[1]);
				channelInfo.setSkuChannelName(s.split(":")[0]);
				skuChannelInfoList.add(channelInfo);
			}
			log.info("Obtaining basic user information skuChannelInfoList->{}",JSON.toJSONString(skuChannelInfoList));
			model.addAttribute("skuChannelInfoList", skuChannelInfoList);
		}
		skuInfoService.initSkuAccount(currentUser,model);
        model.addAttribute("currentUser", currentUser);
		model.addAttribute("year", DateUtil.getCurrentYear());
		return "skuinfo/showSkuInfoAdd";
	}
	
	/**
	 * 跳转sku编辑页面
	 */
	@RequestMapping("/showSkuInfoEdit")
	public String showSkuInfoEdit(HttpSession session, String skuKey, Model model){
        SysUserBasis currentUser = this.getUserBasis(session);
        SkuInfo skuInfo = skuInfoService.findById(skuKey);
		String skuChannelInfos = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
				.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.SKUCHANNEL);
		if(StringUtils.isNotBlank(skuChannelInfos)){
			List<String> skuChannels = Arrays.asList(skuChannelInfos.split(","));
			List<SkuInfo.SkuChannelInfo> skuChannelInfoList = new ArrayList<>();
			for(String s : skuChannels){
				SkuInfo.SkuChannelInfo channelInfo = new SkuInfo.SkuChannelInfo();
				channelInfo.setSkuChannelCode(s.split(":")[1]);
				channelInfo.setSkuChannelName(s.split(":")[0]);
				skuChannelInfoList.add(channelInfo);
			}
			skuInfo.setSkuChannel(skuChannelInfoList);
		}
		skuInfoService.initSkuAccount(currentUser,model);
        model.addAttribute("currentUser", currentUser);
		model.addAttribute("skuInfo", skuInfo);
		return "skuinfo/showSkuInfoEdit";
	}
	
	/**
	 * 添加sku信息
	 */
	@RequestMapping("/doSkuInfoAdd")
	public String doSkuInfoAdd(HttpSession session, SkuInfo skuInfo,Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    skuInfo.setCompanyKey(currentUser.getCompanyKey());
		    skuInfo.fillFields(currentUser.getUserKey());
		    skuInfoService.insertSkuInfo(skuInfo);
			model.addAttribute("errMsg", "新增成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "新增失败");
			log.error("SKU新增失败", ex);
		}
		return "forward:showSkuInfoList.do"; 
	}
	
	/**
	 * 编辑sku信息
	 */
	@RequestMapping("/doSkuInfoEdit")
	public String doSkuInfoEdit(HttpSession session, SkuInfo skuInfo, Model model){
		try{
		    skuInfoService.updateSkuInfo(skuInfo);
			model.addAttribute("errMsg", "编辑成功");
		} catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("SKU编辑失败", ex);
        }
        return "forward:showSkuInfoList.do"; 
	} 
	
	/**
	 * 删除sku信息
	 */
	@RequestMapping("/doSkuInfoDelete")
	public String doSkuInfoDelete(HttpSession session, String skuKey,Model model){
		try{
		    // 首先判断该sku下有没有活动，活动存在不允许删除
		    int count = vcodeActivityService.queryActivityCountBySkuKey(skuKey);
		    if(count == 0){
		        skuInfoService.deleteSkuInfo(skuKey);
		        model.addAttribute("errMsg", "删除成功");
		    }else{
		        model.addAttribute("errMsg", "删除失败,先删除该sku关联的活动");
		    }
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("SKU删除失败", ex);
        }
        return "forward:showSkuInfoList.do"; 
	} 
	
	/**
	 * 上传图片
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("imgUpload")
	@ResponseBody
	public String imgUpload(Model model,HttpServletRequest request, HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files=multipartRequest.getFiles("file");
		String  result="";
		String imgSrc = "";
		if(files!=null&&files.size()>0){
			for (int i = 0; i < files.size(); i++) {
				try {
					String fileName = files.get(i).getOriginalFilename();
					if(StringUtils.isEmpty(fileName)) {
						return "error";
					}

					//判断后缀
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
					if("png".equals(suffix) || "bmp".equals(suffix) || "jpeg".equals(suffix) || "jpg".equals(suffix)) {
						String type = FileTypeUtil.getType(files.get(i).getInputStream());
						if("png".equals(type) || "bmp".equals(type) || "jpeg".equals(type) || "jpg".equals(type)) {
							imgSrc = HttpReq.uploadImgFile(files.get(i));
						} else {
							return "error";
						}
					} else {
						return "error";
					}

				} catch (Exception  e) {
					e.printStackTrace();
					return "error";
				}
				if(i==files.size()-1){
					result+=imgSrc;
				}else{
					result+=imgSrc+",";
				}
			}
		}

		return result;
	}

	/**
	 * 上传图片
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("imgUploadUrl")
	@ResponseBody
	public String imgUploadUrl(Model model,HttpServletRequest request, HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files=multipartRequest.getFiles("file");
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(files!=null&&files.size()>0){
			for (int i = 0; i < files.size(); i++) {
				try {

					String fileName = files.get(i).getOriginalFilename();
					if(StringUtils.isEmpty(fileName)) {
						return "error";
					}

					//判断后缀
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
					if("gif".equals(suffix) || "png".equals(suffix) || "bmp".equals(suffix) || "jpeg".equals(suffix) || "jpg".equals(suffix)) {
						String type = FileTypeUtil.getType(files.get(i).getInputStream());
						if(StringUtils.isBlank(type) || "gif".equals(type) || ("png".equals(type) || "bmp".equals(type) || "jpeg".equals(type) || "jpg".equals(type))) {
							itemMap = HttpReq.uploadImgFileForUrl(files.get(i));
						} else {
							resultMap.put("errMsg", "error");
							return JSON.toJSONString(resultMap);
						}
					} else {
						resultMap.put("errMsg", "error");
						return JSON.toJSONString(resultMap);
					}

				} catch (Exception  e) {
					e.printStackTrace();
				}
				if (!resultMap.containsKey("path")) {
					resultMap.put("path", itemMap.get("path").toString());
					resultMap.put("ipUrl", itemMap.get("ipUrl").toString());
					resultMap.put("domainUrl", itemMap.get("domainUrl").toString());
				} else {
					resultMap.put("path", resultMap.get("path") + "," + itemMap.get("path").toString());
					resultMap.put("ipUrl", resultMap.get("ipUrl") + "," + itemMap.get("ipUrl").toString());
					resultMap.put("domainUrl", resultMap.get("domainUrl") + "," + itemMap.get("domainUrl").toString());
				}
			}
		}

		if (resultMap == null || resultMap.isEmpty()) {
			resultMap.put("errMsg", "error");
		}

		return JSON.toJSONString(resultMap);
	}
}
