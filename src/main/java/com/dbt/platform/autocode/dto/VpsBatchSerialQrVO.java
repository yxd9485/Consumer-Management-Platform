package com.dbt.platform.autocode.dto;

import com.dbt.platform.autocode.bean.VpsBatchSerialQr;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class VpsBatchSerialQrVO extends VpsBatchSerialQr {
    private String startDate;
    private String endDate;

    public VpsBatchSerialQrVO() {

    }
    public VpsBatchSerialQrVO(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.startDate = paramAry.length > 0 ? paramAry[0] : "";
        this.endDate = paramAry.length > 1 ? paramAry[1] : "";
    }
}
