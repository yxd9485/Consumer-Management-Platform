package com.dbt.platform.doubtuser.service;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.blacklist.bean.Blacklist;
import com.dbt.platform.blacklist.dao.IBlacklistDao;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;
import com.dbt.platform.doubtuser.dao.IVCodeScanRecordDao;
import com.dbt.platform.doubtuser.dao.IVcodeBlacklistDao;

/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午5:49:52
 * @description 可疑名单service
 */
@Service
public class VcodeBlacklistService extends BaseService<VcodeBlacklistAccount> {

    @Autowired
    private IVcodeBlacklistDao iVcodeBlacklistDao;
    @Autowired
    private IBlacklistDao iBlacklistDao;
    @Autowired
    private IVCodeScanRecordDao scanRecordDao;
    @Autowired
    private VpsConsumerUserInfoService vpsConsumerUserInfoService;

    private final static Log log = LogFactory.getLog(VcodeBlacklistService.class);
    /**
     * 列表
     * 
     * @param queryBean
     * @param pageInfo
     * @return
     */
    public List<VcodeBlacklistAccount> findDoubtUserList(
                        VcodeBlacklistAccount queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);
        List<VcodeBlacklistAccount> resultList = iVcodeBlacklistDao.findDoubtUserList(queryMap);
        for (VcodeBlacklistAccount account : resultList) {
            account.setCreateTime(DateUtil.trimAfterPointer(account.getCreateTime()));
            account.setJoinTime(DateUtil.trimAfterPointer(account.getJoinTime()));
        }
        return resultList;
    }

    /**
     * 列表条数
     * 
     * @param queryBean
     * @return
     */
    public int countDoubtUserList(VcodeBlacklistAccount queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return iVcodeBlacklistDao.countDoubtUserList(queryMap);
    }
    
    /**
     * 把当前用户加入可疑
     * 
     * @param userKey
     * @param optionUserkey
     * @param vcodeActivityKey
     * @return 0:添加成功、1：用户不存在、2：此用户已在黑名单、3：此用户已经可经名单
     * @throws Exception
     */
    public String addDoubtList(String userKey, String optionUserkey, String vcodeActivityKey,
                            String doubtReason, String province, String city, String county) throws Exception {
        
        String bizCode = "0";
        
        // 校验用户是否存在
        VpsConsumerUserInfo  userInfo = vpsConsumerUserInfoService.queryUserInfoByUserkey(userKey);
        if (userInfo == null) {
            bizCode = "1";
            return bizCode;
        }
        
        // 校验当前用户否已经在黑名单
        Map<String, Object> map = new HashMap<>();
        map.put("blackListValue", userKey);
        Blacklist blackAccount = iBlacklistDao.findByBlackListValue(map);
        if (blackAccount != null) {
            bizCode = "2";
            return bizCode;
        }
        
        // 校验当前用户否已经在可疑名单
        map.clear();
        map.put("blackListValue", userKey);
        VcodeBlacklistAccount account = iVcodeBlacklistDao.findByBlackListValue(map);
        if (account != null) {
            bizCode = "3";
            return bizCode;
        }
        
        // 插入可疑用户列表
        List<VcodeBlacklistAccount> doubtList = new ArrayList<>();
        account = new VcodeBlacklistAccount(callUUID(), Constant.blackWhiteList.BLACK_LIST_BLACK, 
                Constant.blackListType.BLACK_LIST_USERKEY, userKey, vcodeActivityKey, doubtReason, "6","0", province, 
                city, county);
        account.setData(DateUtil.getDateTime(), optionUserkey);
        doubtList.add(account);

        // 入库
        map.clear();
        map.put("doubtList", doubtList);
        iVcodeBlacklistDao.batchCreate(map);
        
        // 修改用户状态及缓存 add by hanshimeng 20170411
        UpdateUserStatus(userKey, Constant.userStatus.USERTYPE_1);
        
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("doubtUserKey", userKey);
        logService.saveLog("vcodeDoubtUser", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(map), "增加可疑用户");
        
        return bizCode;
        
    }
    
    /**
     * 从可疑名单中移除
     * 
     * @param blacklistKey
     * @param currentUserKey
     * @throws Exception 
     */
    public void removeDoubtUser(String blacklistValue, String currentUserKey) throws Exception {
        
        // 删除列表中的数据
        List<String> userKeyLst = Arrays.asList(blacklistValue.split(","));

        // 将扫码记录重置为0 add by jiquanwei 20160302
        for (String userKey : userKeyLst) {
            
            // 删除可疑
            iVcodeBlacklistDao.removeByUserKey(userKey);;
            
            Map<String,Object> map = new HashMap<String,Object>();
            if(StringUtils.isNotBlank(blacklistValue)){
                map.put("userkey", userKey);
            }
            scanRecordDao.resetVcodeScanCounts(map);
            scanRecordDao.resetVcodeScanCountsNew(map);

            // 修改用户状态及缓存 add by hanshimeng 20170411
            UpdateUserStatus(userKey, Constant.userStatus.USERTYPE_0);
        }
        
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("doubtUserKey", blacklistValue);
        logService.saveLog("vcodeDoubtUser", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(logMap), "移出可疑用户");
    }
    
    /**
     * 加入到黑名单
     * 
     * @param blacklistKey
     * @param currentUserKey
     */
    public void addBlacklist(String blacklistValue, String currentUserKey) throws Exception{

        // 删除列表中的数据
        List<String> userKeyLst = Arrays.asList(blacklistValue.split(","));
        
        Map<String, Object> map = new HashMap<String, Object>();
        for (String userKey : userKeyLst) {
            map.put("blackListValue", userKey);
            VcodeBlacklistAccount blackAccount = iVcodeBlacklistDao.findByBlackListValue(map);
            
            // 校验当前用户否已经在黑名单
            Blacklist blacklist = iBlacklistDao.findByBlackListValue(map);
            if (null == blacklist) {
                // 写入到黑名单表
                Blacklist blacklistAccount = new Blacklist(callUUID(), Constant.blackListType.BLACK_LIST_USERKEY,
                                    userKey, blackAccount.getVcodeActivityKey(), Constant.blackWhiteList.BLACK_LIST_BLACK, 
                                        blackAccount.getProvince(), blackAccount.getCity(), blackAccount.getCounty(), blackAccount.getDoubtReason());
                blacklistAccount.setObject(currentUserKey);
                iBlacklistDao.create(blacklistAccount);
            }
            
            // 删除可疑
            iVcodeBlacklistDao.removeByUserKey(userKey);;
            
            // 修改用户状态及缓存 add by hanshimeng 20170411
            UpdateUserStatus(userKey, Constant.userStatus.USERTYPE_2);
        }
        
        
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("doubtUserKey", blacklistValue);
        logService.saveLog("vcodeDoubtUser", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "拉入黑名单");
    }

    /**
     * 修改用户状态及缓存
     * @param userKey </br>
     * @param userStatus 0正常， 1可疑，2黑名单</br>  
     * @return void </br>
     */
    public void UpdateUserStatus(String userKey, String userStatus) throws Exception{
        VpsConsumerUserInfo consumerUserInfo = 
                vpsConsumerUserInfoService.queryUserInfoByUserkey(userKey);
        if(null != consumerUserInfo){
            consumerUserInfo.setUserStatus(userStatus);
            vpsConsumerUserInfoService.updateConsumerUserInfoByUserkey(consumerUserInfo);
        }
    }

    /**
     * 定时释放指定可疑时间并到期的可疑人员
     */
    public void updateReleaseOfSuspects() throws  Exception{
        log.warn("定时将可疑人员移除====任务开始");
        //1 查出所有可疑表下的可疑人员
     
        List<VcodeBlacklistAccount> releaseList= iVcodeBlacklistDao.findDoubtUserListBy();
        if(null==releaseList||releaseList.size()<=0){
            log.warn("定时将可疑人员移除====任务结束，暂无符合条件的可以人员");
            return;
        }
        //2 循环判断指定可疑时间并到期的的人员放入释放名单中
        HashSet<String> set = new HashSet<String>();  // 用户的userKey
        HashSet<String> setKeys = new HashSet<String>();  // 用户的userKey
        StringBuffer infoKyes= new StringBuffer();    // 可疑表中的主键
          for(int i=0;i<releaseList.size();i++){
              VcodeBlacklistAccount va=releaseList.get(i);
              set.add(va.getBlacklistValue());
              setKeys.add(va.getBlacklistKey());
              infoKyes.append(va.getBlacklistKey()+",");
          }
        
        //3  将释放人员的用户状态改为正常
        vpsConsumerUserInfoService.updateConsumerUserStatus(set);
          
        //4  删除可疑表中的纪录
        Map<String,Object> param = new HashMap<>();
        param.put("set",setKeys);
        iVcodeBlacklistDao.deleteByInfoKeys(param);
        
        // 5 清除扫码情况记录表
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String userKey = it.next();
            Map<String,Object> map = new HashMap<String,Object>();
            if(StringUtils.isNotBlank(userKey)){
                map.put("userkey", userKey);
            }
            if(null!=map&&map.size()>0) {
                scanRecordDao.resetVcodeScanCounts(map);
                scanRecordDao.resetVcodeScanCountsNew(map);
            }
        }
        log.warn("定时将可疑人员移除====任务结束");
        }
    
}
