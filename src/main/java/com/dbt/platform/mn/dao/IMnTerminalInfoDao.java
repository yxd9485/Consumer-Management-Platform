package com.dbt.platform.mn.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.mn.bean.MnTerminalInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
public interface IMnTerminalInfoDao extends IBaseDao<MnTerminalInfoEntity> {

    void insert(MnTerminalInfoEntity reco);

    void removeAll();
    void deleteById(@Param("id") Long id);
}
