package com.dbt.platform.clientcenter.dao;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.clientcenter.bean.VpsClientUserInfo;

/**
 * @author 作者姓名
 * @version 版本号
 * @createTime 2015年5月6日 上午9:49:59
 * @description 类说明
 */

public interface IVpsClientUserInfoDao extends IBaseDao<VpsClientUserInfo> {
	
	/**
	 * 查询用户信息List
	 * @param map
	 * @return
	 */
	public List<VpsClientUserInfo> findVpsClientUserList(Map<String, Object> map);
	
	/**
	 * 查询数据条数
	 * @param vpsClientUserInfo
	 * @return
	 */
	public int countVpsClientUserInfo(VpsClientUserInfo vpsClientUserInfo);
	
	
	/**
	 * 根据userKey获取获取信息
	 */
	public VpsClientUserInfo findById(String userKey);
	
	/**
	 * 根据昵称或账号查询个人信息
	 * @param param
	 * @return
	 */
	public VpsClientUserInfo queryClientUserInfoByAccountOrNickName(String param);
	
	/**
	 * 根据userkey获取openid
	 * @param userKey
	 * @return
	 */
	public String queryOpenidByUserkey(String userKey);
	
}
