package com.dbt.platform.marquee.service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.marquee.bean.MarqueeCogInfo;
import com.dbt.platform.marquee.dao.MarqueeInfoDao;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.service.VpsSysTicketInfoService;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.dao.IVpointsCouponCogDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarqueeInfoService {

    @Autowired
    private MarqueeInfoDao marqueeInfoDao;
    @Autowired
    private IVpointsCouponCogDao vpointsCouponDao;
    @Autowired
    private VpsSysTicketInfoService sysTicketInfoService;


    public List<MarqueeCogInfo> queryForLst(MarqueeCogInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return marqueeInfoDao.queryForLst(map);
    }

    public int queryForCount(MarqueeCogInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return marqueeInfoDao.queryForCount(map);
    }


    public void createMarqueeCog(MarqueeCogInfo marqueeCogInfo, SysUserBasis currentUser) {
        marqueeCogInfo.setInfoKey(UUIDTools.getInstance().getUUID());
        marqueeCogInfo.setCreateUser(currentUser.getUserName());
        marqueeCogInfo.setUpdateUser(currentUser.getUserName());
        marqueeCogInfo.setCreateTime(DateUtil.getDateTime());
        marqueeCogInfo.setUpdateTime(DateUtil.getDateTime());
        if (StringUtils.isNotBlank(marqueeCogInfo.getVpointsCog())) {
            String[] split = marqueeCogInfo.getVpointsCog().split(",");
            StringBuilder vpointsCog = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                vpointsCog.append(split[i]).append("-").append(split[i + 1]).append(",");
                i += 1;
            }
            marqueeCogInfo.setVpointsCog(vpointsCog.toString());
        }
        if (StringUtils.isNotBlank(marqueeCogInfo.getMoneyCog())) {
            String[] split = marqueeCogInfo.getMoneyCog().split(",");
            StringBuilder moneyCog = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                moneyCog.append(split[i]).append("-").append(split[i + 1]).append(",");
                i += 1;
            }
            marqueeCogInfo.setMoneyCog(moneyCog.toString());
        }

        marqueeInfoDao.create(marqueeCogInfo);

        // 清除缓存
        String redisKey = Constant.marquee.MARQUEECOGKEY + Constant.DBTSPLIT + marqueeCogInfo.getWinType();
        RedisApiUtil.getInstance().del(true, redisKey);
    }

    public MarqueeCogInfo findById(String infoKey) {
        return marqueeInfoDao.findById(infoKey);
    }

    // 判断添加或修改的跑马灯配置是否已存在
    public boolean existMarquee(String startTime, String endTime, String winType, String infoKey) {
        return marqueeInfoDao.existMarquee(startTime, endTime, winType, infoKey);
    }

    public void doMarqueeCogInfoEdit(MarqueeCogInfo marqueeCogInfo, SysUserBasis currentUser) {
        marqueeCogInfo.setUpdateUser(currentUser.getUserName());
        marqueeCogInfo.setUpdateTime(DateUtil.getDateTime());
        if (StringUtils.isNotBlank(marqueeCogInfo.getVpointsCog())) {
            String[] split = marqueeCogInfo.getVpointsCog().split(",");
            StringBuilder vpointsCog = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                vpointsCog.append(split[i]).append("-").append(split[i + 1]).append(",");
                i += 1;
            }
            marqueeCogInfo.setVpointsCog(vpointsCog.toString());
        }
        if (StringUtils.isNotBlank(marqueeCogInfo.getMoneyCog())) {
            String[] split = marqueeCogInfo.getMoneyCog().split(",");
            StringBuilder moneyCog = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                moneyCog.append(split[i]).append("-").append(split[i + 1]).append(",");
                i += 1;
            }
            marqueeCogInfo.setMoneyCog(moneyCog.toString());
        }
        marqueeInfoDao.update(marqueeCogInfo);

        // 清除缓存
        String redisKey = Constant.marquee.MARQUEECOGKEY + Constant.DBTSPLIT + marqueeCogInfo.getWinType();
        RedisApiUtil.getInstance().del(true, redisKey);
    }


    public Map<String, String> queryAllCouponCog() {

        LinkedHashMap<String, String> couponMap = new LinkedHashMap<>();

        List<VpointsCouponCog> vpointsCouponCogs = vpointsCouponDao.queryAllCouponCog();
        for (VpointsCouponCog vpointsCouponCog : vpointsCouponCogs) {
            couponMap.put(vpointsCouponCog.getCouponNo(), vpointsCouponCog.getCouponName());
        }

        List<VpsSysTicketInfo> sysTicketInfoList = sysTicketInfoService.localList();
        for (VpsSysTicketInfo vpsSysTicketInfo : sysTicketInfoList) {
            couponMap.put(vpsSysTicketInfo.getTicketNo(), vpsSysTicketInfo.getCategoryName() + "-" + vpsSysTicketInfo.getTicketName());
        }

        return couponMap;
    }

    public void doMarqueeCogInfoDelete(String infoKey, SysUserBasis currentUser) {
        marqueeInfoDao.deleteById(infoKey);
    }

    public void deleteMarqueeInfoByMarqueeCogInfo() {
        MarqueeCogInfo queryBean = new MarqueeCogInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", null);
        List<MarqueeCogInfo> marqueeCogInfos = marqueeInfoDao.queryForLst(map);
        for (MarqueeCogInfo marqueeCogInfo : marqueeCogInfos) {
            List<String> infoKeyList = marqueeInfoDao.queryNotDeleteMarqueeInfo(marqueeCogInfo.getInfoKey(), Integer.parseInt(marqueeCogInfo.getShowNum()));
            marqueeInfoDao.deleteMarqueeInfoByCogInfo(marqueeCogInfo.getInfoKey(), infoKeyList);
        }
    }
}
