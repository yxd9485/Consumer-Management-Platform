package com.dbt.platform.autocode.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSONObject;

import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.platform.autocode.dto.VpsBatchSerialQrExcelTemplate;
import com.dbt.platform.system.bean.SysUserBasis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@Service
public class ConverterDataListener implements ReadListener<VpsBatchSerialQrExcelTemplate> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private List<VpsBatchSerialQrExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private SysUserBasis currentUser;
    private String id;
    private VpsQrcodeMakeService vpsQrcodeMakeService;
    public ConverterDataListener(){
    }
    public ConverterDataListener(SysUserBasis userx,String id,VpsQrcodeMakeService vpsQrcodeMakeService){
        this.currentUser = userx;
        this.id = id;
        this.vpsQrcodeMakeService = vpsQrcodeMakeService;
    }
    @Override
    public void invoke(VpsBatchSerialQrExcelTemplate data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONObject.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        cachedDataList.forEach(no->{
            if(StringUtils.isNotEmpty(no.getBatchSerialNo())){
                BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
                Map<String, String> analysiscol5Map = analysisCol5(no.getBatchSerialNo());
                analysiscol5Map.put("queryNotes", no.getQueryNotes());
                vpsQrcodeMakeService.executeQuery(currentUser, analysiscol5Map, baseResult, id);
                no.setErrMsg(baseResult.getErrmsg());

            }
        });
        log.info("存储数据库成功！");
    }
    private static Map<String,String> analysisCol5(String col5){
        Map<String, String> resultMap = new HashMap<>();
        if(col5 != null && col5.length() > 3){
            resultMap.put("code",col5.substring(0, 3));
            resultMap.put("col",col5.substring(3));
            resultMap.put("col5",col5);
        }
        return resultMap;
    }
}