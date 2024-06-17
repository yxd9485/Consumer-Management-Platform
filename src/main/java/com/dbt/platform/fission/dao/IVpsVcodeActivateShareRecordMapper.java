package com.dbt.platform.fission.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.platform.fission.bean.VpsVcodeActivateShareRecord;

import java.util.List;

/**
 * @author shuDa
 * @date 2022/5/17
 **/
public interface IVpsVcodeActivateShareRecordMapper extends BaseMapper<VpsVcodeActivateShareRecord> {
    List<VpsVcodeActivateShareRecord> selectOverDeadline();
}
