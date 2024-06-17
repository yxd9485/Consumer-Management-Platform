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
@TableName("mn_department")
public class MnDepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID（主键）
     */
    @TableId("ID")
    private Long id;

    /**
     * 部门父id
     */
    @TableField("PID")
    private Long pid;

    /**
     * 部门名称
     */
    @TableField("DEP_NAME")
    private String dep_name;

    /**
     * 排序号
     */
    @TableField("ORDER_NUM")
    private Integer order_num;

    /**
     * 备注
     */
    @TableField("PRO_REMARK")
    private String pro_remark;

    /**
     * 归属mdg大区id
     */
    @TableField("MDGBIGAREAID")
    private String mdgbigareaid;

    /**
     * 归属mdg省区id
     */
    @TableField("MDGPROVINCEID")
    private String mdgprovinceid;

    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * 等级
     */
    @TableField("LEVEL")
    private Integer level;

    /**
     * 创建人id
     */
    @TableField("CREUSER")
    private Long creuser;

    /**
     * 修改人id
     */
    @TableField("UPUSER")
    private Long upuser;

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


    public static final String ID = "ID";

    public static final String PID = "PID";

    public static final String DEP_NAME = "DEP_NAME";

    public static final String ORDER_NUM = "ORDER_NUM";

    public static final String PRO_REMARK = "PRO_REMARK";

    public static final String MDGBIGAREAID = "MDGBIGAREAID";

    public static final String MDGPROVINCEID = "MDGPROVINCEID";

    public static final String STATUS = "STATUS";

    public static final String LEVEL = "LEVEL";

    public static final String CREUSER = "CREUSER";

    public static final String UPUSER = "UPUSER";

    public static final String CRETIME = "CRETIME";

    public static final String UPTIME = "UPTIME";

}
