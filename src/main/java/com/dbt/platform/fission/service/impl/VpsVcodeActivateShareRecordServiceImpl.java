package com.dbt.platform.fission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.platform.fission.bean.VpsVcodeActivateShareRecord;
import com.dbt.platform.fission.dao.IVpsVcodeActivateShareRecordMapper;
import com.dbt.platform.fission.service.IVpsVcodeActivateShareRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shuDa
 * @date 2022/5/17
 **/
@Service
public class VpsVcodeActivateShareRecordServiceImpl extends ServiceImpl<IVpsVcodeActivateShareRecordMapper, VpsVcodeActivateShareRecord> implements IVpsVcodeActivateShareRecordService {

    @Override
    public void removeActivateRedEnvelopeShareRecordJobHandle() {
        IVpsVcodeActivateShareRecordMapper baseMapper = this.baseMapper;
        //查询大于截至时间的数据
        List<VpsVcodeActivateShareRecord> list =  baseMapper.selectOverDeadline();
        List<String> ids = list.stream().map(VpsVcodeActivateShareRecord::getId).collect(Collectors.toList());
        if(ids != null && ids.size()>0){
            int deleteBatchIdsCount = baseMapper.deleteBatchIds(ids);
            log.warn("删除未使用且过期分享的裂变活动分享记录："+deleteBatchIdsCount+"条");
        }
    }
}
