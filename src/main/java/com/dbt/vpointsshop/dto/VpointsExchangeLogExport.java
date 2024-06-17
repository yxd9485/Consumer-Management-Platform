package com.dbt.vpointsshop.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 兑换日志bean
 * @author zhaohongtao
 *2017年11月16日
 */
@Data
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER
        ,fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND
        ,fillForegroundColor = 48)
@HeadFontStyle(color=1,fontHeightInPoints=10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ColumnWidth(20)
@HeadRowHeight(14)
public class VpointsExchangeLogExport implements Serializable {
	private static final long serialVersionUID = 1L;
    @ExcelProperty("序号")
    @ColumnWidth(10)
    private int index;
    @ExcelProperty("订单编号")
	private String exchangeId;//兑换ID
    @ExcelProperty("支付单号")
    @ColumnWidth(30)
	private String transactionId;
    @ExcelProperty("兑换品牌")
	private String brandName;
    @ExcelProperty("兑换商品名称")
    @ColumnWidth(60)
	private String goodsName;
    @ExcelProperty("商品编号")
	private String goodsClientNo;
    @ExcelProperty("数量")
	private int exchangeNum;
    @ExcelProperty("单品积分")
	private long unitVpoints;
    @ExcelProperty("订单积分")
	private long exchangeVpoints;
    @ExcelProperty("订单金额")
	private String exchangePayMoney;
    @ExcelProperty("累计退款积分")
	private long refundVpoints;
    @ExcelProperty("累计退款金额")
	private String refundPayMoney;
    @ExcelProperty("订单类型")
    private String orderType;
    @ExcelProperty("使用权益")
    @ColumnWidth(30)
	private String quanYiType;
    @ExcelProperty("赠品名称")
	private String giftGoodsName;
    @ExcelProperty("赠品数量")
	private int giftGoodsNum;
    @ExcelProperty("收件人")
	private String userName;
    @ExcelProperty("电话")
	private String phoneNum;
    @ExcelProperty("省")
	private String province;
    @ExcelProperty("市")
	private String city;
    @ExcelProperty("县")
	private String county;
    @ExcelProperty("地址")
    @ColumnWidth(60)
	private String address;
    @ExcelProperty("客户留言")
	private String customerMessage;
    @ExcelProperty("订单时间")
	private String exchangeTime;
    @ExcelProperty("物流公司")
	private String expressCompany;
    @ExcelProperty("物流单号")
	private String expressNumber;
    @ExcelProperty("礼品卡兑换订单")
	private String isGiftCard;
    @ExcelProperty("发货备注")
	private String expressSendMessage;
}