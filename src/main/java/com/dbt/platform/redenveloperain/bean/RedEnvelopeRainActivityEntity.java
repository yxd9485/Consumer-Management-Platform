package com.dbt.platform.redenveloperain.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_red_envelope_rain_activity_cog")
public class RedEnvelopeRainActivityEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "info_key",type = IdType.ASSIGN_UUID)
    private String infoKey;

    /**
     * 活动名称
     */
    @TableField(value = "activity_name")
    private String activityName;

    /**
     * 区域code, (000000,0100000)
     */
    @TableField("area_codes")
    private String areaCodes;

    /**
     * 开始时间
     */
    @TableField(value = "start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private Date endDate;

    /**
     * 时间段 00:00:00@23:59:59

     */
    @TableField("time_range")
    private String timeRange;

    /**
     * 热区主键

     */
    @TableField("hot_area_key")
    private String hotAreaKey;

    /**
     * 关联活动
     */
    @TableField("vcode_activity_key")
    private String vcodeActivityKey;

    /**
     * 限制时间类型 0、规则时间 1、每天
     */
    @TableField("restrict_time_type")
    private Integer restrictTimeType;

    /**
     * 消费限制用户次数
     */
    @TableField("restrict_user_count")
    private Integer restrictUserCount;

    /**
     * 消费限制金额

     */
    @TableField("restrict_money")
    private Double restrictMoney;

    /**
     * 消费限制瓶数

     */
    @TableField("restrict_bottle")
    private Integer restrictBottle;
    @TableField("delete_flag")
    private String deleteFlag;
    @TableField("rule_content_url")
    private String ruleContentUrl;

    /**
     * 状态：0未启用、1已启用

     */
    @TableField("status_flag")
    private Integer statusFlag;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_time")
    private Date updateTime;

    @TableField("update_user")
    private String updateUser;

}
