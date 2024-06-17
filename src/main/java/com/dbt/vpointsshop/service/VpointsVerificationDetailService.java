package com.dbt.vpointsshop.service;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.vpointsshop.bean.VpointsVerificationDetail;
import com.dbt.vpointsshop.bean.VpointsVerificationInfo;
import com.dbt.vpointsshop.dao.IVpointsVerificationDetailDao;

/**
 * 积分商场实物奖兑换核销明细表Service
 */
@Service
public class VpointsVerificationDetailService extends BaseService<VpointsVerificationDetail> {

	@Autowired
	private IVpointsVerificationDetailDao verificationDetailDao;
	@Autowired
	private VpointsVerificationInfoService verificationInfoService;
    
	/**
	 * 获取核销列表
	 */
	public List<VpointsVerificationDetail> queryForLst(VpointsVerificationDetail queryBean, PageOrderInfo pageInfo) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return verificationDetailDao.queryForLst(map);
	}
	
	/**
	 * 获取核销列表记录总个数
	 */
	public int queryForCount(VpointsVerificationDetail queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return verificationDetailDao.queryForCount(map);
	}
	
	/**
	 * 获取预览核销列表
	 */
	public List<VpointsVerificationDetail> queryPreviewForLst(VpointsVerificationDetail queryBean, PageOrderInfo pageInfo) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return verificationDetailDao.queryPreviewForLst(map);
	}
	
	/**
	 * 获取预览核销列表记录总个数
	 */
	public int queryPreviewForCount(VpointsVerificationDetail queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return verificationDetailDao.queryPreviewForCount(map);
	}
    
    /**
     * 批量插入明细
     * @param detailLst
     */
    public void batchWrite(List<VpointsVerificationDetail> detailLst) {
        Map<String, Object> map = new HashMap<>();
        map.put("detailLst", detailLst);
        verificationDetailDao.batchWrite(map);
    }

    /**
     * 依据核销记录主键获取核销明细
     * 
     * @param verificationId 核销记录主键
     * @return
     */
    public List<VpointsVerificationDetail> queryByVerificationId(String verificationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("verificationId", verificationId);
        return verificationDetailDao.queryByVerificationId(map);
    }
    
    /**
     * 依据核销截止日期获取核销明细
     * 
     * @param verificationEndDate 核销截止日期
     * @return
     */
    public List<VpointsVerificationDetail> queryByPreviewVerificationId(String verificationEndDate, String brandKeys) {
        Map<String, Object> map = new HashMap<>();
        map.put("verificationEndDate", verificationEndDate);
        map.put("brandKeys", Arrays.asList(brandKeys.split(",")));
        return verificationDetailDao.queryByPreviewVerificationId(map);
    }


    /**
     * 核销表格明细下载
     */
    public void exportVerificationDetailList(String verificationId,
            PageOrderInfo pageInfo, HttpServletResponse response) throws Exception {
        // 核销记录主表
        VpointsVerificationInfo verificationInfo 
                        = verificationInfoService.findById(verificationId);
        Date verificationStartDate = DateUtil.parse(verificationInfo.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT);
        Date verificationEndDate = DateUtil.parse(verificationInfo.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT);
        
        // 核销数据汇总
        VpointsVerificationDetail queryBean = new VpointsVerificationDetail();
        queryBean.setVerificationId(verificationId);
        int index = 1;
        pageInfo.setPagePerCount(0);
        List<VpointsVerificationDetail> summaryLst = queryForLst(queryBean, pageInfo);
        for (VpointsVerificationDetail item : summaryLst) {
            item.setIndex(String.valueOf(index++));
        }
        
        // 获取导出信息
        index = 1;
        List<VpointsVerificationDetail> detailLst = queryByVerificationId(verificationId);
        for (VpointsVerificationDetail item : detailLst) {
            item.setIndex(String.valueOf(index++));
            item.setVerificationProvince(item.getAddress().split("-")[0]);
        }
        
        // 导出数据
        OutputStream outStream = response.getOutputStream();
        try {
            ExcelUtil<VpointsVerificationDetail> excel = new ExcelUtil<VpointsVerificationDetail>(); 
            
            // 创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            
            // 核销数据汇总
            String projectName = DatadicUtil.getDataDicValue(DatadicKey
                    .dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_NAME);
            String summary = "核销编号：" + verificationId + "，" + projectName + "积分商品核销时间：从" 
                    + DateUtil.getDateTime(verificationStartDate, DateUtil.DEFAULT_DATE_FORMAT_CH) + " 至 "
                                        + DateUtil.getDateTime(verificationEndDate, DateUtil.DEFAULT_DATE_FORMAT_CH);
            String[] headers = {"序号", "兑换品牌", "兑换商品名称", "商品编号", "总数量", "兑换总积分", "商品单价", "商品总价"};
            String[] valueTags = {"index", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "exchangeVpoints", "unitMoney", "totalMoney"};
            excel.writeExcel(workbook, "核销数据汇总", summary, headers, valueTags, summaryLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            
            // 核销数据明细
            headers = new String[] {"序号", "订单编号", "兑换品牌", "兑换商品名称", "商品编号", "数量", "单品积分", "兑换积分", "商品单价", "商品总价", "兑换人", "收件人", "电话", "地址", "订单时间", "物流公司", "物流单号", "发货时间", "核销省区"};
            valueTags = new String[] {"index", "exchangeId", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "unitVpoints", "exchangeVpoints", "unitMoney", "totalMoney", "nickName", "userName", "phoneNum", "address", "exchangeTime", "expressCompany", "expressNumber", "expressSendTime", "verificationProvince"};
            excel.writeExcel(workbook, "核销数据明细", null, headers, valueTags, detailLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            
            // 写文件
            workbook.write(outStream);
            
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new BusinessException("导出失败");
        }
        
        outStream.close();
    }
    
    /**
     * 预览核销表格明细下载
     */
    public void exportPreviewVerificationDetailList(String endDate, String brandKeys,
            	PageOrderInfo pageInfo, HttpServletResponse response) throws Exception {
    	 // 获取核销记录主表
        VpointsVerificationInfo verificationInfo = 
        		verificationInfoService.findPreviewForVerificationInfo(endDate, brandKeys);
        if(null != verificationInfo){
        	Date verificationStartDate = DateUtil.parse(verificationInfo.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT);
            Date verificationEndDate = DateUtil.parse(endDate, DateUtil.DEFAULT_DATE_FORMAT);
            
            // 核销数据汇总
            VpointsVerificationDetail queryBean = new VpointsVerificationDetail();
            queryBean.setVerificationEndDate(endDate);
            queryBean.setBrandKeys(brandKeys);
            int index = 1;
            pageInfo.setPagePerCount(0);
            List<VpointsVerificationDetail> summaryLst = queryPreviewForLst(queryBean, pageInfo);
            for (VpointsVerificationDetail item : summaryLst) {
                item.setIndex(String.valueOf(index++));
            }
            
            // 获取导出信息
            index = 1;
            List<VpointsVerificationDetail> detailLst = queryByPreviewVerificationId(endDate, brandKeys);
            for (VpointsVerificationDetail item : detailLst) {
                item.setIndex(String.valueOf(index++));
                item.setVerificationProvince(item.getAddress().split("-")[0]);
            }
            
            // 导出数据
            OutputStream outStream = response.getOutputStream();
            try {
                ExcelUtil<VpointsVerificationDetail> excel = new ExcelUtil<VpointsVerificationDetail>(); 
                
                // 创建工作簿
                HSSFWorkbook workbook = new HSSFWorkbook();
                
                // 核销数据汇总
                String projectName = DatadicUtil.getDataDicValue(DatadicKey
                        .dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_NAME);
                String summary = "核销预览-" + projectName +"积分商品核销时间：从" 
                        + DateUtil.getDateTime(verificationStartDate, DateUtil.DEFAULT_DATE_FORMAT_CH) + " 至 "
                                            + DateUtil.getDateTime(verificationEndDate, DateUtil.DEFAULT_DATE_FORMAT_CH);
                String[] headers = {"序号", "兑换品牌", "兑换商品名称", "商品编号", "总数量", "兑换总积分", "商品单价", "商品总价"};
                String[] valueTags = {"index", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "exchangeVpoints", "unitMoney", "totalMoney"};
                excel.writeExcel(workbook, "核销数据汇总", summary, headers, valueTags, summaryLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
                
                // 核销数据明细
                headers = new String[] {"序号", "订单编号", "兑换品牌", "兑换商品名称", "商品编号", "数量", "单品积分", "兑换积分", "商品单价", "商品总价", "兑换人", "收件人", "电话", "地址", "订单时间", "物流公司", "物流单号", "发货时间", "核销省区"};
                valueTags = new String[] {"index", "exchangeId", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "unitVpoints", "exchangeVpoints", "unitMoney", "totalMoney", "nickName", "userName", "phoneNum", "address", "exchangeTime", "expressCompany", "expressNumber", "expressSendTime", "verificationProvince"};
                excel.writeExcel(workbook, "核销数据明细", null, headers, valueTags, detailLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
                
                // 写文件
                workbook.write(outStream);
                
                outStream.close();
                
            } catch (Exception e) {
                log.error("导出失败", e);
                throw new BusinessException("导出失败");
            }
        }
    }
}
