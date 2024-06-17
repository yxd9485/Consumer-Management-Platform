package com.dbt.platform.appuser.dao;
  
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
/**
 * 消费者三方账户信息dao
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年3月13日 </br>
 */
public interface IVpsConsumerThirdAccountInfoDao extends IBaseDao<VpsConsumerThirdAccountInfo>
{ 

	/**
	 * 根据openid查询消费者三方账户信息
	 * @param openid openid
	 * @return VpsConsumerThirdAccountInfo 消费者三方账户信息
	 */
	public VpsConsumerThirdAccountInfo queryThirdAccountInfoByOpenid(String openid);
	/**
	 * 根据userKey查询三方账户信息
	 * @param userKey
	 * @return
	 */
	public VpsConsumerThirdAccountInfo queryThirdAccountInfoByUserKey(String userKey);
	
	/**
	 * 获取累计积分大于vpoints的用户openid
	 * @return
	 */
	public Set<String> queryValidOpenid(Map<String, Object> map);
	
	/**
	 * 获取累计积分大于vpoints的用户数量
	 * @return
	 */
	public int queryValidOpenidCount(Map<String, Object> map);
	
	/**
	 * 获取累计积分大于vpoints的用户paOpenid
	 * @return
	 */
	public Set<String> queryValidPaOpenid(Map<String, Object> map);
	
	/**
 	 * 通过userKey数组获取三方账户
 	 * @param userKey
 	 * @return
 	 */
	public Set<String> queryThirdAccountInfoByUserKeyGro(Map<String, Object> map);
	
	/**
	 * 通过userKey数组获取三方账户
	 * @param userKey
	 * @return
	 */
	public Set<String> queryPaOpenidByUserkey(Map<String, Object> map);
	
	/**
	 * 查询所有的applet_openid
	 * @return
	 */
	public List<String> findAllAppletOpenid();
	
	/**
 	 * 获取累计积分大于vpoints的用户数量
 	 * @param minVpoint
 	 * @param minVpoint
 	 * @return
 	 */
	public int queryValidPaOpenidCount(Map<String, Object> map);
	
	/**
	 * 依据用户创建时间获取其appletOpenid
	 * @return
	 */
	public List<String> queryAppletOpenidByCreateTime(Map<String, Object> map);
}