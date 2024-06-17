package com.dbt.platform.appuser.dao;
  
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
/**
 * 消费者用户信息dao
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年3月9日 </br>
 */
public interface IVpsConsumerUserInfoDao extends IBaseDao<VpsConsumerUserInfo>
{ 
	/**
	 * 根据userkey查询用户信息
	 * @param userKey userkey
	 * @return VpsConsumerUserInfo对象
	 */
	public VpsConsumerUserInfo queryUserInfoByUserkey(String userKey);
	/**
	 * 根据openid查询用户信息
	 * @param openid
	 * @return VpsConsumerUserInfo对象
	 */
	public VpsConsumerUserInfo queryUserInfoByOpenid(String openid);
	
	/**
	 * 获取用户List
	 * @param paramsMap </br> 
	 * @return List<VpsConsumerUserInfo> </br>
	 */
	public List<VpsConsumerUserInfo> findUserInfoList(Map<String, Object> paramsMap);
	
	/**
	 * 获取用户count
	 * @param paramsMap</br> 
	 * @return Integer </br>
	 */
	public Integer findUserInfoCount(Map<String, Object> paramsMap);
	
	/**
	 * 修改幸运用户
	 * @param paramMap </br> 
	 * @return void </br>
	 */
	public void updateIsLuckyUser(Map<String, Object> paramMap);
	/**
	 * 通过userKey获取openid
	 * @param userKey
	 * @return
	 */
	public String queryOpenidByUserkey(String userKey);

	/**
	 * 更新可疑用户为正常用户
	 */
	public void updateConsumerUserStatus (Map<String,Object>   set);
	
	/** 
	 * 依据用户手机号及角色查询用户信息
	 * */
	public List<VpsConsumerUserInfo> queryByPhoneNumAndRole(Map<String, Object> paramMap);

	/**
	 * 根据userKey批量查询实名认证字段
	 * @param paramMap
	 * @return
	 */
	public List<VpsConsumerUserInfo> queryUserInfoByUserkeys(Map<String, Object> paramMap);
}