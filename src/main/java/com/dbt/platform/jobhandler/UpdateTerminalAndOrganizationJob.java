package com.dbt.platform.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.platform.home.util.DateTimeUtil;
import com.dbt.platform.mn.bean.MnDepartmentEntity;
import com.dbt.platform.mn.bean.MnTerminalInfoEntity;
import com.dbt.platform.mn.dto.ResultJSON;
import com.dbt.platform.mn.dto.ResultPage;
import com.dbt.platform.mn.service.MnDepartmentService;
import com.dbt.platform.mn.service.MnTerminalInfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author shuDa
 * @date 2021/12/16
 **/
@Component
@JobHandler(value = "UpdateTerminalAndOrganizationJob")
public class UpdateTerminalAndOrganizationJob extends IJobHandler {
    Logger logger = Logger.getLogger(this.getClass());
    /**
     * 密钥
     */
    public static final String KEY = "abcdefgabcdefg12";
    private static final  String DEPARTMENT = "department";
    private static final  String TERMINAL = "terminal";
    private static final  String DEPARTMENT_SERVICE = "kylin-nzg-departmentapi-service/";
    private static final  String TERMINAL_SERVICE = "kylin-nzg-terminalapi-service/";
    private static final  SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String URL = "https://kylin.mengniu.com.cn/mnapi-gateway-zuul/";
    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    /**
     * 组织信息
     */
    @Autowired
    private MnDepartmentService mnDepartmentService;
    /**
     * 网点信息
     */
    @Autowired
    private MnTerminalInfoService mnTerminalInfoService;
    /**
     * 同步数据
     * 默认取昨天数据
     * 参数为 “all” 所有数据更新
     * 其他参数 “2021-12-17” 更新参数日期后的数据
     */
    public void execute(String param,String d) {
            logger.info("--------------------------蒙牛同步数据 start---------------------------------------"+ SF.format(new Date()));
        try {
            DbContextHolder.clearDBType();
            DbContextHolder.setDBType("mengniu");
            //0918 区分组织和网点两部分更新的时间参数
            String departmentQueryTime = DateTimeUtil.getLastDay();
            String terminalQueryTime = DateTimeUtil.getLastDay();
            long sleepTime = 5000;

            //参数为department时，只进行部门的全量更新
            if(DEPARTMENT.equals(param)){
                departmentQueryTime = "";
                mnDepartmentService.removeAll();
            }
            //参数为terminal时，只进行网点的全量更新
            else if (TERMINAL.equals(param))     {
                terminalQueryTime = "";
                mnTerminalInfoService.removeAll();
            }
            //参数不为空，则作为更新时间传入
            else if(StringUtils.isNotEmpty(param)){
                departmentQueryTime = param;
                terminalQueryTime = param;
            }

            RestTemplate restTemplate = new RestTemplate();
            // 获取job执行的省区

            JSONObject deptParam = new JSONObject();
            deptParam.put("pageNum", 1);
            deptParam.put("pageSize", 3000);
            deptParam.put("system", "departmentApi");
            deptParam.put("appid", "vl84da");
            deptParam.put("sgin", aesEncrypt("vl84da" + "@" + System.currentTimeMillis(), "3fvf4zzjg76aefsg"));
            //2021-11-17 15:00:00 不传默认全部  传的话 获取更新时间  > 当前参数时间后的数据
            deptParam.put("uptime", departmentQueryTime);
            logger.info("组织架构请求参数："+deptParam);
            ResultJSON deptResult = restTemplate.postForObject(URL + DEPARTMENT_SERVICE, deptParam,  ResultJSON.class);
            JSONObject deptObject = deptResult.getPage();
            ResultPage deptResultPage = JSONObject.toJavaObject(deptObject, ResultPage.class);
            // 先清空组织架构表
            logger.info("执行新增组织架构:"+deptResultPage.getTotal()+"条");
            for (int i = 1; i <= deptResultPage.getPages(); i++) {
                Thread.sleep(sleepTime);
                deptParam.put("pageNum", i);
                ResultJSON deptResult2 = restTemplate.postForObject(URL + DEPARTMENT_SERVICE, deptParam,  ResultJSON.class);
                logger.info("执行新增组织架构 pageNum = "+i+"条");
                JSONObject jsonObject2 = deptResult2.getPage();
                ResultPage resultPage2 = JSONObject.toJavaObject(jsonObject2, ResultPage.class);
                List records = resultPage2.getRecords();
                for (int j = 0; j < records.size(); j++) {
                    LinkedHashMap record = (LinkedHashMap) records.get(j);
                    MnDepartmentEntity entity = JSON.parseObject(JSON.toJSONString(record), MnDepartmentEntity.class);
                    if(StringUtils.isNotEmpty(departmentQueryTime)){
                        mnDepartmentService.deleteById(entity.getId());
                    }
                    mnDepartmentService.save(entity);
                }

            }
            JSONObject terminalParam = new JSONObject();
            terminalParam.put("pageNum", 1);
            terminalParam.put("pageSize", 5000);
            terminalParam.put("system", "terminalApi");
            terminalParam.put("appid", "vq89jc");
            terminalParam.put("sgin", aesEncrypt("vq89jc" + "@" + System.currentTimeMillis(), "3gvh4zyjg75tefaj"));
            //2021-11-17 15:00:00 不传默认全部  传的话 获取更新时间  > 当前参数时间后的数据
            terminalParam.put("uptime", terminalQueryTime);
            logger.info("网点请求参数："+terminalParam);
            ResultJSON terminalParamResult = restTemplate.postForObject(URL + TERMINAL_SERVICE, terminalParam,  ResultJSON.class);
            JSONObject terminalParamObject = terminalParamResult.getPage();
            ResultPage terminalParamResultPage = JSONObject.toJavaObject(terminalParamObject, ResultPage.class);
            logger.info("执行新增网点:"+terminalParamResultPage.getPages()+"条");
            Thread.sleep(sleepTime);
            //回退次数 限制
            int backNum = 0;
            for (int i = 1; i <= terminalParamResultPage.getPages(); i++) {
                if (backNum >= 30) {
                    logger.info("网络异常更新数据失败");
                    return;
                }
                terminalParam.put("pageNum", i);
                try{
                    ResultJSON terminalPage = restTemplate.postForObject(URL + TERMINAL_SERVICE, terminalParam,  ResultJSON.class);
                    logger.info("执行新增网点第"+i+"条");
                    Thread.sleep(sleepTime);
                    JSONObject terminalPageObject = terminalPage.getPage();
                    ResultPage terminalResultPageObject = JSONObject.toJavaObject(terminalPageObject, ResultPage.class);
                    List records = terminalResultPageObject.getRecords();
                    for (int j = 0; j < records.size(); j++) {
                        LinkedHashMap record = (LinkedHashMap) records.get(j);
                        MnTerminalInfoEntity entity = JSON.parseObject(JSON.toJSONString(record), MnTerminalInfoEntity.class);
                        if(StringUtils.isNotEmpty(terminalQueryTime)){
                            mnTerminalInfoService.deleteById(entity.getId());;
                        }
                        mnTerminalInfoService.insert(entity);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("执行新增网点第"+i+"条异常");
                    i -= 1;
                    backNum++;
                    logger.info("回退到新增网点第"+i+"条 等待30s");
                    Thread.sleep(30000);

                }


            }
        }catch (Exception e){
            e.printStackTrace();

        }

        logger.info("--------------------------蒙牛同步数据 end---------------------------------------"+ SF.format(new Date()));
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

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        execute(param,"true");
        return new ReturnT<String>(200,"成功");
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResultJSON> resultJSONResponseEntity = restTemplate.postForEntity("https://csm.fjyxgl.com/rest/csm/terminal/info/findAllTerminalList", null, ResultJSON.class);
        Iterator<Object> iterator = resultJSONResponseEntity.getBody().getData().iterator();
        resultJSONResponseEntity.getStatusCode().isError();
        while (iterator.hasNext()) {
            iterator.next().toString();
            System.out.println(iterator.next());
        }

    }
}
