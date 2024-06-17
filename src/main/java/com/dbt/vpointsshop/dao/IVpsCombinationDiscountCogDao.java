package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.vpointsshop.bean.VpsCombinationDiscountCog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountCogVO;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-07
 */
public interface IVpsCombinationDiscountCogDao  extends BaseMapper<VpsCombinationDiscountCog> {

    Page<VpsCombinationDiscountDTO> selectPageVO(IPage<VpsCombinationDiscountCogVO> page, @Param("ew") VpsCombinationDiscountDTO wrapper);

    List<VpsCombinationDiscountDTO> selectSetUpGoodsByOnline();

    List<VpsCombinationDiscountDTO> checkGoodsInfo(VpsCombinationDiscountCogVO queryParam);
}
