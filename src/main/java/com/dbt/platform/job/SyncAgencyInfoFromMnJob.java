package com.dbt.platform.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.platform.home.util.DateTimeUtil;
import com.dbt.platform.mn.bean.MnAgencyEntity;
import com.dbt.platform.mn.dto.ResultJSON;
import com.dbt.platform.mn.dto.ResultPage;
import com.dbt.platform.mn.service.MnAgencyService;

/**
 * @author shuDa
 * @date 2021/12/16
 **/
@Service("syncAgencyInfoFromMnJob")
public class SyncAgencyInfoFromMnJob{
    Logger logger = Logger.getLogger(this.getClass());
    /**
     * 密钥
     */
    public static final String KEY = "abcdefgabcdefg12";
    private static final  String ALL = "all";
    private static final  String AGENCY_SERVICE = "kylin-nzg-agencyapi-service/";
    private static final  SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String URL = "https://kylin.mengniu.com.cn/mnapi-gateway-zuul/";
    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    
    /**
     * 	经销商Service
     */
    @Autowired
    private MnAgencyService mnAgencyService;
    
    /**
     * 同步数据
     * 默认取昨天数据
     * 参数为 “all” 所有数据更新
     * 其他参数 “2021-12-17” 更新参数日期后的数据
     */
	@SuppressWarnings("rawtypes")
	public void updateAgencyInfo(String date) {
            logger.info("--------------------------蒙牛同步经销商数据 start---------------------------------------"+ SF.format(new Date()));
        try {
        	// 获取job执行的省区
            DbContextHolder.clearDBType();
            DbContextHolder.setDBType("mengniu");
            
            String time = DateTimeUtil.getLastDay();
            long sleepTime = 10000;
            if(ALL.equals(date)){
                time = "";
                mnAgencyService.removeAll();
            }else if(StringUtils.isNotEmpty(date)){
                time = date;
            }
            logger.info("手动设置参数date=" + date + ",执行参数time=" + time);
            
            RestTemplate restTemplate = new RestTemplate();
            JSONObject agencyParam = new JSONObject();
            agencyParam.put("pageNum", 1);
            agencyParam.put("pageSize", 1000);
            agencyParam.put("system", "agencyApi");
            agencyParam.put("appid", "ag39ex");
            agencyParam.put("sgin", aesEncrypt("ag39ex" + "@" + System.currentTimeMillis(), "9hae1jfjg35jifen"));
            //2021-11-17 15:00:00 不传默认全部  传的话 获取更新时间  > 当前参数时间后的数据
            agencyParam.put("uptime", time);
            ResultJSON agencyResult = restTemplate.postForObject(URL + AGENCY_SERVICE, agencyParam,  ResultJSON.class);
            if(0 == agencyResult.getCode()) {
            	 JSONObject agencyObject = agencyResult.getPage();
                 ResultPage agencyResultPage = JSONObject.toJavaObject(agencyObject, ResultPage.class);
                 
                 logger.info("执行新增经销商:"+agencyResultPage.getTotal()+"条");
                 if(1 == agencyResultPage.getPages()) {
                 	saveResult(agencyResultPage.getRecords(), time);
                 }else {
                 	for (int i = 1; i <= agencyResultPage.getPages(); i++) {
                 		Thread.sleep(sleepTime);
                         agencyParam.put("pageNum", i);
                         ResultJSON agencyResult2 = restTemplate.postForObject(URL + AGENCY_SERVICE, agencyParam,  ResultJSON.class);
                         logger.info("执行新增经销商 pageNum = "+i+"页");
                         JSONObject jsonObject2 = agencyResult2.getPage();
                         ResultPage resultPage2 = JSONObject.toJavaObject(jsonObject2, ResultPage.class);
                         List records = resultPage2.getRecords();
                         saveResult(records, time);
                     }
                 }
            }else {
            	// 同步出错
            	logger.info("同步msg=" + agencyResult.getMsg());
            }
           
        }catch (Exception e){
            e.printStackTrace();
        }

        logger.info("--------------------------蒙牛同步经销商数据 end---------------------------------------"+ SF.format(new Date()));
    }
    
	@SuppressWarnings("rawtypes")
    private void saveResult(List records, String time) {
    	for (int j = 0; j < records.size(); j++) {
            LinkedHashMap record = (LinkedHashMap) records.get(j);
            MnAgencyEntity entity = JSON.parseObject(JSON.toJSONString(record), MnAgencyEntity.class);
            if(StringUtils.isNotEmpty(time)){
                mnAgencyService.deleteById(entity.getId());
            }
            mnAgencyService.save(entity);
        }
    }
    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]   new BASE64Decoder().decodeBuffer(base64Code)
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }


    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }


    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }


    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        // base64Decode(encryptStr) base64加密
        // aesDecryptByBytes  aes解密
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }
}
