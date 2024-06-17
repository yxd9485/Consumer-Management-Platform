package com.dbt.platform.mn.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rank_report_all")
public class RankReportAll implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("infoKey")
    private String infoKey;

    /**
     * 活动key
     */
    @TableField("activityKey")
    private String activityKey;

    /**
     * 用户key
     */
    @TableField("userKey")
    private String userKey;
    /**
     * userRole
     */
    @TableField("userRole")
    private String userRole;

    /**
     * 扫码数量
     */
    @TableField("scanCount")
    private Long scanCount;

    /**
     * 排行
     */
    @TableField("rankNum")
    private Integer rankNum;

    /**
     * sku扫码数量
     */
    @TableField("scanSku")
    private String scanSku;

    @TableField("lev2Name")
    private String lev2Name;

    @TableField("lev3Name")
    private String lev3Name;

    @TableField("lev4Name")
    private String lev4Name;

    @TableField("lev5Name")
    private String lev5Name;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("county")
    private String county;

    @TableField("nickName")
    private String nickName;

    @TableField("phoneNum")
    private String phoneNum;


    public static final String INFOKEY = "infoKey";

    public static final String ACTIVITYKEY = "activityKey";

    public static final String USERKEY = "userKey";

    public static final String SCANCOUNT = "scanCount";

    public static final String RANKNUM = "rankNum";

    public static final String SCANSKU = "scanSku";

    public static final String LEV2NAME = "lev2Name";

    public static final String LEV3NAME = "lev3Name";

    public static final String LEV4NAME = "lev4Name";

    public static final String LEV5NAME = "lev5Name";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String COUNTY = "county";

    public static final String NICKNAME = "nickName";

    public static final String PHONENUM = "phoneNum";

}
