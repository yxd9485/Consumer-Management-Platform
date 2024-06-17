package com.dbt.platform.mn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.platform.mn.bean.TerminalRankActivityCog;
import com.dbt.platform.mn.dto.TerminalRankActivityCogVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-17
 */
public interface ITerminalRankActivityCogDao extends BaseMapper<TerminalRankActivityCog> {

    IPage<TerminalRankActivityCogVO> selectPageVO(IPage<TerminalRankActivityCog> page, @Param("param") TerminalRankActivityCogVO queryBean);
}
