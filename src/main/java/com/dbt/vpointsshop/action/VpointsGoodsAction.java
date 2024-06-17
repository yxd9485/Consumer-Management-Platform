package com.dbt.vpointsshop.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.core.io.FileTypeUtil;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.util.*;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.vpointsshop.bean.*;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import com.dbt.vpointsshop.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/vpointsGoods")
public class VpointsGoodsAction extends BaseAction{

	@Autowired
	private VpointsGoodsService goodService;
	@Autowired
    private SysAreaService sysAreaService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;
	@Autowired
	private VpointsGoodsGiftRelationService vpointsGoodsGiftRelationService;
	@Autowired
	private VpointsGroupBuyingActivityService groupBuyingActivityService;
	@Autowired
	private VpointsSeckillActivityService seckillActivityService;
	@Autowired
	private VcodeActivityHotAreaCogService hotAreaCogService;
	/**
	 * 新增商品
	 * @param model
	 * @param session
	 * @param info
	 * @return
	 */
	@RequestMapping("/addGoods")
	public String addGoods(Model model,HttpSession session,VpointsGoodsInfo info){
		SysUserBasis user=getUserBasis(session);
		String userKey=user.getUserKey();
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(info.getCompanyKey())){
//			info.setCompanyKey(companyKey);
//		}
		if(StringUtils.isEmpty(info.getGoodsValue())){
			info.setGoodsValue("0");
		}else {
			if(info.getGoodsValue().equals("10")) {
				info.setDaicaiVpoints("1052");
			}
			if(info.getGoodsValue().equals("20")) {
                info.setDaicaiVpoints("2104");
            }
			if(info.getGoodsValue().equals("30")) {
				info.setDaicaiVpoints("3156");
			}
			if(info.getGoodsValue().equals("50")) {
				info.setDaicaiVpoints("5159");
			}
		}
		info.fillFields(userKey);
		info.setGoodsId(UUIDTools.getInstance().getUUID());

		String errMsg = "添加失败";
		try {
			info.setGoodsRemains(info.getGoodsNum());
			info.setGoodsContent(replaceBlank(info.getGoodsContent()));
			if(StringUtils.isEmpty(info.getGoodsValue())){
				info.setGoodsValue("0");
			}
			//如果不展示销量 则展示销量类型与展示销量件数都为null
			if(info.getIsShowSales().equals("1")){
				info.setShowSalesBase(null);
				info.setShowSalesType(null);
			}
			if(null == info.getShowSalesType() || info.getShowSalesType().equals("0")){
				info.setShowSalesBase(null);
			}
			if(null != info.getPromotionSkuKeyAry() && info.getPromotionSkuKeyAry() != ""){
				String[] skuKeys = info.getPromotionSkuKeyAry().split(",");
				List<String> skuKeysAry = new ArrayList<>(Arrays.asList(skuKeys));
				HashSet keySet = new HashSet(skuKeysAry);
				skuKeysAry.clear();
				skuKeysAry.addAll(keySet);
				VpointsGoodsGiftRelation giftRelation = new VpointsGoodsGiftRelation();
				for (String goodsId : skuKeysAry) {
					giftRelation.setRelationId(UUID.randomUUID().toString());
					giftRelation.setGiftId(info.getGoodsId());
					giftRelation.setGoodsId(goodsId);
					vpointsGoodsGiftRelationService.addGoodsGift(giftRelation);
				}
			}
			goodService.addGoods(info);
			errMsg = "添加成功";
			// 清除尊享卡相关缓存
			goodService.removeExchangeCardCache();
		} catch (BusinessException e) {
		    errMsg = e.getMessage();
		} catch (Exception e) {
			errMsg = "添加失败";
			log.error(e.getMessage(), e);
		}
		model.addAttribute("errMsg", errMsg);
		return "forward:/vpointsGoods/getGoodsList.do";
	}
	/**
	 * 新增跳转
	 * @return
	 */
	@RequestMapping("/goodsAdd")
	public String goodsAdd(Model model,HttpSession session) throws Exception {
		List<VpointsCategoryType> firstList=goodService.getFirstCategoryList();
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(companyKey)){
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
		model.addAttribute("currentUser", user);
        
        // 当前用户可查看品牌信息
        List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", user.getUserName());
        
        // 获取项目地区配置
        List<String> areaCodeList = new ArrayList<>();
        List<SysAreaM> areaLst = new ArrayList<>();
        String areaCode = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
				DatadicKey.filterCompanyInfo.PROJECT_AREA);
        if (!StringUtils.isBlank(areaCode)) {
            String[] areaCodeAry = areaCode.split(",");
            areaCodeList = new ArrayList<>();
            for (String item : areaCodeAry) {
                areaCodeList.add(item + "0000");
            }
        }   
        
        // 返回配置的省级区域
        if(areaCodeList.size() > 0) {
        	areaLst = sysAreaService.queryAllByAreaCode(areaCodeList);
        }else {
        	areaLst = sysAreaService.queryAllList();
        }
        
        if(CollectionUtils.isNotEmpty(areaLst)){
        	JSONObject json = null;
        	List<JSONObject> areaList = new ArrayList<>();
        	for (SysAreaM item : areaLst) {
        		json = new JSONObject();
        		json.put("id", item.getAreaCode());
        		json.put("pId", item.getParentCode());
        		json.put("name", item.getAreaName());
        		areaList.add(json);
			}
        	model.addAttribute("areaJson", JSON.toJSONString(areaList));
        }
        
		// 获取该区域下的热区
		List<VcodeActivityHotAreaCog> hotAreaList = null;
		hotAreaList = hotAreaCogService.findHotAreaListByAreaCode("000000");
		model.addAttribute("hotAreaList", hotAreaList);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        model.addAttribute("firstList", firstList);
        model.addAttribute("brandLst", brandLst);
		model.addAttribute("exchangeCardTypeMap", vpointsCogService.queryBigPrizeForZunXiang());
		return "vpointsGoods/addGoods";
	}
	/**
	 * 通过兑换渠道获取对应商品
	 * @param channelType
	 * @return
	 */
	@RequestMapping("/getGoodsAsGift")
	@ResponseBody
	public String getGoodsByExchangeChannel(String channelType){
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("exchangeChannel",channelType);
			map.put("operateFlag","0");
			List<VpointsGoodsInfo> goodsList  = goodService.findByGoodsAsGift(map);
			return JSON.toJSONString(goodsList);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"1\",\"msg\":\"操作异常\"}";
		}
	}
	/**
	 * 删除商品
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/delGoods")
	public String delGoods(String goodsId,Model model){
		String result = "deleteSuccess";
		try {
			result=goodService.delGoods(goodsId);
			vpointsGoodsGiftRelationService.deleteByGiftKeys(goodsId);
			// 删除缓存
			CacheUtilNew.removeByKey(CacheUtilNew.cacheKey
					.shoppingMallKey.KEY_GOODS_COG + Constant.DBTSPLIT + goodsId);
			// 清除尊享卡相关缓存
			goodService.removeExchangeCardCache();
		} catch (Exception e) {
			result = "deleteFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/vpointsGoods/getGoodsList.do";
	}
	/**
	 * 查询商品列表
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getGoodsList")
	public String getGoodsList(Model model, String pageParam,String queryParam,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
		VpointsGoodsInfo bean=new VpointsGoodsInfo(queryParam);
//		if(StringUtils.isNotEmpty(companyKey)&&StringUtils.isEmpty(bean.getCompanyKey())){
//			bean.setCompanyKey(companyKey);
//		}else{
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
		int countAll=0;
		List<VpointsGoodsInfo> goodList=null;
		try {
			goodList = goodService.getGoodsList(info,bean);
			countAll = goodService.getGoodsCount(info, bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		List<VpointsCategoryType> firstList=goodService.getFirstCategoryList();
		if(!StringUtils.isEmpty(bean.getCategoryParent())){
			List<VpointsCategoryType> secondList=goodService.getCategoryByParent(bean.getCategoryParent());
			model.addAttribute("secondList", secondList);
		}
        List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0");
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
		model.addAttribute("firstList", firstList);
		model.addAttribute("goodsList", goodList);
		model.addAttribute("brandLst", brandLst);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
		model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
		return "vpointsGoods/showGoodsList";
	}
	/**
	 * 详情
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @return
	 */
	@RequestMapping("/getGoodsDetail")
	public String getGoodsDetail(Model model,String goodsId,HttpSession session) throws Exception {
        SysUserBasis user=getUserBasis(session);
		PageOrderInfo info = new PageOrderInfo(null);
		VpointsGoodsInfo bean=new VpointsGoodsInfo();
		bean.setGoodsId(goodsId);
		try {
			bean = goodService.getGoodsList(info,bean).get(0);
			if(DateUtil.beforeNow(bean.getGoodsStartTime())){
				bean.setIsUpdate("1");
			}
			bean.setGoodsPayMoney(bean.getGoodsPay() / 100D);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		List<VpointsCategoryType> firstList=goodService.getFirstCategoryList();
		if(!StringUtils.isEmpty(bean.getCategoryParent())){
			List<VpointsCategoryType> secondList=goodService.getCategoryByParent(bean.getCategoryParent());
			model.addAttribute("secondList", secondList);
		}
		if(bean.getExchangeType().equals("2")){
			List<VpointsCouponBatch> batchList=goodService.getBatchList();
			model.addAttribute("batchList", batchList);
		}
//					SysUserBasis user=getUserBasis(session);
//					String companyKey=user.getCompanyKey();
//					if(StringUtils.isEmpty(companyKey)){
//						List<CompanyInfo> companyList=goodService.getCompanyList();
//						model.addAttribute("companyList", companyList);
//					}
		model.addAttribute("currentUser", user);

        // 当前用户可查看品牌信息
        List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", user.getUserName());
        
        // 获取项目地区配置
        List<String> areaCodeList = new ArrayList<>();
        List<SysAreaM> areaLst = new ArrayList<>();
		String areaCode = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
				DatadicKey.filterCompanyInfo.PROJECT_AREA);
        if (!StringUtils.isBlank(areaCode)) {
            String[] areaCodeAry = areaCode.split(",");
            areaCodeList = new ArrayList<>();
            for (String item : areaCodeAry) {
                areaCodeList.add(item + "0000");
            }
        }   
        
        // 返回配置的省级区域
        if(areaCodeList.size() > 0) {
        	areaLst = sysAreaService.queryAllByAreaCode(areaCodeList);
        }else {
        	areaLst = sysAreaService.queryAllList();
        }
        
        if(CollectionUtils.isNotEmpty(areaLst)){
        	// 山东省-济南市-平阴县;山东省-青岛市；河南省；
        	String shipmentsArea = StringUtils.defaultIfBlank(bean.getShipmentsArea(),"");
        	String[] areaGroup = shipmentsArea.split(";");
        	JSONObject json = null;
        	List<JSONObject> areaList = new ArrayList<>();
        	for (SysAreaM item : areaLst) {
        		json = new JSONObject();
        		json.put("id", item.getAreaCode());
        		json.put("pId", item.getParentCode());
        		json.put("name", item.getAreaName());
        		if(areaGroup.length > 0 && shipmentsArea.contains(item.getAreaName())){
        			String dbArea = sysAreaService.getAreaNameByCode(item.getAreaCode()).replaceAll(" ", "");
        			for (String areaItem : areaGroup) {
						if(areaItem.startsWith(dbArea)){
							json.put("checked", true);
						}
					}
        		}
        		areaList.add(json);
			}
			model.addAttribute("areaJson", JSON.toJSONString(areaList));
		}
		
		List<String> relation_goods = new ArrayList<>();
		List<VpointsGoodsGiftRelation> goodsKeys = vpointsGoodsGiftRelationService.findGoodsByGiftId(bean.getGoodsId());
		if(null != goodsKeys || !goodsKeys.isEmpty()){
			for(VpointsGoodsGiftRelation key : goodsKeys){
				relation_goods.add(key.getGoodsId());
			}
		}
		Map<String,Object> searchmap = new HashMap<>();
		searchmap.put("exchangeChannel",bean.getExchangeChannel());
		searchmap.put("operateFlag","1");
		searchmap.put("goodsId",bean.getGoodsId());
		List<VpointsGoodsInfo> goodsList = goodService.findByGoodsAsGift(searchmap);
		//礼品卡与半价活动换购活动冲突计算（怕该回去 先注释掉）
//		List<VpointsGoodsHalfPriceActivityCogVO> halfPriceActivityCogVOS = halfPriceActivityCogService.getCurrentActivity();
//		List<VpointsGoodsExchangeActivityCogEntity> exchangeActivityCogEntities = exchangeActivityCogService.getCurrentActivity();
//		List<String> halfGoodsId = halfPriceActivityCogVOS.stream().map(VpointsGoodsHalfPriceActivityCogVO::getGoodsId).collect(Collectors.toList());
//		List<String> exchangeGoodsId = exchangeActivityCogEntities.stream().map(VpointsGoodsExchangeActivityCogEntity::getGoodsId).collect(Collectors.toList());
//
//		if (halfGoodsId.contains(bean.getGoodsId()) || exchangeGoodsId.contains(bean.getGoodsId())) {
//			model.addAttribute("giftCardType", false);
//		}
		// 获取该区域下的热区
		List<VcodeActivityHotAreaCog> hotAreaList = null;
		hotAreaList = hotAreaCogService.findHotAreaListByAreaCode("000000");
		model.addAttribute("hotAreaList", hotAreaList);
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
		model.addAttribute("firstList", firstList);
		model.addAttribute("brandLst", brandLst);
		model.addAttribute("salesDetail", bean);
		model.addAttribute("relation_goods", relation_goods);
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("exchangeCardTypeMap", vpointsCogService.queryBigPrizeForZunXiang());
		return "vpointsGoods/editGoods";
	}
	/**
	 * 更新商品
	 * @param model
	 * @param session
	 * @param info
	 * @return
	 */
	@RequestMapping("/updateGoods")
	public String updateGoods(Model model,HttpSession session,VpointsGoodsInfo info){
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
//		if(StringUtils.isEmpty(info.getCompanyKey())){
//			info.setCompanyKey(companyKey);
//		}
		info.setUpdateTime(DateUtil.getDateTime());
		String errMsg="修改失败";
		try {
			if(StringUtils.isEmpty(info.getGoodsValue())){
				info.setGoodsValue("0");
			}else {
				if(info.getGoodsValue().equals("10")) {
					info.setDaicaiVpoints("1052");
				}
				if(info.getGoodsValue().equals("20")) {
	                info.setDaicaiVpoints("2104");
	            }
				if(info.getGoodsValue().equals("30")) {
					info.setDaicaiVpoints("3156");
				}
				if(info.getGoodsValue().equals("50")) {
					info.setDaicaiVpoints("5159");
				}
			}
			if(null != info.getPromotionSkuKeyAry() && info.getPromotionSkuKeyAry() != ""){
				VpointsGoodsGiftRelation giftRelation = new VpointsGoodsGiftRelation();
				vpointsGoodsGiftRelationService.deleteByGiftKeys(info.getGoodsId());
				if(!info.getPromotionSkuKeyAry().contains(",")){
					giftRelation.setRelationId(UUID.randomUUID().toString());
					giftRelation.setGiftId(info.getGoodsId());
					giftRelation.setGoodsId(info.getPromotionSkuKeyAry());
					vpointsGoodsGiftRelationService.addGoodsGift(giftRelation);
				}else{
					String[] skuKeys = info.getPromotionSkuKeyAry().split(",");
					List<String> skuKeysAry = new ArrayList<>(Arrays.asList(skuKeys));
					HashSet keySet = new HashSet(skuKeysAry);
					skuKeysAry.clear();
					skuKeysAry.addAll(keySet);
					for (String key : skuKeysAry){
						giftRelation.setRelationId(UUID.randomUUID().toString());
						giftRelation.setGiftId(info.getGoodsId());
						giftRelation.setGoodsId(key);
						vpointsGoodsGiftRelationService.addGoodsGift(giftRelation);
					}

				}
			}
			info.setGoodsContent(replaceBlank(info.getGoodsContent()));
			long difference=info.getGoodsNum()-info.getBeforeCounts();//计算商品个数与剩余之间的关系
			info.setGoodsRemains(difference);
			//如果不展示销量 则展示销量类型与展示销量件数都为null
			if(info.getIsShowSales().equals("1")){
				info.setShowSalesBase(null);
				info.setShowSalesType(null);
			}
			if(null == info.getShowSalesType() || info.getShowSalesType().equals("0")){
				info.setShowSalesBase(null);
			}
			goodService.updateGoods(info);
			// 删除缓存
			CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.shoppingMallKey
						.KEY_GOODS_COG + Constant.DBTSPLIT + info.getGoodsId());
			// 清除尊享卡相关缓存
			goodService.removeExchangeCardCache();
			errMsg = "修改成功";
		} catch (BusinessException e) {
		    errMsg = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		model.addAttribute("errMsg", errMsg);
		return "forward:/vpointsGoods/getGoodsList.do";
	}
	/**
	 * 根据父ID获取商品子分类
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/getCategoryByParent")
	@ResponseBody
	public String getCategoryByParent(String parentId){
		try {
			List<VpointsCategoryType> secondList=goodService.getCategoryByParent(parentId);
			return JSON.toJSONString(secondList);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"1\",\"msg\":\"操作异常\"}";
		}
	}
	/**
	 * 获取电子券批次
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/getBatchList")
	@ResponseBody
	public String getBatchList(HttpSession session){
		SysUserBasis user=getUserBasis(session);
//		if(StringUtils.isEmpty(companyKey)){
//			companyKey=user.getCompanyKey();
//		}
		try {
			List<VpointsCouponBatch> batchList=goodService.getBatchList();
			return JSON.toJSONString(batchList);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"1\",\"msg\":\"操作异常\"}";
		}
	}
	/**
	 * 获取电子券可用数量
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/getUseCount")
	@ResponseBody
	public String getUseCount(String batchKey,String goodsId){
		try {
			if(StringUtils.isEmpty(goodsId)){
				goodsId="";
			}
			int count=goodService.getUseCount(batchKey,goodsId);
			return count+"";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"1\",\"msg\":\"操作异常\"}";
		}
	}
	
	@RequestMapping("/queryByShipmentProvince")
	@ResponseBody
	public String queryByShipmentsAreaForProvince(String province, String goodsId) {
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    try {
	        if (StringUtils.isNotBlank(province)) {
	            resultMap.put("goodsInfoLst", goodService.queryByShipmentProvince(province, goodsId));
	        }
            
        } catch (Exception e) {
            log.error(e);
            resultMap.put("msg", "查询异常");
        }
	    return JSON.toJSONString(resultMap);
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
		StringBuilder result= new StringBuilder();
		if(files!=null&&files.size()>0){
			for (int i = 0; i < files.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				try {

                    String fileName = files.get(i).getOriginalFilename();
                    if(org.apache.commons.lang3.StringUtils.isEmpty(fileName)) {
                        return "error";
                    }

                    //判断后缀
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if("png".equals(suffix) || "bmp".equals(suffix) || "jpeg".equals(suffix) || "jpg".equals(suffix)) {
                        String type = FileTypeUtil.getType(files.get(i).getInputStream());
                        if("png".equals(type) || "bmp".equals(type) || "jpeg".equals(type) || "jpg".equals(type)) {
                            map=ImgUploadUtil.uploadFile(files.get(i));
                        } else {
                            map.put("SUCESS", "uploadFail");
                            map.put("data", "Image format error");
                        }
                    } else {
                        map.put("SUCESS", "uploadFail");
                        map.put("data", "Image format error");
                    }

					Thread.sleep(10);//等待10毫秒，不等待会出现两个图片同时保存，id相同的bug
				} catch (IOException  e) {
					e.printStackTrace();
					return "error";
				}catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if(i==files.size()-1){
					result.append(map.get("data").toString());
				}else{
					result.append(map.get("data").toString()).append(",");
				}
			}
		}

		return result.toString();
	}
	/**
	 * 处理商品详情，过滤回车换行
	 * @param str
	 * @return
	 */
	private static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * 依据品牌ID获取商品信息
	 */
	@ResponseBody
	@RequestMapping("/queryGoodsByBrandId")
	public String queryGoodsByBrandId(String brandId){
	    Map<String, Object> resultMap = new HashMap<>();
	    try {
	        List<VpointsGoodsInfo> goodsLst = goodService.queryGoodsByBrandId(brandId);
	        resultMap.put("goodsAry", goodsLst);
	        resultMap.put("errMsg", "查询成功");
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        resultMap.put("errMsg", "查询失败");
	    }
	    return JSON.toJSONString(resultMap);
	}
	
    /**
     * 校验商品客户自定义编号是否已存在
     */
	@ResponseBody
    @RequestMapping("/validGoodsClientNo")
    public String validGoodsClientNo(String goodsId, String goodsClientNo){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            goodService.validGoodsClientNo(goodsId, goodsClientNo);
            resultMap.put("errMsg", "校验成功");
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }
    /**
     * 校验商品是否存在 秒杀或者拼团活动
     */
	@ResponseBody
    @RequestMapping("/checkRunActivity")
    public String checkRunActivity(String goodsId){
		Map<String, Object> resultMap = new HashMap<>();
		VpointsGroupBuyingActivityCog queryBean = new VpointsGroupBuyingActivityCog();
		queryBean.setTabsFlag("2");
		queryBean.setCurrDate(DateUtil.getDate());
		queryBean.setGoodsId(goodsId);
		VpointsSeckillActivityCog seckillActivityCog = new VpointsSeckillActivityCog();
		seckillActivityCog.setTabsFlag("2");
		seckillActivityCog.setCurrDate(DateUtil.getDate());
		seckillActivityCog.setGoodsId(goodsId);
		int groupBuyActivityCount = groupBuyingActivityService.countVcodeActivityList(queryBean);
		int seckillActivityCount = seckillActivityService.countVcodeActivityList(seckillActivityCog);
		if(groupBuyActivityCount>0){
			resultMap.put("status", 1);
			resultMap.put("msg", "您配置的商品正在参加拼团活动，不可配置此活动");
		}else if(seckillActivityCount > 0){
			resultMap.put("status", 1);
			resultMap.put("msg", "您配置的商品正在参加秒杀活动，不可配置此活动");
		}else{
			resultMap.put("status", 200);
		}
		return JSON.toJSONString(resultMap);
    }

	/**
	 * 获取商城商品
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getShopGoods")
	@ResponseBody
	public BaseResult<List<VpointsGoodsInfo>> getShopGoods(VpointsGoodsInfo vpointsGoodsInfo) {

		BaseResult<List<VpointsGoodsInfo>> result = null;
		try {
			String exhcangeChannel = "";
			if("hebei".equals(DbContextHolder.getDBType())){
				exhcangeChannel = Constant.exchangeChannel.CHANNEL_6;
			}else{
				exhcangeChannel = Constant.exchangeChannel.CHANNEL_1;
			}
			vpointsGoodsInfo.setExchangeChannel(exhcangeChannel);
			result = goodService.getShopGoods(vpointsGoodsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result != null) {
			result.setReplyTime(System.currentTimeMillis());
			return result;
		}
		return result;
	}
}
