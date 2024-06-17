package com.dbt.platform.autocode.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.dbt.platform.autocode.bean.VpsBatchSerialQr;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.sl.draw.DrawNotImplemented;

@Data
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER
        ,fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND
        ,fillForegroundColor = 48)
@HeadFontStyle(color=1,fontHeightInPoints=10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ColumnWidth(20)
@HeadRowHeight(14)
public class VpsBatchSerialQrExcelTemplate  {
    @ExcelProperty(value = "二维码编号")
    @ColumnWidth(60)
    private String batchSerialNo;
    @ExcelProperty(value = "查询原因")
    @ColumnWidth(60)
    private String queryNotes;
    @ExcelProperty(value = "查询结果")
    private String errMsg;

}
