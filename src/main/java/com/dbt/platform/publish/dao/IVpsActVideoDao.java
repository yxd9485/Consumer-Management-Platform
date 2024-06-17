package com.dbt.platform.publish.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.platform.publish.bean.VpsActVideo;
import com.dbt.platform.publish.vo.VpsActVideoVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 规则表 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-11
 */
public interface IVpsActVideoDao extends BaseMapper<VpsActVideo> {

    IPage<VpsActVideo> selectPageVO(IPage<VpsActVideo> page,@Param("ew") VpsActVideo vpsActVideo);

    void updateVpsActVideoShow(VpsActVideoVO vpsActVideoVO);



    void updateVpsActVideoShowOther(VpsActVideoVO vpsActVideoVO);
}
