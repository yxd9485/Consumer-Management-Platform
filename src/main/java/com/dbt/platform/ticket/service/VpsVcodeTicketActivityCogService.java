package com.dbt.platform.ticket.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbt.datasource.util.DbContextHolder;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.ticket.bean.VpsSysTicketCategory;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.bean.VpsVcodeTicketActivityCog;
import com.dbt.platform.ticket.bean.VpsVcodeTicketInfo;
import com.dbt.platform.ticket.dao.IVpsSysTicketInfoDao;
import com.dbt.platform.ticket.dao.IVpsVcodeTicketActivityCogDao;

/**
 * 优惠券活动Service
 */
@Service
public class VpsVcodeTicketActivityCogService extends BaseService<VpsVcodeTicketActivityCog> {

	@Autowired
	private VpsSysTicketCategoryService sysTicketCategoryService;
	@Autowired
	private IVpsSysTicketInfoDao sysTicketInfoDao;
	@Autowired
	private VpsVcodeTicketInfoService ticketInfoService;
	@Autowired
	private VpsVcodeTicketLibService ticketLibService;
	@Autowired
	private IVpsVcodeTicketActivityCogDao ticketActivityCogDao;

	/**
	 * 获取优惠券活动
	 *
	 * @param vcodeActivityKey
	 * @return
	 */
	public VpsVcodeTicketActivityCog loadTicketActivityByKey(String vcodeActivityKey) {
		return ticketActivityCogDao.findById(vcodeActivityKey);
	}

	/**
	 * 优惠券活动列表
	 *
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<VpsVcodeTicketActivityCog> findTicketActivityList(
	        VpsVcodeTicketActivityCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		List<VpsVcodeTicketActivityCog> activityLst = ticketActivityCogDao.loadTicketActivityList(queryMap);
		if (activityLst != null) {
		    for (VpsVcodeTicketActivityCog item : activityLst) {
		        item.setStartDate(DateUtil.removeEndPointZero(item.getStartDate()));
		        item.setEndDate(DateUtil.removeEndPointZero(item.getEndDate()));
            }
		}
		return activityLst;
	}

	/**
	 * 优惠券活动列表条数
	 *
	 * @param queryBean
	 * @return
	 */
	public int countTicketActivityList(VpsVcodeTicketActivityCog queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return ticketActivityCogDao.countTicketActivityList(queryMap);
	}

	/**
	 * 创建优惠券活动
	 *
	 * @param activityCog
	 * @param currentUserKey
	 * @throws Exception
	 */
	public void writeTicketActivityCog(VpsVcodeTicketActivityCog
	                activityCog, String currentUserKey, Model model) throws Exception {

	    // 检验是否重复
		if("1".equals(checkBussionName("vcodeTicketActivity",
				"ACTIVITY_KEY", null, "ACTIVITY_NAME", activityCog.getActivityName()))){
			throw new BusinessException("活动名称已存在");
		}

		// 初始活动属性
		String infoKey = callUUID();
		activityCog.setActivityNo(getBussionNo("vcodeTicketActivity", "activity_no", Constant.OrderNoType.type_TK));
		activityCog.setActivityKey(infoKey);
		activityCog.fillFields(currentUserKey);

		// 生成优惠券库名称
		/*String libName = "vps_vcode_ticket_lib";
		libName += Constant.DBTSPLIT + new DateTime().toString("yyyyMMddHHmmss");
		activityCog.setLibName(libName);*/

		// 创建券码表
		/*ticketLibService.createTicketTable(libName);*/

		// 保存活动
		ticketActivityCogDao.create(activityCog);
        model.addAttribute("errMsg", "创建成功");
        logService.saveLog("vcodeTicketActivity", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(activityCog), "创建" + activityCog.getActivityName());
	}

	/**
	 * 更新优惠券活动
	 *
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException
	 */
	public void updateTicketActivityCog(VpsVcodeTicketActivityCog activityCog,
	                        String currentUserKey, Model model) throws Exception {
		// 检验是否重复
		if("1".equals(checkBussionName("vcodeTicketActivity",
				"ACTIVITY_KEY", activityCog.getActivityKey(), "ACTIVITY_NAME", activityCog.getActivityName()))){
			throw new BusinessException("活动名称已存在");
		}

        // 保存活动
        activityCog.setUpdateTime(DateUtil.getDateTime());
        activityCog.setUpdateUser(currentUserKey);
        ticketActivityCogDao.update(activityCog);

        // 删除包含当前类型的活动缓存
        VpsSysTicketCategory ticketCategory =
        		sysTicketCategoryService.findById(activityCog.getCategoryKey());
        if(null != ticketCategory){
        	RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.ticket
        			.KEY_VCODE_TICKET_ACTIVITY_LIST + Constant.DBTSPLIT + ticketCategory.getCategoryType());
        }

        model.addAttribute("errMsg", "更新成功");
        logService.saveLog("vcodeTicketActivity", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activityCog), "更新" + activityCog.getActivityName());
	}


	public VpsVcodeTicketActivityCog findById(String vcodeActivityKey) {
		return ticketActivityCogDao.findById(vcodeActivityKey);
	}

    /**
     * 新增优惠券
     * @param ticketType
     * @param ticketCodeList
     * @param fileName
     * @param map
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public Map<String, String> addTicketInfo(VpsVcodeTicketInfo ticketInfo, MultipartFile batchFile) throws Exception {
		Map<String, String> resurtMap = new HashMap<String, String>();

		String ticketNo = ticketInfo.getTicketNo();
		VpsSysTicketInfo sysTicketInfo = sysTicketInfoDao.findByTicketNo(ticketNo);
		if(null == sysTicketInfo){
			resurtMap.put("errMsg", "保存失败：获取不到编号为" + ticketNo + "的优惠券面额");
			return resurtMap;
		}

		VpsVcodeTicketActivityCog ticketActivityCog =
				ticketActivityCogDao.findById(ticketInfo.getActivityKey());
		if(null == ticketActivityCog){
			resurtMap.put("errMsg", "保存失败：获取不到该活动：" + ticketInfo.getActivityKey());
			return resurtMap;
		}

        String ticketKey = null;
        List<Set<String>> list = null;
        Map<String, Object> map = null;
	    String nowTime = DateUtil.getDateTime();
	    String fileName = batchFile.getOriginalFilename();

	    // 连接格式
	    if(Constant.TICKET_TYPE.ticketType_0.equals(sysTicketInfo.getTicketType())
	    		|| Constant.TICKET_TYPE.ticketType_2.equals(sysTicketInfo.getTicketType())){
	    	ticketInfo.setTicketUrl(ticketInfo.getTicketUrl());
	    	ticketInfo.setTicketCount(0L);
	    }
	    // 券码格式
	    else if(Constant.TICKET_TYPE.ticketType_1.equals(sysTicketInfo.getTicketType())){
	    	// 检验是否重复
			if("1".equals(checkBussionName("vcodeTicketInfo", "TICKET_KEY", null, "FILE_NAME", fileName))){
				resurtMap.put("errMsg", "导入失败，请不要重复导入");
	            return resurtMap;
			}

			// 解析券码文件
			map = checkTicketFile(batchFile);
	        if((int)map.get("ticketCount") == 0){
	        	resurtMap.put("errMsg", "导入失败，优惠券文件为空");
	            return resurtMap;
	        }

	    	ticketInfo.setTicketCount(Long.parseLong(map.get("ticketCount")+""));

	    	// 券码List
		    list = (List<Set<String>>)map.get("list");
	    }


	    // 查询当前活动下该优惠券类型对象
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("activityKey", ticketInfo.getActivityKey());
	    paramMap.put("ticketNo", ticketInfo.getTicketNo());
	    VpsVcodeTicketInfo queryTicketInfo = ticketInfoService.findTicketInfoForMap(paramMap);
	    if(null == queryTicketInfo){
	    	// 保存优惠券面额
	    	ticketKey = UUIDTools.getInstance().getUUID();
			ticketInfo.setTicketKey(ticketKey);
			ticketInfo.setFileName(fileName);
	    	ticketInfoService.addTicketInfo(ticketInfo);
	    }else{
	    	ticketKey = queryTicketInfo.getTicketKey();

	    	// 更新优惠券面额对象
	    	paramMap.clear();
	    	paramMap.put("ticketKey", ticketKey);
	    	paramMap.put("ticketUrl", ticketInfo.getTicketUrl());
	    	paramMap.put("ticketCount", ticketInfo.getTicketCount());
	    	paramMap.put("fileName", fileName);
	    	paramMap.put("updateUser", ticketInfo.getUpdateUser());
	    	paramMap.put("updateTime", nowTime);
	    	ticketInfoService.updateTicketInfo(paramMap);
	    }


	    // 保存券码
	    if(Constant.TICKET_TYPE.ticketType_1.equals(sysTicketInfo.getTicketType())){
    		paramMap.clear();
    		paramMap.put("activityKey", ticketInfo.getActivityKey());
    		paramMap.put("ticketKey", ticketKey);
    		paramMap.put("libName", ticketActivityCog.getLibName());
    		paramMap.put("useStatus", "0");
    		paramMap.put("createTime", nowTime);
    		for (Set<String> ticketCodeList : list) {
    			paramMap.put("ticketCodeList", ticketCodeList);
    			ticketLibService.batchWrite(paramMap);
    		}
    		resurtMap.put("errMsg", "导入成功-券码总量：" +
    				map.get("fileCount").toString() + ",成功导入：" + map.get("ticketCount").toString());
	    }else{
	    	resurtMap.put("errMsg", "保存成功");
	    }


	    // 删除包含当前类型的活动缓存
	    RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.ticket
    			.KEY_VCODE_TICKET_ACTIVITY_LIST + Constant.DBTSPLIT + ticketNo.substring(0,1));

	    // 记录日志
	    logService.saveLog("vcodeTicketActivity", Constant.OPERATION_LOG_TYPE.TYPE_1,
	    		JSON.toJSONString(ticketInfo), "导入券码活动：" +ticketInfo.getActivityKey()+ ",文件："+fileName);
	    return resurtMap;
	}

	/**
     * 处理优惠券CSV文件
     * @param batchFile
     * @return
     * @throws Exception
     */
	private Map<String, Object> checkTicketFile(MultipartFile ticketFile){
		BufferedReader reader = null;
		Set<String> ticketCodeList = new HashSet<String>();
		List<Set<String>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		try{
			reader = new BufferedReader(
					 new InputStreamReader(ticketFile.getInputStream(), "GBK"));
			int idx = 0;
			String str = null;
			int fileCount = 0; // 券码总量
			while((str=reader.readLine()) != null){
				fileCount++;
				if(str.length() == 16){
					idx ++;
					ticketCodeList.add(str);
					if(idx % 3000 == 0){
						list.add(ticketCodeList);
						ticketCodeList = new HashSet<String>();
					}
				}else{
					System.out.println(str);
				}
			}

			// 最后不足1000的券码
			if(!CollectionUtils.isEmpty(ticketCodeList)){
				list.add(ticketCodeList);
			}
			map.put("list", list);
			map.put("ticketCount", idx);
			map.put("fileCount", fileCount);
		}catch(Exception e){
			log.error("处理优惠券CSV文件异常："+ e.getMessage());
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					log.error("处理优惠券CSV文件关闭BufferedReader异常："+ e.getMessage());
				}
			}
		}
		return map;
	}



	 /**
     * 新增优惠券(文件入库格式)
     * @param ticketType
     * @param ticketCodeList
     * @param fileName
     * @param map
     * @return
     * @throws Exception
     */
	public Map<String, String> addTicketInfoByTxt(VpsVcodeTicketInfo ticketInfo) throws Exception {
		Map<String, String> resurtMap = new HashMap<String, String>();
		String ticketNo = ticketInfo.getTicketNo();
		VpsSysTicketInfo sysTicketInfo = sysTicketInfoDao.findByTicketNo(ticketNo);
		if(null == sysTicketInfo){
			resurtMap.put("errMsg", "保存失败：获取不到编号为" + ticketNo + "的优惠券面额");
			return resurtMap;
		}

		VpsVcodeTicketActivityCog ticketActivityCog =
				ticketActivityCogDao.findById(ticketInfo.getActivityKey());
		if(null == ticketActivityCog){
			resurtMap.put("errMsg", "保存失败：获取不到该活动：" + ticketInfo.getActivityKey());
			return resurtMap;
		}

		String ticketKey = null;// 面额KEY
		String activityKey = ticketActivityCog.getActivityKey();// 活动KEY
        String nowTime = DateUtil.getDateTime();

        // 查询当前活动下该优惠券类型对象
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("activityKey", ticketInfo.getActivityKey());
	    paramMap.put("ticketNo", ticketInfo.getTicketNo());
	    VpsVcodeTicketInfo queryTicketInfo = ticketInfoService.findTicketInfoForMap(paramMap);
	    if(null == queryTicketInfo){
	    	ticketKey = UUIDTools.getInstance().getUUID();
	    }else{
	    	ticketKey = queryTicketInfo.getTicketKey();
	    }


	    if(null == queryTicketInfo){
	    	// 保存优惠券面额
			ticketInfo.setTicketKey(ticketKey);
	    	ticketInfoService.addTicketInfo(ticketInfo);
	    }else{
	    	// 更新优惠券面额对象
	    	paramMap.clear();
	    	paramMap.put("ticketKey", ticketKey);
	    	paramMap.put("ticketUrl", ticketInfo.getTicketUrl());
	    	paramMap.put("ticketLimit", ticketInfo.getTicketLimit());
	    	paramMap.put("ticketLimitNum", ticketInfo.getTicketLimitNum());
	    	paramMap.put("ticketCount", ticketInfo.getTicketCount());
	    	paramMap.put("updateUser", ticketInfo.getUpdateUser());
	    	paramMap.put("updateTime", nowTime);
	    	ticketInfoService.updateTicketInfo(paramMap);
	    }


	    // 删除包含当前类型的活动缓存
	    RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.ticket
    			.KEY_VCODE_TICKET_ACTIVITY_LIST + Constant.DBTSPLIT + ticketNo.substring(0,1));

	    // 记录日志
	    logService.saveLog("vcodeTicketActivity", Constant.OPERATION_LOG_TYPE.TYPE_1,
	    		JSON.toJSONString(ticketInfo), "导入券码活动：" +ticketInfo.getActivityKey());
		resurtMap.put("errMsg", "保存成功");
	    return resurtMap;
	}


	/**
     * 处理优惠券CSV文件(文件入库格式)
     * @param batchFile
     * @return
     * @throws Exception
     */
	public Map<String, Object> checkTicketFileByTxt(MultipartFile ticketFile, String ticketKey, String nowTime){
		BufferedReader reader = null;
		FileOutputStream fileOut = null;
		String separator = System.getProperty("line.separator");
		String directoryPath = PropertiesUtil.getPropertyValue("ticketFilePath") + DbContextHolder.getDBType() + "/" ;
		File directoryFile = new File(directoryPath);
		if(!directoryFile.exists()){
			directoryFile.mkdirs();
		}
		String filePath = directoryPath + "ticketCodeTxt" + DateUtil.getDateTime(DateUtil.DEFAULT_DATETIME_FORMAT_SHT) + ".txt";
		File textFile = new File(filePath);

		StringBuilder qrcodeStr = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		try{
			reader = new BufferedReader(
					 new InputStreamReader(ticketFile.getInputStream(), "GBK"));
			fileOut = new FileOutputStream(textFile, true);
			String str = null;
			int fileCount = 0;
			int ticketCount = 0; // 券码总量
			while((str=reader.readLine()) != null){
				fileCount++;
				if(str.length() > 0){
					ticketCount++;
					qrcodeStr.append(UUIDTools.getInstance().getUUID()).append("+")
					.append(ticketKey).append("+")
					.append(str).append("+")
					.append("0").append("+")
					.append(nowTime);

					// 队列中的二维码写入文件
					fileOut.write((qrcodeStr.toString() + separator).getBytes());
					// 清空二维码builder
					qrcodeStr.setLength(0);
				}
			}
			map.put("filePath", filePath);
			map.put("fileCount", fileCount);
			map.put("ticketCount", ticketCount);
		}catch(Exception e){
			log.error("处理优惠券CSV文件异常："+ e.getMessage());
		}finally{
			if(fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
					log.error("处理优惠券CSV文件关闭fileOut异常："+ e.getMessage());
				}
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					log.error("处理优惠券CSV文件关闭BufferedReader异常："+ e.getMessage());
				}
			}
		}
		return map;
	}
}
