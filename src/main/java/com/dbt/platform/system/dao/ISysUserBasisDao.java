package com.dbt.platform.system.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author RoyFu
 * @createTime 2016年4月20日 上午10:41:10
 * @description
 */

public interface ISysUserBasisDao extends IBaseDao<SysUserBasis> {

	public SysUserBasis loadUserByUserName(@Param("userName") String userName, @Param("unionid") String unionid);

	public void updatePassword(SysUserBasis user);

    public List<SysUserBasis> findUserList();

}
