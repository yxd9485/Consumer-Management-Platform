package com.dbt.vpointsshop.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author shuDa
 * @date 2021/12/27
 **/
@Data
public class GiftCardVpointsExchangeLogDTO {
    private String receiveExchangeId;
    private String receiveGiftCardExchangeId;
    private String buyExchangeId;
    private String buyIsGiftCard;
    private long buyExchangePay;
    private long buyExchangeVpoints;
    private String buyGiftCardExchangeId;
    private Date buyExchangeTime;
}
