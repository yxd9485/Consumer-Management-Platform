package com.dbt.platform.clientcenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.clientcenter.bean.VpsClientUserInfo;
import com.dbt.platform.clientcenter.dao.IVpsClientUserInfoDao;

/**
 * @author 作者姓名
 * @version 版本号
 * @createTime 2015年5月6日11:30:27
 * @description 类说明
 */

@Service("vpsClientUserInfoService")
public class VpsClientUserInfoService extends BaseService<VpsClientUserInfo>{
	
	@Autowired
	private IVpsClientUserInfoDao iVpsClientUserInfoDao;
	
	/**
	 * 查询数据条数
	 * @param vpsClientUserInfo
	 * @return
	 */
	public int countVpsClientUserInfo(VpsClientUserInfo vpsClientUserInfo){
		
		return this.iVpsClientUserInfoDao.countVpsClientUserInfo(vpsClientUserInfo);		
	}
	
	/**
	 * 根据Id获取用户信息
	 * @param userKey
	 * @return
	 */
	public VpsClientUserInfo findById(String userKey){
		return this.iVpsClientUserInfoDao.findById(userKey);
	}
	
	/**
	 * 根据昵称或账号查询个人信息
	 * @param param
	 * @return
	 */
	public VpsClientUserInfo queryClientUserInfoByAccountOrNickName(String param){
		return iVpsClientUserInfoDao.queryClientUserInfoByAccountOrNickName(param);
	}

	/**
	 * 通过userKey获取openid
	 * @param userKey
	 * @return
	 */
	public String queryOpenidByUserkey(String userKey){
		return iVpsClientUserInfoDao.queryOpenidByUserkey(userKey);
	}
}
