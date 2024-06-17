/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2016-8-25 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.wechat.bean.CreateQrcode;
import com.dbt.framework.wechat.bean.QrcodeActionInfo;
import com.dbt.framework.wechat.bean.QrcodeScene;
import org.springframework.web.multipart.MultipartFile;


/**
 * The Class WechatUtil.
 */
public class WechatUtil {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(WechatUtil.class);
	
	
	/**
	 * 消息类型.    
	 *
	 * @date 2014-07-14
	 */
	public static interface msgType {
		
		/**  发送文本消息 *. */
		public static String TYPE_TEXT = "text";
		
		/**  发送图片消息 *. */
		public static String TYPE_IMAGE = "image";
		
		/**  发送语音消息 *. */
		public static String TYPE_VOICE = "voice";
		
		/**  发送视频消息 *. */
		public static String TYPE_VIDEO = "video";
		
		/**  发送音乐消息 *. */
		public static String TYPE_MUSIC = "music";
		
		/**  发送图文消息 *. */
		public static String TYPE_NEWS = "news";
	}
	
	/**
	 * 发送消息给微信用户.
	 *
	 * @param openid 微信唯一id
	 * @param msgType 消息类型，默认为文本消息，传入null，为文本类型
	 * @param thirdpParameter 动态参数，为null、或为""，则表示没有第三方参数内容
	 * @return 发送是否成功
	 */
	private static int sendMsgCount = 0;
    public static void sendMsg(String appid, String openid, String msgType, String thirdpParameter) {
        // 获取公众号accessToken
        String accesstoken = null;
        String accessTokenKey =  appid + Constant.DBTSPLIT + "accessTokenKey";
        accesstoken = RedisApiUtil.getInstance().get(accessTokenKey);
        if(null == accesstoken){
            accesstoken = getAccessTokenByH5(appid);
            RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
        }

        // 发送消息内容
        if(null != accesstoken){
            String sendText = setMsgText(openid, msgType, thirdpParameter);// 发送消息内容
            JSONObject object = HttpReq.handerHttpReq(
                    PropertiesUtil.getPropertyValue("send_customer_service_msg") + accesstoken, sendText);// 发送客服消息回复报文
            logger.error("sendMsg------->"+object);
            if(object.containsKey("errcode")){
                String errcode = object.getString("errcode");
                if("40001".equals(errcode) || "42001".equals(errcode) || "40014".equals(errcode)){
                    sendMsgCount++;
                    accesstoken = getAccessTokenByH5(appid);
                    RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
                    if(sendMsgCount < 5){
                        sendMsg(appid, openid, msgType, thirdpParameter);
                    }else{
                        sendMsgCount = 0;
                    }
                }
            }
        }
    
    }
	
	/**
	 * 设置发送消息内容.
	 *
	 * @param openid the openid
	 * @param msgType the msg type
	 * @param thirdpParameter the thirdp parameter
	 * @return the string
	 */
	private static String setMsgText( String openid, String msgType,
			String thirdpParameter) {
		msgType = StringUtils.isBlank(msgType) ? WechatUtil.msgType.TYPE_TEXT : msgType;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", openid);
		jsonObject.put("msgtype", msgType);
		JSONObject contentJsonObject = new JSONObject();
		// 文本类型
		if (WechatUtil.msgType.TYPE_TEXT.equals(msgType)) {
			contentJsonObject.put("content", thirdpParameter);
			jsonObject.put("text", contentJsonObject);
		}
		// 图文详情
		else if (WechatUtil.msgType.TYPE_NEWS.equals(msgType)) {
			JSONArray articlesJsonArr = new JSONArray();
			thirdpParameter = null == thirdpParameter ? "" : thirdpParameter;
			String[] thirdpParameterArr =
					thirdpParameter.split(Constant.THREEPAR_SPLIT_CHART);
			JSONObject articlesJsonObject = new JSONObject();
			if (null != thirdpParameterArr) {
				// title,description,url,picurl
				if (thirdpParameterArr.length > 0) {
					articlesJsonObject.put("title", thirdpParameterArr[0]);
				}
				if (thirdpParameterArr.length > 1) {
					if(!thirdpParameterArr[1].equals("null")){
						articlesJsonObject.put("description", thirdpParameterArr[1]);
					}
				}
				if (thirdpParameterArr.length > 2) {
					articlesJsonObject.put("url", thirdpParameterArr[2]);
				}
				if (thirdpParameter.length() > 3) {
					articlesJsonObject.put("picurl", thirdpParameterArr[3]);
				}
			}
			articlesJsonArr.add(articlesJsonObject);
			contentJsonObject.put("articles", articlesJsonArr);
			jsonObject.put("news", contentJsonObject);
		}
		return jsonObject.toJSONString();
	}
	
	/**
     * 发送模板消息.
     */
    private static int sendTemplateMsgCount = 0;
    public static void sendTemplateMsg(String sendText){
    	String company_appid = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                DatadicKey.filterWxPayTemplateInfo.COMPANY_APPID);
        sendTemplateMsg(company_appid, sendText);
    }
    public static void sendTemplateMsg(String appid, String sendText){
        
        // 获取公众号accessToken
        String accesstoken = null;
        String accessTokenKey =  appid + Constant.DBTSPLIT + "accessTokenKey";
        accesstoken = RedisApiUtil.getInstance().get(accessTokenKey);
        if(null == accesstoken){
            accesstoken = getAccessTokenByH5(appid);
            RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
        }

        // 发送消息内容
        if(null != accesstoken){
        	String url = PropertiesUtil.getPropertyValue("send_template_service_msg") + accesstoken;
            JSONObject object = HttpReq.handerHttpReq(url, sendText);
            logger.error("推送消息url------->"+url);
            logger.error("推送消息内容：------->"+sendText);
            logger.error("推送消息结果：------->"+object);
            if(object.containsKey("errcode")){
                String errcode = object.getString("errcode");
                if("40001".equals(errcode) || "42001".equals(errcode) || "40014".equals(errcode)){
                    sendTemplateMsgCount++;
                    accesstoken = getAccessTokenByH5(appid);
                    RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
                    if(sendTemplateMsgCount < 5){
                        sendTemplateMsg(appid, sendText);
                    }else{
                        sendTemplateMsgCount = 0;
                    }
                }
            }
        }
    }
    
    /**
     * 发送小程序订阅消息.
     * @param sendText
     * @param appid
     * @throws UnsupportedEncodingException
     */
    public static boolean sendAppletTemplateMsg(String sendText, String appid, String appsec) 
        throws UnsupportedEncodingException {
    	logger.error("发送内容：" + sendText);
        String accesstoken = null;
        String accessTokenKey =  appid + Constant.DBTSPLIT + "accessTokenKey";
        accesstoken = RedisApiUtil.getInstance().get(accessTokenKey);
        if(null == accesstoken){
            //暂时直接调用微信获取accesstoken（前端没有小程序获取token的公共方法）
            // accesstoken = getAccessToken(appid, appsec);
            accesstoken = getAccessTokenAppletByH5(appid);
            RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
        }
        
        // 发送消息内容
        JSONObject object = HttpReq.handerHttpReq(
                PropertiesUtil.getPropertyValue("send_applet_subscribe_service_msg")
                        + accesstoken, sendText);// 发送客服消息回复报文
        logger.error("sendAppletTemplateMsg------->"+object + "-----appid=" + appid + ",----accesstoken=" + accesstoken);
        if(object.containsKey("errcode")){
             String errcode = object.getString("errcode");
             if ("0".equals(errcode)) {
                 return true;
             } else if("40001".equals(errcode) || "42001".equals(errcode) || "40014".equals(errcode)){
                sendTemplateMsgCount++;
                // accesstoken = getAccessToken(appid, appsec);
                accesstoken = getAccessTokenAppletByH5(appid);
                RedisApiUtil.getInstance().set(accessTokenKey, accesstoken, 5400); // 过期时间1.5小时
                if(sendTemplateMsgCount < 5){
                 return sendAppletTemplateMsg(sendText, appid, appsec);
                }else{
                    sendTemplateMsgCount = 0;
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
	
	/**
	 * 通过前端H5获取AccessToken
	 * @param appid
	 * @return
	 */
	public static String getAccessTokenByH5(String appid){
		String url = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL, 
                DatadicKey.filterHttpUrl.WCT_TOKEN_URL_H5) + appid;
		JSONObject accessTokenJsonObject =
				HttpReq.handerHttpReq(url, null);
		logger.error("获取token：------->"+url +"，返回结果" +accessTokenJsonObject);
		String accesstoken = accessTokenJsonObject.getString("access_token");
		return accesstoken;
	}
    
    /**
     * 直接调用微信获取accesstoken.
     *
     * @return the access token
     */
	@Deprecated
    public static String getAccessToken(String appid, String appsec) {
        JSONObject replyJsonObject = getAccessTokenReplyJsonObject(appid, appsec);
        // 得到accessToken
        String accesstoken = replyJsonObject.getString("access_token");
        return accesstoken;
    }
    
    /**
     * Gets the access token reply json object.
     *
     * @return the access token reply json object
     */
	@Deprecated
    public static JSONObject getAccessTokenReplyJsonObject(String appid, String appsec) {
        JSONObject accessTokenJsonObject =
                HttpReq.handerHttpReq(getInterfaceUri(appid, appsec), null);// 获取accessToken
        return accessTokenJsonObject;
    }
    
    /**
     * 根据类型获取接口端url.
     *
     * @return the interface uri
     */
    private static String getInterfaceUri(String appid, String appsec) {
        StringBuilder sbBuilder = new StringBuilder(120);
        sbBuilder.append(PropertiesUtil.getPropertyValue("wct_token_url"));
        sbBuilder.append(appid);
        sbBuilder.append("&secret=").append(appsec);
        return sbBuilder.toString();
    }
	
	/**
	 * 生成二维码
	 * @param expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
	 * @param action_name	二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
	 * @param action_info	二维码详细信息
	 * @param scene_id	场景值ID（id和str只用一个即可），临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @param scene_str	场景值ID（id和str只用一个即可）（字符串形式的ID），字符串类型，长度限制为1到64
	 * @return JSONObject 
	 * 正确的Json返回结果:{"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm3sUw==",
	 *  					"expire_seconds":60,"url":"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI"}
	 *  ticket	获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
	 *	expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
	 *	url	二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	 */
	public static JSONObject CreateQrcode(String expire_seconds, String action_name, String scene_id, String scene_str) {
		// 初始二维码bean
		QrcodeScene scene = new QrcodeScene(scene_id, scene_str);
		QrcodeActionInfo actionInfo = new QrcodeActionInfo(scene);
		CreateQrcode createQrcode = new CreateQrcode(expire_seconds, action_name, actionInfo);
		
		// 获取accesstoken
		String accesstoken = getAccessTokenByH5(DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                DatadicKey.filterWxPayTemplateInfo.ACTIVATE_APPID));
		
		// 生成二维码
		JSONObject jsonObject = HttpReq.handerHttpReq(
				PropertiesUtil.getPropertyValue("create_qrcode_service_url") 
						+ accesstoken, JSON.toJSONString(createQrcode));
		return jsonObject;
	}
	/**
	 * 通过前端小程序获取AccessToken
	 * @return
	 */
	public static String getAccessTokenAppletByH5(String appid){
		String provinceCode = StringUtils.defaultIfBlank(Constant.SERVERMAP.get(appid), "all");
		String url = PropertiesUtil.getPropertyValue("wct_token_applet_h5_url");
//		String url = "https://xcxact.vjifen.com/wx/getAccessToken";
		Map<String,String> param = new HashMap<>();
		param.put("provinceCode", provinceCode);
		JSONObject accessTokenJsonObject =
				HttpReq.handerHttpReq(url, JSONObject.toJSONString(param));
		String accesstoken = accessTokenJsonObject.getString("access_token");
		return accesstoken;
	}
	/**
	 *  恶意图片过滤
	 * @param multipartFile
	 * @return
	 */
	public static Boolean checkPic(MultipartFile multipartFile) {
		try {
		    String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
		            .FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
			String accessToken = getAccessTokenAppletByH5(appid);
			CloseableHttpClient httpclient = HttpClients.createDefault();

			CloseableHttpResponse response = null;

			HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
			request.addHeader("Content-Type", "application/octet-stream");



			InputStream inputStream = multipartFile.getInputStream();

			byte[] byt = new byte[inputStream.available()];
			inputStream.read(byt);
			request.setEntity(new ByteArrayEntity(byt, ContentType.create("image/jpg")));


			response = httpclient.execute(request);
			HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
			JSONObject jso = JSONObject.parseObject(result);
			System.out.println(jso + "-------------验证效果");


			Object errcode = jso.get("errcode");
			int errCode = (int) errcode;
			if (errCode == 0) {
				return true;
			} else if (errCode == 87014) {
				System.out.println("图片内容违规-----------");
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------调用腾讯内容过滤系统出错------------------");
			return true;
		}
	}
	
}
