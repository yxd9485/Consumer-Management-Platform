package com.dbt.vpointsshop.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class VpsCombinationDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cogKey;
    private String cogNo;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动开始日期
     */
    private Date startTime;

    /**
     * 活动结束日期
     */
    private Date endTime;

    /**
     * 是否开启 0 未开启 1、开启
     */
    private String openFlag;

    /**
     * 商品A
     */
    private String goodsA;

    /**
     * 商品B
     */
    private String goodsB;

    /**
     * 商品C
     */
    private String goodsC;

    /**
     * 活动价格
     */
    private BigDecimal activityPrice;

    /**
     * 单人限购套数
     */
    private String singlePersonLimit;

    /**
     * 组合优惠总套数
     */
    private Integer totalNumber;

    /**
     * 单日限购套数
     */
    private String singleDayLimit;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private String goodsNameA;
    private String goodsNameB;
    private String goodsNameC;
    private String stateFlag;
}
