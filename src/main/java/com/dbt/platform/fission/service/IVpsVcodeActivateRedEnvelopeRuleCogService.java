package com.dbt.platform.fission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.platform.fission.vo.VpsVcodeActivateRedEnvelopeRuleCogVO;
import com.dbt.platform.system.bean.SysUserBasis;

import java.util.List;

/**
 * <p>
 * 激活红包规则（裂变红包） 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-11
 */
public interface IVpsVcodeActivateRedEnvelopeRuleCogService extends IService<VpsVcodeActivateRedEnvelopeRuleCogEntity> {

    IPage<VpsVcodeActivateRedEnvelopeRuleCogVO>  selectPage(PageOrderInfo pageInfo, VpsVcodeActivateRedEnvelopeRuleCogVO queryBean, String tabsFlag,String companyKey);

    String checkName(String checkName,String activityKey);

    VpsVcodeActivateRedEnvelopeRuleCogVO getOneByActivityKey(String activityKey);

    VcodeActivityCog transformForActivityCog(VpsVcodeActivateRedEnvelopeRuleCogVO oneByActivityKey);

    void changeStatus(String vcodeActivityKey);

    String checkRule(String ruleKey);

    List<String> getTabs1SkuKey(String s,String ruleKey,String startDate,String endDate,String shareType);

    List<String> getTabs1ActivateList(String s, String actityKeyList, String startDate, String endDate,String shareType);

    String updateActivityCog(SysUserBasis currentUser,VpsVcodeActivateRedEnvelopeRuleCogVO param) throws Exception;

    String saveActivityCog(SysUserBasis currentUser, VpsVcodeActivateRedEnvelopeRuleCogVO param)throws Exception;
}
