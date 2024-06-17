package com.dbt.platform.mn.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mn_terminal_info")
public class MnTerminalInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 网点ID（主键）
     */
    @TableId("ID")
    private Long id;

    /**
     * 麒麟平台网点编码
     */
    @TableField("TER_CODE")
    private String ter_code;

    /**
     * MDG网点编码
     */
    @TableField("MDG_TERCODE")
    private String mdg_tercode;

    /**
     * 牛掌柜编码
     */
    @TableField("NZGTERID")
    private String nzgterid;

    /**
     * 所属大区ID
     */
    @TableField("MDGBIGAREAID")
    private String mdgbigareaid;

    /**
     * 归属大区名称
     */
    @TableField("MDGBIGAREANAME")
    private String mdgbigareaname;

    /**
     * 所属省区ID
     */
    @TableField("MDGPROVINCEID")
    private String mdgprovinceid;

    /**
     * 归属省区名称
     */
    @TableField("MDGPROVINCENAME")
    private String mdgprovincename;

    /**
     * 所属片区ID
     */
    @TableField("SECAREAID")
    private String secareaid;

    /**
     * 所属片区名称
     */
    @TableField("SECAREANAME")
    private String secareaname;

    /**
     * 所属区块ID
     */
    @TableField("GRIDID")
    private Long gridid;

    /**
     * 所属区块名称
     */
    @TableField("GRIDNAME")
    private String gridname;

    /**
     * 所属路线ID
     */
    @TableField("ROUTEID")
    private Long routeid;

    /**
     * 所属路线名称
     */
    @TableField("ROUTENAME")
    private String routename;

    /**
     * 网点名称
     */
    @TableField("TER_NAME")
    private String ter_name;

    /**
     * 联系人
     */
    @TableField("TER_CONTACT")
    private String ter_contact;

    /**
     * 联系电话
     */
    @TableField("TER_MOBILE")
    private String ter_mobile;

    /**
     * 省
     */
    @TableField("TER_PROVINCE")
    private String ter_province;

    /**
     * 省区名称
     */
    @TableField("PROVINCENAME")
    private String provincename;

    /**
     * 市
     */
    @TableField("TER_CITY")
    private String ter_city;

    /**
     * 市区名称
     */
    @TableField("CITYNAME")
    private String cityname;

    /**
     * 区县
     */
    @TableField("TER_COUNTY")
    private String ter_county;

    /**
     * 区县名称
     */
    @TableField("COUNTYNAME")
    private String countyname;

    /**
     * 详细地址
     */
    @TableField("TER_ADDRESS")
    private String ter_address;

    /**
     * 经度
     */
    @TableField("LON")
    private String lon;

    /**
     * 维度
     */
    @TableField("LAT")
    private String lat;

    /**
     * 定位地址
     */
    @TableField("TER_POIADDRESS")
    private String ter_poiaddress;

    /**
     * 渠道类型1级
     */
    @TableField("MAINCHANNEL")
    private Long mainchannel;

    /**
     * 渠道类型1级名称
     */
    @TableField("MAINCHANNELNAME")
    private String mainchannelname;

    /**
     * 渠道类型2级
     */
    @TableField("MINCHANNEL")
    private Long minchannel;

    /**
     * 渠道类型2级名称
     */
    @TableField("MINCHANNELNAME")
    private String minchannelname;

    /**
     * 状态 Y有效N无效
     */
    @TableField("STATUS")
    private String status;

    /**
     * 注册状态 Y已注册N未注册
     */
    @TableField("REGISTERSTATUS")
    private String registerstatus;

    /**
     * 创建时间
     */
    @TableField("CRETIME")
    private Date cretime;

    /**
     * 更新时间
     */
    @TableField("UPTIME")
    private Date uptime;

    /**
     * 图片
     */
    @TableField("ter_picpath")
    private String ter_picpath;


    public static final String ID = "ID";

    public static final String TER_CODE = "TER_CODE";

    public static final String MDG_TERCODE = "MDG_TERCODE";

    public static final String NZGTERID = "NZGTERID";

    public static final String MDGBIGAREAID = "MDGBIGAREAID";

    public static final String MDGBIGAREANAME = "MDGBIGAREANAME";

    public static final String MDGPROVINCEID = "MDGPROVINCEID";

    public static final String MDGPROVINCENAME = "MDGPROVINCENAME";

    public static final String SECAREAID = "SECAREAID";

    public static final String SECAREANAME = "SECAREANAME";

    public static final String GRIDID = "GRIDID";

    public static final String GRIDNAME = "GRIDNAME";

    public static final String ROUTEID = "ROUTEID";

    public static final String ROUTENAME = "ROUTENAME";

    public static final String TER_NAME = "TER_NAME";

    public static final String TER_CONTACT = "TER_CONTACT";

    public static final String TER_MOBILE = "TER_MOBILE";

    public static final String TER_PROVINCE = "TER_PROVINCE";

    public static final String PROVINCENAME = "PROVINCENAME";

    public static final String TER_CITY = "TER_CITY";

    public static final String CITYNAME = "CITYNAME";

    public static final String TER_COUNTY = "TER_COUNTY";

    public static final String COUNTYNAME = "COUNTYNAME";

    public static final String TER_ADDRESS = "TER_ADDRESS";

    public static final String LON = "LON";

    public static final String LAT = "LAT";

    public static final String TER_POIADDRESS = "TER_POIADDRESS";

    public static final String MAINCHANNEL = "MAINCHANNEL";

    public static final String MAINCHANNELNAME = "MAINCHANNELNAME";

    public static final String MINCHANNEL = "MINCHANNEL";

    public static final String MINCHANNELNAME = "MINCHANNELNAME";

    public static final String STATUS = "STATUS";

    public static final String REGISTERSTATUS = "REGISTERSTATUS";

    public static final String CRETIME = "CRETIME";

    public static final String UPTIME = "UPTIME";

}
