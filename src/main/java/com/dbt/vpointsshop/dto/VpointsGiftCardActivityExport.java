package com.dbt.vpointsshop.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 礼品卡订单导出excel实体类
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
public class VpointsGiftCardActivityExport implements Serializable {
	private static final long serialVersionUID = 1L;
    @ExcelProperty("订单编号")
	private String exchangeId;
    @ExcelProperty("商品名称")
    @ColumnWidth(30)
	private String goodsName;
    @ExcelProperty("礼品卡名称")
    private String giftCardName;
    @ExcelProperty("礼品卡类型")
    private String giftCardType;
    @ExcelProperty("支付方式")
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