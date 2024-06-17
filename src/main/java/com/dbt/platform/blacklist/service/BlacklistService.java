package com.dbt.platform.blacklist.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.blacklist.bean.Blacklist;
import com.dbt.platform.blacklist.dao.IBlacklistDao;
import com.dbt.platform.doubtuser.service.VcodeBlacklistService;
import com.dbt.platform.doubtuser.service.VcodeScanRecordService;

/**
 * 黑名单Service
 * @author:Jiquanwei<br>
 * @date:2015-7-8 上午10:46:59<br>
 * @version:1.0.0<br>
 * 
 */
@Service("blacklistService")
public class BlacklistService extends BaseService<Blacklist>{
	
	@Autowired
	private IBlacklistDao blacklistDao;
	@Autowired
	private VcodeScanRecordService scanRecordService;
	@Autowired
	private VcodeBlacklistService vcodeBlacklistService;
	
	/**
	 * 查询黑名单列表
	 * @param paramMap
	 * @return
	 */
	public List<Blacklist> findBlacklistByBlackType(Blacklist queryBean, PageOrderInfo pageInfo){
	    Map<String, Object> map = new HashMap<>();
	    map.put("queryBean", queryBean);
	    map.put("pageInfo", pageInfo);
	    
		return blacklistDao.findBlacklistByBlackType(map);
	}
	
	/**
	 * 统计黑名单总数
	 * @param paramMap
	 * @return
	 */
	public int countBlacklistByBlackType(Blacklist queryBean){
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
		return blacklistDao.countBlacklistByBlackType(map);
	}
    
    /**
     * 删除黑名单
     */
    public void deleteBlacklist(String blacklistValue, String currentUserKey) throws Exception{
        String[] blackValueAry = blacklistValue.split(",");
        for (String blackValue : blackValueAry) {
            if (StringUtils.isBlank(blackValue)) continue;
            Map<String, Object> map = new HashMap<>();
            map.put("blackListValue", blackValue);
            Blacklist blacklist = blacklistDao.findByBlackListValue(map);
            
            // 删除黑名单中的数据
            blacklistDao.removeByBlackListValue(map);
            
            // 把当前用户加入到可疑列表
            String doubtReason = StringUtils.defaultIfBlank(blacklist.getDoubtReason(), Constant.DoubtReasonType.DOUBTRESON_FOUR);
            vcodeBlacklistService.addDoubtList(blackValue, currentUserKey, blacklist.getVcodeActivityKey(), 
                                        doubtReason, blacklist.getProvince(), blacklist.getCity(), blacklist.getCounty());
            
            // 将扫码记录重置为0 add by jiquanwei 20160302
            scanRecordService.resetVcodeScanCounts(blackValue, "");
            
            // 修改用户状态及缓存 add by hanshimeng 20170411
            vcodeBlacklistService.UpdateUserStatus(blackValue, Constant.userStatus.USERTYPE_1);
            
            // 删除用户24小时内扫码SKU记录的缓存
            String cacheKeyStr = "sweep_code_sku_everyday" + blackValue + DateUtil.getDate();
            RedisApiUtil.getInstance().del(true, cacheKeyStr);
        }
        logService.saveLog("blacklist", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(blacklistValue), "移除黑名单");
    }
}
