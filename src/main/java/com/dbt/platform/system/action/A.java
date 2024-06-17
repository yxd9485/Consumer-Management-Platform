//package com.dbt.platform.system.action.A;
//
//import java.io.IOException;
//import java.net.URLDecoder;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import com.dbt.framework.util.RedisApiUtil;
//import com.dbt.web.VjifenManageConfig;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Lists;
//import com.vjifen.server.base.module.login.bean.SingleAccountInfo;
//import com.vjifen.server.base.module.login.bean.SingleAccountPass;
//import com.vjifen.server.base.module.login.bean.SingleAccountPlatForm;
//import com.vjifen.server.base.module.login.service.SingleAccountService;
//import com.vjifen.server.base.utils.Encryption3DESUtil;
//import com.vjifen.server.base.utils.MD5Util;
//import com.vjifen.server.base.web.rpcresult.Result;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSON;
//import com.dbt.framework.base.action.BaseAction;
//import com.dbt.framework.base.bean.Constant;
//import com.dbt.framework.datadic.util.DatadicUtil;
//import com.dbt.framework.securityauth.SecurityContext;
//import com.dbt.framework.zone.service.SysAreaService;
//import com.vjifen.module.message.builder.shortmsg.ShortMsgBuilder;
//import com.vjifen.module.message.scan.MessageFactory;
//
///**
// *
// * <B>功能简述</B><br>
// * 功能详细描述
// *
// * @date 2015-6-1 下午7:59:01
// * @author Eddy
// * @since [产品/模块版本]
// */
//@Controller
//@RequestMapping("/sysUser")
//public class A extends BaseAction {
//    static private String secret = "SPwENLxrO0WzTR2ghz97n8wVrR5c2aYY";
//    static public String PLATFORM_KEY = "terDas";
//    static public String LOGIN_TOKEN_KEY = "login:child:" + PLATFORM_KEY + ":token:%s";
//    static public int SESSION_TIMEOUT = 3600;
//
//    static private String LOGIN_NUM_KEY = "login:child:" + PLATFORM_KEY + ":num:%s";
//    static private Integer LIMIT_MAX = 3;
//
//    @Autowired
//    private VjifenManageConfig config;
//    @Autowired
//    private SingleAccountService singleAccountService;
//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private SysFuncService sysFuncService;
//    @Autowired
//    private SysRoleMService sysRoleMService;
//    @Autowired
//    private SysRolePropertyService sysRolePropertyService;
//    @Autowired
//    private SysUserropertyService sysUserropertyService;
//    @Autowired
//    private SysAreaService sysAreaService;
//    @Autowired
//    private MessageFactory messageFactory;
//
//    /**
//     * 用户登录
//     */
//    @RequestMapping("/login")
//    @UserLogInterface(systemType = Constant.USER_OPERATE_TYPE.TYPE_USER,
//            operateType = Constant.USER_OPERATE_TYPE.LOGIN, operateData = "登录系统")
//    public String login(SysUser sysUser, Model model, HttpSession session, HttpServletRequest request) {
//        if (sysUser.getUsername() == null || sysUser.getPwd() == null || sysUser.getPhoneNum() == null)
//            return loginFail(model, "");
//
//        //统一账号认证
//        SingleAccountPass pass = new SingleAccountPass();
//        if(sysUser.getMainKey() != null) {
//            //main跳转登录，检查秘钥
//            long diff = 11000;
//            try {
//                String dc = URLDecoder.decode(sysUser.getMainKey(), "UTF-8");
//                String date = Encryption3DESUtil.decryptCode(secret, dc);
//                Calendar ca = Calendar.getInstance();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date mainDate = format.parse(date);
//                long now = ca.getTime().getTime();
//                ca.setTime(mainDate);
//                long main = ca.getTime().getTime();
//                diff = now - main;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if(diff > 10000)
//                return loginFail(model, "登录异常，请联系管理员。");
//            pass.setValidCode(1);
//        }
//
//        //用户登录次数
//        String numKey = String.format(LOGIN_NUM_KEY, sysUser.getPhoneNum());
//        long num = 0;
//        String sn = RedisApiUtil.getInstance().get(numKey);
//        if(sn != null) {
//            num = Long.parseLong(sn);
//        }
//
//        //登录计数达到最大值禁止登录
//        if(num >= LIMIT_MAX)
//            return loginFail(model, "用户已锁定，请30分钟后再试。");
//
//        pass.setUsername(sysUser.getUsername());
//        pass.setPassword(sysUser.getPwd());
//        pass.setPhoneNum(sysUser.getPhoneNum());
//        pass.setCode(sysUser.getVeriCode());
//        pass.setPlatformKey(PLATFORM_KEY);
//        if(!"pro".equals(config.getEnv())) {
//            pass.setValidCode(1);
//        }
//
//        Result<SingleAccountInfo> res;
//        try {
//            res = singleAccountService.usernameLogin(pass);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return loginFail(model, "登录异常，请联系管理员。");
//        }
//
//        if(res.getCode() != 1025) {
//            return singleAccountLogin(res, sysUser, model, session, request, numKey);
//        }
//
//        //本地账号认证
//        String msg = oldLogin(sysUser, model, session, request, numKey);
//        if(msg != null)
//            return loginFail(model, msg);
//
//        RedisApiUtil.getInstance().del(numKey);
//        return "../main";
//    }
//
//    @RequestMapping("/scanLogin")
//    @UserLogInterface(systemType = Constant.USER_OPERATE_TYPE.TYPE_USER,
//            operateType = Constant.USER_OPERATE_TYPE.SCAN_LOGIN,operateData = "扫码登录")
//    public String scanLogin(HttpSession session, HttpServletRequest request, Model model) {
//        //获取微信 code 和 state
//        String code = request.getParameter("code");
//        String state = request.getParameter("state");
////        state = "-青啤河北";
//
////        String unionId = "ooIuHs2YxOBS9E0BkWKWJeBjJy5g";
//        String unionId = oauthUnionIdNode(code);
//        if(StringUtils.isEmpty(unionId))
//            return loginFail(model, "扫码登录信息错误。");
//
//        String username = null;
//        if(StringUtils.isNotEmpty(state)) {
//            String[] arr = state.split("-");
//            if(arr.length > 1) {
//                username = arr[1];
//            }
//        }
//
//        //尝试统一账号扫码登录
//        SingleAccountPass pass = new SingleAccountPass();
//        pass.setUnionId(unionId);
//        pass.setPlatformKey(PLATFORM_KEY);
//        pass.setUsername(username);
//        Result<SingleAccountInfo> res = null;
//        try {
//            res = singleAccountService.scanLogin(pass);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        if(res == null)
//            return loginFail(model, "登录异常，请联系管理员。");
//
//        if(res.getCode() == 0)
//            return singleAccountLogin(res, null, model, session, request, null);
//
//        //尝试本地账号登录
//        //验证全局unionId白名单
//        String onionIdWhite = DatadicUtil.getDataDicValue("data_constant_config", "scan_login_unionid");
//        if(StringUtils.isNotEmpty(onionIdWhite) && onionIdWhite.contains(unionId)) {
//            unionId = null;
//        }
//
//        SysUser user = sysUserService.selectUser(username, unionId);
//        if(user == null)
//            return loginFail(model, "登录信息错误。");
//
//        String token = UUID.randomUUID().toString();
//        String msg = createSession(user, session, request, model, null, token, true);
//        if(msg != null)
//            return loginFail(model, msg);
//        return "../main";
//    }
//
//    @RequestMapping("/linkLogin")
//    @UserLogInterface(systemType = Constant.USER_OPERATE_TYPE.TYPE_USER,
//            operateType = Constant.USER_OPERATE_TYPE.LOGIN, operateData = "跳链登录")
//    public String linkLogin(String token, Model model, HttpSession session, HttpServletRequest request) {
//        //统一账号认证
//        Result<SingleAccountInfo> res = tokenLogin(token, 1);
//        if(res == null)
//            return loginFail(model, "登录异常，请联系管理员。");
//        return singleAccountLogin(res, null, model, session, request, null);
//    }
//
//    public String initSession(String token, SysUser user, HttpSession session, boolean isLocal) {
//        SecurityContext sc = userSecurity(user);
//        if (null != sc.getAttribute())
//            return sc.getAttribute().get("message");
//
//        // 将用户所有关联信息存入session
//        session.setAttribute(token, sc);
//        // 存在用户session
//        session.setAttribute(Constant.USER_SESSION, sc);
//
//        if(isLocal) {
//            //存储本地token
//            String key = String.format(LOGIN_TOKEN_KEY, token);
//            RedisApiUtil.getInstance().set(key, JSON.toJSONString(sc), SESSION_TIMEOUT);
//        }
//
//        if(session.getAttribute("regionMap") == null) {
//            // 放入区域信息
//            try {
//                sysAreaService.setSessionArea(session);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public Result<SingleAccountInfo> tokenLogin(String token, int showInfo) {
//        SingleAccountPass pass = new SingleAccountPass();
//        pass.setPlatformKey(PLATFORM_KEY);
//        pass.setToken(token);
//        pass.setShowUserInfo(showInfo);
//        try {
//            return singleAccountService.tokenLogin(pass);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public SysUser selectUser(String username) {
//        return sysUserService.selectUser(username, null);
//    }
//
//    /**
//     * 获取手机验证码
//     * @param phoneNum	手机号
//     * @return	0-成功，1-失败
//     */
//    @RequestMapping("/getPhoneVeriCode")
//    @ResponseBody
//    public String getPhoneVeriCode(String phoneNum, String userName) {
//        if(StringUtils.isEmpty(userName)) {
//            return "1";
//        }
//
//        //统一账号认证
//        SingleAccountPass sa = new SingleAccountPass();
//        sa.setUsername(userName);
//        sa.setPhoneNum(phoneNum);
//        sa.setPlatformKey(PLATFORM_KEY);
//        Result result = null;
//        try {
//            result = singleAccountService.sendCode(sa);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        if(result != null && result.getCode() != 1025) {
//            if(result.getCode() == 0)
//                return "0";
//            else if(result.getCode() == 1027)
//                return "1";
//            else
//                return "2";
//        }
//
//        //本地权限验证
//        if(!sysUserService.checkUserPhone(phoneNum, userName)) {
//            return "2";
//        }
//        ShortMsgBuilder builder = messageFactory.getShortMsgMessageBuilder();
//        if(!builder.isUserValid(phoneNum)) {
//            return "1";
//        }
//        return (builder.code(phoneNum)==0) ? "0" : "1";
//    }
//
//    private String singleAccountLogin(Result<SingleAccountInfo> res, SysUser login, Model model, HttpSession session,
//                                      HttpServletRequest request, String numKey) {
//        if(res.getCode() > 0) {
//            return loginFail(model, res.getMessage());
//        }
//
//        SingleAccountInfo sa = res.getResult();
//
//        //关联系统信息
//        List<SingleAccountPlatForm> link = Lists.newArrayList();
//        String username = null;
//        for(SingleAccountPlatForm pf: sa.getPlatform()) {
//            if(PLATFORM_KEY.equals(pf.getPlatformKey()))
//                username = pf.getUsername();
//            else
//                link.add(pf);
//        }
//
//        if(StringUtils.isEmpty(username)) {
//            return loginFail(model, "登录异常，请联系管理员。");
//        }
//
//        SysUser user = sysUserService.selectUser(username, null);
//        if(user == null) {
//            return loginFail(model, "登录异常，请联系管理员。");
//        }
//        //不完全认证验证本地账号密码
//        String msg = null;
//        if(sa.getFull() == 1 && login != null) {
//            MD5Util encrypt = new MD5Util();
//            System.out.println(encrypt.getMD5ofStr(login.getPwd()));
//            System.out.println(new com.dbt.framework.util.MD5Util().getMD5ofStr(login.getPwd()));
//
//            if(!user.getPwd().equals(encrypt.getMD5ofStr(login.getPwd()))) {
//                if(numKey == null)
//                    msg = "登录信息错误，请联系管理员。";
//                else
//                    msg = loginNumFail(numKey);
//            }
//        }
//
//        if(msg == null)
//            msg = createSession(user, session, request, model, numKey, sa.getToken(), false);
//        if(msg != null)
//            return loginFail(model, msg);
//
//        model.addAttribute("link", link);
//
//        if(numKey != null)
//            RedisApiUtil.getInstance().del(numKey);
//        return "../main";
//    }
//
//    private String oldLogin(SysUser sysUser, Model model, HttpSession session, HttpServletRequest request, String numKey) {
//        String env = config.getEnv();
//        //非生产环境不需要验证码
//        if("pro".equals(env)) {
//            String ip = getIpAddress(request);
//            //IP验证+手机号验证
//            String ipConfig = DatadicUtil.getDataDicValue("data_constant_config", "quick_login_ip");
//
//            if(StringUtils.isNotEmpty(ipConfig) && !ipConfig.contains(ip)) {
//                if (sysUser.getPhoneNum() == null || sysUser.getVeriCode() == null)
//                    return "验证码错误。";
//            }
//
//            ShortMsgBuilder builder = messageFactory.getShortMsgMessageBuilder();
//            if (sysUser.getPhoneNum() != null && sysUser.getVeriCode() != null &&
//                    builder.verification(sysUser.getPhoneNum(), sysUser.getVeriCode()) != 0) {
//                return "验证码错误。";
//            }
//        }
//
//        SysUser user = sysUserService.queryUser(sysUser);
//        if(user == null || user.getPwd() == null)
//            return loginNumFail(numKey);
//
//        user.setPhoneNum(sysUser.getPhoneNum());
//        String token = UUID.randomUUID().toString();
//        return createSession(user, session, request, model, numKey, token, true);
//    }
//
//    private String createSession(SysUser user, HttpSession session, HttpServletRequest request, Model model,
//                                 String numKey, String token, boolean isLocal) {
//        if("0".equals(user.getStatus())) {
//            if(numKey == null)
//                return "登录信息错误，请联系管理员。";
//            else
//                return loginNumFail(numKey);
//        }
//
//        String url = request.getRequestURL().toString();
//        String urlPrefix = url.substring(0, url.indexOf(request.getContextPath())) + request.getContextPath() + "/";
//
//        // 获取所有菜单
//        List<SysFunc> menuList = sysFuncService.getAllMenu(urlPrefix, token);
//        if (menuList != null && !menuList.isEmpty()) {
//            // PS:自定义标签不支持后台发送到前台，所以发送菜单到前端进行递归展示leftMenu.jsp、recursive.jsp
//            model.addAttribute("menuList", menuList);
//            valiIsCustomer(model, user);
//        }
//        user.setPwd(null);
//        model.addAttribute("sysUser", user);
//        String msg = initSession(token, user, session, isLocal);
//        if(msg == null)
//            model.addAttribute("token", token);
//        return msg;
//    }
//
//    private String loginFail(Model model, String message) {
//        model.addAttribute("appid", config.getAuthAppid());
//        model.addAttribute("redirect_uri", config.getAuthCallback());
//        model.addAttribute("state", LoginAction.scanLoginState());
//        model.addAttribute("message", message);
//        return "../login";
//    }
//
//    private String loginNumFail(String numKey) {
//        long num = RedisApiUtil.getInstance().incr(numKey);
//        //重置过期时间
//        RedisApiUtil.getInstance().expire(numKey, 60 * 30);
//
//        if(num >= LIMIT_MAX)
//            return "用户已锁定，请30分钟后再试。";
//        return "登录信息错误，还有" + (LIMIT_MAX - num) + "次机会。";
//    }
//
//    private String oauthUnionIdNode(String code) {
//        if(StringUtils.isEmpty(code))
//            return null;
//
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
//                "appid=" + config.getAuthAppid() +
//                "&secret=" + config.getAuthSecret() +
//                "&code=" + code + "&grant_type=authorization_code";
//        HttpGet get = new HttpGet(url);
//
//        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
//            HttpResponse resp = client.execute(get);
//            //提取返回值
//            HttpEntity entity = resp.getEntity();
//            String resultStr = EntityUtils.toString(entity, "UTF-8");
//            JsonNode result = new ObjectMapper().readTree(resultStr);
//            JsonNode unionIdNode = result.get("unionid");
//            if (unionIdNode != null) {
//                return unionIdNode.asText();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     *
//     * <B>功能简述</B><br>
//     * 封装所有关于用户的基础信息及权限
//     *
//     * @date 2015-6-1 下午7:55:43
//     * @author Eddy
//     * @param user
//     * @return
//     */
//    private SecurityContext userSecurity(SysUser user) {
//        // 所有有关用户的关联信息都单独储存，不进行封装,使用方便
//        SecurityContext securityContext = new SecurityContext();
//        // 错误信息
//        Map<String, String> messageMap = new HashMap<String, String>();
//        // 用户详细
//        securityContext.setSysUserBasis(user);
//        String userkey = user.getUserbasiskey();
//        // 用户角色
//        securityContext.setSysRoleMList(sysRoleMService.queryUserRoleByUserkey(userkey));
//        // 判断用户是否有角色，或者有角色是否状态正常
//        if (validateRole(securityContext.getSysRoleMList())) {
//            // 用户角色属性
//            securityContext.setRolePropertyList(sysRolePropertyService
//                    .querySysRolePropertyByRoleId(securityContext.getSysRoleMList()));
//            // 用户详细属性
//            if (null != securityContext.getRolePropertyList()
//                    && securityContext.getRolePropertyList().size() != 0) {
//                securityContext.setUserPropertyList(sysUserropertyService
//                        .getSysUserPropertyByRolePptIdAndUserId(
//                                securityContext.getRolePropertyList(), userkey));
//            }
//        } else {
//            messageMap.put("message", "该用户无权限或所属角色已停用，请联系管理员");
//            securityContext.setAttribute(messageMap);
//        }
//        try {
//            // 用户权限（菜单及按钮）
//            List<SysPri> userPri = sysFuncService.findPermissionByUser(userkey);
//            if (null != userPri && userPri.size() != 0) {
//                securityContext.setMapPri(getPrivMap(userPri));
//            } else {
//                messageMap.put("message", "该用户无权限，请联系管理员");
//                securityContext.setAttribute(messageMap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return securityContext;
//    }
//
//    /**
//     *
//     * <B>功能简述</B><br>
//     * 判断用户是否有角色，或者有角色是否状态正常
//     *
//     * @date 2015-6-2 下午9:24:23
//     * @author Eddy
//     * @param sysRoleList
//     * @return
//     */
//    private boolean validateRole(List<SysRole> sysRoleList) {
//        boolean bf = false;
//        if (null != sysRoleList && sysRoleList.size() != 0) {
//            for (SysRole sysRole : sysRoleList) {
//                if (sysRole.getRolestatus().equals("1")) {
//                    bf = true;
//                }
//            }
//        }
//        return bf;
//    }
//
//    /**
//     *
//     * <B>功能简述</B><br>
//     * 封装权限到MAP供标签使用
//     *
//     * @date 2015-6-1 下午7:55:29
//     * @author Eddy
//     * @param privlist
//     * @return
//     * @throws Exception
//     */
//    private Map<String, SysPri> getPrivMap(List<SysPri> privlist) throws Exception {
//        if (privlist == null || privlist.size() == 0) {
//            return null;
//        }
//        Map<String, SysPri> map = new HashMap<String, SysPri>();
//        for (int i = 0; i < privlist.size(); i++) {
//            SysPri sysPri = privlist.get(i);
//            map.put(sysPri.getFunctionkey(), sysPri);
//        }
//        return map;
//    }
//
//    /**
//     * 校验是否为消费者用户
//     *
//     * @param model
//     * @param user
//     */
//    private void valiIsCustomer(Model model, SysUser user) {
//        boolean flag = Constant.USER_TYPE.PLATFORM.equals(user.getCompanykey()) ? false : true;
//        if (flag) {
//            model.addAttribute("isCustomer", "yes");
//        }
//    }
//
//    /**
//     * 用户退出
//     *
//     * @param session
//     * @return
//     */
//    @RequestMapping("/loginOut")
//    public String loginOut(HttpSession session, Model model) {
//        session.removeAttribute(Constant.USER_SESSION);
//        model.addAttribute("message", "");
//        model.addAttribute("appid", config.getAuthAppid());
//        model.addAttribute("redirect_uri", config.getAuthCallback());
//        model.addAttribute("state", LoginAction.scanLoginState());
//        return "../login";
//    }
//
//    /**
//     * 修改密码
//     *
//     * @param session
//     * @param model
//     * @param orgPwd
//     * @param newPwd
//     * @param decPwd
//     * @return
//     */
//    @RequestMapping("/updatePassword")
//    public String updatePassword(HttpSession session, Model model, String orgPwd, String newPwd,
//                                 String decPwd) {
//        SysUser user = this.getUserBasis(session);
//        MD5Util encrypt = new MD5Util();
//
//        if (newPwd.equals(decPwd)) {
//            String pwd = encrypt.getMD5ofStr(newPwd);
//            user.setPwd(pwd);
//            this.sysUserService.updateSysUserBasis(user);
//            model.addAttribute("result", "success");
//        } else {
//            model.addAttribute("result", "not_equal");
//        }
//
//        return "indexPromotion/password/showUserByIdView";
//    }
//
//    /**
//     * 密码校验
//     *
//     * @param userKey
//     * @param name
//     * @return
//     */
//    @RequestMapping("/validPassword")
//    public @ResponseBody
//    String validatePassword(String userKey, String name) {
//        String status = "null";
//
//        try {
//            SysUser sysUser = this.sysUserService.findById(userKey);
//
//            MD5Util encrypt = new MD5Util();
//            if (!StringUtils.isEmpty(name) && sysUser.getPwd().equals(encrypt.getMD5ofStr(name))) {
//                status = "Y";
//            } else {
//                status = "N";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return JSON.toJSONString(status);
//
//    }
//
//    /**
//     * 修改密码跳转
//     *
//     * @param session
//     * @param model
//     * @return
//     */
//
//    @RequestMapping("/showModifyPassword")
//    public String showModifyPassword(HttpSession session, Model model) {
//        SysUser user = getUserBasis(session);
//        model.addAttribute("user", user);
//        return "system/password/modifyPassword";
//    }
//
//    /**
//     * 验证用户名称
//     *
//     * @return
//     */
//    @RequestMapping("/validateUser")
//    public @ResponseBody
//    boolean validateUser(String name) {
//        SysUser su = new SysUser();
//        su.setUsername(name);
//        SysUser sysUsers = sysUserService.validateUser(su);
//        if (sysUsers != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 开发中
//     *
//     * @return
//     */
//    @RequestMapping("/developping")
//    public String developping() {
//        return "../developping";
//    }
//
//    /**
//     * 欢迎页面
//     *
//     * @return
//     */
//    @RequestMapping("/welcome")
//    public String welcome() {
//        return "../welcome";
//    }
//}
