package com.dbt.platform.blacklist.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.blacklist.bean.Blacklist;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;

/**
 * 黑名单dao接口
 * @author:Jiquanwei<br>
 * @date:2015-7-8 上午10:17:47<br>
 * @version:1.0.0<br>
 * 
 */
public interface IBlacklistDao extends IBaseDao<Blacklist> {
	
	/**
	 * 查询黑名单
	 * @param paramMap
	 * @return
	 */
	public List<Blacklist> findBlacklistByBlackType(Map<String,Object> paramMap);
	
	/**
	 * 合计黑名单总数
	 * @param paramMap
	 * @return
	 */
	public int countBlacklistByBlackType(Map<String,Object> paramMap);
	
	/**
	 * 依据黑名单值查询相应数据
	 * 
	 * @param map keys:blackListValue
	 * @return
	 */
	public Blacklist findByBlackListValue(Map<String, Object> map);
    
    /**
     * 删除黑名单(物理删除)
     * 
     * @param map keys:blackListValue
     * @return
     */
    public void removeByBlackListValue(Map<String, Object> map);
	
}
