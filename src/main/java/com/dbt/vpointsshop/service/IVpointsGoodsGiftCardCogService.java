package com.dbt.vpointsshop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardCog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.vpointsshop.dto.VpointsGoodsGiftCardCogVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 礼品卡活动表 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
public interface IVpointsGoodsGiftCardCogService {
    public String getRuleNo();

    IPage<VpointsGoodsGiftCardCogVO> selectPageVO(IPage initPage, VpointsGoodsGiftCardCog entity);

    boolean create(VpointsGoodsGiftCardCogVO vo);

    VpointsGoodsGiftCardCogVO findByInfoKey(String infoKey);

    boolean update(VpointsGoodsGiftCardCogVO vo);

    BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsGiftCardCogVO vo);

    List<VpointsGoodsGiftCardCogVO> getCurrentActivity();
}
