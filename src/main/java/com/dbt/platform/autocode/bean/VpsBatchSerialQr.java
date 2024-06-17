package com.dbt.platform.autocode.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vps_vcode_batch_serial_qr_record")
public class VpsBatchSerialQr {
    @TableId("id")
    private String id;
    @TableField("query_number")
    private Integer queryNumber;
    @TableField("query_User_Phone_Num")
    private String queryUserPhoneNum;
    @TableField("import_Excel_Path")
    private String importExcelPath;
    @TableField("qr_url")
    private String qrUrl;
    @TableField("query_status")
    private String queryStatus;
    @TableField("create_time")
    private String createTime;
}
