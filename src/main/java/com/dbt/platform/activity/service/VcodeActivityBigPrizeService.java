package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.dao.IVcodeActivityBigPrizeDao;

/**
 * @Description: 实物奖service
 * @Author bin.zhang
 **/
@Service
public class VcodeActivityBigPrizeService extends BaseService<VcodePrizeBasicInfo> {
    /**
     * {@link IVcodeActivityBigPrizeDao}
     */
    @Autowired
    private IVcodeActivityBigPrizeDao vcodeActivityBigPrizeDao;

    public List<VcodePrizeBasicInfo> queryBigPrize(
            VcodePrizeBasicInfo queryBean, PageOrderInfo pageInfo) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        List<VcodePrizeBasicInfo> bigPrizeList = vcodeActivityBigPrizeDao.queryForLst(map);
        return bigPrizeList;
    }

    /**
     * 查询记录条数
     * @param queryBean
     * @return
     */
    public int countVcodeActivityList(VcodePrizeBasicInfo queryBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
        return vcodeActivityBigPrizeDao.queryForCount(map);
    }

    /**
     * 新增记录
     * @param vcodePrizeBasicInfo
     * @return
     */
    public void writePrizeInfo(VcodePrizeBasicInfo vcodePrizeBasicInfo) {
         vcodeActivityBigPrizeDao.writePrize(vcodePrizeBasicInfo);

         logService.saveLog("vcodePrizeBasicInfo", Constant.OPERATION_LOG_TYPE.TYPE_1,
                 JSON.toJSONString(vcodePrizeBasicInfo), "修改大奖信息:" + vcodePrizeBasicInfo.getPrizeNo());
    }

    public void delPrize(String infoKey) {
        VcodePrizeBasicInfo tempPrizeBasicInfo = vcodeActivityBigPrizeDao.findBigPrizeByKey(infoKey);
        vcodeActivityBigPrizeDao.delPrize(infoKey);
        
        logService.saveLog("vcodePrizeBasicInfo", Constant.OPERATION_LOG_TYPE.TYPE_3,
                JSON.toJSONString(tempPrizeBasicInfo), "删除大奖信息:" + tempPrizeBasicInfo.getPrizeNo());
    }


    public void updatePrizeInfo(VcodePrizeBasicInfo vcodePrizeBasicInfo) {

        VcodePrizeBasicInfo tempPrizeBasicInfo = vcodeActivityBigPrizeDao.findBigPrizeByKey(vcodePrizeBasicInfo.getInfoKey());
        
        vcodeActivityBigPrizeDao.updatePrizeInfo(vcodePrizeBasicInfo);

        logService.saveLog("vcodePrizeBasicInfo", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(tempPrizeBasicInfo), "修改大奖信息:" + tempPrizeBasicInfo.getPrizeNo());
    }

    public VcodePrizeBasicInfo findBigPrizeByKey(String infoKey) {
        return vcodeActivityBigPrizeDao.findBigPrizeByKey(infoKey);
    }

    public VcodePrizeBasicInfo findBigPrizeByPrizeNo(String prizeNo) {
        return vcodeActivityBigPrizeDao.findBigPrizeByPrizeNo(prizeNo);
    }

    public List<VcodePrizeBasicInfo> queryAllBigPrize() {
        return vcodeActivityBigPrizeDao.queryAllBigPrize();
    }
    /**
     * 获取所有大奖奖项
     * @return
     */
    public List<VcodePrizeBasicInfo> getBigPrizeList() throws Exception {
        return vcodeActivityBigPrizeDao.queryAllBigPrize();
    }

    /**
     * 获取实物奖map
     * @return
     */
    public Map<String, VcodePrizeBasicInfo> getPrizeMap() throws Exception {
        List<VcodePrizeBasicInfo> bigPrizes = getBigPrizeList();
        Map<String, VcodePrizeBasicInfo> prizeMap = new HashMap<>();
        for (VcodePrizeBasicInfo p : bigPrizes) {
            prizeMap.put(p.getPrizeNo(), p);
        }
        return prizeMap;
    }
    /**
     * 获取实物奖类型map
     * @return
     */
    public Map<String, String> getPrizeTypeMap() throws Exception {
        List<VcodePrizeBasicInfo> bigPrizes = getBigPrizeList();
        Map<String, String> prizeMap = new LinkedHashMap<>();
        for (VcodePrizeBasicInfo p : bigPrizes) {
            prizeMap.put(p.getPrizeNo(), p.getPrizeName());
        }
        return prizeMap;
    }

    /**
     * 获取需要回收的实物奖
     * @return
     */
    public List<String> getRecoveryPrizes() throws Exception {
        List<VcodePrizeBasicInfo> bigPrizes = getBigPrizeList();
        List<String> recoveryPrizes = new ArrayList<>();
        for (VcodePrizeBasicInfo p : bigPrizes)  {
            if(p.getIsRecycle().equals("0")){
                recoveryPrizes.add(p.getPrizeNo());
            }
        }
        return recoveryPrizes;
    }

	public int queryTerminalPrizeCount(VcodePrizeBasicInfo queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		return vcodeActivityBigPrizeDao.queryTerminalPrizeCount(map);
	}

	public List<VcodePrizeBasicInfo> queryTerminalPrizeLst(VcodePrizeBasicInfo queryBean, PageOrderInfo pageOrderInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageOrderInfo);
		return vcodeActivityBigPrizeDao.queryTerminalPrizeLst(map);
	}


}
