package com.dbt.platform.publish.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbt.framework.util.RedisApiUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.publish.bean.VpsAdHome;
import com.dbt.platform.publish.bean.VpsAdShop;
import com.dbt.platform.publish.bean.VpsAdUp;
import com.dbt.platform.publish.dao.AdHomeDao;
import com.dbt.platform.publish.dao.AdShopDao;
import com.dbt.platform.publish.dao.AdUpDao;

@Service
public class AdPubService {
	@Autowired
	private AdUpDao adUpDao;
	@Autowired
	private AdHomeDao adHomeDao;
	@Autowired
	private AdShopDao adShopDao;
	@Autowired
	private CRMServiceServiceImpl crmService;

	public List<VpsAdUp> queryForLst(VpsAdUp queryBean, PageOrderInfo pageInfo) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    return adUpDao.queryForLst(queryMap);
	}


	public int queryForCount(VpsAdUp queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return adUpDao.queryForCount(queryMap);
	}


	public void updateVpsAdUp(VpsAdUp vpsAdUp) {
	    if (StringUtils.isNotBlank(vpsAdUp.getPopNum())) {
	        vpsAdUp.setPopNum(vpsAdUp.getPopNum().replace("，", ","));
	    }
	    if ("4".equals(vpsAdUp.getJumpTyp())
	            && !vpsAdUp.getJumpUrl().startsWith("/")) {
	        vpsAdUp.setJumpUrl("/" + vpsAdUp.getJumpUrl());
	    }
		adUpDao.updateVpsAdUp(vpsAdUp);
		
	}

	public VpsAdUp findById(String adUpKey) {
		return adUpDao.findById(adUpKey);
	}


	public void adPubDelete(String adPubKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("adPubKey", adPubKey);
	    adUpDao.deleteById(map);
	}


	public void addAdUp(VpsAdUp vpsAdUp) {
        if (StringUtils.isNotBlank(vpsAdUp.getPopNum())) {
            vpsAdUp.setPopNum(vpsAdUp.getPopNum().replace("，", ","));
        }
        if ("4".equals(vpsAdUp.getJumpTyp())
                && !vpsAdUp.getJumpUrl().startsWith("/")) {
            vpsAdUp.setJumpUrl("/" + vpsAdUp.getJumpUrl());
        }
		adUpDao.addAdUp(vpsAdUp);
		
	}


	public List<VpsAdHome> queryHomeAdForLst(VpsAdHome queryBean, PageOrderInfo pageInfo) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    return adHomeDao.queryForLst(queryMap);
	}


	public int queryHomeAdForCount(VpsAdHome queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return adHomeDao.queryForCount(queryMap);
	}


	public void addAdHome(VpsAdHome vpsAdHome) {
        if ("4".equals(vpsAdHome.getJumpTyp())
                && !vpsAdHome.getJumpUrl().startsWith("/")) {
            vpsAdHome.setJumpUrl("/" + vpsAdHome.getJumpUrl());
        }
		adHomeDao.addAdHome(vpsAdHome);
		
	}


	public void updateVpsAdHome(VpsAdHome vpsAdHome) {
        if ("4".equals(vpsAdHome.getJumpTyp())
                && !vpsAdHome.getJumpUrl().startsWith("/")) {
            vpsAdHome.setJumpUrl("/" + vpsAdHome.getJumpUrl());
        }
		adHomeDao.updateVpsAdHome(vpsAdHome);
		
	}


	public VpsAdHome findHomeAdById(String adHomeKey) {
		return adHomeDao.findById(adHomeKey);
	}


	public void adHomeDelete(String adHomeKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("adHomeKey", adHomeKey);
	    adHomeDao.deleteById(map);
		
	}


	public List<VpsAdShop> queryShopAdForLst(VpsAdShop queryBean, PageOrderInfo pageInfo) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    return adShopDao.queryForLst(queryMap);
	}


	public int queryShopAdForCount(VpsAdShop queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return adShopDao.queryForCount(queryMap);
	}


	public void addAdShop(VpsAdShop vpsAdShop) {
        if ("4".equals(vpsAdShop.getJumpTyp())
                && !vpsAdShop.getJumpUrl().startsWith("/")) {
            vpsAdShop.setJumpUrl("/" + vpsAdShop.getJumpUrl());
        }
		adShopDao.addAdShop(vpsAdShop);
	}


	public void adShopDelete(String adShopKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("adShopKey", adShopKey);
	    adShopDao.deleteById(map);
		
	}


	public VpsAdShop findShopAdById(String adShopKey) {
		return adShopDao.findById(adShopKey);
	}


	public void updateVpsAdShop(VpsAdShop vpsAdShop) {
        if ("4".equals(vpsAdShop.getJumpTyp())
                && !vpsAdShop.getJumpUrl().startsWith("/")) {
            vpsAdShop.setJumpUrl("/" + vpsAdShop.getJumpUrl());
        }
		adShopDao.updateVpsAdShop(vpsAdShop);
		
	}

	public List<VpsAdHome> queryAllAdHome() {
		return adHomeDao.queryAllAdHome();
	}

	public List<VpsAdShop> querAllAdShop() {
		return adShopDao.queryAllAdShop();
	}

    public List<VpsAdUp> quAllAdUp() {
		return adUpDao.quAllAdUp();
    }
    
    public void initCrmGroup(Model model) throws NoSuchAlgorithmException {
        String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_GROUP);
        if(DatadicUtil.isSwitchON(groupSwitch)) {
            model.addAttribute("groupList", crmService.queryVcodeActivityCrmGroup());
        }
        model.addAttribute("groupSwitch", DatadicUtil.isSwitchON(groupSwitch) ? "1" : "0");
    }
}
