package com.dbt.vpointsshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpsCombinationDiscountCog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountCogVO;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-07
 */
public interface IVpsCombinationDiscountCogService{

    void insert(VpsCombinationDiscountCogVO queryParam, SysUserBasis user);

    Page<VpsCombinationDiscountDTO> getList(VpsCombinationDiscountCogVO queryParam);

    void delete(String cogKey);

    void update(VpsCombinationDiscountCogVO queryParam, SysUserBasis user);

    VpsCombinationDiscountCogVO selectById(String cogKey);

    List<VpsCombinationDiscountDTO> selectSetUpGoodsByOnline();

    BaseResult<VpsCombinationDiscountDTO> checkGoodsInfo(VpsCombinationDiscountCogVO queryParam);
}
