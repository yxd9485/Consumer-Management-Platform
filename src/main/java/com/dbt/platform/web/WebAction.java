package com.dbt.platform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.platform.job.BatchActivateCogJob;
import com.dbt.platform.job.SyncAgencyInfoFromMnJob;

@Controller
@RequestMapping("/webAction")
public class WebAction extends BaseAction{

	@Autowired
	private SyncAgencyInfoFromMnJob syncAgencyInfoFromMnJob;
	
	@Autowired
	private BatchActivateCogJob batchActivateCogJob;
	
	@ResponseBody
	@RequestMapping(value = "/updateAgencyInfoJob", method = RequestMethod.GET)
	public String updateAgencyInfoJob(String date){
		syncAgencyInfoFromMnJob.updateAgencyInfo(date);
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping(value = "/batchActivateCogJob", method = RequestMethod.GET)
	public String batchActivateCogJob(String date){
		batchActivateCogJob.updateBatchActivateCog();
		return "SUCCESS";
	}
}
