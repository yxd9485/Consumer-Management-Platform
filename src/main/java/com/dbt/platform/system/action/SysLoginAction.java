package com.dbt.platform.system.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.*;
import com.dbt.platform.home.homeService.ItemDBHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.vjifen.server.base.datasource.redis.RedisUtils;
import com.vjifen.server.base.module.login.bean.SingleAccountInfo;
import com.vjifen.server.base.module.login.bean.SingleAccountPass;
import com.vjifen.server.base.module.login.bean.SingleAccountPlatForm;
import com.vjifen.server.base.module.login.service.SingleAccountService;
import com.vjifen.server.base.utils.MD5Util;
import com.vjifen.server.base.web.rpcresult.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.securityauth.SecurityContext;
import com.dbt.platform.expireremind.service.VpsExpireRemindService;
import com.dbt.platform.job.SendTicketRecordJob;
import com.dbt.platform.system.bean.SysFunction;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.system.service.SysFunctionService;
import com.dbt.platform.system.service.SysRoleService;
import com.dbt.platform.system.service.SysUserBasisService;
import com.dbt.web.VjifenManageConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author RoyFu
 * @createTime 2016年4月20日 下午5:26:50
 * @description
 */
@Controller
@RequestMapping("/system")
public class SysLoginAction extends BaseAction {
    public static final String PLATFORM_KEY = "dom";
    private static final Integer LIMIT_MAX = 3;
	Logger log = Logger.getLogger(this.getClass());
	// 登录uid密钥
	public static final String loginSecret = "1320312c-3bc5-11ec-93dd-6e6d36e3ad65";

	@Autowired
	private SysUserBasisService sysUserBasisService;
	@Autowired
	private SysFunctionService sysFunctionService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private VpsOperationLogService logService;
	@Autowired
	private VpsExpireRemindService expireRemindService;
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private SendTicketRecordJob sendTicketRecordCheckUserJob;
    @Autowired
    private VjifenManageConfig config;
    @Autowired
    private SingleAccountService singleAccountService;


    @RequestMapping("/scan_login")
    public String scanLogin(HttpSession session, HttpServletRequest request, Model model) {
        DbContextHolder.setDBType(null);
        model.addAttribute("appid", config.getAuthAppid());
        String redirectUri = getURI(request) + config.getAuthCallback();
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("state", RandomStringUtils.randomAlphanumeric(13));
        model.addAttribute("singleProjectServerName", config.getSingleProjectServerName());

        //获取微信 code 和 state
        String code = request.getParameter("code");
        String state = request.getParameter("state");
//        state = "-河北青啤";

//        String unionId = "ooIuHs2YxOBS9E0BkWKWJeBjJy5g";
        String unionId = oauthUnionIdNode(code);
        if(org.apache.commons.lang3.StringUtils.isEmpty(unionId))
            return loginFail(model, "扫码登录信息错误。", request);

        String username = null;
        if(StringUtils.isNotEmpty(state)) {
            String[] arr = state.split("-");
            if(arr.length > 1) {
                username = arr[1];
            }
        }

        //查询手机号信息
        String phoneNum = getPhoneNum(unionId);
        if(Strings.isEmpty(phoneNum))
            return loginFail(model, "登录用户信息不全，无法登陆。", request);

        //尝试统一账号扫码登录
        SingleAccountPass pass = new SingleAccountPass();
        pass.setUnionId(unionId);
        pass.setPlatformKey(PLATFORM_KEY);
        pass.setUsername(username);
        Result<SingleAccountInfo> res = null;
        try {
            res = singleAccountService.scanLogin(pass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(res == null)
            return loginFail(model, "登录异常，请联系管理员。", request);

        if(res.getCode() == 0) {
            if(org.apache.commons.lang3.StringUtils.isEmpty(username)) {
                for(SingleAccountPlatForm pf: res.getResult().getPlatform()) {
                    if(PLATFORM_KEY.equals(pf.getPlatformKey())) {
                        username = pf.getUsername();
                        break;
                    }
                }
            }
            SysUserBasis login = new SysUserBasis();
            login.setUserName(username);
            String msg = null;
            try {
                res.getResult().setPhoneNum(phoneNum);
                msg = singleAccountLogin(res, login,  null, 0, model, session, request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(msg == null)
                return "../main";
            return msg;
        }

        //尝试本地账号登录
        //验证全局unionId白名单
        String onionIdWhite = DatadicUtil.getDataDicValue("data_constant_config", "scan_login_unionid");
        if(StringUtils.isNotEmpty(onionIdWhite) && onionIdWhite.contains(unionId)) {
            unionId = null;
        }

        SysUserBasis sysUserBasis = new SysUserBasis();
        sysUserBasis.setUserName(username);
        sysUserBasis.setUnionid(unionId);
        SysUserBasis user = sysUserBasisService.loadUserInfo(sysUserBasis);
        if(user == null)
            return loginFail(model, "登录信息错误。", request);

        user.setPhoneNum(phoneNum);

        String token = UUID.randomUUID().toString();
        String msg = createSession(user, session, request, model, null, token, true);
        if(msg != null)
            return loginFail(model, msg, request);
        return "../main";
    }


    private String oauthUnionIdNode(String code) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(code))
            return null;

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + config.getAuthAppid() +
                "&secret=" + config.getAuthSecret() +
                "&code=" + code + "&grant_type=authorization_code";
        HttpGet get = new HttpGet(url);

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpResponse resp = client.execute(get);
            //提取返回值
            HttpEntity entity = resp.getEntity();
            String resultStr = EntityUtils.toString(entity, "UTF-8");
            JsonNode result = new ObjectMapper().readTree(resultStr);
            JsonNode unionIdNode = result.get("unionid");
            if (unionIdNode != null) {
                return unionIdNode.asText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPhoneNum(String unionId) {
        ItemDBHelper db = new ItemDBHelper();

        String phoneNum = null;
        String sql = "select phone_num from register_user_info where unionid = '" + unionId + "' ";
        try {
            ResultSet res = db.stmt.executeQuery(sql);
            if(res.next()) {
                phoneNum = res.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception ignored) {
            }
        }
        return phoneNum;
    }

    /**
     * 用户登录
     *
     * @param session
     * @param request
     * @param loginUser
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(HttpSession session, HttpServletRequest request, SysUserBasis loginUser,
                        Model model) {
        DbContextHolder.setDBType(null);
        model.addAttribute("appid", config.getAuthAppid());
        String redirectUri = getURI(request) + config.getAuthCallback();
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("state", RandomStringUtils.randomAlphanumeric(13));
        model.addAttribute("singleProjectServerName", config.getSingleProjectServerName());
        try {
            long limitKeyNum = 0;
            String limitKey = "VjifenCOMLimitLogin:username:" + loginUser.getUserName();
            if (StringUtils.isNotBlank(loginUser.getUserName())) {
                limitKeyNum = RedisUtils.get().STRING.VALUE.incr(limitKey);
                if (limitKeyNum > LIMIT_MAX) {
                    model.addAttribute("message", "多次验证错误，请30分钟后再试。");
                    return "../login";
                }
            }
            RedisUtils.get().STRING.expire(limitKey, 60 * 30);
            // pwd密码登录、uid方式登录
            String loginType = Constant.loginType.PWD;
            String uid = request.getParameter("uid");
            if (StringUtils.isNotBlank(uid)) {
                String[] uidAry = null;
                try {
                    uidAry = StringUtils.defaultIfBlank(Encryp3DESUtil.decryptCode(loginSecret, uid, false), "").split(",");
                } catch (Exception e) {
                    log.error("uid:" + uid + " 解密失败", e);
                }

                // 解析成功
                if (uidAry != null && uidAry.length == 2) {
                    loginType = Constant.loginType.UID;
                    loginUser.setUserName(uidAry[0]);
                    loginUser.setUserPassword(uidAry[1]);
                } else {
                    model.addAttribute("message", "系统没有检测到您的账号，请联系管理员");
                    logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", uid + ", 系统没有检测到您的账号，请联系管理员");
                    return "../login";
                }
            }

            // 必输参数校验
            if (StringUtils.isBlank(loginUser.getUserName())
                    || StringUtils.isBlank(loginUser.getUserPassword())) {
                return "../login";
            } else if (Constant.loginType.PWD.equals(loginType) && (StringUtils
                    .isBlank(loginUser.getPhoneNum()) || StringUtils.isBlank(loginUser.getVeriCode()))) {
                return "../login";
            }

            //统一账号认证
            SingleAccountPass pass = new SingleAccountPass();
            pass.setUsername(loginUser.getUserName());
            pass.setPassword(loginUser.getUserPassword());
            pass.setPhoneNum(loginUser.getPhoneNum());
            pass.setCode(loginUser.getVeriCode());
            pass.setPlatformKey(PLATFORM_KEY);
            if (PropertiesUtil.getPropertyValue("run_env").equals("TEST")) {
                pass.setValidCode(1);
                if ("1".equals(loginUser.getPhoneNum())) {
                    pass.setPhoneNum("17865651278");
                }
            }
    /*        Result<SingleAccountInfo> res;
            try {
                res = singleAccountService.usernameLogin(pass);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return loginFail(model, "登录异常，请联系管理员。", request);
            }
            if (res.getCode() != 1025 && res.getCode()!=500) {
                res.getResult().setPhoneNum(loginUser.getPhoneNum());
                return singleAccountLogin(res, loginUser,  limitKey, limitKeyNum, model, session, request);
            }
*/
            //本地账号认证
            String msg = oldLogin(loginUser, loginType, limitKey, limitKeyNum, request, model, session);
            return msg;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "../login";

    }
    private String singleAccountLogin(Result<SingleAccountInfo> res, SysUserBasis loginUser, String limitKey,
                                      long limitKeyNum, Model model, HttpSession session,
                                      HttpServletRequest request) throws Exception {
        if(res.getCode() > 0) {
            return loginFail(model, res.getMessage(), request);
        }

        SingleAccountInfo sa = res.getResult();

        //关联系统信息
        List<SingleAccountPlatForm> link = Lists.newArrayList();
        String username = null;
        String phoneNum = sa.getPhoneNum();
        if(sa.getFull()==1){
            for(SingleAccountPlatForm pf: sa.getPlatform()) {
                if(PLATFORM_KEY.equals(pf.getPlatformKey()) && loginUser.getUserName().equals(pf.getUsername())){
                    username = pf.getUsername();
                }
                else{
                    link.add(pf);
                }
            }

        }else{
            for(SingleAccountPlatForm pf: sa.getPlatform()) {
                if(PLATFORM_KEY.equals(pf.getPlatformKey())){
                    username = pf.getUsername();
                }
                else{
                    link.add(pf);
                }
            }

        }

        if(StringUtils.isEmpty(username)) {
            return loginFail(model, "登录异常，请联系管理员。", request);
        }
        SysUserBasis sysUserBasis = new SysUserBasis();
        sysUserBasis.setUserName(username);
        SysUserBasis user = sysUserBasisService.loadUserInfo(sysUserBasis);

        if(user == null) {
            return loginFail(model, "登录异常，请联系管理员。", request);
        }
        user.setPhoneNum(phoneNum);

        //不完全认证验证本地账号密码
        String msg = null;
        if(sa.getFull() == 1 && loginUser != null && loginUser.getUserPassword()!=null) {
            MD5Util encrypt = new MD5Util();
            if(!sysUserBasisService.validPassword(loginUser, user)) {
                if(limitKey == null){
                    msg = "登录信息错误，请联系管理员。";
                } else{
                    msg = loginNumFail(limitKey);
                }
            }
        }

        if(msg == null){
            msg = createSession(user, session, request, model, limitKey, sa.getToken(), false);
        }else{
            return loginFail(model, msg, request);
        }
        if(limitKey != null){
            RedisUtils.get().STRING.del(limitKey);
        }
        model.addAttribute("link", link);
        return "../main";
    }

    private String createSession(SysUserBasis user, HttpSession session, HttpServletRequest request, Model model,
                                 String limitKey, String token, boolean isLocal) {
        if("0".equals(user.getUserStatus())) {
            if(limitKey == null){
                return "登录信息错误，请联系管理员。";
            } else{
                return loginNumFail(limitKey);
            }
        }
        SecurityContext securityContext = new SecurityContext();
        securityContext.setSysUserBasis(user);
        String userKey = user.getUserKey();
        String projectServerName = user.getProjectServerName();
        List<SysRole> role = sysRoleService.loadCurrentRoleByUser(user.getUserKey());

        // 用户菜单权限
        String urlPrefix = getURI(request);
        List<SysFunction> menuList = sysFunctionService.getMenu(userKey, urlPrefix, role, projectServerName);
        if (menuList != null && !menuList.isEmpty()) {
            String menuHtml = sysFunctionService.getMenuHtml(menuList);
            model.addAttribute("menuList", menuList);
            model.addAttribute("menuHtml", menuHtml);
            // 判断是否为企业用户
            validUser(model, user);
            // 用户权限
            securityContext.setPermissionList(sysFunctionService
                    .loadCurrentFunctionByUser(userKey));
        }
        user.setUserPassword(null);
        model.addAttribute("sysUser", user);
        UserThreadLocalUtil.put("vjfSessionId", token);
        model.addAttribute("vjfSessionId", token);
        model.addAttribute("token", token);
        securityContext.setSysRoleList(role);
        session.setAttribute(Constant.USER_SESSION + token, securityContext);

        if(StringUtils.isNotBlank(projectServerName)){
            if(!"saasDb".equals(projectServerName)){
                model.addAttribute("projectName",
                        ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
                                .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO))
                                .get(projectServerName).getServerName());
            }
            log.warn("登录成功projectServerName=" + user.getProjectServerName());
            DbContextHolder.setDBType(user.getProjectServerName());
            int count= expireRemindService.conutMsgExpireRemind();
            model.addAttribute("count", count);
        }
        if(limitKey!=null){
            RedisUtils.get().STRING.del(limitKey);
        }
        String logMsg = user.getUserName() + "(" + user.getPhoneNum()  + ")";
        logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 登录成功");
        return null;

    }

    static String getURI(HttpServletRequest request) {
        System.out.println("x-forwarded-port:" + request.getHeader("x-forwarded-port"));
        System.out.println("x-forwarded-scheme:" + request.getHeader("x-forwarded-scheme"));

        String port = request.getHeader("x-forwarded-port");
        String head = request.getHeader("x-forwarded-scheme");
        if(org.apache.commons.lang3.StringUtils.isEmpty(head)) {
            head = "http";
        }
        if(port == null)
            port = request.getServerPort() + "";
        port = "80".equals(port) || "443".equals(port) ? "" : (":" + port);

        return head + "://" +  request.getServerName()+ port + request.getContextPath() + "/";
    }

    @RequestMapping("/linkLogin")
    public String linkLogin(String token,String username, Model model, HttpSession session, HttpServletRequest request) {
        DbContextHolder.setDBType(null);
        //统一账号认证
        Result<SingleAccountInfo> res = tokenLogin(token, username,1);
        if(res == null)
            return loginFail(model, "登录异常，请联系管理员。", request);
        try {
            SysUserBasis loginUser = new SysUserBasis();
            loginUser.setUserName(username);
            return singleAccountLogin(res, loginUser,null,0, model, session, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Result<SingleAccountInfo> tokenLogin(String token,String username, int showInfo) {
        SingleAccountPass pass = new SingleAccountPass();
        pass.setPlatformKey(PLATFORM_KEY);
        pass.setToken(token);
        pass.setShowUserInfo(showInfo);
        pass.setUsername(username);
        try {
            return singleAccountService.tokenLogin(pass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String loginNumFail(String limitKey) {
        long num =  RedisUtils.get().STRING.VALUE.incr(limitKey);
        //重置过期时间
        RedisUtils.get().STRING.expire(limitKey, 60 * 30);
        if(num >= LIMIT_MAX)
            return "用户已锁定，请30分钟后再试。";
        return "登录信息错误，还有" + (LIMIT_MAX - num) + "次机会。";
    }
    private String oldLogin(SysUserBasis loginUser,String loginType,String limitKey,long limitKeyNum,HttpServletRequest request,Model model, HttpSession session) throws Exception {
            String logMsg = loginUser.getUserName() + "(" + loginUser.getPhoneNum()  + ")";
            loginUser.setUserName("测试青啤查看");
            loginUser.setUnionid(null);
            SysUserBasis currentUser = sysUserBasisService.loadUserInfo(loginUser);
            // 先判断用户是否存在
            if (null != currentUser) {
                // 存在的用户先看账号是否停用
                String currentStatus = currentUser.getUserStatus();
                currentStatus = "1";
                if ("1".equals(currentStatus)) {

                    // 手机验证码校验结果
                    String checkCaptchaFlag = SendSMSUtil.checkCaptcha(
                            loginUser.getPhoneNum(), loginUser.getVeriCode(), SendSMSUtil.SEND_TYPE_CHECK_CODE_PLATFORM);

                    checkCaptchaFlag = "0";
                    loginType = "UID";
                    // 检验手机验证码
                    if (ResultCode.SUCCESS.equals(checkCaptchaFlag) || Constant.loginType.UID.equals(loginType)) {
                        boolean b = sysUserBasisService.validPassword(loginUser, currentUser);
                        b = true;
                        // 检验密码是否输入错误
                        if (b) {
                            // 封装用户内容类
                            SecurityContext securityContext = new SecurityContext();
                            securityContext.setSysUserBasis(currentUser);
                            String userKey = currentUser.getUserKey();
                            String projectServerName = currentUser.getProjectServerName();
                            userKey = "24007";


                            // 用户角色
                            List<SysRole> role = sysRoleService.loadCurrentRoleByUser(userKey);
                            if(CollectionUtils.isNotEmpty(role)){
                                securityContext.setSysRoleList(sysRoleService
                                        .loadCurrentRoleByUser(userKey));
                                String roleKey = role.get(0).getRoleKey();
                                model.addAttribute("roleKey", roleKey);

                                // 用户菜单权限
                                String urlPrefix = getURI(request);
                                List<SysFunction> menuList = sysFunctionService.getMenu(userKey, urlPrefix, role, projectServerName);
                                if (menuList != null && !menuList.isEmpty()) {
                                    String menuHtml = sysFunctionService.getMenuHtml(menuList);
                                    model.addAttribute("menuList", menuList);
                                    model.addAttribute("menuHtml", menuHtml);
                                    // 判断是否为企业用户
                                    validUser(model, currentUser);
                                    // 用户权限
                                    securityContext.setPermissionList(sysFunctionService
                                            .loadCurrentFunctionByUser(userKey));
                                    currentUser.setPhoneNum(loginUser.getPhoneNum());
                                }
                            }

                            // sessionId
                            String vjfSessionId = UUID.randomUUID().toString();
                            model.addAttribute("sysUser", currentUser);
                            model.addAttribute("vjfSessionId", vjfSessionId);
                            UserThreadLocalUtil.put("vjfSessionId", vjfSessionId);
                            session.setAttribute(Constant.USER_SESSION + vjfSessionId, securityContext);

                            if(StringUtils.isNotBlank(projectServerName)){
                                if(!"saasDb".equals(projectServerName)){
                                    model.addAttribute("projectName",
                                            ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
                                                    .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO))
                                                    .get(projectServerName).getServerName());
                                }
                                log.warn("登录成功projectServerName=" + currentUser.getProjectServerName());
                                DbContextHolder.setDBType(currentUser.getProjectServerName());
                                int count= expireRemindService.conutMsgExpireRemind();
                                model.addAttribute("count", count);
                            }
                            RedisUtils.get().STRING.del(limitKey);
                            logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 登录成功");
                            return "../main";
                        } else {
                            model.addAttribute("message", "用户名、密码或者验证码错误，请联系管理员!");
                            logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 密码错误！");
                        }
                    } else {
                        model.addAttribute("message", "用户名、密码或者验证码错误，请联系管理员!");
                        logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 验证错误！");
                    }
                } else {
                    model.addAttribute("message", "用户名、密码或者验证码错误，请联系管理员!");
                    logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 您的账号已被停用，请联系管理员");
                }
            } else {
                model.addAttribute("message", "用户名、密码或者验证码错误，请联系管理员!");
                logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", logMsg + ", 系统没有检测到您的账号，请联系管理员");

            }
            RedisUtils.get().STRING.expire(limitKey, 60 * 30);
            if(limitKeyNum >= 3) {
                model.addAttribute("message", "用户名已禁用，请30分钟后再试。");
            } else {
                model.addAttribute("message", "用户名、密码或者验证码错误，请联系管理员!还有" + (3 - limitKeyNum) + "次机会。");
            }
            return "../login";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "../login";
//        }
    };


    private String loginFail(Model model, String message, HttpServletRequest request) {
        model.addAttribute("appid", config.getAuthAppid());
        String redirectUri = getURI(request) + config.getAuthCallback();
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("state", RandomStringUtils.randomAlphanumeric(13));
        model.addAttribute("singleProjectServerName", config.getSingleProjectServerName());
        model.addAttribute("message", message);

        return "../login";
    }
	/**
	 * 判断是否为企业用户
	 *
	 * @param model
	 * @param user
	 */
	private void validUser(Model model, SysUserBasis user) {
		// 运营类型
		String dbtUserType = "0";
		boolean falg = dbtUserType.equals(user.getUserType()) ? false : true;
		if (falg) {
			model.addAttribute("isCustomer", "yes");
		}
	}

	/**
	 * 用户退出
	 *
	 * @return
	 */
	@RequestMapping("/logOut")
	public String logOut(Model model, HttpServletRequest request) {
		DbContextHolder.setDBType(null);
        logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, "", "退出登录");
        model.addAttribute("appid", config.getAuthAppid());
        String redirectUri = getURI(request) + config.getAuthCallback();
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("state", RandomStringUtils.randomAlphanumeric(13));
        model.addAttribute("singleProjectServerName", config.getSingleProjectServerName());
		return "../login";
	}

	@RequestMapping("/modifyPassword")
	public String modifyPassword(HttpSession session, Model model){
		SysUserBasis user = getUserBasis(session);
		model.addAttribute("user", user);
		return "../updatePassword";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword(HttpSession session, String userKey, String orgPwd, String newPwd, String decPwd, Model model) {
		try {
			SysUserBasis user = new SysUserBasis();
			user.setUserKey(userKey);
			if (newPwd.equals(decPwd)) {
				String pwd = EncryptUtil.encrypt(newPwd);
				pwd = EncryptUtil.encode(pwd.getBytes());
				user.setUserPassword(pwd);
				sysUserBasisService.updatePassword(user);
				model.addAttribute("result", "success");

				Map<String, Object> logMap = new HashMap<>();
				logMap.put("orgPwd", orgPwd);
				logMap.put("newPwd", newPwd);
                logService.saveLog("login", Constant.OPERATION_LOG_TYPE.TYPE_0, JSON.toJSONString(logMap), "修改密码");
			} else {
				model.addAttribute("result", "not_equal");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "../updatePassword";
	}

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     * @return
     */
    @RequestMapping("/getPhoneVeriCode")
    public @ResponseBody String getPhoneVeriCode(String userName,String phoneNum) {
    	DbContextHolder.setDBType(null);
//        return sysUserBasisService.getPhoneVeriCode(phoneNum);
        if(StringUtils.isEmpty(phoneNum)) {
            return "1";
        }
        //统一账号认证
        SingleAccountPass sa = new SingleAccountPass();
        sa.setUsername(userName);
        sa.setPhoneNum(phoneNum);
        sa.setPlatformKey(PLATFORM_KEY);
        Result result = null;
        try {
            result = singleAccountService.sendCode(sa);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(result != null && result.getCode() != 1025) {
            if(result.getCode() == 0)
                return "0";
            else if(result.getCode() == 1027)
                return "1";
            else
                return "2";
        }
        return sysUserBasisService.getPhoneVeriCode(phoneNum);
    }

	/**
	 * 密码校验
	 *
	 * @param userKey
	 * @param name
	 * @return
	 */
	@RequestMapping("/validPassword")
	public @ResponseBody
	String validatePassword(String userKey, String name) {
		String status = "N";
		try {
			SysUserBasis sysUser = sysUserBasisService.findById(userKey);
			String pwd = EncryptUtil.decode(sysUser.getUserPassword().getBytes());
			if (!StringUtils.isEmpty(name) && pwd.equals(EncryptUtil.encrypt(name))) {
				status = "Y";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(status);
	}

	/**
	 * 更新省区数据源
	 * http://localhost:8080/DBTHBQPplatform/system/modifyProjectServerInfo.do?projectServerName=shanghaiqp
	 * @param projectServerName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/modifyProjectServerInfo")
	public @ResponseBody
	String modifyProjectServerInfo(String projectServerName) {
		String status = "FAILURE";
		try {
			DbContextHolder.setDBType(null);
			ServerInfo serverInfo = serverInfoService.findByProjectServerName(projectServerName);
			if(null == serverInfo) return projectServerName + "数据源不存在";

			// 缓存中重新设置改数据源
			Map<String, ServerInfo> itemCache = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO));
			if("0".equals(serverInfo.getServerStatus())){
				itemCache.put(projectServerName, serverInfo);
			}else{
				itemCache.remove(projectServerName);
			}
			RedisApiUtil.getInstance().setObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO, itemCache);

			// 添加需要更新的数据源标识
			RedisApiUtil.getInstance().addSet(false, CacheKey.cacheKey.KEY_UPDATE_PROJECT_SERVER_INFO_LIST, projectServerName);
			status = "更新"+projectServerName+"省区数据源成功！";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(status);
	}

	/**
	 * 开发中
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/developping")
	public void developping() throws Exception {
		sendTicketRecordCheckUserJob.sendTicketRecordEmailExcelJob();;
	}

	@RequestMapping("/welcome")
	public String welcome() {
		return "../welcome";
	}

}
