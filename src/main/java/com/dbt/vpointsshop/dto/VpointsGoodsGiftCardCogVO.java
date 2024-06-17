package com.dbt.vpointsshop.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 礼品卡活动表
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VpointsGoodsGiftCardCogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String infoKey;
    private String infoNo;

    /**
     * 礼品卡名称
     */
    private String giftCardName;

    /**
     * 礼品卡类型，0普通卡，1充值卡，2兑付卡
     */
    private String giftCardType;
    private String giftCardStatus;

    /**
     * 封面
     */
    private String cover;

    /**
     * 礼品卡上线状态：0、待上线 1、已上线 2、已下线
     */
    private String cardStatus;

    /**
     * 停止卖卡：0、否 1、是
     */
    private String stopSellCards;

    /**
     * 支付金额
     */
    private String realPay;

    /**
     * 面额
     */
    private String denomination;

    /**
     * 有效期，单位：月
     */
    private Integer termOfValidity;

    private String createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    /**
     * 0 未删除 1 已删除
     */
    private String deleteFlag;
    //扩展查询参数
    @TableField(select = false)
    private String startDate;
    @TableField(select = false)
    private String endDate;
    private String isBegin;
    private String goodsName;
    private String exchangeId;
    private List<String> goodsIdList;
    private String startRealPay;
    private String endRealPay;
    private String goodsId;
}
