package com.dbt.vpointsshop.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 商城第二件半价活动
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vpoints_goods_half_price_activity_cog")
public class VpointsGoodsHalfPriceActivityCogEntity extends BasicProperties {

    /**
     * 主键
     */
    private String infoKey;

    /**
     * 活动名称
     */
    private String infoName;

    /**
     * 活动编号
     */
    private String infoNo;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 用户购买限制
     */
    private String consumerBuyLimit;


    private String isBegin;
    private String discount;
    public VpointsGoodsHalfPriceActivityCogEntity(){
    }
    public VpointsGoodsHalfPriceActivityCogEntity(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.infoName = paramAry.length > 0 ? paramAry[0] : "";
        this.isBegin = paramAry.length > 1 ? paramAry[1] : "";
        try {
            this.startDate = paramAry.length > 2 ? DateUtil.parse(paramAry[2],DateUtil.DEFAULT_DATE_FORMAT) : null;
            this.endDate = paramAry.length > 3 ?  DateUtil.parse(paramAry[3],DateUtil.DEFAULT_DATE_FORMAT) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
