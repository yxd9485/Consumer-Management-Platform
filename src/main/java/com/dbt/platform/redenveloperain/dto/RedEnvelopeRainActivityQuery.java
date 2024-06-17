package com.dbt.platform.redenveloperain.dto;

import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RedEnvelopeRainActivityQuery implements Serializable {
    /**
     * 主键
     */
    private String infoKey;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 区域code
     */
    private String[] areaCodes;
    private String areaCode;
    /**
     * 活动开始时间
     */
    private String startDate;
    /**
     * 活动结束shijian时间
     */
    private String endDate;
    /**
     * 时间段 00:00:00,12:12:12#01:00:00,02:00:00
     */
    private String timeRange;
    private String[] beginTime;
    private String[] endTime;
    /**
     * 热区主键
     */
    private String hotAreaKey;
    /**
     * 关联活动
     */
    private String vcodeActivityKey;
    private String[] vcodeActivityKeyArray;
    /**
     * 限制时间类型 0、规则时间 1、每天
     */
    private int restrictTimeType;
    /** 消费限制用户次数 **/
    private int restrictUserCount;
    /** 消费限制金额 **/
    private double restrictMoney;
    /** 已使用的金额 **/
    private double useRestrictMoney;
    /** 消费限制瓶数 **/
    private int restrictBottle;
    /** 状态：0未启用、1已启用 **/
    private int statusFlag;
    
    private String createTime;
    private String updateTime;

    private String isBegin;
    private List<VcodeActivityRebateRuleCog> rebateRuleCogList;
    private String[] randomType;
    private String[] minMoney;
    private String[] maxMoney;
    private String[] unitMoney;
    private String[] prizePercent;
    private String[] fixationMoney;
    /**
     * 规则图片
     */
    private String ruleContentUrl;

    public RedEnvelopeRainActivityQuery() {
    }
    public RedEnvelopeRainActivityQuery(String queryParam) {
            String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
            this.activityName = paramAry.length > 0 ? paramAry[0] : "";
            this.isBegin = paramAry.length > 1 ? paramAry[1] : "";
            this.startDate = paramAry.length > 2 ? paramAry[2] : "";
            this.endDate = paramAry.length > 3 ? paramAry[3] : "";
    }

    public RedEnvelopeRainActivityQuery(RedEnvelopeRainActivityEntity entity) {
        this.infoKey = entity.getInfoKey();
        this.activityName = entity.getActivityName();
        this.areaCodes = entity.getAreaCodes().split(",");
        this.areaCode = entity.getAreaCodes();
        this.startDate = DateUtil.getDateTime(entity.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT);
        this.endDate = DateUtil.getDateTime(entity.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT);
        this.timeRange = entity.getTimeRange();
        String[] timeRangeArray = entity.getTimeRange().split(",");
        this.beginTime = new String[timeRangeArray.length];
        this.endTime = new String[timeRangeArray.length];
        for (int i = 0; timeRangeArray.length > i; i++) {
            beginTime[i] = timeRangeArray[i].split("@")[0];
            endTime[i] = timeRangeArray[i].split("@")[1];
        }
        this.hotAreaKey = entity.getHotAreaKey();
        this.vcodeActivityKey = entity.getVcodeActivityKey();
        if(StringUtils.isNotEmpty(entity.getVcodeActivityKey())){
            this.vcodeActivityKeyArray = entity.getVcodeActivityKey().split(",");
        }
        this.restrictTimeType = entity.getRestrictTimeType();
        this.restrictUserCount = entity.getRestrictUserCount();
        this.restrictMoney = entity.getRestrictMoney();
        this.restrictBottle = entity.getRestrictBottle();
        this.statusFlag = entity.getStatusFlag();
        this.ruleContentUrl = entity.getRuleContentUrl();
        this.createTime = DateUtil.getDateTime(entity.getCreateTime(),DateUtil.DEFAULT_DATETIME_FORMAT);
        this.updateTime = DateUtil.getDateTime(entity.getUpdateTime(),DateUtil.DEFAULT_DATETIME_FORMAT);
    }

}
