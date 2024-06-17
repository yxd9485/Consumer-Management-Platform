package com.dbt.vpointsshop.dao;




import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpsOrderComment;

public interface VpsOrderCommentDao extends IBaseDao<VpsOrderComment>{

	List<VpsOrderComment> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);

    List<VpsOrderComment> queryCommentImg(String expireTime);

    void updateImg(VpsOrderComment c);
}
