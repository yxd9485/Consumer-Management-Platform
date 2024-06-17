package com.dbt.platform.mn.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dbt.framework.base.bean.Constant;
import com.dbt.platform.mn.bean.TerminalRankActivityAreaCog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class TerminalRankActivityCogVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String infoKey;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动开始时间
     */
    private String startTime;

    /**
     * 活动结束时间
     */
    private String endTime;

    /**
     * 是否包含级联数据 0 否 1 是
     */
    private String cascadeFlag;
    /**
     * 0 组织架构 ，1 行政区域
     */
    private String activityType;

    /**
     * 活动角色
     */
    private String activityRole;

    /**
     * 活动 sku
     */
    private String skuKeys;

    /**
     * 活动规则
     */
    private String ruleContent;
    /**
     * 入口 banner
     */
    private String initBannerUrl;

    private String activityBannerUrl;
    private String activityArea;
    private String activityAreaName;
    private String department;
    private String departmentName;
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    private String isBegin;
    private List<TerminalRankActivityAreaCog> terminalRankActivityAreaCogList = new ArrayList<>();
    public TerminalRankActivityCogVO() {
    }
    public TerminalRankActivityCogVO(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.activityType = paramAry.length > 1 ? paramAry[1] : "";
        this.isBegin = paramAry.length > 2 ? paramAry[2] : "";
        this.startTime = paramAry.length > 3 ? paramAry[3] : "";
        this.endTime = paramAry.length > 4 ? paramAry[4] : "";
    }


}
