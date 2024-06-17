package com.dbt.framework.token.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.token.bean.TokenInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.SignUtil;
import com.dbt.web.VjifenManageConfig;

@Service
public class TokenService {
    
    @Autowired
    private VjifenManageConfig manageConfig;

    /**
     * 获取Token
     * 
     * @param clientId
     * @param clientSecret
     * @return
     * @throws Exception 
     */
    public Map<String, Object> initToken(String clientId, long timestamp, String sign) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String errMsg = null;
        if (StringUtils.isBlank(clientId) || StringUtils.isBlank(sign)) {
            errMsg = "参数不全";
        }
        
        // 校验时间戳
        if (Math.abs(System.currentTimeMillis() - timestamp) > 300000) {
                errMsg ="系统时间异常";
        } else {
            Map<String, String> signMap = new HashMap<String, String>();
            signMap.put("client_id", clientId);
            signMap.put("timestamp", String.valueOf(timestamp));
            signMap.put("sign", sign);
            if (!SignUtil.isSignatureValid(signMap, manageConfig.getAutoQrcodeSecret().get(clientId))) {
                errMsg = "非法请求";
                
            } else {
                
                String serverName = manageConfig.getAutoQrcodeAppid().get(clientId);
                DbContextHolder.setDBType(serverName);

                // 获取token
                String redisKey = RedisApiUtil.CacheKey.token + clientId;
                String accessToken = RedisApiUtil.getInstance().get(redisKey);
                TokenInfo tokenInfo = null;
                if (StringUtils.isBlank(accessToken)) {
                    // 过期时间
                    Date currTime = DateUtil.getNow();
                    Date expireTime = DateUtil.add(currTime, 7200, Calendar.SECOND);
                    accessToken = UUID.randomUUID().toString().replace("-", "");
                    RedisApiUtil.getInstance().set(redisKey, accessToken);
                    RedisApiUtil.getInstance().expireAt(redisKey, expireTime.getTime()/1000);
                    
                    // token对应的信息
                    String tokenInfoKey = RedisApiUtil.CacheKey.token + accessToken;
                    tokenInfo = new TokenInfo();
                    tokenInfo.setClientId(clientId);
                    tokenInfo.setServerName(serverName);
                    tokenInfo.setAccess_token(accessToken);
                    tokenInfo.setExpires_in(7200L);
                    tokenInfo.setCreated_at(currTime.getTime());
                    RedisApiUtil.getInstance().setObject(false, tokenInfoKey, tokenInfo);
                    RedisApiUtil.getInstance().expireAt(false, tokenInfoKey, expireTime.getTime()/1000);
                } else {
                    tokenInfo = (TokenInfo) RedisApiUtil.getInstance()
                            .getObject(false, RedisApiUtil.CacheKey.token + accessToken);
                }
                
                // 返回Token
                if (tokenInfo != null) {
                    resultMap.put("access_token", tokenInfo.getAccess_token());
                    resultMap.put("token_type", "bearer");
                    resultMap.put("expires_in", tokenInfo.getExpires_in());
                    resultMap.put("created_at", tokenInfo.getCreated_at());
                    return resultMap;
                } else {
                    errMsg = "获取Token失败";
                }
            }
        }
        
        // 异常情况
        resultMap.put("error", "invalid_client");
        resultMap.put("error_description", errMsg);
        return resultMap;
    }
    
    public static void main(String[] args) throws Exception {
        long timestamp = System.currentTimeMillis();
        Map<String, String> signMap = new HashMap<String, String>();
//        signMap.put("client_id", "202110252315CPIS");
//        signMap.put("timestamp", String.valueOf(timestamp));
////        signMap.put("sign", SignUtil.generateSignature(signMap, "840d30d83ae411ec93dd6e6d36e3ad65")); // 线上
//        signMap.put("sign", SignUtil.generateSignature(signMap, "dcb63a95353111ecbd136e6d36e3ad65")); // 测试
        
        signMap.put("client_id", "202401091013LYJG");
        signMap.put("timestamp", String.valueOf(timestamp));
//        signMap.put("sign", SignUtil.generateSignature(signMap, "840d30d83ae411ec93dd6e6d36e3ad65")); // 线上
        signMap.put("sign", SignUtil.generateSignature(signMap, "dcb63a95353111ecbd136e6d36e3ad65")); // 测试
        System.out.println("获取token：timestamp:" + timestamp + " sign:" + signMap.get("sign"));
        

        Map<String, String> orderMap = new HashMap<String, String>();
        orderMap.put("amount", "10");
        orderMap.put("sku_name", "随便");
        orderMap.put("sku_code", "201609141-001");
        orderMap.put("factory_name", "高印厂1");
        orderMap.put("product_line_name", "产线1");
        orderMap.put("client_order_no", "clientOrderNo002");
        orderMap.put("work_group", "workGroup");
        orderMap.put("timestamp", String.valueOf(timestamp));
        orderMap.put("access_token", "fcd61cd97a0d4d1395e76edab3fa1f60");
//        orderMap.put("sign", SignUtil.generateSignature(orderMap, "840d30d83ae411ec93dd6e6d36e3ad65")); // 线上
      orderMap.put("sign", SignUtil.generateSignature(orderMap, "dcb63a95353111ecbd136e6d36e3ad65")); // 测试
        System.out.println("timestamp:" + timestamp + " sign:" + orderMap.get("sign"));
        
    }
}
