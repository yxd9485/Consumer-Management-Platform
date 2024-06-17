package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VpsPrizeTerminalRelation;
import com.dbt.platform.activity.bean.VpsTerminalInfo;
import com.dbt.platform.activity.dao.IVcodeTeminalInfoDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class VcodeTeminalInfoService extends BaseService<VcodeTeminalInfoService>{

	@Autowired
	private IVcodeTeminalInfoDao teminalInfoDao;

	public List<VpsTerminalInfo> queryForList(VpsTerminalInfo queryBean, PageOrderInfo pageInfo) {
		Map <String,Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return teminalInfoDao.queryForTeminalList(map);
	}

	public int queryForCount(VpsTerminalInfo queryBean) {
		Map <String,Object> map = new HashMap<>(); 
		map.put("queryBean", queryBean);
		return teminalInfoDao.queryForCount(map);
	}

	/**
	 * 大奖授权门店
	 */
	public void createRlation(VpsPrizeTerminalRelation prizeTerminalRelation) {	
		teminalInfoDao.createRlation(prizeTerminalRelation);		
	}
	
	
	/**
	 * 大奖取消授权
	 */
	public void deleteRlation(VpsPrizeTerminalRelation prizeTerminalRelation) {	
		teminalInfoDao.deleteRlation(prizeTerminalRelation);		
	}

	/**
	 * 批量授权
	 * @param terminalKeyLst
	 * @param prizeType
	 */
	public void createRlationLst(List<String> relationKeyLst, String relationKey,boolean flag) {
		Map <String,Object> map = new HashMap<>(); 
		map.put("relationKeyLst", relationKeyLst);
		map.put("relationKey", relationKey);
		map.put("flag", flag);
		teminalInfoDao.createRlationLst(map);
		
	}
	
	/**
	 * 批量删除授权
	 * @param terminalKeyLst
	 * @param prizeType
	 */
	public void deleteRlationLst(List<String> relationKeyLst, String relationKey,boolean flag,String status) {
		Map <String,Object> map = new HashMap<>(); 
		map.put("relationKeyLst", relationKeyLst);
		map.put("relationKey", relationKey);
		map.put("flag", flag);
		map.put("status", status);
		teminalInfoDao.deleteRlationLst(map);
		
	}

	/**
	 * 删除门店
	 * @param terminalKey
	 */
	public void delTerminal(String terminalKey) {
		teminalInfoDao.deleteById(terminalKey);
		
	}

	/**
	 * 查看门店
	 * @param terminalKey
	 * @return
	 */
	public VpsTerminalInfo findTerminalByKey(String terminalKey) {
		VpsTerminalInfo terminalInfo = teminalInfoDao.findById(terminalKey);
		terminalInfo.setOldPrizeTyleLst(teminalInfoDao.findRlationByPrize(terminalKey));
		return terminalInfo;
	}
	
	
	

	/**
	 * 停用大奖
	 * @param vpsPrizeTerminalRelation
	 */
	public void stopPrize(VpsPrizeTerminalRelation vpsPrizeTerminalRelation) {
		teminalInfoDao.updateRlation(vpsPrizeTerminalRelation);		
	}

	/**
	 * 停用 启用门店
	 * @param status
	 * @param terminalKey
	 */
	public void updateTerminalStatus(String status, String terminalKey,SysUserBasis sysUserBasis) {
		VpsTerminalInfo vpsTerminalInfo = new VpsTerminalInfo();
		vpsTerminalInfo.fillUpdateFields(sysUserBasis.getUserKey());
		vpsTerminalInfo.setTerminalKey(terminalKey);
		vpsTerminalInfo.setStatus(status);
		teminalInfoDao.update(vpsTerminalInfo);
	}
	
	

	/**
	 * 新增门店
	 * @param terminalInfo
	 * @param sysUserBasis 
	 */
	public void createTerminal(VpsTerminalInfo terminalInfo, SysUserBasis sysUserBasis) {
		terminalInfo.fillFields(sysUserBasis.getUserKey());
		terminalInfo.setTerminalKey(callUUID());		
		//编号
		terminalInfo.setTerminalNo(getBussionNo("vpsVcodeTerminal", "TERMINAL_NO", Constant.OrderNoType.type_MD));
		teminalInfoDao.create(terminalInfo);
		if (StringUtils.isNotBlank(terminalInfo.getPrizeTyleLst())) {
			createRlationLst(Arrays.asList(terminalInfo.getPrizeTyleLst().split(",")), terminalInfo.getTerminalKey(),false);
		}
	}

	/**
	 * 修改门店
	 * @param terminalInfo
	 * @param userBasis
	 */
	public void updateTerminal(VpsTerminalInfo terminalInfo, SysUserBasis userBasis) {
		terminalInfo.fillUpdateFields(userBasis.getUserKey());
		teminalInfoDao.update(terminalInfo);
		//更新奖品多选
		updateForList(terminalInfo);
	}

	
	
	/**
	 * 
	 * @param oldList
	 * @param newList
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateForList(VpsTerminalInfo terminalInfo){
		boolean flag = false;
		boolean statusFlag = false;
		if (StringUtils.isBlank(terminalInfo.getOldPrizeTyleLst())
				&& StringUtils.isBlank(terminalInfo.getPrizeTyleLst()))
			return;
		
		List<String> addKeysLst = new ArrayList<>(),updKeysLst = new ArrayList<>();
		List<String> oldList = (List<String>) (StringUtils.isNotBlank(terminalInfo.getOldPrizeTyleLst())
				? new ArrayList(Arrays.asList(terminalInfo.getOldPrizeTyleLst().split(","))) : null);		
		List<String> newList = (List<String>) (StringUtils.isNotBlank(terminalInfo.getPrizeTyleLst())
				? new ArrayList(Arrays.asList(terminalInfo.getPrizeTyleLst().split(","))) : null);
		//之前为空新增的
		if (CollectionUtils.isEmpty(oldList) && CollectionUtils.isNotEmpty(newList)){
			addKeysLst = newList;
			flag =true;			
		}
		//之前不为空修改的
		if (CollectionUtils.isNotEmpty(oldList) && CollectionUtils.isEmpty(newList)){
			updKeysLst = oldList;
			flag =true;			
		}
		
		if (!flag) {
			// 修改的
			for (String key : oldList) {
				if (!newList.contains(key)) {
					updKeysLst.add(key);
				}
			}

			// 新增的
			for (String key : newList) {
				if (!oldList.contains(key)) {
					addKeysLst.add(key);
				}
			}
		}
		// 更新多选
		if (CollectionUtils.isNotEmpty(addKeysLst)) {
			List<VpsPrizeTerminalRelation> list = findByPrizeType(addKeysLst,terminalInfo.getTerminalKey(),true);
			if (CollectionUtils.isNotEmpty(list)) {
				for (VpsPrizeTerminalRelation relation : list) {
					OK:for (int i = 0; i < addKeysLst.size(); i++) {
						//被删了
						if ("1".equals(relation.getStatus()) && relation.getPrizeType().equals(addKeysLst.get(i))) {
							updKeysLst.add(relation.getPrizeType());
							// 如果是全量更新那么要更新为开启
							statusFlag =  true;
							addKeysLst.remove(i);
							break OK;
						}
					}
				}
			}			
		}
		if (CollectionUtils.isNotEmpty(addKeysLst)) {
			createRlationLst(addKeysLst, terminalInfo.getTerminalKey(), false);			
		}
		
		if (CollectionUtils.isNotEmpty(updKeysLst)) {
			deleteRlationLst(updKeysLst, terminalInfo.getTerminalKey(), false, statusFlag ? "0" : "1");
		}
	}

	/**
	 * 查询授权表已授权类型
	 * @param addKeysLst
	 * @param relationKey
	 * @param flag
	 * @return
	 */
	public List<VpsPrizeTerminalRelation> findByPrizeType(List<String> addKeysLst,String relationKey,boolean flag){
		Map<String, Object> map = new HashMap<>();
		map.put("addKeysLst", addKeysLst);
		map.put("relationKey", relationKey);
		map.put("flag", flag);
		return teminalInfoDao.findByPrizeType(map);
	}
	
	
	/**
	 * 批量授权
	 * 
	 * @param addKeysLst
	 * @param prizeType
	 * @param b
	 */
	public void addorUpdTerminalRelation(List<String> addKeysLst, String prizeType) {
		List<String> updKeysLst = new ArrayList<>();
		List<VpsPrizeTerminalRelation> list = findByPrizeType(addKeysLst, prizeType, false);
		if (CollectionUtils.isNotEmpty(list)) {
			for (VpsPrizeTerminalRelation relation : list) {
				OK: for (int i = 0; i < addKeysLst.size(); i++) {
					// 被删了
					if ("1".equals(relation.getStatus()) && relation.getTerminalKey().equals(addKeysLst.get(i))) {
						updKeysLst.add(relation.getTerminalKey());
						addKeysLst.remove(i);
						break OK;
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(addKeysLst)) {
			createRlationLst(addKeysLst, prizeType, true);
		}
		if (CollectionUtils.isNotEmpty(updKeysLst)) {
			deleteRlationLst(updKeysLst, prizeType, true, "1");
		}
	}

	
	/**
	 * 授权门店
	 * @param prizeTerminalRelation
	 */

	public void addPrizeTerminal(VpsPrizeTerminalRelation prizeTerminalRelation) {
		if (teminalInfoDao.countByRlation(prizeTerminalRelation)>0) {
			prizeTerminalRelation.setStatus("0");
			teminalInfoDao.updateRlation(prizeTerminalRelation);
		} else {
			createRlation(prizeTerminalRelation);
		}
	}
	
}
