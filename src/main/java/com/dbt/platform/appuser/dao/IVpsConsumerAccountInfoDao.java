package com.dbt.platform.appuser.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;







import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 文件名:IVpsConsumerAccountInfoDao.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: 消费者账户信息dao<br>
* 修改人: cpgu<br>
* 修改时间：2016-04-21 11:10:31<br>
* 修改内容：新增<br>
*/
public interface IVpsConsumerAccountInfoDao extends IBaseDao<VpsConsumerAccountInfo>
{
    /**
     * 更新个人账户剩余金额
     * @param map keys:userKey、vpoint、money(单位:元)
     *//*
    public void updateConsumerAccountInfoByUserkey(Map<String, Object> map);

	*//**
	 * 退还提现金额
	 * @param map keys:userKey、money(单位:元)
	 *//*
	public void updateConsumerAccountInfoByUserkeyForBack(Map<String, Object> map);

	*//**
	 * 根据userkey查询账户
	 * @param userKey
	 * @return
	 *//*
	public VpsConsumerAccountInfo queryConsumerAccountInfoByUserKey(String userKey);

	*//**
	 * 根据userkey查询账户
	 * @param userKey
	 * @return
	 *//*
	public VpsConsumerAccountInfo queryConsumerAccountInfoByUserKeyForUpdate(String userKey);

	*//**
	 * 将个人账户剩余金额置为零
	 * @param userKey
	 *//*
	public void resetConsumerAccountInfoByUserkey(String userKey);

	*//**
	 * 扣减用户剩余金额及积分
	 *
	 * @param map keys:userKey、vpoint、money(单位:元)
	 *//*
	public void subtractConsumerAccountInfoByUserkey(Map<String, Object> map);*/

	/**
	 * 根据用户查询积分
	 * @param map keys:userKey、vpoint、money(单位:元)
	 */
	List<VpsConsumerAccountInfo> findUserVpoints(HashMap<String, Object> map);

	/**
	 * 查询用户扫码详情
	 * @param map keys:userKey、vpoint、money(单位:元)
	 */
    List<VpsConsumerAccountInfo> findUserScanList(HashMap<String,Object> map);

	/**
	 * 查询用户扫码详情总数
	 * @param
	 */
	Integer countUserScanList(String  userKey);

   /**
    *  通过V码查询信息
    * @param
    */
    VpsConsumerAccountInfo findQrcodeContent(HashMap<String,Object> map);
    
   	/**
	 * 根据用户查询积分
	 * @param map keys:userKey、vpoint、money(单位:元)
	 */
     Integer findUserVpointsCount (HashMap<String, Object> map);

     /**
      * 获取剩余积分大于0的用户
      * @return
      */
     List<VpsConsumerAccountInfo> queryUserByVpoints();
     
     /**
      * 更新账户余额
      * @param map keys:userKey、vpoint、money(单位:元)
      */
     public void updateConsumerAccountInfo(Map<String, Object> map);

     /**
      * 撤单返还账户积分
      * @param map
      */
     public void updateForRevokeOrder(Map<String, Object> map);
     
     /**
      * 获取用户帐户信息
      * 
      * @param map keys:userKey、forUpdate
      * @return
      */
     public VpsConsumerAccountInfo findByUserKey(Map<String, Object> map);
     
     /**
      * 增加指定帐户金额并清零冻结金额
      * 
      * @param map keys:accountKey、surplusMoney
      */
     public void updateFreezeMoney(Map<String, Object> map);

	/**
      * 批量更新用户积分
      * @param map
      */
	 public void batchUpdateUserVpoints(Map<String, Object> map);

	/**
	  * 更新金额
	  * @param map
	  */
	void executeAddUserAccountPoints(Map<String, Object> map);

	void updateBatchVpoints(Map<String, Object> map);
}