package com.dbt.platform.piclib.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.piclib.bean.PicLibrary;
import com.dbt.platform.piclib.dao.IPicLibDao;

/**
 * @Author bin.zhang
 **/
@Service
public class PicLibService extends BaseService<PicLibrary> {
    @Autowired
    private IPicLibDao picLibDao;
    public List<PicLibrary> findPicLib(PicLibrary queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return picLibDao.findPicLib(map);
    }

    public void addPicLib(PicLibrary picLibrary) {
         picLibDao.addPicLib(picLibrary);
    }

    public void deleteById(List<String> keys) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKeyList", keys);
        picLibDao.deleteByIds(map);
    }

    public PicLibrary findById(String key) {
        return picLibDao.findPicLibByKey(key);
    }

    public void updatePicLib(PicLibrary picLibrary) {
         picLibDao.updatePicLib(picLibrary);
    }

    public int findPicLibCount(PicLibrary queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return picLibDao.findPicLibCount(map);
    }

    public void doBatchPicLibUpdate(List<String> infoKeys, PicLibrary queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("infoKeyList", infoKeys);
         picLibDao.doBatchPicLibUpdate(map);
    }

    public List<PicLibrary> queryAll(PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageInfo", pageInfo);
        return picLibDao.queryAll(map);
    }

    public void setPicUnDefault(PicLibrary picLibrary) {
        picLibDao.setPicUnDefault(picLibrary);
    }
    
    /**
     * 获取图片服务器品牌类型
     * @return
     */
    public Map<String, String> getPicBrandType() {
        Map<String, String> picBrandMap = new LinkedHashMap<String, String>();
        String[] brandAry = StringUtils.defaultString(DatadicUtil.getDataDicValue(DatadicUtil
                .dataDicCategory.DATA_CONSTANT_CONFIG, DatadicUtil.dataDic.dataConstantConfig.PIC_BRAND_TYPE)).split(";");
        String[] itemAry = null;
        for (String item : brandAry) {
            itemAry = item.split(":");
            if (itemAry.length != 2) continue;
            picBrandMap.put(itemAry[0], itemAry[1]);
        }
        return picBrandMap;
    }
}
