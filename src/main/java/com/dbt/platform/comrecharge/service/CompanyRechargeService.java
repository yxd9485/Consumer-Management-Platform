package com.dbt.platform.comrecharge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.pagination.PageResult;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.platform.comrecharge.bean.CompanyPrerechargeInfo;
import com.dbt.platform.comrecharge.bean.CompanyRechargeInfo;
import com.dbt.platform.comrecharge.dao.ICompanyPrerechargeDao;
import com.dbt.platform.comrecharge.dao.ICompanyRechargeDao;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 下午1:43:17
 * @description 类说明
 */
@Service
public class CompanyRechargeService extends BaseService<CompanyRechargeInfo> {

	@Autowired
	private ICompanyPrerechargeDao iCompanyPrerechargeDao;
	@Autowired
	private ICompanyRechargeDao iCompanyRechargeDao;
	@Autowired
	private SysDataDicService sysDataDicService;

	/**
	 * 列表
	 *
	 * @param queryInfo
	 * @param pageInfo
	 * @return
	 */
	public List<CompanyPrerechargeInfo> loadPreInfoList(CompanyPrerechargeInfo queryInfo,
			PageOrderInfo pageInfo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("params", pageInfo);
		queryMap.put("query", queryInfo);
		return iCompanyPrerechargeDao.loadPreInfoList(queryMap);
	}

	/**
	 * 条数
	 *
	 * @param queryInfo
	 * @return
	 */
	public int countPreInfoList(CompanyPrerechargeInfo queryInfo) {
		return iCompanyPrerechargeDao.countPreInfoList(queryInfo);
	}

	/**
	 * 发起新充值
	 *
	 * @param rechargeInfo
	 * @param sysUserKey
	 */
	public void createNewRecharge(CompanyPrerechargeInfo rechargeInfo, String sysUserKey) {
	    
	    // 初始化充值记录
	    rechargeInfo.setTradeUser(sysUserKey);
	    rechargeInfo.setRechargeVpoints((long)MathUtil.round(rechargeInfo.getRechargeMoney() * 100,0));
	    rechargeInfo.setRechargeTime(DateUtil.getDateTime());
	    rechargeInfo.setTransStatus("1");
	    
	    if (StringUtils.isBlank(rechargeInfo.getPreKey())) {
	        rechargeInfo.setPreKey(callUUID());
	        iCompanyPrerechargeDao.create(rechargeInfo);
	        logService.saveLog("companyRecharge", Constant.OPERATION_LOG_TYPE.TYPE_1, 
	                    JSON.toJSONString(rechargeInfo), "发起充值:" + rechargeInfo.getRechargeMoney());
	    } else {
	        iCompanyPrerechargeDao.update(rechargeInfo);
            logService.saveLog("companyRecharge", Constant.OPERATION_LOG_TYPE.TYPE_2, 
                        JSON.toJSONString(rechargeInfo), "发起充值:" + rechargeInfo.getRechargeMoney());
	    }
	}

	public CompanyPrerechargeInfo findById(String preKey) {
		CompanyPrerechargeInfo rechargeInfo = iCompanyPrerechargeDao.findById(preKey);
		rechargeInfo.setRechargeTime(DateUtil.trimAfterPointer(rechargeInfo.getRechargeTime()));
		return rechargeInfo;
	}

	/**
	 * 确认充值，没有积分池的创建，有积分池的直接加
	 *
	 * @param preInfo
	 * @param sysUserKey
	 */
	public boolean writeRecharge(CompanyPrerechargeInfo preInfo, String sysUserKey) {
		String status = preInfo.getTransStatus();
		String currentTime = DateUtil.getDateTime();
		String companyKey = preInfo.getCompanyKey();

	    // 确认充值
		if ("2".equals(status)) {

		    // 获取总积分成本
		    SysDataDic sysDataDic = sysDataDicService.getDatadicByCatCodeAndDataid(
		            DatadicKey.ent_stats.CATEGORY_CODE, DatadicKey.ent_stats.TOTAL_VPOINTS);
		    long preTotal = Long.parseLong(sysDataDic.getDataValue());
		    long currTotal = preTotal + preInfo.getRechargeVpoints();

            // 更新总积分成本
            sysDataDic.setDataValue(String.valueOf(currTotal));
            sysDataDicService.updateDataDic(sysDataDic);

			// 写入充值
			CompanyRechargeInfo rechargeInfo = new CompanyRechargeInfo(callUUID(),
					preInfo.getPreKey(), companyKey, preInfo.getContractNum(),
					preInfo.getContractName(), currentTime, preInfo.getTransType(),
					preInfo.getRechargeMoney(), preInfo.getRechargeVpoints(), preTotal, currTotal, sysUserKey);
			iCompanyRechargeDao.create(rechargeInfo);
			logService.saveLog("companyRecharge", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(rechargeInfo), "市场确认:" + preInfo.getRechargeMoney());

			preInfo.setTransStatus("2");

		// 终止
		} else if ("3".equals(status)) {
			preInfo.setTransStatus("3");
			preInfo.setTerminalTime(currentTime);
		}
		// 更新预充值状态
		iCompanyPrerechargeDao.updateStatus(preInfo);
		return true;
	}

	/**
	 * 企业已充值记录查看
	 *
	 * @param companyKey
	 * @param pageParam
	 * @return
	 */
	public String loadCompanyRechargeInfo(String companyKey, String pageParam) {
		String result = "";
		PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("companyKey", companyKey);
		queryMap.put("param", pageInfo);
		List<CompanyRechargeInfo> rechargeList = iCompanyRechargeDao
				.loadRechargeInfoByCompany(queryMap);
		for (CompanyRechargeInfo rechargeInfo : rechargeList) {
			rechargeInfo.setRechargeTime(DateUtil.trimAfterPointer(rechargeInfo.getRechargeTime()));
		}
		int countAll = iCompanyRechargeDao.countRechargeInfoByCompany(companyKey);
		PageResult<CompanyRechargeInfo> pageResult = new PageResult<CompanyRechargeInfo>(
				rechargeList, countAll, pageInfo);
		result = JSON.toJSONString(pageResult);
		return StringUtils.isEmpty(result) ? "" : result;
	}

	/**
	 * 删除充值
	 * @param preKey
	 */
	public void removeRecharge(String preKey) {
		iCompanyPrerechargeDao.deleteById(preKey);
		logService.saveLog("companyRecharge", Constant.OPERATION_LOG_TYPE.TYPE_1, preKey, "删除充值记录：" + preKey);
	}

	public void updateCompanyPrerechargeInfo(CompanyPrerechargeInfo rechargeInfo) {
		iCompanyPrerechargeDao.updateCompanyPrerechargeInfo(rechargeInfo);
	}

    /**
     * 确认充值
     *
     * @return
     */
    public void confirmUpdate(HashMap<String, String> map) {
        iCompanyPrerechargeDao.confirmUpdate(map);
    }

    /**
     * 统计数据
     *
     * @return
     */
    public int loadPreInfoListCount(CompanyPrerechargeInfo queryInfo,
                                    PageOrderInfo pageInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("params", pageInfo);
        queryMap.put("query", queryInfo);
        int resultList = iCompanyPrerechargeDao.loadPreInfoListCount(queryMap);
        return resultList;
    }
}
