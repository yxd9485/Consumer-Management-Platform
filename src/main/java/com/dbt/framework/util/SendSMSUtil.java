package com.dbt.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.platform.wctaccesstoken.bean.WechatRemindTemplateMsg;
import com.vjifen.module.message.dto.MessageInfo;
import com.vjifen.module.message.dto.ThirdMessageResult;
import com.vjifen.module.message.scan.MessageFactory;
import com.vjifen.module.message.scan.ThirdMessageService;

/**
 * 发送短信工具类
 * update 2020-06-18 整合消息服务
 * 
 */
public class SendSMSUtil {
    
    /** 短信码证码有效期(秒)  */
    public static final int VERI_CODE_VALID = 5 * 60;
    /** 验证码长度**/
    public static final int CAPTCHADIGIT = 4;
    
    // 一等奖
    public static final String SEND_TYPE_FRIST_PRIZE = "0";
    // 校验码
    public static final String SEND_TYPE_CHECK_CODE = "1";
    // 平台
    public static final String SEND_TYPE_CHECK_CODE_PLATFORM = "2";
	
	/** The log. */
	private static final Log LOG = LogFactory.getLog("SendSMS");
	
	private static MessageFactory factory = (MessageFactory) BeanFactoryUtil
			.getbeanFromWebContext("messageFactory");
	private static ThirdMessageService service = (ThirdMessageService) BeanFactoryUtil
			.getbeanFromWebContext("thirdMessageService");
    private static ThreadPoolTaskExecutor taskExecutor  = 
            (ThreadPoolTaskExecutor) BeanFactoryUtil.getbeanFromWebContext("taskExecutor");
	
	/**
	 * 新方法
	 * 
	 * @param args
	 * @throws IOException0:成功 -:失败
	 */
	public static String sendSms(String content, String dest) {
		String result = "";
		try {
			int idx=content.indexOf("【");
			//user-手机号&source-签名&contentText内容，标准模式下签名应该是独立的字段，现在内容和签名是一个字段，简单格式处理
			MessageInfo info=factory.getShortMsgMessageBuilder().simple().user(dest).source(content.substring(idx+1,content.length()-1)).contentText(content.substring(0, idx)).getMessageInfo();
			ThirdMessageResult messageResult=service.send(info);
			result=String.valueOf(messageResult.getState());//0=成功 其他编码失败
			LOG.info("optime:" + DateUtil.getDateTime() + "mobile:" + dest + ",status:success");// 成功记录
		} catch (Exception e) {
			LOG.info("optime:" + DateUtil.getDateTime() + "mobile:" + dest + ",status:failue");
			e.printStackTrace();
		} 
		return result;
	}

    /**
     * 新方法
     *
     * @param content
     * @throws String:成功 -:失败
     */
    public static String sendSms2(String content, String dest) {
        String result = "";
        try {
            int idxStart = content.indexOf("【");
            int idxEnd = content.lastIndexOf("】");
            String source = "";
            String contentText = "";
            if (idxStart != -1 && idxEnd != -1 & idxEnd > idxStart) {
                source = content.substring(idxStart + 1, idxEnd);
                contentText = content.substring(idxEnd + 1);
            }else {
                return "-1";
            }
            //user-手机号&source-签名&contentText内容，标准模式下签名应该是独立的字段，现在内容和签名是一个字段，简单格式处理
            MessageInfo info = factory.getShortMsgMessageBuilder().simple1().user(dest).source(source).contentText(contentText).getMessageInfo();
            ThirdMessageResult messageResult = service.send(info);
            result = String.valueOf(messageResult.getState());//0=成功 其他编码失败
            LOG.info("optime:" + DateUtil.getDateTime() + "mobile:" + dest + ",status:success");// 成功记录
        } catch (Exception e) {
            LOG.info("optime:" + DateUtil.getDateTime() + "mobile:" + dest + ",status:failue");
            e.printStackTrace();
        }
        return result;
    }
	
	
	/**
	 * 多个手机号发送（半角逗号隔开）
	 * 
	 * @param content 短信内容
	 * @param mobiles 手机号数组
	 * @throws IOException0:成功 -:失败
	 */
	public static void sendSmsByMobileArray(String content, String mobiles) {
		String[] mobileArray = mobiles.split(",");
		int idx = 0;
		String newContent = "";
		for (String mobile : mobileArray) {
			if(StringUtils.isNotBlank(mobile)){
				idx ++;
				// 达到短信内容不一样效果
				newContent = idx + Constant.DBTSPLIT + content; 
				try {
					String result=sendSms(newContent, mobile);
					LOG.error("发送手机号返回结果result=" + result + ",mobile:" + mobile);
				} catch (Exception e) {
					LOG.error("optime:" + DateUtil.getDateTime() + "mobile:" + mobile + ",status:failue");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
     * 获取验证码
     * @param phoneNum
     * @return
     */
    public static String getCaptcha(String phoneNum,String sendType) {
        String businessCode = Constant.ResultCode.FILURE;
        sendType = StringUtils.defaultIfBlank(sendType, SEND_TYPE_CHECK_CODE);
        phoneNum = StringUtils.defaultIfBlank(phoneNum, "").trim();
        if(StringUtils.isBlank(phoneNum)) return businessCode;
        
        // 生成4位随机码
        String veriCode = RandomUtils.randomString(RandomUtils.NUM_CHAR, CAPTCHADIGIT);
        String smsContent = null;
        if(SEND_TYPE_FRIST_PRIZE.equals(sendType)){
            smsContent = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.CATEGROY_CODE_APP_GETSMS,
                    DatadicKey.app_sms_content.QPGX_GETSMS_VERICODE);
            
        } else if(SEND_TYPE_CHECK_CODE_PLATFORM.equals(sendType)){
            smsContent = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.CATEGROY_CODE_APP_GETSMS,
                    DatadicKey.app_sms_content.SEND_TYPE_CHECK_CODE_PLATFORM);
        } else {
            smsContent = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.CATEGROY_CODE_APP_GETSMS,
                    DatadicKey.app_sms_content.QPGX_GETSMS_CHECKCODE);
        }
        
        smsContent = smsContent.replace("{0}", veriCode);
        // 发送成功
        if ("0".equals(SendSMSUtil.sendSms(smsContent, phoneNum))) {
            String cacheKeyStr = CacheKey.cacheKey.captcha.KEY_SEND_CAPTCHA_SMS
                                + Constant.DBTSPLIT + phoneNum + Constant.DBTSPLIT + sendType;
            try {
                CacheUtilNew.setCacheValue(cacheKeyStr, VERI_CODE_VALID, veriCode);
            } catch (Exception e) {
                businessCode = Constant.ResultCode.FILURE;
            }
            businessCode = Constant.ResultCode.SUCCESS;
        }
        return businessCode;
    }
    
    /**
     * 校验验证码
     * @param phoneNum
     * @param captcha
     * @param secretKey 秘钥（可为空）
     * @return 0:成功、1:失败、2:过期
     */
    public static String checkCaptcha(String phoneNum, String captcha, String sendType) {
        String businessCode = Constant.ResultCode.FILURE;
        phoneNum = StringUtils.defaultIfBlank(phoneNum, "").trim();
        sendType = StringUtils.defaultIfBlank(sendType, SEND_TYPE_CHECK_CODE);
        try {
            String cacheKeyStr = CacheKey.cacheKey.captcha.KEY_SEND_CAPTCHA_SMS
                    + Constant.DBTSPLIT + phoneNum + Constant.DBTSPLIT + sendType;
            String currVeriCode = (String)CacheUtilNew.getCacheValue(cacheKeyStr);
            String propVeriCode = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_PLATFORM_VERIFYCODE, 
                    DatadicKey.filterPlatformVerifycode.PLATFORM_VERIFYCODE);
            
            if (StringUtils.isNotBlank(propVeriCode)) {
                currVeriCode = propVeriCode;
            }
            if (StringUtils.isNotBlank(currVeriCode)) {
                if (currVeriCode.equals(captcha)) {
                    businessCode = Constant.ResultCode.SUCCESS;
                } else {
                    businessCode = Constant.ResultCode.FILURE;
                }
            } else {
                businessCode = Constant.ResultCode.RESULT_TWO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessCode;
    }
    
    /**
     * AI助手预警消息
     * 
     * @param content
     * @param mobiles
     */
    public static void sendSmsByAIOpenidArray(String openid, String title, String content) {
        
        // AI助手appid 
        String projectName = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_NAME);
        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
                    .FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.AI_APPID);
        
        String[] openidAry = StringUtils.defaultIfBlank(openid, "").split(",");
        for (String item : openidAry) {
            if (StringUtils.isBlank(item)) continue;
            taskExecutor.execute(new WechatRemindTemplateMsg(appid, item).initRemindMsg(title, projectName, content));
        }
        LOG.warn("openid：" + openid + " title：" + title + " content：" + content);
    }
    
    /**
     * AI助手预警消息
     */
    public static void sendSmsByAIOpenid(boolean techFlag, boolean pmFlag, String title, String content) {
        String openid = null;
        if (techFlag) {
            openid = openid + "," + DatadicUtil.getDataDicValue(DatadicKey
                    .dataDicCategory.AI_REMIND_OPENID, DatadicKey.aiRemindOpenid.AI_REMIND_TECH_OPENID);
        }
        if (pmFlag) {
            openid = openid + "," + DatadicUtil.getDataDicValue(DatadicKey
                    .dataDicCategory.AI_REMIND_OPENID, DatadicKey.aiRemindOpenid.AI_REMIND_PM_OPENID);
        }
        sendSmsByAIOpenidArray(openid, title, content);
    }
	
}
