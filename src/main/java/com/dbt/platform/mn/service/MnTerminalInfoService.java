package com.dbt.platform.mn.service;


import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.mn.bean.MnTerminalInfoEntity;
import com.dbt.platform.mn.dao.IMnTerminalInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
@Service
public class MnTerminalInfoService extends BaseService<MnTerminalInfoEntity> {
    @Autowired
    private IMnTerminalInfoDao iMnTerminalInfoDao;
    public void removeAll() {
        iMnTerminalInfoDao.removeAll();
    }

    public void insert(MnTerminalInfoEntity reco) {
        iMnTerminalInfoDao.insert(reco);

    }

    public void deleteById(Long id) {
        iMnTerminalInfoDao.deleteById(id);
    }
}
