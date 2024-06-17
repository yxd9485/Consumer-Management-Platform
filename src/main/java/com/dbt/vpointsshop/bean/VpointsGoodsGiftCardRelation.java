package com.dbt.vpointsshop.bean;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 礼品卡活动商品关联表
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vpoints_goods_gift_card_relation")
public class VpointsGoodsGiftCardRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "info_key",type = IdType.ASSIGN_UUID)
    private String infoKey;

    /**
     * 商品主键
     */
    @TableField("goods_id")
    private String goodsId;

    /**
     * 礼品卡活动主键
     */
    @TableField("gift_card_info_key")
    private String giftCardInfoKey;


    public static final String INFO_KEY = "info_key";

    public static final String GOODS_ID = "goods_id";

    public static final String GIFT_CARD_INFO_KEY = "gift_card_info_key";

}
