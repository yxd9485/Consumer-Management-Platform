package com.dbt.platform.mn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.mn.bean.MnDepartmentEntity;
import com.dbt.platform.mn.dao.IMnDepartmentDao;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
@Service
public class MnDepartmentService extends BaseService<MnDepartmentEntity>{
    @Autowired
    private IMnDepartmentDao iMnDepartmentDao;
    public void removeAll() {
        iMnDepartmentDao.removeAll();
    }

    public void save(MnDepartmentEntity mnDepartmentEntity) {
        iMnDepartmentDao.insert(mnDepartmentEntity);
    }

    public void deleteById(Long id) {
        iMnDepartmentDao.deleteById(id);
    }
    
    /**
     * 查询组织架构List
     * @param pid	父Id，从0开始，-1不查询
     * @param level	级别：2冰品事业部、3销管中心、4大区、5省区、6市区、7区/县
     * @return
     */
    public List<MnDepartmentEntity> queryListByMap(long pid, int level) {
    	Map<String, Object> map = new HashMap<>();
    	map.put("pid", pid);
    	map.put("level", level);
    	return iMnDepartmentDao.queryListByMap(map);
    }

	public MnDepartmentEntity findById(String id) {
		return iMnDepartmentDao.findById(id);
	}

//    public  List<MnDepartmentEntity>  queryList() {
////        iMnDepartmentDao.queryListByMap()
//    }
}
