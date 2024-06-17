package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;

/**
 * @author: LiangRunBin
 * @create-date: 2024/1/17 15:21
 */
public class VcodeActivityCogSkuRelation  {

    private String infoKey;
    private String vcodeActivityKey;
    private String skuKey;
    private String createTime;


    public VcodeActivityCogSkuRelation(String cogKey, String skuKey) {
        this.infoKey = UUIDTools.getInstance().getUUID();
        this.vcodeActivityKey = cogKey;
        this.skuKey = skuKey;
        this.createTime = DateUtil.getDateTime();
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
