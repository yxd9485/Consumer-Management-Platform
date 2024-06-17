package com.dbt.framework.token.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.token.service.TokenService;

@RestController
@RequestMapping("/auth")
public class TokenAction extends BaseAction {
    
    @Autowired
    private TokenService tokenService;

    /**
     *  获取accessToken
     *  
     * @param client_id
     * @param client_secret
     * @return
     */
    @ResponseBody
    @RequestMapping("/token")
    public String initToken(String client_id, long timestamp, String sign) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap = tokenService.initToken(client_id, timestamp, sign);
        } catch (Exception e) {
            resultMap.put("error", "invalid_client");
            resultMap.put("error_description", "获取token失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
