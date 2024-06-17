package com.dbt.platform.mn.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dbt.platform.mn.bean.RankReportAll;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-19
 */
@Data
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER
        ,fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND
        ,fillForegroundColor = 48)
@HeadFontStyle(color=1,fontHeightInPoints=10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ColumnWidth(20)
@HeadRowHeight(14)
public class RankReportAllExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户key
     */
    @ExcelProperty("用户 id")
    @ColumnWidth(10)
    private String userKey;

    @ExcelProperty("姓名")
    private String nickName;

    @ExcelProperty("手机号")
    private String phoneNum;
    @ExcelProperty("角色")
    private String userRole;
    @ExcelProperty("scanSku")
    private String scanSku;
    @ExcelProperty("扫码数量")
    private Long scanCount;
    @ExcelProperty("扫码数量排行")
    private Integer rankNum;
    @ExcelProperty("lev2Name")
    private String lev2Name;

    @ExcelProperty("lev3Name")
    private String lev3Name;

    @ExcelProperty("lev4Name")
    private String lev4Name;

    @ExcelProperty("lev5Name")
    private String lev5Name;

    @ExcelProperty("省")
    private String province;

    @ExcelProperty("市")
    private String city;

    @ExcelProperty("县")
    private String county;

    public RankReportAllExcel(RankReportAll rank) {
        this.userKey = rank.getUserKey();
        this.nickName = rank.getNickName();
        this.phoneNum = rank.getPhoneNum();
        this.userRole = rank.getUserRole();

        this.scanSku = rank.getScanSku();
        this.scanCount = rank.getScanCount();
        this.rankNum = rank.getRankNum();
        this.lev2Name = rank.getLev2Name();
        this.lev3Name = rank.getLev3Name();
        this.lev4Name = rank.getLev4Name();
        this.lev5Name = rank.getLev5Name();
        this.province = rank.getProvince();
        this.city = rank.getCity();
        this.county = rank.getCounty();
    }
}
