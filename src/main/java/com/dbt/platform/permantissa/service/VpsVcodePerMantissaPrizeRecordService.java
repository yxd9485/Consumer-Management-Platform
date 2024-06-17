package com.dbt.platform.permantissa.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaPrizeRecord;
import com.dbt.platform.permantissa.dao.IVpsVcodePerMantissaPrizeRecordDao;

/**
 * 逢尾数中奖纪录Service
 */
@Service
public class VpsVcodePerMantissaPrizeRecordService extends BaseService<VpsVcodePerMantissaPrizeRecord>{
	@Autowired
	private IVpsVcodePerMantissaPrizeRecordDao perMantissaPrizeRecordDao;

	/**
	 * 获取列表
	 */
	public List<VpsVcodePerMantissaPrizeRecord> queryForList(
	        VpsVcodePerMantissaPrizeRecord queryBean, PageOrderInfo pageInfo, boolean isLimit) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		map.put("isLimit", isLimit);
		return perMantissaPrizeRecordDao.queryForList(map);
	}

	/**
	 * 获取列表Count
	 * @param queryBean
	 * @return
	 */
	public int queryForCount(VpsVcodePerMantissaPrizeRecord queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return perMantissaPrizeRecordDao.queryForCount(map);
	}

	/**
	 * 导出中奖明细
	 */
	public void exportPrizeRecord(VpsVcodePerMantissaPrizeRecord queryBean, 
	        PageOrderInfo pageInfo, HttpServletResponse response) throws IOException {
		// 获取导出信息
		List<VpsVcodePerMantissaPrizeRecord> queryList = queryForList(queryBean, pageInfo, false);
		OutputStream outStream = response.getOutputStream();
		
		String[] headers = {"用户ID", "昵称", "中奖V码", "中奖SKU", "中奖瓶数", "奖项名称", "中奖金额", "中奖时间"};
		String[] valueTags = {"userKey", "nickName", "qrcodeContent", "skuName", "prizeScanNum", "prizeName", "earnMoney", "createTime"};
		ExcelUtil<VpsVcodePerMantissaPrizeRecord> excel = new ExcelUtil<VpsVcodePerMantissaPrizeRecord>(); 
		excel.writeExcel("逢尾数中奖名单", headers, valueTags, queryList, DateUtil.DEFAULT_DATE_FORMAT, outStream);
		outStream.close();
	}
}
