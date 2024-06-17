package com.dbt.platform.enterprise.service;

import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.enterprise.bean.ConsumerCenterCogInfo;
import com.dbt.platform.enterprise.dao.IConsumerCenterDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerCenterService {
    @Autowired
    private IConsumerCenterDao consumerCenterDao;


    public List<ConsumerCenterCogInfo> queryForLst() {
        return consumerCenterDao.queryForLst();
    }


    public String doConsumerCenterAdd(ConsumerCenterCogInfo consumerCenterCogInfo, SysUserBasis currentUser) {

        int idx = -1;
        ConsumerCenterCogInfo item = null;
        List<ConsumerCenterCogInfo> resultConsumerCenterCogInfoList = new ArrayList<>();
        String[] consumerCenterType = consumerCenterCogInfo.getConsumerCenterType().split(",");
        String[] servicePhonenumDescribe = consumerCenterCogInfo.getServicePhonenumDescribe().split(",");
        String[] servicePhonenum = consumerCenterCogInfo.getServicePhonenum().split(",");
        String[] companyWechatDescribe = consumerCenterCogInfo.getCompanyWechatDescribe().split(",");
        String[] companyWechatLink = consumerCenterCogInfo.getCompanyWechatLink().split(",");
        String[] consumerStatusType = consumerCenterCogInfo.getStatus().split(",");
        
        for (String consumerCenter : consumerCenterType) {
            idx++;
            item = new ConsumerCenterCogInfo();
            item.setInfoKey(UUIDTools.getInstance().getUUID());
            item.setConsumerCenterType(idx < consumerCenterType.length ? consumerCenterType[idx] : "");
            item.setStatus(idx < consumerStatusType.length ? consumerStatusType[idx] : "");
            item.setOrderFlag(String.valueOf(idx));
            item.setDeleteFlag("0");
            item.setUpdateUser(currentUser.getUserName());
            item.setUpdateTime(DateUtil.getDateTime());
            
            if (consumerCenter.equals("0")) {
                item.setServicePhonenumDescribe(idx < servicePhonenumDescribe.length ? servicePhonenumDescribe[idx] : "");
                item.setServicePhonenum(idx < servicePhonenum.length ? servicePhonenum[idx] : "");
                if (StringUtils.isBlank(item.getServicePhonenumDescribe()) || StringUtils.isBlank(item.getServicePhonenum())) {
                    return "客服中心类型为电话时 客服电话描述不能为空";
                }
                
            } else if (consumerCenter.equals("1")) {
                item.setCompanyWechatDescribe(idx < companyWechatDescribe.length ? companyWechatDescribe[idx] : "");
                item.setCompanyWechatLink(idx < companyWechatLink.length ? companyWechatLink[idx] : "");
                if (StringUtils.isBlank(item.getCompanyWechatDescribe()) || StringUtils.isBlank(item.getCompanyWechatLink())) {
                    return "客服中心类型为链接时 企业微信描述及链接不能为空";
                }
            } else if (consumerCenter.equals("2")) {
                item.setServicePhonenumDescribe(idx < servicePhonenumDescribe.length ? servicePhonenumDescribe[idx] : "");
                if (StringUtils.isBlank(item.getServicePhonenumDescribe())) {
                    return "客服中心类型为原生时 客服电话描述不能为空";
                }
            }
            resultConsumerCenterCogInfoList.add(item);
        }

        // 添加之前将原有数据删除
        consumerCenterDao.deleteConsumerCenter();
        
        consumerCenterDao.create(resultConsumerCenterCogInfoList);
        return "保存成功";
    }
}
