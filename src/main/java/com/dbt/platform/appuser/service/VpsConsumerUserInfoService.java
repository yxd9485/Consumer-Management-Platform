package com.dbt.platform.appuser.service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey.cacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerAccountInfoDao;
import com.dbt.platform.appuser.dao.IVpsConsumerThirdAccountInfoDao;
import com.dbt.platform.appuser.dao.IVpsConsumerUserInfoDao;
import com.dbt.platform.wctaccesstoken.service.WctAccessTokenService;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.dao.VpointsExchangeDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * 消费者用户信息service
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>
 * @createTime 2017年3月9日 </br>
 */
@Service("vpsConsumerUserInfoService")
public class VpsConsumerUserInfoService extends BaseService<VpsConsumerUserInfo> {

	private final static Log log = LogFactory.getLog(VpsConsumerUserInfoService.class);
	@Autowired
	private IVpsConsumerUserInfoDao iVpsConsumerUserInfoDao;
	@Autowired
	private IVpsConsumerThirdAccountInfoDao iVpsConsumerThirdAccountInfoDao;
	@Autowired
	private WctAccessTokenService wctAccessTokenService;
    @Autowired
	private IVpsConsumerAccountInfoDao consumerAccountInfoDao;
    @Autowired
    private VpointsExchangeDao exchangeDao;

    //身份证号加解密key
	private final static String MYSQL_AES_ENCRYPT_KEY = "223b2a7e-a827-4a3f-a177-44a91c8d4f30";

	/**
	 * 根据userkey查询用户信息
	 *
	 * @param userKey
	 *            userkey
	 * @return VpsConsumerUserInfo对象
	 */
	public VpsConsumerUserInfo queryUserInfoByUserkey(String userKey) {
		VpsConsumerUserInfo consumerUserInfoObj = iVpsConsumerUserInfoDao.queryUserInfoByUserkey(userKey);
		return consumerUserInfoObj;
	}

	/**
	 * 根据openid查询用户信息
	 *
	 * @param openid
	 * @return VpsConsumerUserInfo对象
	 */
	public VpsConsumerUserInfo queryUserInfoByOpenid(String openid) {
		VpsConsumerUserInfo consumerUserInfoObj  = iVpsConsumerUserInfoDao.queryUserInfoByOpenid(openid);
		return consumerUserInfoObj;
	}

	/**
	 * 根据userkey修改用户信息
	 *
	 * @param userKey
	 *            userkey
	 * @throws Exception
	 */
	public void updateConsumerUserInfoByUserkey(VpsConsumerUserInfo vpsConsumerUserInfo)
			throws Exception {
		this.iVpsConsumerUserInfoDao.update(vpsConsumerUserInfo);
		CacheUtilNew.removeGroupByKey(cacheKey.USER_INFO.KEY_CONSUMER_USER_INFO
				+ Constant.DBTSPLIT + vpsConsumerUserInfo.getUserKey());
		
		// 删除风控用户缓存
        VpsConsumerThirdAccountInfo thirdInfo = 
        		iVpsConsumerThirdAccountInfoDao.queryThirdAccountInfoByUserKey(vpsConsumerUserInfo.getUserKey());
        if(null != thirdInfo) {
        	RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.RISK_USER + "_" + DateUtil.getDate() + ":" + thirdInfo.getOpenid());
        }
	}

	/**
	 * 获取用户List
	 * @param queryBean </br>
	 * @param pageInfo </br>
	 * @return List<VpsConsumerUserInfo> </br>
	 */
	public List<VpsConsumerUserInfo> findUserInfoList(
			VpsConsumerUserInfo queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("queryBean", queryBean);
		paramsMap.put("pageInfo", pageInfo);
		return iVpsConsumerUserInfoDao.findUserInfoList(paramsMap);
	}

	/**
	 * 获取用户count
	 * @param queryBean </br>
	 * @param pageInfo </br>
	 * @return int </br>
	 */
	public int findUserInfoCount(VpsConsumerUserInfo queryBean) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("queryBean", queryBean);
		return iVpsConsumerUserInfoDao.findUserInfoCount(paramsMap);
	}

	/**
	 * 修改幸运用户
	 * @param userKey</br>
	 * @return void </br>
	 */
	public boolean updateIsLuckyUser(String userKey, String luckLevel) throws Exception{
	    if (StringUtils.isBlank(userKey) || StringUtils.isBlank(luckLevel)) return false;

	    // 依据userKey获取用户对象
        VpsConsumerUserInfo userInfo = queryUserInfoByUserkey(userKey);

        // 如果当前用户有其它状态设置为普通用户, 则用户状态设置为正常用户
        if (Constant.luckLevel.LEVEL0.equals(luckLevel) &&
                !Constant.luckLevel.LEVEL0.equals(userInfo.getIsLuckyUser())) {
            userInfo.setUserStatus(Constant.userStatus.USERTYPE_0);
        }


        String luckLevelDesc = luckLevel;
        if (Constant.luckLevel.LEVEL0.equals(luckLevel)) {
            luckLevelDesc = "普通用户";
        } else if (Constant.luckLevel.LEVEL1.equals(luckLevel)) {
            luckLevelDesc = "幸运用户 ";
        } else if (Constant.luckLevel.LEVEL2.equals(luckLevel)) {
            luckLevelDesc = "餐饮服务员";
        } else if (Constant.luckLevel.LEVEL3.equals(luckLevel)) {
            luckLevelDesc = "扫码专员";
        } else if (Constant.luckLevel.LEVEL4.equals(luckLevel)) {
            luckLevelDesc = "回收站用户";
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("userKey", userInfo.getUserKey());
        logMap.put("oldLuckLevel", userInfo.getIsLuckyUser());
        logMap.put("newLuckLevel", luckLevel);
        logService.saveLog("user", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(logMap), "修改幸运级别为" + StringUtils.defaultIfBlank(luckLevelDesc, ""));

        userInfo.setIsLuckyUser(luckLevel);
        updateConsumerUserInfoByUserkey(userInfo);
        return true;
	}
	/**
	 * 查询用户积分
	 */
	public List<VpsConsumerAccountInfo> findUserVpoints(HashMap<String,Object> map) {

		return	consumerAccountInfoDao.findUserVpoints(map);
	}
	
	/**
	 * 查询用户积分
	 */
	public Integer findUserVpointsCount(HashMap<String,Object> map) {
		
		return	consumerAccountInfoDao.findUserVpointsCount(map);
	}

	/**
	 * 查询用户的详细扫码
	 */
    public List<VpsConsumerAccountInfo> findUserScanList(HashMap<String,Object> map) {
		return	consumerAccountInfoDao.findUserScanList(map);
    }

	/**
	 * 查询用户的详细扫码
	 */
	public Integer countUserScanList(String userKey) {
		return	consumerAccountInfoDao.countUserScanList(userKey);
	}

    /**
     * 表格下载
     */
    public void exportUserExcelList(HashMap<String,Object>map, HttpServletResponse response,String type) throws Exception {
        // 获取导出信息
        List<VpsConsumerAccountInfo> userVpointsLst = null;
        List<VpsConsumerAccountInfo> vpsList =null;
        List<VpointsExchangeLog> exchangeLogLst =null;

        if("0".equals(type)) {
             userVpointsLst = consumerAccountInfoDao.findUserVpoints(map);
			// 处理显示数据
			int index = 1;
			Date expressSendTime = null;
			for (VpsConsumerAccountInfo item : userVpointsLst) {
				item.setIndex(String.valueOf(index++));

			}
        }
        if("1".equals(type)) {
              vpsList = 	consumerAccountInfoDao.findUserScanList(map);
			// 处理显示数据
			int index = 1;
			Date expressSendTime = null;
			for (VpsConsumerAccountInfo item : vpsList) {
				item.setIndex(String.valueOf(index++));

			}
        }

        if("2".equals(type)) {
            exchangeLogLst = exchangeDao.queryForExpressLst(map);
			// 处理显示数据
			int index = 1;
			Date expressSendTime = null;
			for (VpointsExchangeLog item : exchangeLogLst) {
				item.setIndex(String.valueOf(index++));

			}
        }

        OutputStream outStream = response.getOutputStream();


        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
	    String title = null;
	    if(!"0".equals(type)) {
	      title=  map.get("title").toString();
        }
        // 用户积分
        if ("0".equals(type)) {
            bookName = "用户积分列表";
            headers = new String[] {"序号", "用户ID", "呢称", "手机号", "总积分", "兑换积分", "剩余积分", "扫码次数","兑换次数","首次领取时间"};
            valueTags = new String[] {"index", "userKey", "nikeName", "phoneNum", "accountVpoints", "exchangePoints", "surplusVpoints", "totalScan", "totalExchangeNum", "createTime",};
            ExcelUtil<VpsConsumerAccountInfo> excel = new ExcelUtil<>();
            excel.writeExcel(bookName, headers, valueTags, userVpointsLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            outStream.close();
            //获取积分
        } else if ("1".equals(type)) {

            bookName = "获取积分列表";
            headers = new String[] {"序号", "中奖积分", "中奖SKU", "中奖V码", "中奖区域", "中奖时间", };
            valueTags = new String[] {"index", "earnVpoints", "skuName", "qrcodeContent", "address", "earnTime"};
            ExcelUtil<VpsConsumerAccountInfo> excel = new ExcelUtil<VpsConsumerAccountInfo>();
            excel.writeExcelIncludeCaption(bookName, headers, valueTags, vpsList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream,title);
            outStream.close();
            // 兑换积分
        } else if ("2".equals(type)) {
            bookName = "兑换积分列表";
            headers = new String[] {"序号", "订单编号", "兑换品牌", "兑换商品名称", "商品编号", "数量", "单品积分", "兑换积分","收件人","电话","地址",
		            "订单时间"};
            valueTags = new String[] {"index", "exchangeId", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "unitVpoints", "exchangeVpoints", "userName", "phoneNum", "address", "exchangeTime"};
            ExcelUtil<VpointsExchangeLog> excel = new ExcelUtil<VpointsExchangeLog>();
            excel.writeExcelIncludeCaption(bookName, headers, valueTags, exchangeLogLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream,title);
            outStream.close();

        }

    }


     public VpsConsumerAccountInfo  findQrcodeContent(HashMap<String,Object> map){
	    return  consumerAccountInfoDao.findQrcodeContent(map);
     }
     
     /**
 	 * 通过userKey获取三方账户
 	 * @param userKey
 	 * @return
 	 */
 	public VpsConsumerThirdAccountInfo queryOpenidByUserkey(String userKey){
 		return iVpsConsumerThirdAccountInfoDao.queryThirdAccountInfoByUserKey(userKey);
 	}
 	
 	/**
 	 * 通过userKey数组获取三方账户
 	 * @param userKey
 	 * @return
 	 */
	public Set<String> queryOpenidByUserkey(List<String> userKeyGro) {
		Map<String, Object> map = new HashMap<>();
		map.put("userKeyGro", userKeyGro);
		return iVpsConsumerThirdAccountInfoDao.queryThirdAccountInfoByUserKeyGro(map);
	}
	
	/**
	 * 通过userKey数组获取三方账户
	 * @param userKey
	 * @return
	 */
	public Set<String> queryPaOpenidByUserkey(List<String> userKeyGro) {
		Map<String, Object> map = new HashMap<>();
		map.put("userKeyGro", userKeyGro);
		return iVpsConsumerThirdAccountInfoDao.queryPaOpenidByUserkey(map);
	}
 	
 	/**
 	 * 获取累计积分大于vpoints的用户openid
 	 * @param minVpoint
 	 * @param minVpoint
 	 * @return
 	 */
 	public Set<String> queryValidOpenid(String minVpoint, String maxVpoint, String minMoney, String maxMoney){
 		Map<String, Object> map = new HashMap<>();
		map.put("minVpoint", minVpoint);
		map.put("maxVpoint", maxVpoint);
		map.put("minMoney", minMoney);
		map.put("maxMoney", maxMoney);
		map.put("projectServerName", DbContextHolder.getDBType());
 		return iVpsConsumerThirdAccountInfoDao.queryValidOpenid(map);
 	}
 	
 	/**
 	 * 获取累计积分大于vpoints的用户数量
 	 * @param minVpoint
 	 * @param minVpoint
 	 * @return
 	 */
 	public int queryValidOpenidCount(String minVpoint, String maxVpoint){
 		Map<String, Object> map = new HashMap<>();
 		map.put("minVpoint", minVpoint);
 		map.put("maxVpoint", maxVpoint);
 		return iVpsConsumerThirdAccountInfoDao.queryValidOpenidCount(map);
 	}
 	
 	/**
 	 * 获取累计积分大于vpoints的用户openid
 	 * @param minVpoint
 	 * @param minVpoint
 	 * @return
 	 */
 	public Set<String> queryValidPaOpenid(String minVpoint, String maxVpoint){
 		Map<String, Object> map = new HashMap<>();
 		map.put("minVpoint", minVpoint);
 		map.put("maxVpoint", maxVpoint);
 		return iVpsConsumerThirdAccountInfoDao.queryValidPaOpenid(map);
 	}
 	
 	/**
 	 * 获取累计积分大于vpoints的用户数量
 	 * @param minVpoint
 	 * @param minVpoint
 	 * @return
 	 */
 	public int queryValidPaOpenidCount(String minVpoint, String maxVpoint){
 		Map<String, Object> map = new HashMap<>();
 		map.put("minVpoint", minVpoint);
 		map.put("maxVpoint", maxVpoint);
 		return iVpsConsumerThirdAccountInfoDao.queryValidPaOpenidCount(map);
 	}
 	
 	/**
 	 * 获取剩余积分大于0的用户
 	 * @return
 	 */
	public List<VpsConsumerAccountInfo> queryUserByVpoints() {
		return consumerAccountInfoDao.queryUserByVpoints();
	}

	/**
	 * 更新可疑用户为正常状态
	 */
	public void updateConsumerUserStatus (HashSet<String> set)throws Exception {
		Map<String,Object> param = new HashMap<>();
		param.put("set",set);
		iVpsConsumerUserInfoDao.updateConsumerUserStatus(param);
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String userKey = it.next();
			CacheUtilNew.removeGroupByKey(cacheKey.USER_INFO.KEY_CONSUMER_USER_INFO	+ Constant.DBTSPLIT + userKey);
			
			// 删除风控用户缓存
	        VpsConsumerThirdAccountInfo thirdInfo = 
	        		iVpsConsumerThirdAccountInfoDao.queryThirdAccountInfoByUserKey(userKey);
	        if(null != thirdInfo) {
	        	RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.RISK_USER + "_" + DateUtil.getDate() + ":" + thirdInfo.getOpenid());
	        }
		}
	}

	public VpsConsumerUserInfo findById(String userKey) {
		return iVpsConsumerUserInfoDao.findById(userKey);
	}
    
/**
 * 依据用户手机号及角色查询用户信息
 * @param phoneNumber
 * @param userRole
 * @return
 */
   public List<VpsConsumerUserInfo> queryByPhoneNumAndRole(String phoneNumber, String userRole){
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("phoneNumber", phoneNumber);
       map.put("userRole", userRole);
       return iVpsConsumerUserInfoDao.queryByPhoneNumAndRole(map);
   }

	/**
	 * 根据userKey批量查询实名认证字段 用户角色
	 * @param userKeys
	 * @return
	 */
	public List<VpsConsumerUserInfo> queryUserInfoByUserkeys(List<String> userKeys){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aesEncryptKey", MYSQL_AES_ENCRYPT_KEY);
		map.put("userKeys", userKeys);
		return iVpsConsumerUserInfoDao.queryUserInfoByUserkeys(map);
	};
}
