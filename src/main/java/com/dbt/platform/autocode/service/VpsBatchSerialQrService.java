package com.dbt.platform.autocode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.autocode.bean.VpsBatchSerialQr;
import com.dbt.platform.autocode.dao.VpsBatchSerialQrDao;
import com.dbt.platform.autocode.dto.VpsBatchSerialQrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuDa
 * @date 2021/12/3
 **/
@Service
public class VpsBatchSerialQrService extends BaseService<VpsBatchSerialQr> {

    @Autowired
    private VpsBatchSerialQrDao vpsBatchSerialQrDao;

    public IPage<VpsBatchSerialQr> queryPage(PageOrderInfo pageOrderInfo, VpsBatchSerialQrVO vo) {
        QueryWrapper<VpsBatchSerialQr> qrQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(vo.getStartDate())){
            qrQueryWrapper.ge("create_time", DateUtil.getStartDateTimeByDay(vo.getStartDate()));
        }
        if(StringUtils.isNotEmpty(vo.getEndDate())){
            qrQueryWrapper.le("create_time", DateUtil.getEndDateTimeByDay(vo.getEndDate()));
        }
        qrQueryWrapper.orderByDesc("create_time");
        return vpsBatchSerialQrDao.selectPage(pageOrderInfo.initPage(), qrQueryWrapper);
    }

    public String save(String exclePath, String qrUrl, int size, String phoneNum) {
        VpsBatchSerialQr entity = new VpsBatchSerialQr();
        String bussionNo = getBussionNo("vps_vcode_batch_serial_qr_record", "id", "");
        entity.setId(bussionNo);
        entity.setImportExcelPath(exclePath+"/"+bussionNo+".xls");
        entity.setQrUrl(qrUrl);
        entity.setQueryNumber(size);
        entity.setQueryUserPhoneNum(phoneNum);
        entity.setQueryStatus("0");
        entity.setCreateTime(DateUtil.getDateTime());
        vpsBatchSerialQrDao.insert(entity);
        return bussionNo;
    }

    public void updateQueryNumber(String id, int size) {
        VpsBatchSerialQr entity = new VpsBatchSerialQr();
        entity.setId(id);
        entity.setQueryNumber(size);
        vpsBatchSerialQrDao.updateById(entity);
    }

    public void updateQrImageUrl(String id, String s) {
        VpsBatchSerialQr entity = new VpsBatchSerialQr();
        entity.setId(id);
        entity.setQrUrl(s);
        entity.setQueryStatus("1");
        vpsBatchSerialQrDao.updateById(entity);
    }
}
