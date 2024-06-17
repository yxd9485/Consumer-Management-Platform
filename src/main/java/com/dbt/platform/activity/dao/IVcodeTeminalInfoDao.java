package com.dbt.platform.activity.dao;
  
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VpsPrizeTerminalRelation;
import com.dbt.platform.activity.bean.VpsTerminalInfo;
/**
 * 
 * @author Administrator
 *
 */
public interface IVcodeTeminalInfoDao extends IBaseDao<VpsTerminalInfo> {

	
	List<VpsTerminalInfo> queryForTeminalList(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);

	//删除对应的记录
	void deleteRlation(VpsPrizeTerminalRelation prizeTerminalRelation);
	//新增关系对应
	void createRlation(VpsPrizeTerminalRelation prizeTerminalRelation);

	void createRlationLst(Map<String, Object> map);

	void deleteRlationLst(Map<String, Object> map);
	//修改状态
	void updateRlation(VpsPrizeTerminalRelation vpsPrizeTerminalRelation);

	//查询门店配置的奖品
	String findRlationByPrize(String terminalKey);

	List<VpsPrizeTerminalRelation> findByPrizeType(Map<String, Object> map);

	//查看是否授权
	int countByRlation(VpsPrizeTerminalRelation prizeTerminalRelation);

}