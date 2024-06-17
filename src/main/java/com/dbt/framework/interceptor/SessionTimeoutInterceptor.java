package com.dbt.framework.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.SendSMSUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.securityauth.SecurityContext;
import com.dbt.framework.util.UserThreadLocalUtil;

public class SessionTimeoutInterceptor implements HandlerInterceptor {
    //配置不拦截的资源
    private String[] allowUrls;

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }

    // 允许长度为1000的参数
    private String[] longParameterLengthUrls;

    public void setLongParameterLengthUrls(String[] longParameterLengthUrls) {
        this.longParameterLengthUrls = longParameterLengthUrls;
    }

    // 不限制参数长度的请求
    private String[] unlimitedParameterLengthUrls;

    public void setUnlimitedParameterLengthUrls(String[] unlimitedParameterLengthUrls) {
        this.unlimitedParameterLengthUrls = unlimitedParameterLengthUrls;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object object) throws Exception {

        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");

        //获取所有参数值
        Enumeration<String> names = request.getParameterNames();
        StringBuilder values = new StringBuilder();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            //得到参数对应值
            String[] v = request.getParameterValues(name);

            if ("queryParam".equals(name)) {
                for (String s : v) {
                    values.append(s);
                }
            } else {
                StringBuilder vs = new StringBuilder();
                for (String s : v) {
                    vs.append(s);
                }

                int n = 100;
                if (Arrays.asList(longParameterLengthUrls).contains(name)) {
                    n = 1000;
                }
                if (!checkUnlimitedParameter(unlimitedParameterLengthUrls,requestUrl) && vs.toString().length() > n) {
                    response.setContentType("text/html; charset=utf-8");
                    PrintWriter out = response.getWriter();
                    String msg = "参数" + name + "长度超上限!请联系管理员";
                    out.print("{\"code\":500, \"message\":\"" + msg + "\"}");
                    SendSMSUtil.sendSmsByAIOpenid(true, false, "消费者管理平台参数长度超出限制",
                            "请求链接:"+requestUrl+" 超长参数:"+name+" 参数值:"+vs);
                    return false;
                }
                values.append(vs);
            }
        }

        if (sqlValidate(values.toString())) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":500, \"message\":\"您发送请求中的参数中含有非法字符\"}");
            SendSMSUtil.sendSmsByAIOpenid(true, false, "消费者管理平台请求参数非法字符",
                    "请求链接:"+requestUrl+" 参数值:"+values);
            return false;
        }

        /** SaaS多租户配置 连接主库 **/
        if (requestUrl.contains("login")) {
            String[] uids = request.getParameterValues("uid");
            if(values.toString().contains("蒙牛") || values.toString().contains("冰码")
                    || values.toString().contains("支码") || uids != null)
                request.getSession().setMaxInactiveInterval(3600 * 15);
            else
                request.getSession().setMaxInactiveInterval(3600 * 2);
        }
        if (null != allowUrls && allowUrls.length >= 1) {
            for (String url : allowUrls) {
                if (requestUrl.contains(url)) {
                    return true;
                }
            }
        }

        // sessionId
        String vjfSessionId = request.getParameter("vjfSessionId");
        if(StringUtils.isBlank(vjfSessionId)){
            vjfSessionId = request.getParameter("token");
        }
        if (StringUtils.isBlank(vjfSessionId)) {
            outPrint(request, response);
            return false;
        }

        UserThreadLocalUtil.put("vjfSessionId", vjfSessionId);
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(Constant.USER_SESSION + vjfSessionId);
        if (securityContext != null && securityContext.getSysUserBasis() != null) {
            System.out.println("SessionTimeoutInterceptor projectServerName=" + securityContext.getSysUserBasis().getProjectServerName());
            DbContextHolder.setDBType(securityContext.getSysUserBasis().getProjectServerName());
            return true;
        } else {
            outPrint(request, response);
            return false;
        }
    }


    //效验
    private static boolean sqlValidate(String str) {
        String s = str.toLowerCase();//统一转为小写
        String badStr =
                "(.*)(select |update |and |or |delete |insert |truncate |char |into |substr |ascii |declare |exec |" +
                        "count |master |drop |execute |table |sitename |xp_cmdshell |like |from |grant |use |" +
                        "group_concat |column_name |sleep |information_schema.columns |table_schema |union |where |" +
                        "order |by )(.*)";//过滤掉的sql关键字，特殊字符前面需要加\\进行转义
        //使用正则表达式进行匹配
        return s.matches(badStr);
    }

    /**
     * 输出打印
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void outPrint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //去除页面乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        StringBuilder builder = new StringBuilder();
        builder.append("<script type=\'text/javascript\' charset=\'UTF-8\'>");
        builder.append("alert(\'页面过期，请重新登录\');");
        builder.append("window.top.location.href=\'");
        builder.append(request.getContextPath());
        builder.append("/");
        builder.append("\';</script>");
        out.print(builder.toString());
        out.close();
    }

    /**
     * 不验证参数长度接口配置
     * @param unlimitedParameterLengthUrls
     * @return
     */
    private boolean checkUnlimitedParameter(String[] unlimitedParameterLengthUrls, String requestUrl) {
        for (String unlimitedParameterLengthUrl : unlimitedParameterLengthUrls) {
            if (unlimitedParameterLengthUrl.contains("*")){
                if (requestUrl.contains(unlimitedParameterLengthUrl.substring(0,unlimitedParameterLengthUrl.indexOf("*")))){
                    return true;
                }
            } else if (unlimitedParameterLengthUrl.equals(requestUrl)){
                return true;
            }
        }
        return false;
    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object object, Exception exception)
            throws Exception {

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object object, ModelAndView model) throws Exception {
        CacheUtilNew.clearModelCache();
    }
}
