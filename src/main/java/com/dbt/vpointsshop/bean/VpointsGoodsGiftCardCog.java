package com.dbt.vpointsshop.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.dbt.framework.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

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
@TableName("vpoints_goods_gift_card_cog")
public class VpointsGoodsGiftCardCog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId("info_key")
    private String infoKey;
    /**
     * 编号
     */
      @TableField("info_no")
    private String infoNo;

    /**
     * 礼品卡名称
     */
    @TableField("gift_card_name")
    private String giftCardName;

    /**
     * 礼品卡类型，0普通卡，1充值卡，2兑付卡
     */
    @TableField("gift_card_type")
    private String giftCardType;
    /**
     * 礼品卡类型，0普通卡，1商务卡，2兑付卡,3至尊卡，4超级至尊卡
     */
    @TableField("gift_card_status")
    private String giftCardStatus;

    /**
     * 封面
     */
    @TableField("cover")
    private String cover;

    /**
     * 礼品卡上线状态：0、待上线 1、已上线 2、已下线
     */
    @TableField("card_status")
    private String cardStatus;

    /**
     * 停止卖卡：0、否 1、是
     */
    @TableField("stop_sell_cards")
    private String stopSellCards;

    /**
     * 支付金额
     */
    @TableField("real_pay")
    private String realPay;

    /**
     * 面额
     */
    @TableField("denomination")
    private String denomination;

    /**
     * 有效期，单位：月
     */
    @TableField("term_of_validity")
    private Integer termOfValidity;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_time")
    private Date updateTime;

    @TableField("update_user")
    private String updateUser;

    /**
     * 0 未删除 1 已删除
     */
    @TableField("delete_flag")
    private String deleteFlag;
    //扩展查询参数
    @TableField(select = false)
    private String startDate;
    @TableField(select = false)
    private String endDate;

    public VpointsGoodsGiftCardCog(){
    }
    public VpointsGoodsGiftCardCog(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.giftCardName = paramAry.length > 0 ? paramAry[0] : "";
        this.giftCardType = paramAry.length > 1 ? paramAry[1] : "";
        this.cardStatus = paramAry.length > 2 ? paramAry[2] : "";
        this.startDate = paramAry.length > 3 ? DateUtil.getStartDateTimeByDay(paramAry[3]) : "";
        this.endDate = paramAry.length > 4 ? DateUtil.getEndDateTimeByDay(paramAry[4]) : "";
    }

}
