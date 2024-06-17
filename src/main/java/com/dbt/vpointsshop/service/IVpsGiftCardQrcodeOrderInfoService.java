package com.dbt.vpointsshop.service;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeOrderInfo;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoVO;

import java.util.List;

/**
 * <p>
 * 礼品卡卡号订单表 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
public interface IVpsGiftCardQrcodeOrderInfoService  {

    String updateOrderInfo(VpsGiftCardQrcodeOrderInfo orderInfo);

    void addOrderInfo(VpsGiftCardQrcodeOrderInfoVO orderInfo, String userKey);

    int queryForCount(VpsGiftCardQrcodeOrderInfoVO queryBean);

    List<VpsGiftCardQrcodeOrderInfoVO> queryForLst(VpsGiftCardQrcodeOrderInfoVO queryBean, PageOrderInfo pageInfo)  throws Exception;

    VpsGiftCardQrcodeOrderInfo findById(String batchKey);

    void deleteOrderInfoById(String orderKey, String userKey) throws Exception;

    void createCodeMain(VpsGiftCardQrcodeOrderInfo orderInfo, String userKey) throws Exception;
}
