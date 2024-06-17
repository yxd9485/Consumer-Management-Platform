package com.dbt.platform.doubtuser.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;

/**
 * @author hanshimeng 
 * @createTime 2016年4月21日 下午5:46:18
 * @description 可疑名单接口Dao
 */

public interface IVcodeBlacklistDao extends IBaseDao<VcodeBlacklistAccount> {

	/**
	 * 可疑名单列表
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	public List<VcodeBlacklistAccount> findDoubtUserList(Map<String, Object> map);
	
	/**
	 * 可疑名单列表条数
	 * @param map keys:queryBean
	 * @return
	 */
	public Integer countDoubtUserList(Map<String, Object> map);
	
	/**
	 * 批量加入可疑名单
	 * @param map
	 */
	public void batchCreate(Map<String, Object> map);
	
	/**
	 * 依据可疑值查询相应数据
	 * 
	 * @param map keys:blackListValue
	 * @return
	 */
	public VcodeBlacklistAccount findByBlackListValue(Map<String, Object> map);

	/**
	 * 查询一个月之前的可疑用户
	 * @param </br> 
	 * @return List<VcodeBlacklistAccount> </br>
	 */
	public List<VcodeBlacklistAccount> findDoubtUserListByTime();
	
	/**
	 * 物理删除可疑用户
	 * @param userKey
	 */
	public void removeByUserKey(String userKey);

	/**
	 * 查询出需要释放的可疑规则
	 * @return
	 */
	public List<VcodeBlacklistAccount> findDoubtUserListBy ();

    public void deleteByInfoKeys (Map<String,Object> set);
}
