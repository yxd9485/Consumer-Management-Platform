package com.dbt.platform.wctaccesstoken.dao;
  
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wctaccesstoken.bean.WctAccessToken;
/**
* 文件名:IWctAccessTokenDao.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: 微信公众号唯一票据信息<br>
* 修改人: guchangpeng<br>
* 修改时间：2014-07-03 17:57:34<br>
* 修改内容：新增<br>
*/
public interface IWctAccessTokenDao extends IBaseDao<WctAccessToken>
{ 
	/**
	 * 根据appid查询公众号唯一票据
	 * @param appid appid
	 * @return WctAccessToken 对象
	 */
	public WctAccessToken queryWctAccessTokenByAppid(Map<String,Object> map);
}