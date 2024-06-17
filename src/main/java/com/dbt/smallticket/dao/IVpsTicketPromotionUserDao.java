package com.dbt.smallticket.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.smallticket.bean.VpsTicketPromotionUser;
import com.dbt.smallticket.bean.VpsTicketWararea;

public interface IVpsTicketPromotionUserDao extends IBaseDao<VpsTicketPromotionUser> {
	
	public List<VpsTicketPromotionUser> queryForLst(Map<String, Object> queryMap);

	public int queryForCount(Map<String, Object> queryMap);

    /**
     * 依据手机号查询促销人员信息
     * @param phoneNum
     * @return
     */
    public VpsTicketPromotionUser findByPhoneNum(String phoneNum);
    
    /**
     * 依据用户主键查询促销人员信息
     * @param userKey
     * @return
     */
    public VpsTicketPromotionUser findByUserKey(String userKey);

    /**
     * 查询战区List
     * @return
     */
	public List<VpsTicketWararea> queryWarareaAll();

	public void insertPromotionUser(VpsTicketPromotionUser promotionUser);

	/**
	 * 	审核
	 * @param promotionUser
	 */
	public void updatePromotionUserForCheck(VpsTicketPromotionUser promotionUser);

	/**
	 * 	批量入库
	 * @param list
	 */
	public void batchInsert(List<VpsTicketPromotionUser> list);

	public List<String> queryRepetitionList(List<VpsTicketPromotionUser> list);
}
