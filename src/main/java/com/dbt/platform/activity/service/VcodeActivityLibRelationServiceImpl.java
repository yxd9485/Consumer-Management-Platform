/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 上午11:35:32 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.activity.bean.VcodeActivityLibRelation;
import com.dbt.platform.activity.dao.IVcodeActivityCogDao;
import com.dbt.platform.activity.dao.IVcodeActivityLibRelationDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class VcodeActivityLibRelationServiceImpl {

	private final static Log log = LogFactory.getLog(VcodeActivityLibRelationServiceImpl.class);
	
	@Autowired
	private IVcodeQrcodeLibDao qrcodeLibDao;
	@Autowired
	private IVcodeActivityCogDao activityCogDao;
	@Autowired
	private IVcodeActivityLibRelationDao vcodeQrcodeLibRelationDao;

	/**
	 * 根据批次更新关联表中对应活动key
	 * @param libRelation </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void updateLibRelation(VcodeActivityLibRelation libRelation) throws Exception {
		vcodeQrcodeLibRelationDao.update(libRelation);
		CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_LIB_RELATION 
				+ Constant.DBTSPLIT + libRelation.getVcodeUniqueCode());
	}

	/**
	 * 根据批次查询
	 * @param batchKey </br> 
	 * @return VcodeActivityLibRelation </br>
	 */
	public VcodeActivityLibRelation findLibRelationBybatchKey(String batchKey) {
		return vcodeQrcodeLibRelationDao.findLibRelationBybatchKey(batchKey);
	}

	/**
	 * 根据码库标识查询
	 * @param vcodeUniqueCode 码库标识</br> 
	 * @return VcodeActivityLibRelation </br>
	 */
	public VcodeActivityLibRelation findLibRelationByUniqueCode(String vcodeUniqueCode) {
		return vcodeQrcodeLibRelationDao.findLibRelationByUniqueCode(vcodeUniqueCode);
	}
}
