package com.dbt.vpointsshop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityRecordVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城第二件半价活动 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
public interface IVpointsGoodsExchangeActivityCogService {
    public String getRuleNo();

    IPage<VpointsGoodsExchangeActivityCogVO> selectPageVO(IPage initPage, VpointsGoodsExchangeActivityCogEntity entity);

    boolean create(VpointsGoodsExchangeActivityCogVO vo);

    boolean update(VpointsGoodsExchangeActivityCogVO vo) throws Exception;

    VpointsGoodsExchangeActivityCogVO findByInfoKey(String infoKey);

    BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsExchangeActivityCogVO vo);

    List<VpointsGoodsExchangeActivityCogEntity> getCurrentActivity();

    IPage<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(IPage initPage, VpointsGoodsExchangeActivityRecordVO vo);

    List<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(VpointsGoodsExchangeActivityRecordVO vo);
}
