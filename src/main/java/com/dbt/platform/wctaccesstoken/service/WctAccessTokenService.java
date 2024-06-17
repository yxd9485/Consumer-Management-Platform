package com.dbt.platform.wctaccesstoken.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.json.reply.BaseReplyResult;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.wctaccesstoken.bean.WctAccessToken;
import com.dbt.platform.wctaccesstoken.dao.IWctAccessTokenDao;

/**
 * 文件名：WctAccessTokenService.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 微信公众号唯一票据信息<br>
 * 修改人: guchangpeng<br>
 * 修改时间：2014-07-03 17:57:34<br>
 * 修改内容：新增<br>
 */
@Service("wctAccessTokenService")
public class WctAccessTokenService {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private IWctAccessTokenDao wctAccessTokenDao;

	public WctAccessToken findWctAccessTokenById(String id) {
		return wctAccessTokenDao.findById(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addWctAccessToken(WctAccessToken wctAccessToken) throws Exception {
		wctAccessTokenDao.create(wctAccessToken);
//		CacheUtil.removeGroupByKey(CacheKey.cacheKey.KEY_WCTACCESSTOKEN + Constant.DBTSPLIT
//				+ wctAccessToken.getEnvironment());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateWctAccessToken(WctAccessToken wctAccessToken) throws Exception {
		wctAccessTokenDao.update(wctAccessToken);
//		CacheUtil.removeGroupByKey(CacheKey.cacheKey.KEY_WCTACCESSTOKEN + Constant.DBTSPLIT
//				+ wctAccessToken.getEnvironment());
	}


	private long setWctAccessToken(WctAccessToken wctAccessTokenReply, WctAccessToken wctToken) {
		int openExpiresIn = Integer.parseInt(wctToken.getExpires_in());// 凭证有效时间
		Date date = DateUtil.add(new Date(), openExpiresIn - 1800, Calendar.SECOND);
		wctAccessTokenReply.setExpiretime(DateUtil.getDateTime(date,
				DateUtil.DEFAULT_DATETIME_FORMAT));
		wctAccessTokenReply.setAccesstoken(wctToken.getAccess_token());
		return date.getTime();
	}

	private StringBuffer getUri(String appid, String appsec) {
		StringBuffer uriBuffer = new StringBuffer("");
		// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=
		uriBuffer.append(PropertiesUtil.getPropertyValue("wct_token_url"));
		uriBuffer.append(appid);
		// &secret=
		uriBuffer.append("&secret=").append(appsec);
		return uriBuffer;
	}

	/**
	 * 根据appid查询公众号唯一票据
	 * 
	 * @param appid
	 *            appid
	 * @param falg
	 *            是否强制
	 * @return WctAccessToken 对象
	 */
	public WctAccessToken queryWctAccessTokenByAppid(String appid) {
		Map<String,Object> appidmap = new HashMap<String,Object>();
		appidmap.put("appid", appid);
		WctAccessToken wctAccessToken = wctAccessTokenDao
				.queryWctAccessTokenByAppid(appidmap);
		return wctAccessToken;
	}

	
	/**
	 * 获取微信公众号token
	 * 
	 * @param filterPr
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String getAccessToken() throws Exception {
		String appId = PropertiesUtil.getPropertyValue("appid");
		String appSec = PropertiesUtil.getPropertyValue("appsec");
		if (StringUtils.isBlank(appId) && StringUtils.isBlank(appSec)) {
			throw new NullPointerException("this appid with appSec is null!");
		}
		
		BaseReplyResult baseReplyResult = new BaseReplyResult();
		baseReplyResult.setBusinessCode(Constant.ResultCode.SUCCESS);
		baseReplyResult.setCode(Constant.ResultCode.SUCCESS);

		WctAccessToken wctAccessToken = queryWctAccessTokenByAppid(appId);
		long timeMillis = 0l;// 返回的毫秒数
		if (null == wctAccessToken) {
			// 第一次 请求入库
			wctAccessToken = new WctAccessToken();
			wctAccessToken.setAccesskey(UUIDTools.getInstance().getUUID());
			timeMillis = setWctAccessToken(wctAccessToken, getHttpWctToken(baseReplyResult, appId,
					appSec));// 设置WctAccessToken对象
			wctAccessToken.setEnvironment(appId);
			addWctAccessToken(wctAccessToken);
		}else{
			String expiretimeStr = wctAccessToken.getExpiretime();
			if (StringUtils.isNotBlank(expiretimeStr)) {
				Date date = DateUtil.parse(expiretimeStr, DateUtil.DEFAULT_DATETIME_FORMAT);
				timeMillis = date.getTime();
				long currentTime = System.currentTimeMillis();
				// 超时，做修改
				if ((date.getTime() - currentTime) <= 0) {
					timeMillis = setWctAccessToken(wctAccessToken, getHttpWctToken(
							baseReplyResult, appId, appSec));
					wctAccessToken.setEnvironment(appId);
					updateWctAccessToken(wctAccessToken);
				}else {
					wctAccessToken = queryWctAccessTokenByAppid(appId);
				}
			}
		}
		return wctAccessToken.getAccesstoken();
	}
	
	/**
	 * 访问微信url，获取微信token
	 * 
	 * @return WctAccessToken 微信公众号唯一票据信息
	 */
	private WctAccessToken getHttpWctToken(BaseReplyResult replyResult, String appid, String appsec) {
		WctAccessToken ectAccessToken = null;
		try {
			StringBuffer uriBuffer = getUri(appid, appsec);
			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(uriBuffer.toString());
			post.addHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String resBody = IOUtils.toString(entity.getContent(), "UTF-8");
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				ectAccessToken = JSON.toJavaObject(parseJsonstrToJsonObject(resBody), WctAccessToken.class);
			} else {
				replyResult.setBusinessCode(Constant.ResultCode.FILURE);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("In the method WctAccessTokenAction.getWctToken() exists UnsupportedEncodingException error!", e);
		} catch (ClientProtocolException e) {
			log.error("In the method WctAccessTokenAction.getWctToken() exists ClientProtocolException error!", e);
		} catch (IllegalStateException e) {
			log.error("In the method WctAccessTokenAction.getWctToken() exists IllegalStateException error!", e);
		} catch (IOException e) {
			log.error("In the method WctAccessTokenAction.getWctToken() exists IOException error!",	e);
		} catch (Exception e) {
			log.error("In the method WctAccessTokenAction.getWctToken() exists error!", e);
		}
		return ectAccessToken;
	}
	
	/**
	 * <pre>
	 * 请求Json字符串转化为Json对象.
	 * </pre>
	 *
	 * @param jsonString the json string
	 * @return the jSON object
	 * @author 孙顺博 2015-1-16
	 */
	protected JSONObject parseJsonstrToJsonObject(String jsonString) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		return JSON.parseObject(jsonString);
	}
	
	/**
	 * <pre>
	 * 转换请求报文为Json对象.
	 * </pre>
	 *
	 * @param request the request
	 * @return the jSON object
	 * @throws Exception the exception
	 * @author 孙顺博 2015-1-16
	 */
	protected JSONObject parseRequestToJsonObject(HttpServletRequest request) throws Exception {
		return parseJsonstrToJsonObject(parseRequestToJsonstr(request));
	}
	
	/**
	 * <pre>
	 * 转换请求报文为Json字符串.
	 * </pre>
	 *
	 * @param request the request
	 * @return the string
	 * @throws Exception the exception
	 * @author 孙顺博 2015-1-16
	 */
	protected String parseRequestToJsonstr(HttpServletRequest request) throws Exception {
		StringBuffer sbBuffer;
		try {
			sbBuffer = new StringBuffer();
			InputStream inputStream = request.getInputStream();
			/** The Constant CACHESIZE. */
			byte[] b = new byte[2048];
			for (int n; (n = inputStream.read(b)) != -1;) {
				sbBuffer.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (Exception e) {
			log.error("解析请求报文出错：", e);
			throw e;
		}
		String jsonStr = sbBuffer.toString();
		return jsonStr;
	}
}
