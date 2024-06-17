package com.dbt.platform.appuser.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerAccountInfoDao;

@Service
public class VpsConsumerAccountInfoService extends BaseService<VpsConsumerAccountInfo> {

    @Autowired
    private IVpsConsumerAccountInfoDao accountInfoDao;
    
    /**
     * 获取用户帐户信息
     * 
     * @param userKey
     * @return
     */
    public VpsConsumerAccountInfo findByUserKey(String userKey, boolean forUpdate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userKey", userKey);
        map.put("forUpdate", forUpdate);
        return accountInfoDao.findByUserKey(map);
    }
    
    /**
     * 增加指定帐户金额并清零冻结金额
     * 
     * @param accountKey    帐户主键
     * @param winMoney      要增加或扣除的账户金额
     */
    public void updateFreezeMoney(String accountKey, double winMoney) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountKey", accountKey);
        map.put("winMoney", winMoney);
        accountInfoDao.updateFreezeMoney(map);
    }

	/**
	 * 批量更新账户信息
	 * @param detailList
	 * @param earnVpoints
	 * @param earnMoney
	 * @param earnTime
	 * @throws IOException 
	 */
	public void batchUpdateUserVpoints(List<String> userList, int earnVpoints, double earnMoney, String earnTime) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("earnVpoints", earnVpoints);
		map.put("earnMoney", earnMoney);
		map.put("earnTime", earnTime);
		
		if(!CollectionUtils.isEmpty(userList) && userList.size() > 0){
			int count = userList.size() / 500 + 1;
			List<String> newList = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				if(i == count -1){
					newList = userList.subList(i * 500, userList.size());
				}else{
					newList = userList.subList(i * 500, (i + 1) * 500);
				}
				if(!CollectionUtils.isEmpty(newList)){
					map.put("list", newList);
					accountInfoDao.batchUpdateUserVpoints(map);
				}
			}
		}
	}

	/**
	 * 更新账户金额
	 * @param userKey
	 * @param earnVpoints
	 * @param earnMoney
	 */
	public void executeAddUserAccountPoints(String userKey, int earnVpoints, double earnMoney) {
        Map<String, Object> map = new HashMap<>();
        map.put("userKey",userKey);
        map.put("earnVpoints", earnVpoints);
        map.put("earnMoney", earnMoney);
        accountInfoDao.executeAddUserAccountPoints(map);
	}

	/**
	 * 批量更新账户
	 * @param earnTime
	 * @param accountList
	 * @throws IOException 
	 */
	public void updateBatchVpoints(String earnTime, List<VpsConsumerAccountInfo> accountList) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("earnTime", earnTime);
		map.put("accountList", accountList);
		accountInfoDao.updateBatchVpoints(map);
	}
}
