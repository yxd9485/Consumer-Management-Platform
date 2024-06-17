package com.dbt.platform.activity.service;

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
import com.dbt.platform.activity.bean.VcodePerhundredPrizeRecord;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.dao.IVcodePerhundredPrizeRecordDao;

/**
 * 逢百中奖纪录Service
 * @author hanshimeng
 *
 */
@Service
public class VcodePerhundredPrizeRecordService extends BaseService<VcodePerhundredPrizeRecord>{
	@Autowired
	private IVcodePerhundredPrizeRecordDao perhundredPrizeRecordDao;

	/**
	 * 获取列表List
	 * @param queryBean
	 * @param pageInfo
	 * @param isLimit
	 * @return
	 */
	public List<VcodePerhundredPrizeRecord> queryForList(
			VcodePerhundredPrizeRecord queryBean, PageOrderInfo pageInfo, boolean isLimit) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		map.put("isLimit", isLimit);
		return perhundredPrizeRecordDao.queryForList(map);
	}

	/**
	 * 获取列表Count
	 * @param queryBean
	 * @return
	 */
	public int queryForCount(VcodePerhundredPrizeRecord queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return perhundredPrizeRecordDao.queryForCount(map);
	}

	/**
	 * 导出中奖明细
	 * @param queryBean
	 * @param pageInfo
	 * @param response
	 * @throws IOException 
	 */
	public void exportPerhundredPrizeRecord(
			VcodePerhundredPrizeRecord queryBean, PageOrderInfo pageInfo, HttpServletResponse response) throws IOException {
		// 获取导出信息
		List<VcodePerhundredPrizeRecord> queryList = queryForList(queryBean, pageInfo, false);
		OutputStream outStream = response.getOutputStream();
		
		String[] headers = {"用户ID", "昵称", "中奖V码", "中奖倍数", "中奖金额", "中奖时间"};
		String[] valueTags = {"userKey", "nickName", "qrcodeContent", "multiple", "earnMoney", "createTime"};
		ExcelUtil<VcodePerhundredPrizeRecord> excel = new ExcelUtil<VcodePerhundredPrizeRecord>(); 
		excel.writeExcel("逢百中奖名单", headers, valueTags, queryList, DateUtil.DEFAULT_DATE_FORMAT, outStream);
		outStream.close();
	}
}
