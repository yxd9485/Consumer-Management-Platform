package com.dbt.platform.appuser.service;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.appuser.bean.VpsConsumerCheckUserInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerCheckUserInfoDao;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 大奖核销人员Service
 * @author hanshimeng
 *
 */
@Service
public class VpsConsumerCheckUserInfoService extends BaseService<VpsConsumerCheckUserInfo>{

	@Autowired
	private IVpsConsumerCheckUserInfoDao checkUserInfoDao;

	/**
	 * 查询核销人员List
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VpsConsumerCheckUserInfo> findCheckUserInfoList(VpsConsumerCheckUserInfo queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return checkUserInfoDao.findCheckUserInfoList(map);
	}

	/**
	 * 查询核销人员Count
	 * @param queryBean
	 * @return
	 */
	public int findCheckUserInfoCount(VpsConsumerCheckUserInfo queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return checkUserInfoDao.findCheckUserInfoCount(map);
	}

	/**
	 * 更新核销人员状态
	 * @param infoKey
	 * @param userStatus
	 * @param currentUser 
	 */
	public void updateCheckUserStatus(String infoKey, String userStatus, SysUserBasis currentUser) throws Exception{
		VpsConsumerCheckUserInfo checkUserInfo = checkUserInfoDao.findById(infoKey);
		if(null != checkUserInfo){
			Map<String, Object> map = new HashMap<>();
			map.put("infoKey", infoKey);
			map.put("userStatus", userStatus);
			map.put("updateUser", currentUser.getUserKey());
			checkUserInfoDao.updateCheckUserStatus(map);
			CacheUtilNew.removeByKey(CacheKey.cacheKey.USER_INFO.KEY_CONSUMER_THIRD_ACCOUNT_INFO
	                + Constant.DBTSPLIT + checkUserInfo.getOpenid());
			logService.saveLog("checkUser", Constant.OPERATION_LOG_TYPE.TYPE_2,
	                JSON.toJSONString(map), "修改核销人员[" +checkUserInfo.getUserName()+ "]状态为：" + StringUtils.defaultIfBlank(userStatus, ""));
		}
	}

	/**
	 * 删除核销人员
	 * @param infoKey
	 */
	public void deleteCheckUser(String infoKey) throws Exception{
		VpsConsumerCheckUserInfo checkUserInfo = checkUserInfoDao.findById(infoKey);
		if(null != checkUserInfo){
			checkUserInfoDao.deleteCheckUser(infoKey);
			CacheUtilNew.removeByKey(CacheKey.cacheKey.USER_INFO.KEY_CONSUMER_THIRD_ACCOUNT_INFO
	                + Constant.DBTSPLIT + checkUserInfo.getOpenid());
			logService.saveLog("checkUser", Constant.OPERATION_LOG_TYPE.TYPE_3,
					JSON.toJSONString(checkUserInfo), "删除核销人员主键：" + StringUtils.defaultIfBlank(infoKey, ""));
		}
	}

	public void exportUserList(VpsConsumerCheckUserInfo queryBean, HttpServletResponse response) throws Exception {
		response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();
        
        // 获取导出信息
        List<VpsConsumerCheckUserInfo> userInfoList = findCheckUserInfoList(queryBean, null);
		for (VpsConsumerCheckUserInfo userInfo : userInfoList) {
			if ("0".equals(userInfo.getUserStatus())){
				userInfo.setUserStatus("停用");
			} else {
				userInfo.setUserStatus("启用");
			}
		}

        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "核销人员管理导出";
		headers = new String[] { "openid", "核销人员", "状态", "手机号", "地址", "门店名称", "注册时间"};
		valueTags = new String[] { "openid", "userName", "userStatus", "phoneNum", "userAddress", "terminalName", "createTime" };

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpsConsumerCheckUserInfo> excel = new ExcelUtil<VpsConsumerCheckUserInfo>();
        excel.writeExcel(bookName, headers, valueTags, userInfoList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
		
	}
	
}
