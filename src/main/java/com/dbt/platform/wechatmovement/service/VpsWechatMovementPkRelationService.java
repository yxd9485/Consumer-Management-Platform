package com.dbt.platform.wechatmovement.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPkRelation;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementPkRelationDao;

/**
 * 用户参与微信运动PK关系Service
 * @author hanshimeng
 *
 */
@Service
public class VpsWechatMovementPkRelationService extends BaseService<VpsWechatMovementPkRelation>{

	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private IVpsWechatMovementPkRelationDao wechatMovementPkRelationDao;
	

}
