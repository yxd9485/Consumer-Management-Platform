package com.dbt.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;

/**
 * 文件名: HttpReq.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: http请求 <br>
 * 修改人: guchangpeng <br>
 * 修改时间：2014-7-18 上午10:36:26 <br>
 * 修改内容：新增 <br>
 */
public class HttpReq {
	private static Log log = LogFactory.getLog(HttpReq.class);

	public static enum PATH_TYPE {PATH, IPURL, DOMAINURL}
	/**
	 * 附件上传url
	 */
	public static final String ATTACH_UPLOAD_SERVICE_URL = PropertiesUtil
			.getPropertyValue("attach_upload_service_url");

	/**
	 * 处理http请求
	 *
	 * @param uri
	 * @param parameterStr
	 * @return
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public static JSONObject handerHttpReq(String uri, String parameterStr) {
		JSONObject jsonObject = null;
		HttpClient client = null;
		HttpPost post = null;
		HttpResponse response = null;
		try {
			client = new DefaultHttpClient();
			post = new HttpPost(uri);
			post.addHeader("Content-Type", "application/json");
			if (StringUtils.isNotBlank(parameterStr)) {
				byte[] data = parameterStr.getBytes("UTF-8");
				post.setEntity(new ByteArrayEntity(data));
			}
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String resBody = IOUtils.toString(entity.getContent(), "UTF-8");
//			log.error("resBody=" + resBody);
			if (response.getStatusLine().getStatusCode() == 200 && StringUtils.isNotBlank(resBody)) {
				jsonObject = JSON.parseObject(resBody);
			}
		} catch (UnsupportedEncodingException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists UnsupportedEncodingException error!",
					e);
		} catch (ClientProtocolException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists ClientProtocolException error!",
					e);
		} catch (IllegalStateException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists IllegalStateException error!",
					e);
		} catch (IOException e) {
			log.error("In the method HttpReq.handleHttpClient(String uri,String type) exists IOException error! connect fail");
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());// 释放实体流
				}
			} catch (IOException e) {
				log.error("HttpClient连接释放异常:", e);
			}
			try {
				if (null != post) {
					post.releaseConnection();// 释放HttpPost
				}
			} catch (Exception e) {
				log.error("HttpClient连接释放异常:", e);

			}
			try {
				if (null != client) {
					client.getConnectionManager().shutdown();// 释放HttpClient
				}
			} catch (Exception e) {
				log.error("HttpClient连接释放异常:", e);
			}
		}
		return jsonObject;
	}
	/**
	 * 处理http请求
	 *
	 * @param uri
	 * @param parameterStr
	 * @return
	 */
	public static String uploadImgFile(MultipartFile file) {
		return uploadImgFile(file, PATH_TYPE.PATH);
	}
	public static String uploadImgFile(MultipartFile file, HttpReq.PATH_TYPE type) {
		Map<String, Object> resultMap = HttpReq.uploadImgFileForUrl(file);
		String imgUrl = null;
		if (resultMap != null) {
			switch (type) {
				case IPURL:
					imgUrl = (String)resultMap.get("ipUrl");
					break;

				case DOMAINURL:
					imgUrl = (String)resultMap.get("domainUrl");
					break;

				default:
					imgUrl = (String)resultMap.get("path");
					break;
			}
		}
		return imgUrl;
	}
	/**
	 * 处理http请求
	 *
	 * @param uri
	 * @param parameterStr
	 * @return
	 */
	public static Map<String, Object> uploadImgFileForUrl(MultipartFile file) {
		Map<String, Object> resultMap = null;
		HttpClient client = null;
		HttpPost post = null;
		HttpResponse response = null;
		try {
			client = new DefaultHttpClient();
			post = new HttpPost(getUrl(0, DbContextHolder.getDBType(), ""));
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.addBinaryBody(file.getName(), file.getBytes(), ContentType.MULTIPART_FORM_DATA,
					StringUtils.isNotBlank(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName());
			post.setEntity(entityBuilder.build());
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String resBody = IOUtils.toString(entity.getContent(), "UTF-8");
			if (response.getStatusLine().getStatusCode() == 200 && StringUtils.isNotBlank(resBody)) {
				resultMap = JSON.parseObject(resBody);
			}
		} catch (UnsupportedEncodingException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists UnsupportedEncodingException error!",
					e);
		} catch (ClientProtocolException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists ClientProtocolException error!",
					e);
		} catch (IllegalStateException e) {
			log.error(
					"In the method HttpReq.handleHttpClient(String uri,String type) exists IllegalStateException error!",
					e);
		} catch (IOException e) {
			log.error("In the method HttpReq.handleHttpClient(String uri,String type) exists IOException error! connect fail");
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());// 释放实体流
				}
			} catch (IOException e) {
				log.error("HttpClient连接释放异常:", e);
			}
			try {
				if (null != post) {
					post.releaseConnection();// 释放HttpPost
				}
			} catch (Exception e) {
				log.error("HttpClient连接释放异常:", e);

			}
			try {
				if (null != client) {
					client.getConnectionManager().shutdown();// 释放HttpClient
				}
			} catch (Exception e) {
				log.error("HttpClient连接释放异常:", e);
			}
		}
		return resultMap;
	}

	/**
	 * 上传小票
	 *
	 * @param request
	 *            request对象
	 * @param uuid
	 *            当前用户key
	 * @type 上传文件类型
	 * @return 当前上传文件路径
	 */
	public static String uploadReceipt(String uuid, int type, String filepaths) {
		String path = null;
		try {
			JSONObject jsonObject = handerHttpReq(getUrl(type, uuid, filepaths), "");
			if (null != jsonObject) {
				path = jsonObject.get("path").toString();

			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return path;
	}

	private static String getUrl(int type, String uuid, String filepaths) {
		StringBuffer urlBuffer = new StringBuffer("");
		urlBuffer.append(ATTACH_UPLOAD_SERVICE_URL);
		urlBuffer.append("?uuid=").append(uuid).append("&type=").append(type);
		urlBuffer.append("&env=").append(StringUtils.defaultIfBlank(
				DatadicUtil.getDataDicValue(
						DatadicKey.dataDicCategory.FILTER_HTTP_URL,
						DatadicKey.filterHttpUrl.QRCODE_URL), "").indexOf("vjifen") >  -1 ? "test" : "online");
		switch (type) {
			case Constant.ATTACH_UPLOAD_TYPE.IMG_TYPE:
				urlBuffer.append("&resize=").append(Constant.booleanType.RIGHT_);// 需要压缩
				break;
			case Constant.ATTACH_UPLOAD_TYPE.RECEIPT_TYPE:
				urlBuffer.append("&resize=").append(Constant.booleanType.ERROR_);
				break;
			case Constant.ATTACH_UPLOAD_TYPE.WCT_PATH_TYPE:
				urlBuffer.append("&resize=").append(Constant.booleanType.ERROR_)
						.append("&filepaths=" + filepaths);
				break;
			case Constant.ATTACH_UPLOAD_TYPE.WCT_HEAD_TYPE:
				urlBuffer.append("&resize=").append(Constant.booleanType.ERROR_)
						.append("&filepath=" + filepaths);
				break;
		}
		return urlBuffer.toString();
	}
	public static JSONObject httpGet(String url){
		JSONObject jsonObject = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			String resBody = IOUtils.toString(entity.getContent(), "UTF-8");
			if (response.getStatusLine().getStatusCode() == 200 && StringUtils.isNotBlank(resBody)) {
				jsonObject = JSON.parseObject(resBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jsonObject;
	}
	
	/**
	 * 处理图片服务器的URL
	 * @param content
	 * @return
	 */
	public static String replaceImgUr(String content) {
		String regex = "http://\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:9009/images/vma/";
		return content == null ? null :content.replaceAll(regex, "https://img.vjifen.com/images/vma/");
	}
}
