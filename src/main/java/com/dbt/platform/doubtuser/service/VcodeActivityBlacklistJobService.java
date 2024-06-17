package com.dbt.platform.doubtuser.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;
import com.dbt.platform.doubtuser.dao.IVcodeBlacklistDao;

/**
 * 疑似黑名单持久化job
 * @author:Jiquanwei<br>
 * @date:2016-1-25 上午11:30:21<br>
 * @version:1.0.0<br>
 * 
 */
@Service("vcodeActivityBlacklistJobService")
public class VcodeActivityBlacklistJobService {
	private final static Log log = LogFactory.getLog(VcodeActivityBlacklistJobService.class);
	
	@Autowired
	private VcodeBlacklistService vcodeBlacklistService; 
	@Autowired
	private IVcodeBlacklistDao iVcodeBlacklistDao;

	/**
	 * 可疑用户一个月后自动进入黑名单
	 * @param </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void executeDubiousUserConvertBlackUser() throws Exception {
		// 查询一个月之前的可疑用户
		List<VcodeBlacklistAccount> doubtLst = iVcodeBlacklistDao.findDoubtUserListByTime();
		if(null != doubtLst && !doubtLst.isEmpty()){
			for (VcodeBlacklistAccount vcodeBlacklistAccount : doubtLst) {
				vcodeBlacklistService.addBlacklist(vcodeBlacklistAccount.getBlacklistValue(), Constant.BlackUserFlag.FOUR);
			}
		}
	}
}
