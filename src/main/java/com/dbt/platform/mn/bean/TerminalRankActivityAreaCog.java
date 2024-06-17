package com.dbt.platform.mn.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("terminal_rank_activity_area_cog")
public class TerminalRankActivityAreaCog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("terminal_rank_info_key")
    private String terminalRankInfoKey;

    @TableField("province_name")
    private String provinceName;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("city_name")
    private String cityName;

    @TableField("county")
    private String county;

    @TableField("county_name")
    private String countyName;

    @TableField("LEVEL_CODE2")
    private String levelCode2;

    @TableField("LEVEL_NAME2")
    private String levelName2;

    @TableField("LEVEL_CODE3")
    private String levelCode3;

    @TableField("LEVEL_NAME3")
    private String levelName3;

    @TableField("LEVEL_CODE4")
    private String levelCode4;

    @TableField("LEVEL_NAME4")
    private String levelName4;

    @TableField("LEVEL_CODE5")
    private String levelCode5;

    @TableField("LEVEL_NAME5")
    private String levelName5;


    public static final String ID = "id";

    public static final String TERMINAL_RANK_INFO_KEY = "terminal_rank_info_key";

    public static final String PROVINCE_NAME = "province_name";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String CITY_NAME = "city_name";

    public static final String COUNTY = "county";

    public static final String COUNTY_NAME = "county_name";

    public static final String LEVEL_CODE2 = "LEVEL_CODE2";

    public static final String LEVEL_NAME2 = "LEVEL_NAME2";

    public static final String LEVEL_CODE3 = "LEVEL_CODE3";

    public static final String LEVEL_NAME3 = "LEVEL_NAME3";

    public static final String LEVEL_CODE4 = "LEVEL_CODE4";

    public static final String LEVEL_NAME4 = "LEVEL_NAME4";

    public static final String LEVEL_CODE5 = "LEVEL_CODE5";

    public static final String LEVEL_NAME5 = "LEVEL_NAME5";

}
