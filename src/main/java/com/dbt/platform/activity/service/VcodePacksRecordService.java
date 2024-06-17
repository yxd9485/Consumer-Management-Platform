package com.dbt.platform.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodePacksRecord;
import com.dbt.platform.activity.dao.IVcodePacksRecordDao;

/**
 * 扫码得现金记录
 * @author:Jiquanwei<br>
 * @date:2016-4-25 下午04:33:32<br>
 * @version:1.0.0<br>
 * 
 */
@Service
public class VcodePacksRecordService {

	@Autowired
	private IVcodePacksRecordDao vcodePacksRecordDao;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;
	
	/**
	 * 查询爆点红包List
	 * @param rebateRuleKey </br>
	 * @return List<VpsVcodePacksRecord> </br>
	 */
	public List<VcodePacksRecord> queryEruptRedpacketList(
	        String rebateRuleKey, PageOrderInfo pageInfo, List<String> suffixList) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rebateRuleKey", rebateRuleKey);
		map.put("suffixList", suffixList);
		map.put("pageInfo", pageInfo);
		List<VcodePacksRecord> packsRecordLst = 
		        vcodePacksRecordDao.queryEruptRedpacketList(map);
		if (packsRecordLst != null) {
		    Map<String, String> prizeMap = vpointsCogService.queryAllPrizeType(true, true, true, false, true, true, null);
		    for (VcodePacksRecord item : packsRecordLst) {
                if (vpointsCogService.checkPrizeType(item.getPrizeType())) {
                    item.setEarnPrizeName(prizeMap.get(item.getPrizeType()));
                }
            }
		}
		return packsRecordLst;
	}

	/**
	 * 查询爆点红包count
	 * @param rebateRuleKey </br>
	 * @return List<VpsVcodePacksRecord> </br>
	 */
	public int queryEruptRedpacketCount(String rebateRuleKey, List<String> suffixList) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rebateRuleKey", rebateRuleKey);
		map.put("suffixList", suffixList);
		Integer count = vcodePacksRecordDao.queryEruptRedpacketCount(map);
		return null == count ? 0 : count;
	}
	
}
