package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.util.CacheUtil;
import com.dbt.platform.activity.bean.VcodeQrcodePackInfo;
import com.dbt.platform.activity.dao.IVcodeQrcodePackInfoDao;

/**
 * 文件名：VcodeQrcodePackInfoService.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: V码二维码码包信息<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:22:00<br>
 * 修改内容：新增<br>
 */
@Service("vcodeQrcodePackInfoService")
public class VcodeQrcodePackInfoService {
	@Autowired
	private IVcodeQrcodePackInfoDao iVcodeQrcodePackInfoDao;

	public void addVcodeQrcodePackInfo(VcodeQrcodePackInfo vcodeQrcodePackInfo) {
		this.iVcodeQrcodePackInfoDao.create(vcodeQrcodePackInfo);
	}

	public void updateVcodeQrcodePackInfo(VcodeQrcodePackInfo vcodeQrcodePackInfo) throws Exception {
		this.iVcodeQrcodePackInfoDao.update(vcodeQrcodePackInfo);

//		CacheUtil.removeByRegex(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_PACKINFO
//				+ Constant.DBTSPLIT + vcodeQrcodePackInfo.getPackKey());
	}

	/**
	 * 批量修改包信息
	 * 
	 * @param vcodeQrcodePackInfo
	 * @param currentUserkey
	 * @throws Exception
	 */
	public void batchUpdatePackInfo(VcodeQrcodePackInfo vcodeQrcodePackInfo,String currentUserkey) throws Exception {
		if (StringUtils.isNotBlank(vcodeQrcodePackInfo.getPackCodeArr())) {
			String[] packCodeArr = vcodeQrcodePackInfo.getPackCodeArr().split(",");
			List<VcodeQrcodePackInfo> list = new ArrayList<VcodeQrcodePackInfo>();
			for (String packCode : packCodeArr) {
				VcodeQrcodePackInfo vcodeQrcodePackInfoFor = new VcodeQrcodePackInfo();
				vcodeQrcodePackInfoFor.setPackCode(packCode);
				vcodeQrcodePackInfoFor.setStartDate(vcodeQrcodePackInfo.getStartDate());
				vcodeQrcodePackInfoFor.setEndDate(vcodeQrcodePackInfo.getEndDate());
				vcodeQrcodePackInfoFor.fillFields(currentUserkey);
				list.add(vcodeQrcodePackInfoFor);
			}
//			iVcodeQrcodePackInfoDao.batchUpdatePackInfoByPackCode(list);
//			CacheUtil.removeByRegex(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_PACKINFO);
		}

	}

	/**
	 * 根据编码key查询码包信息
	 * 
	 * @param batchKey
	 *            编码key
	 * @return VcodeQrcodePackInfo V码二维码码包信息
	 */
	public VcodeQrcodePackInfo findPackInfoByBatchkey(String batchKey) {
		return this.iVcodeQrcodePackInfoDao.findPackInfoByBatchkey(batchKey);
	}

}
