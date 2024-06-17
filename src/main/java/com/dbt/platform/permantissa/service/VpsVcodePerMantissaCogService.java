package com.dbt.platform.permantissa.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.util.VcodeActivityMoneyExcel;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaCog;
import com.dbt.platform.permantissa.dao.IVpsVcodePerMantissaCogDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢尾数中奖规则Service
 *
 */
@Service
public class VpsVcodePerMantissaCogService extends BaseService<VpsVcodePerMantissaCog> {
    
	@Autowired
	private IVpsVcodePerMantissaCogDao perMantissaCogDao;
	@Autowired
	private VcodeActivityRebateRuleCogService rebateRuleCogService;

	/**
	 * 获取列表List
	 */
	public List<VpsVcodePerMantissaCog> queryForList(
	        VpsVcodePerMantissaCog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		List<VpsVcodePerMantissaCog> list = perMantissaCogDao.queryForList(map);
		if(CollectionUtils.isNotEmpty(list)){
		    // 时间类型
	        String timeType = "";
			for (VpsVcodePerMantissaCog item : list) {
			    if("1".equals(item.getRestrictTimeType())){
			        timeType = Constant.DBTSPLIT + DateUtil.getDate();
			    }
				item.setRuleTotalBottle(Integer.parseInt(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "bottle"), "0")));
				item.setRuleTotalMoney(Double.parseDouble(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "money"), "0.00")));
				item.setRuleTotalVpoints(Integer.parseInt(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "vpoint"), "0")));
			}
		}
		return list;
	}

	/**
	 * 获取列表Count
	 */
	public int queryForCount(VpsVcodePerMantissaCog queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return perMantissaCogDao.queryForCount(map);
	}

	/**
	 * 根据主键查询信息
	 */
	public VpsVcodePerMantissaCog findById(String infoKey) {
		return perMantissaCogDao.findById(infoKey);
	}

	/**
	 * 添加
	 */
	public void addPerMantissaCog(VpsVcodePerMantissaCog perMantissaCog,
	                    SysUserBasis userBasis, MultipartFile prizeFile) throws Exception{
	    
	    // 校验SKU是否存在活动冲突
	    validForPerMantissaCog(perMantissaCog);
	    
		// 初始化规则
	    String perMantissaKey = UUID.randomUUID().toString();
	    perMantissaCog.setInfoKey(perMantissaKey);
	    perMantissaCog.setCompanyKey(userBasis.getCompanyKey());
		perMantissaCog.setPerMantissaNo(getBussionNo("vpsVcodePerMantissaCog", "permantissa_no", Constant.OrderNoType.type_FW));
		perMantissaCog.fillFields(userBasis.getUserKey());
		perMantissaCogDao.create(perMantissaCog);
		
		// 解析excel并入库
		List<VcodeActivityMoneyImport> excelLst = this.initVpointsCog(prizeFile);
		if (CollectionUtils.isEmpty(excelLst)) throw new BusinessException("请配置有效的奖项配置项");
		rebateRuleCogService.addActivityVpointsItem(excelLst,
		        perMantissaKey, perMantissaKey, "perMantissa", userBasis.getUserKey(), true);
		
		logService.saveLog("vpsVcodePerMantissaCog", Constant.OPERATION_LOG_TYPE.TYPE_1,
		        JSON.toJSONString(perMantissaCog), "添加逢尾数规则:" + perMantissaCog.getPerMantissaName());
	}

	/**
	 * 修改
	 * @throws Exception 
	 */
	public void editPerMantissaCog(VpsVcodePerMantissaCog perMantissaCog, 
	                    SysUserBasis userBasis, MultipartFile prizeFile) throws Exception {
	    
        // 校验SKU是否存在活动冲突
	    validForPerMantissaCog(perMantissaCog);
		
	    // 初始化规则
	    perMantissaCog.fillUpdateFields(userBasis.getUserKey());
		perMantissaCogDao.update(perMantissaCog);

        // 解析excel并入库
		if (prizeFile != null && !prizeFile.isEmpty()) {
		    List<VcodeActivityMoneyImport> excelLst = this.initVpointsCog(prizeFile);
		    if (CollectionUtils.isNotEmpty(excelLst)) {
		        rebateRuleCogService.addActivityVpointsItem(excelLst,
		                perMantissaCog.getInfoKey(), perMantissaCog.getInfoKey(), "perMantissa", userBasis.getUserKey(), true);
		    }        
		}
		logService.saveLog("vpsVcodePerMantissaCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
		        JSON.toJSONString(perMantissaCog), "修改逢尾数规则:" + perMantissaCog.getPerMantissaName());
	}
	
	/**
	 * 解析并校验奖项配置项
	 */
	public List<VcodeActivityMoneyImport> initVpointsCog(MultipartFile prizeFile) throws Exception {
	    
	    if (prizeFile == null || prizeFile.isEmpty()) throw new BusinessException("请选择奖项配置文件");
	    
	    // 解析奖项配置项
	    String[] entities = {"prizeType", "randomType", "vpoints", "money", "amounts", "rangeVal", "scanNum"};
	    List<VcodeActivityMoneyImport> excelLst = VcodeActivityMoneyExcel.importExcel(prizeFile, entities);
	    
        int row = 2;
        String rowTips = "";
        Double minVal, maxVal;
        String scanNumKey = null;
        Map<String, Long> scanNumTotalMap = new HashMap<>();
        for (VcodeActivityMoneyImport item : excelLst) {
            row++;
            rowTips = "导入失败：第" + row + "行, ";
            if (StringUtils.isBlank(item.getPrizeType())) throw new BusinessException(rowTips + "缺少奖项类型配置");
            if (StringUtils.isBlank(item.getRandomType())) throw new BusinessException(rowTips + "缺少随机类型配置");
            if (item.getAmounts() == null || item.getAmounts() < 0) throw new BusinessException(rowTips + "缺少奖项总个数配置");
            if (item.getRangeVal() == null || item.getRangeVal() < 0) throw new BusinessException(rowTips + "缺少概率值配置");
            if (!Pattern.matches("(\\d+-\\d+)?", StringUtils.defaultIfBlank(item.getScanNum(), ""))) throw new BusinessException(rowTips + "阶梯中奖最多只能配置一组n-n");
            if (Constant.PrizeType.status_0.equals(item.getPrizeType())) {
                item.setVpoints(null);
                if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");
                
            } else if (Constant.PrizeType.status_1.equals(item.getPrizeType())) {
                item.setMoney(null);
                if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");
                
            } else if (Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");
                if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");
            }
            
            // 计算总概率值
            scanNumKey = StringUtils.defaultIfBlank(item.getScanNum(), "empty");
            if (scanNumTotalMap.containsKey(scanNumKey)) {
                scanNumTotalMap.put(scanNumKey, scanNumTotalMap.get(scanNumKey) + item.getRangeVal());
            } else {
                scanNumTotalMap.put(scanNumKey, item.getRangeVal());
            }
            
            // 随机类型
            if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                // 积分
                if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0-0");
                if (!item.getVpoints().contains("-")) {
                    throw new BusinessException(rowTips + "积分配置格式错误");
                }
                minVal = Double.valueOf(item.getVpoints().split("-")[0]);
                maxVal = Double.valueOf(item.getVpoints().split("-")[1]);
                if (minVal > maxVal) {
                    throw new BusinessException(rowTips + "积分配置错误，前值应小于等于后值");  
                }
                item.setMinVpoints(minVal.intValue());
                item.setMaxVpoints(maxVal.intValue());
                item.setUnitVpoints((minVal + maxVal)/2);

                // 金额
                if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00-0.00");
                if (!item.getMoney().contains("-")) {
                    throw new BusinessException(rowTips + "金额配置格式错误");
                }
                minVal = Double.valueOf(item.getMoney().split("-")[0]);
                maxVal = Double.valueOf(item.getMoney().split("-")[1]);
                if (minVal > maxVal) {
                    throw new BusinessException(rowTips + "金额配置错误，前值应小于等于后值");  
                }
                item.setMinMoney(minVal);
                item.setMaxMoney(maxVal);
                item.setUnitMoney((minVal + maxVal)/2);
            } else if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
                // 积分
                if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0");
                if (item.getVpoints().contains("-")) {
                    throw new BusinessException(rowTips + "积分配置格式错误");
                }
                item.setMinVpoints(Integer.valueOf(item.getVpoints()));
                item.setMaxVpoints(Integer.valueOf(item.getVpoints()));
                item.setUnitVpoints(Double.valueOf(item.getVpoints()));

                // 金额
                if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00");
                if (item.getMoney().contains("-")) {
                    throw new BusinessException(rowTips + "金额配置格式错误");
                }
                item.setMinMoney(Double.valueOf(item.getMoney()));
                item.setMaxMoney(Double.valueOf(item.getMoney()));
                item.setUnitMoney(Double.valueOf(item.getMoney()));
            }
        }
            
        // 计算各项的占比
        for (VcodeActivityMoneyImport item : excelLst) {
            scanNumKey = StringUtils.defaultIfBlank(item.getScanNum(), "empty");
            item.setScanType(Constant.ScanType.type_1);
            item.setPrizePercent(MathUtil.round(item.getRangeVal() * 100D /scanNumTotalMap.get(scanNumKey), 4));
        }
	    
	    return excelLst;
	}
	
	/**
	 * 删除记录
	 */
	public void deletePerMantissaCog(String infoKey, String updateUser) {
	    VpsVcodePerMantissaCog perMantissaCog = findById(infoKey);
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", infoKey);
	    map.put("optUserKey", updateUser);
	    perMantissaCogDao.deleteById(map);
	    
	    map.put("perMantissaCog", perMantissaCog);
	    logService.saveLog("vpsVcodePerMantissaCog", Constant.OPERATION_LOG_TYPE.TYPE_3, 
	            JSON.toJSONString(map), "删除逢尾数规则:" + perMantissaCog.getPerMantissaName());
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vpsVcodePerMantissaCog", "info_key", infoKey, "permantissa_name", bussionName);
	}
	
	/**
	 * 校验活动有效性
	 * @throws BusinessException 
	 */
	private void validForPerMantissaCog(VpsVcodePerMantissaCog perMantissaCog) throws BusinessException {
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", perMantissaCog.getInfoKey());
	    map.put("startDate", perMantissaCog.getStartDate());
	    map.put("endDate", perMantissaCog.getEndDate());
	    map.put("skuLst", Arrays.asList(perMantissaCog.getSkuKey().split(",")));
	    
	    List<VpsVcodePerMantissaCog> perMantissaCogLst = perMantissaCogDao.queryByDateForSku(map);
	    if (CollectionUtils.isNotEmpty(perMantissaCogLst)) {
	        throw new BusinessException("当前规则与" + perMantissaCogLst.get(0).getPerMantissaName() + "有SKU冲突！");
	    }
	}
}
