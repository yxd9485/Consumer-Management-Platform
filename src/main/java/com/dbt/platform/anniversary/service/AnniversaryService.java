package com.dbt.platform.anniversary.service;


import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.anniversary.bean.VpsAnniversaryShare;
import com.dbt.platform.anniversary.dao.VpsAnniversaryShareDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LiangRunBin
 * @create-date: 2023/12/19 17:26
 */
@Service
public class AnniversaryService {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    VpsAnniversaryShareDao vpsAnniversaryShareDao;

    //获取私享活动列表
    public List<VpsAnniversaryShare> queryShareForList(VpsAnniversaryShare queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);
        return vpsAnniversaryShareDao.queryForLst(queryMap);
    }

    //获取私享活动列表
    public int queryShareForCount(VpsAnniversaryShare queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return vpsAnniversaryShareDao.queryForCount(queryMap);
    }

    //新增私享活动列表
    public int insertAnniversaryShare(VpsAnniversaryShare vpsAnniversaryShare, SysUserBasis currentUser) {
        vpsAnniversaryShare.setCreGmt(DateUtil.getDateTime());
        vpsAnniversaryShare.setCreUser(currentUser.getUserName());
        vpsAnniversaryShare.setModGmt(DateUtil.getDateTime());
        vpsAnniversaryShare.setModUser(currentUser.getUserName());
        return vpsAnniversaryShareDao.insert(vpsAnniversaryShare);
    }
    //新增私享活动列表
    public int updateByKey(VpsAnniversaryShare vpsAnniversaryShare, SysUserBasis currentUser) {
        vpsAnniversaryShare.setModGmt(DateUtil.getDateTime());
        vpsAnniversaryShare.setModUser(currentUser.getUserName());
        return vpsAnniversaryShareDao.update(vpsAnniversaryShare);
    }

    public int deleteAnniversaryShare(String shareKey) {
        return vpsAnniversaryShareDao.deleteByKey(shareKey);
    }

    public VpsAnniversaryShare queryByKey(String shareKey) {
        return vpsAnniversaryShareDao.queryByKey(shareKey);
    }


}
