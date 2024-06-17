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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.enterprise.bean.CompanyInfo;
import com.dbt.platform.enterprise.service.CompanyInfoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/companyAction")
public class CompanyAction extends BaseAction{

	@Autowired
	private CompanyInfoService companyInfoService;

	/**
	 * 企业基础信息
	 *
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showComoanyInfo")
	public String showComoanyInfo(HttpSession session, Model model) {
		try {
			CompanyInfo companyInfo = companyInfoService.findCompanyInfo();

			// 处理有效登录用户
			String companyLoginPhone = companyInfo.getCompanyLoginPhone();
			if(!StringUtils.isEmpty(companyLoginPhone)){
               String [] phones= companyLoginPhone.split(",");
               List<String> arrayList= new ArrayList<String>();

                for(int i=0;i<phones.length;i++){
                     if(!StringUtils.isEmpty(phones[i]) )   {
                         arrayList.add(phones[i]);
                     }

               }
				model.addAttribute("userCount", companyLoginPhone.split(",").length);
				model.addAttribute("companyLoginPhoneList", arrayList.toArray());
			}

			// 处理过期天数
			String companyContractDdate =
					StringUtils.isEmpty(companyInfo.getCompanyContractDdate())
					? DateUtil.getDate() : companyInfo.getCompanyContractDdate();
			model.addAttribute("intervalDay", DateUtil.getDayBytime(companyContractDdate));
			model.addAttribute("companyInfo", companyInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "company/showCompanyInfo";
	}
}
