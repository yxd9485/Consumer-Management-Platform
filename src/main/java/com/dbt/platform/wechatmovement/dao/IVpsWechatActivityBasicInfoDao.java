package com.dbt.platform.wechatmovement.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;

/**
 * 微信运动配置基础信息
 * @author hanshimeng
 *
 */
public interface IVpsWechatActivityBasicInfoDao extends IBaseDao<VpsWechatActivityBasicInfo>{

	/**
	 * 查询微信运动配置信息
	 * @return
	 */
	public VpsWechatActivityBasicInfo findWechatActivityBasicInfo();

	/**
	 * @author zzy
	 * @createTime 2018/6/15 15:33
	 * @description 保存popss活动规则
	 */
	public void   savePopssActivityBaseInfo(VpsWechatActivityBasicInfo popssActivityBaseInfo);

	/**
	 * 获取记录List
	 * @param queryMap
	 * @return
	 */
	public List<VpsWechatActivityBasicInfo> queryPopssActivityInfoList(Map<String, Object> queryMap);

	/**
	 * 获取记录Count
	 * @return
	 */
	public int queryPopssActivityInfoCount();
	
}
