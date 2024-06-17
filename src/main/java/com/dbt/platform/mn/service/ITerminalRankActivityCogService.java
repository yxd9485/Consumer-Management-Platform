package com.dbt.platform.mn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.mn.bean.TerminalRankActivityCog;
import com.dbt.platform.mn.dto.TerminalRankActivityCogVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-17
 */
public interface ITerminalRankActivityCogService extends IService<TerminalRankActivityCog> {

    IPage<TerminalRankActivityCogVO> queryPageVo(PageOrderInfo pageInfo, TerminalRankActivityCogVO queryBean);

    void saveRank(TerminalRankActivityCogVO cogVO);

    TerminalRankActivityCogVO showRankEdit(String infoKey);

    void saveUpdate(TerminalRankActivityCogVO cogVO);

}
