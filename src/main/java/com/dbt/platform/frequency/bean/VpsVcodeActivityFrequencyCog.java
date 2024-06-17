package com.dbt.platform.frequency.bean;


import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeActivityFrequencyCog extends BasicProperties {

  private String infoKey;
  private String companyKey;
  /** 关联活动key */
  private String vcodeActivityKey;
  /** 频次活动编号 */
  private String frequencyNo;
  /** 频次活动名称 */
  private String frequencyName;
  /** 时间范围格式 */
  private String validTimeRange;
  /** 最小有效时间 */
  private String minValidTime;
  /** 最大有效时间 */
  private String maxValidTime;
  /** 限制时间类型 0规则时间 1每天*/
  private String restrictTimeType;
  /** 每人中出次数 */
  private long restrictUserCount;
  /** 配置对象 */
  private String freItems;
  /** 区域范围限制 */
  private String areaRange;
  /** 状态：0未启用、1已启用 */
  private String status;
  private String deleteFlag;
  private String createUser;
  private String createTime;
  private String updateUser;
  private String updateTime;
  private String isBegin;


    public VpsVcodeActivityFrequencyCog(){}
    public VpsVcodeActivityFrequencyCog(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.frequencyNo = paramAry.length > 0 ? paramAry[0] : "";
        this.frequencyName = paramAry.length > 1 ? paramAry[1] : "";
        this.status = paramAry.length > 2 ? paramAry[2] : "";
        this.isBegin = paramAry.length > 3 ? paramAry[3] : "";
    }


    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getFrequencyNo() {
        return frequencyNo;
    }

    public void setFrequencyNo(String frequencyNo) {
        this.frequencyNo = frequencyNo;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getValidTimeRange() {
        return validTimeRange;
    }

    public void setValidTimeRange(String validTimeRange) {
        this.validTimeRange = validTimeRange;
    }

    public String getMinValidTime() {
        return minValidTime;
    }

    public void setMinValidTime(String minValidTime) {
        this.minValidTime = minValidTime;
    }

    public String getMaxValidTime() {
        return maxValidTime;
    }

    public void setMaxValidTime(String maxValidTime) {
        this.maxValidTime = maxValidTime;
    }

    public String getRestrictTimeType() {
        return restrictTimeType;
    }

    public void setRestrictTimeType(String restrictTimeType) {
        this.restrictTimeType = restrictTimeType;
    }

    public long getRestrictUserCount() {
        return restrictUserCount;
    }

    public void setRestrictUserCount(long restrictUserCount) {
        this.restrictUserCount = restrictUserCount;
    }

    public String getFreItems() {
        return freItems;
    }

    public void setFreItems(String freItems) {
        this.freItems = freItems;
    }

    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
