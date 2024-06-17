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
public class VpointsHalfPriceActivityExport implements Serializable {
	private static final long serialVersionUID = 1L;
    @ExcelProperty("订单编号")
	private String exchangeId;
    @ExcelProperty("商品名称")
    @ColumnWidth(30)
	private String goodsName;
    @ExcelProperty("商品价格")
	private String goodsPrice;
    @ExcelProperty("第二件价格")
	private String secondGoodsPrice;
    @ExcelProperty("下单数量")
	private String exchangeNum;
    @ExcelProperty("订单类型")
	private String orderType;
    @ExcelProperty("购买方式")
	private String payTypeName;
    @ExcelProperty("订单金额(元)")
	private String exchangePay;
    @ExcelProperty("订单时间")
	private String exchangeTime;
    @ExcelProperty("收件人")
    @ColumnWidth(30)
	private String address;
    @ExcelProperty("订单状态")
	private String exchangeStatus;
}