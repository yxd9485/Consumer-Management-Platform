package com.dbt.framework.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.token.bean.TokenInfo;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.SignUtil;
import com.dbt.web.VjifenManageConfig;

public class TokenInterceptor implements HandlerInterceptor {
    
    private VjifenManageConfig manageConfig;

    public TokenInterceptor(VjifenManageConfig manageConfig) {
        this.manageConfig = manageConfig;
    }

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object object) throws Exception {
	    
	    // 请求url
        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
        if (!requestUrl.startsWith("/auth/") || requestUrl.equals("/auth/token")) return true;
        
        boolean validFlag = false;
        BaseDataResult<String> baseResult = new BaseDataResult<>();
        String accessToken = request.getParameter("access_token");
        if (StringUtils.isBlank("token")) {
            baseResult.initReslut(ResultCode.FILURE, "缺少token");
        } else {
            TokenInfo tokenInfo = (TokenInfo) RedisApiUtil.getInstance()
                            .getObject(false, RedisApiUtil.CacheKey.token + accessToken);
            if (tokenInfo == null) {
                baseResult.initReslut(ResultCode.FILURE, "accessToken无效");
            } else {
                validFlag = true;
                DbContextHolder.setDBType(tokenInfo.getServerName());
                DbContextHolder.setMapVal("MN-CLIENTID", tokenInfo.getClientId());
                
                // 创建码源订单、码源文件上传
                if (requestUrl.equals("/auth/order/add")
                        || requestUrl.equals("/auth/order/batchUpload")) {
                    Map<String, String> signMap = new HashMap<String, String>();
                    for (String key : request.getParameterMap().keySet()) {
                        signMap.put(key, request.getParameter(key));
                    }
                    if (!SignUtil.isSignatureValid(signMap, manageConfig.getAutoQrcodeSecret().get(tokenInfo.getClientId()))) {
                        validFlag = false;
                        baseResult.initReslut(ResultCode.FILURE, "非法请求");
                    }
                }
            }
            
        }
        
        // 校验未通过
        if (!validFlag) {
            outPrint(request, response, baseResult);
            return false;
        }
        return true;
	}

	/**
	 * 输出打印
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void outPrint(HttpServletRequest request, HttpServletResponse response, BaseDataResult<String> baseResult) throws IOException {
	    PrintWriter out = null;
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/json");
		try {
		    out = response.getWriter();
		    out.print(JSON.toJSONString(baseResult));
            
        } finally {
            if (out != null) out.close();
        }
	}
	
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView model) throws Exception {
	}
}
