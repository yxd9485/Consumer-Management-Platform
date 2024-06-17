package com.dbt.vpointsshop.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_combination_discount_cog")
public class VpsCombinationDiscountCog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("cog_key")
    private String cogKey;
      @TableField("cog_no")
    private String cogNo;

    /**
     * 活动名称
     */
    @TableField("name")
    private String name;

    /**
     * 活动开始日期
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 活动结束日期
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 是否开启 0 未开启 1、开启
     */
    @TableField("open_flag")
    private String openFlag;

    /**
     * 商品A
     */
    @TableField("goods_a")
    private String goodsA;

    /**
     * 商品B
     */
    @TableField("goods_b")
    private String goodsB;

    /**
     * 商品C
     */
    @TableField("goods_c")
    private String goodsC;

    /**
     * 活动价格
     */
    @TableField("activity_price")
    private BigDecimal activityPrice;

    /**
     * 单人限购套数
     */
    @TableField("single_person_limit")
    private String singlePersonLimit;

    /**
     * 组合优惠总套数
     */
    @TableField("total_number")
    private Integer totalNumber;

    /**
     * 单日限购套数
     */
    @TableField("single_day_limit")
    private String singleDayLimit;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_time")
    private Date updateTime;

    @TableField("update_user")
    private String updateUser;

    @TableField(select = false)
    private String goodsNameA;
    @TableField(select = false)
    private String goodsNameB;
    @TableField(select = false)
    private String goodsNameC;
    @TableField(select = false)
    private String stateFlag;


    public static final String COG_KEY = "cog_key";

    public static final String NAME = "name";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String OPEN_FLAG = "open_flag";

    public static final String GOODS_A = "goods_a";

    public static final String GOODS_B = "goods_b";

    public static final String GOODS_C = "goods_c";

    public static final String ACTIVITY_PRICE = "activity_price";

    public static final String SINGLE_PERSON_LIMIT = "single_person_limit";

    public static final String TOTAL_NUMBER = "total_number";

    public static final String SINGLE_DAY_LIMIT = "single_day_limit";

    public static final String CREATE_TIME = "create_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_TIME = "update_time";

    public static final String UPDATE_USER = "update_user";

}
