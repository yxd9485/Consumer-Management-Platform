package com.dbt.framework.log.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.log.bean.VpsOperationLog;
import com.dbt.framework.log.dao.VpsOperationLogDao;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.securityauth.SecurityContext;
import com.dbt.framework.util.UserThreadLocalUtil;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 操作日志Service
 * @author hanshimeng
 *
 */
@Service
public class VpsOperationLogService {
	@Autowired
	private HttpSession session;
	@Autowired
	private VpsOperationLogDao vpsOperationLogDao;
	
	/**
	 * 新增日志
	 * @param menuFlag 菜单标识
	 * @param type	操作类型：0登录；1增加；2修改；3删除
	 * @param content 操作内容（新增和删除只记录主键KEY，修改需存储当前内容的json串）
	 * @param description 操作描述
	 */
	public void saveLog(String menuFlag, String type, String content, String description){
	    try {
	        // 获取用户登录信息
	        if (StringUtils.isNotBlank(UserThreadLocalUtil.get("vjfSessionId"))) {
	            SecurityContext securityContext = (SecurityContext) session
	                    .getAttribute(Constant.USER_SESSION + UserThreadLocalUtil.get("vjfSessionId"));
	            if(null != securityContext){
	                SysUserBasis userBasis = securityContext.getSysUserBasis();
	                
	                // 保存日志
	                VpsOperationLog log = new VpsOperationLog(menuFlag, type,
	                        content, description, userBasis.getUserName(), userBasis.getPhoneNum());
	                vpsOperationLogDao.saveLog(log);
	            }
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * 查询日志列表
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VpsOperationLog> queryVpsOperationLogList(VpsOperationLog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return vpsOperationLogDao.queryVpsOperationLogList(map);
	}

	/**
	 * 查询日志count
	 * @param queryBean
	 * @return
	 */
	public int queryVpsOperationLogCount(VpsOperationLog queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return vpsOperationLogDao.queryVpsOperationLogCount(map);
	}
}
