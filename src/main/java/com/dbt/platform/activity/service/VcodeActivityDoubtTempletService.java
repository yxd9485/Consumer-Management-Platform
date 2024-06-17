package com.dbt.platform.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityDoubtTemplet;
import com.dbt.platform.activity.dao.IVcodeActivityDoubtTempletDao;

/**
 * 风控模板主表Service
 */
@Service
public class VcodeActivityDoubtTempletService extends BaseService<VcodeActivityDoubtTemplet> {

	@Autowired
	private IVcodeActivityDoubtTempletDao doubtTempletDao;
    
    /**
     * 获取列表
     */
    public List<VcodeActivityDoubtTemplet> queryForLst(VcodeActivityDoubtTemplet queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return doubtTempletDao.queryForLst(map);
    }

    /**
     * 获取列表记录总个数
     */
    public int queryForCount(VcodeActivityDoubtTemplet queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return doubtTempletDao.queryForCount(map);
    }

	/**
	 * 根据id查询实体对象
	 */
	public VcodeActivityDoubtTemplet findById(String infoKey) {
	    return doubtTempletDao.findById(infoKey);
	}

	/**
	 * 新增规则模板
	 * @throws Exception 
	 */
    public void addDoubtTemplet(VcodeActivityDoubtTemplet doubtTemplet) {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("doubtTemplet", "INFO_KEY", 
                    null, "TEMPLET_NAME", doubtTemplet.getTempletName()))) {
            throw new BusinessException("规则模板名称已存在");
        }
	    
	    // 保存模板主表
	    doubtTemplet.setInfoKey(UUID.randomUUID().toString());
		doubtTempletDao.create(doubtTemplet);
		
		logService.saveLog("doubtTemplet", Constant.OPERATION_LOG_TYPE.TYPE_1,
		            JSON.toJSONString(doubtTemplet), "创建规则模板:" + doubtTemplet.getTempletName());
	}

	/**
	 * 编辑返利规则模板
	 */
    public void updateDoubtTemplet(VcodeActivityDoubtTemplet doubtTemplet) {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("doubtTemplet", "INFO_KEY", 
                doubtTemplet.getInfoKey(), "TEMPLET_NAME", doubtTemplet.getTempletName()))) {
            throw new BusinessException("规则模板名称已存在");
        }

	    // 模板主表
		doubtTempletDao.update(doubtTemplet);
		
        logService.saveLog("doubtTemplet", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(doubtTemplet), "修改规则模板:" + doubtTemplet.getTempletName());
	}

	/**
	 * 删除返利规则模板
	 */
	public void deleteDoubtTemplet(String infoKey, String optUserKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", infoKey);
	    map.put("optUserKey", optUserKey);
		doubtTempletDao.deleteById(map);
	}
	
	/**
	 * 获取所有
	 * 
	 * @param status 模板状态：0未启用、1已启用
	 * @return
	 */
	public List<VcodeActivityDoubtTemplet> queryForAll(String status) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("status", status);
        return doubtTempletDao.queryForAll(map);
    }
}
