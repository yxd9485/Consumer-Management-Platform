package com.dbt.platform.redenveloperain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityEntity;
import com.dbt.platform.redenveloperain.dto.RedEnvelopeRainActivityQuery;
import com.dbt.platform.system.bean.SysUserBasis;

import java.lang.reflect.InvocationTargetException;

/**
 * @author shuDa
 * @date 2022/1/12
 **/
public interface RedEnvelopeRainActivityService extends IService<RedEnvelopeRainActivityEntity> {

    IPage<RedEnvelopeRainActivityQuery> queryPage(IPage objectPage,RedEnvelopeRainActivityQuery param) throws InvocationTargetException, IllegalAccessException;

    Boolean create(RedEnvelopeRainActivityQuery query, SysUserBasis user) throws Exception;

    RedEnvelopeRainActivityQuery findById(String activityKey);

    Boolean updateById(RedEnvelopeRainActivityQuery query, SysUserBasis currentUser) throws Exception;

    void updateBackRedEnvelopeMoneyJobHandler();
    boolean checkDateTime(RedEnvelopeRainActivityQuery query);
}
