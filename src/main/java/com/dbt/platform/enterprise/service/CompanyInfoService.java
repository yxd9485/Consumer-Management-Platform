package com.dbt.platform.enterprise.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.platform.enterprise.bean.CompanyInfo;

/**
 * @author RoyFu 
 * @createTime 2016年4月21日 下午5:50:28
 * @description 
 */
@Service
public class CompanyInfoService {

	@Autowired
	private SysDataDicService sysDataDicService;
	
	/**
	 * 查询企业基础信息
	 * @return
	 */
	public CompanyInfo findCompanyInfo() {
		CompanyInfo companyInfo = new CompanyInfo();
		
		List<SysDataDic> companyInfoList = 
	    		sysDataDicService.getDataDicByCagegoryCode(DatadicKey.dataDicCategory.COMPANY_INFO);
		
	    if(null != companyInfoList){
	    	for (SysDataDic item : companyInfoList) {
				if(DatadicKey.companyInfo.COMPANY_NAME.equals(item.getDataId())){
					companyInfo.setCompanyName(item.getDataValue());
				}else if(DatadicKey.companyInfo.COMPANY_LINK_USER.equals(item.getDataId())){
					companyInfo.setCompanyLinkUser(item.getDataValue());
				}else if(DatadicKey.companyInfo.COMPANY_PHONE.equals(item.getDataId())){
					companyInfo.setCompanyPhone(item.getDataValue());
				}else if(DatadicKey.companyInfo.COMPANY_EMAIL.equals(item.getDataId())){
					companyInfo.setCompanyEmail(item.getDataValue());
				}else if(DatadicKey.companyInfo.COMPANY_CONTRACT_DATE.equals(item.getDataId())){
					companyInfo.setCompanyContractDdate(item.getDataValue());
				}else if(DatadicKey.companyInfo.COMPANY_LOGIN_PHONE.equals(item.getDataId())){
					companyInfo.setCompanyLoginPhone(item.getDataValue());
				}
			}
	    }
	    return companyInfo;
	}
}
