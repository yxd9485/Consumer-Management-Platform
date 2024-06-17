package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardCog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.vpointsshop.dto.VpointsGoodsGiftCardCogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 礼品卡活动表 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
public interface IVpointsGoodsGiftCardCogDao extends BaseMapper<VpointsGoodsGiftCardCog> {

    IPage<VpointsGoodsGiftCardCogVO> selectPageVO(IPage initPage, @Param("ew") VpointsGoodsGiftCardCog entity);

    int update(VpointsGoodsGiftCardCog cog);

    List<VpointsGoodsGiftCardCogVO> checkGoodsActivity(VpointsGoodsGiftCardCogVO vo);

    List<VpointsGoodsGiftCardCogVO> getCurrentActivity();
}
