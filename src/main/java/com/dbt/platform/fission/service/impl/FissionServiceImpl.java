package com.dbt.platform.fission.service.impl;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.platform.fission.service.FissionService;
import org.springframework.stereotype.Service;

import static com.dbt.framework.base.bean.Constant.OrderNoType.type_JH;

/**
 * @author shuDa
 * @date 2022/1/12
 **/
@Service
public class FissionServiceImpl extends BaseService<VpsVcodeActivateRedEnvelopeRuleCogEntity> implements FissionService {
    @Override
    public String getRuleNo() {
        return this.getBussionNo("fission", "rule_no", type_JH);
    }
}
