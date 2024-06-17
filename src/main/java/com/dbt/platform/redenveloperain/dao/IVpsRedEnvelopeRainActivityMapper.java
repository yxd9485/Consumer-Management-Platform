package com.dbt.platform.redenveloperain.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityEntity;
import com.dbt.platform.redenveloperain.dto.RedEnvelopeRainActivityQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
public interface IVpsRedEnvelopeRainActivityMapper extends BaseMapper<RedEnvelopeRainActivityEntity> {

    IPage<RedEnvelopeRainActivityQuery> selectPageVO(IPage objectPage, @Param("param") RedEnvelopeRainActivityQuery param);

    List<RedEnvelopeRainActivityQuery> validDateTime( @Param("param")  RedEnvelopeRainActivityQuery query);
}
