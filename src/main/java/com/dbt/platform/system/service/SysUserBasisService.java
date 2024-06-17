package com.dbt.platform.system.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.EncryptUtil;
import com.dbt.framework.util.SendSMSUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.system.dao.ISysUserBasisDao;

/**
 * @author RoyFu
 * @createTime 2016年4月21日 上午9:27:34
 * @description
 */
@Service
public class SysUserBasisService extends BaseService<SysUserBasis> {

	@Autowired
	private ISysUserBasisDao iSysUserBasisDao;
    @Autowired
    private ServerInfoService serverInfoService;

	public SysUserBasis loadUserInfo(SysUserBasis loginUser) {
		SysUserBasis currentUser = iSysUserBasisDao.loadUserByUserName(loginUser.getUserName(), loginUser.getUnionid());
		
		// 初始化所属品牌code
		if (currentUser != null && StringUtils.isNotBlank(currentUser.getProjectServerName())) {
		    ServerInfo serverInfo = serverInfoService.findByProjectServerName(currentUser.getProjectServerName());
		    if (serverInfo != null) {
		        currentUser.setBrandCode(serverInfo.getBrandCode());
		    }
		}
		return currentUser;
	}

	public boolean validPassword(SysUserBasis loginUser, SysUserBasis currentUser) throws Exception {
		String loginPwd = EncryptUtil.encrypt(loginUser.getUserPassword());
		String currentPwd = EncryptUtil.decode(currentUser.getUserPassword().getBytes());
		return loginPwd.equals(currentPwd);
	}

	public void updatePassword(SysUserBasis user) {
		iSysUserBasisDao.updatePassword(user);
	}

	public SysUserBasis findById(String userKey) {
		return iSysUserBasisDao.findById(userKey);
	}

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     * @param secretKey 秘钥key（可为空）
     * @return 0：获取失败、 1:获取失败、2:手机号不在白名单中
     */
    public String getPhoneVeriCode(String phoneNum) {
        String bizCode = ResultCode.FILURE;
        phoneNum = StringUtils.defaultIfBlank(phoneNum, "").trim();
        if (phoneNum.length() < 11) return bizCode;

        // 获取平台登录手机白名单
        String phoneNumLst = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.SEND_SMS_ACCOUT, DatadicKey.accountOfsendSMS.VERI_CODE_PLATFROM_PHONENUMS);

        if (StringUtils.isNotBlank(phoneNumLst) && phoneNumLst.contains(phoneNum)) {
            bizCode= SendSMSUtil.getCaptcha(phoneNum, SendSMSUtil.SEND_TYPE_CHECK_CODE_PLATFORM);

        // 不在白名单内
        } else {
            bizCode = ResultCode.RESULT_TWO;
        }

        return bizCode;
    }

    public List<SysUserBasis> queryUserBasisList(){
         return  iSysUserBasisDao.findUserList();
    }
}
