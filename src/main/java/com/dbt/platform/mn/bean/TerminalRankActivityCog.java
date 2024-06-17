package com.dbt.platform.mn.bean;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("terminal_rank_activity_cog")
public class TerminalRankActivityCog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId("info_key")
    private String infoKey;

    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;

    /**
     * 活动开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 是否包含级联数据 0 否 1 是
     */
    @TableField("cascade_flag")
    private String cascadeFlag;

    /**
     * 0 组织架构 ，1 行政区域
     */
    @TableField("activity_type")
    private String activityType;

    /**
     * 活动角色
     */
    @TableField("activity_role")
    private String activityRole;

    /**
     * 活动 sku
     */
    @TableField("sku_keys")
    private String skuKeys;

    /**
     * 活动规则
     */
    @TableField("rule_content")
    private String ruleContent;

    /**
     * 入口 banner
     */
    @TableField("init_banner_url")
    private String initBannerUrl;

    @TableField("activity_banner_url")
    private String activityBannerUrl;

    @TableField("create_user")
    private String createUser;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_user")
    private String updateUser;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 0 删除 1 有效
     */
    @TableField("delete_flag")
    @TableLogic(value = "1",delval = "0")
    private String deleteFlag;


    public static final String INFO_KEY = "info_key";

    public static final String ACTIVITY_NAME = "activity_name";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String CASCADE_FLAG = "cascade_flag";

    public static final String ACTIVITY_TYPE = "activity_type";

    public static final String ACTIVITY_ROLE = "activity_role";

    public static final String SKU_KEYS = "sku_keys";

    public static final String RULE_CONTENT = "rule_content";

    public static final String INIT_BANNER_URL = "init_banner_url";

    public static final String ACTIVITY_BANNER_URL = "activity_banner_url";
    public static final String DELETE_FLAG = "delete_flag";
    public static final String UPDATE_TIME = "update_time";
    public static final String CREATE_TIME = "create_time";

}
