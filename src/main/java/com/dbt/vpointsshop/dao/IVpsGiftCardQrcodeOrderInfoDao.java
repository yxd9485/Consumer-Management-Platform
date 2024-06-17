package com.dbt.vpointsshop.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeOrderInfo;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 礼品卡卡号订单表 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
public interface IVpsGiftCardQrcodeOrderInfoDao extends IBaseDao<VpsGiftCardQrcodeOrderInfo> {


    List<VpsGiftCardQrcodeOrderInfoVO> queryForLst(Map<String, Object> queryMap);

    int queryForCount(Map<String, Object> queryMap);

    void removeGiftCardQrcodeByOrderKey(String itemOrderKey);

    void writeQrcodeToData(Map<String, Object> map);

    void resetGiftCardQrcode(String terminalQrcode);

    void insert(VpsGiftCardQrcodeOrderInfoVO orderInfo);

    VpsGiftCardQrcodeOrderInfoVO findByQrcodeOrderKey(String qrcodeOrderKey);
}
