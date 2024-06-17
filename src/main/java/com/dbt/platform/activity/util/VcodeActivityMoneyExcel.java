package com.dbt.platform.activity.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;

/**
 * @author RoyFu
 * @createTime 2016年1月25日 下午4:15:25
 * @description
 */

public class VcodeActivityMoneyExcel {

	private static String[] entities = {"prizeType", "scanType", "randomType", "vpoints", "money", "cardNo", "allowanceType" ,"prizePayMoney", "prizeDiscount", "amounts", "rangeVal", "prizePercentWarn", "scanNum", "content"};

	public static List<VcodeActivityMoneyImport> importExcel(MultipartFile clientFile) throws Exception {
	    return importExcel(clientFile, entities);
	}
	public static List<VcodeActivityMoneyImport> importExcel(MultipartFile clientFile, String[] entities) throws Exception {
		ExcelUtil<VcodeActivityMoneyImport> vcodeExcel = new ExcelUtil<VcodeActivityMoneyImport>();
		List<VcodeActivityMoneyImport> resultList = new ArrayList<VcodeActivityMoneyImport>();
		try {
			String path = clientFile.getOriginalFilename();
			InputStream inputStream = clientFile.getInputStream();
			Workbook workBook = null;
			if (path.endsWith("xlsx")) {
				workBook = new XSSFWorkbook(inputStream);
			} else {
				workBook = new HSSFWorkbook(inputStream);
			}
			resultList = vcodeExcel.readContent(workBook, resultList,
					new VcodeActivityMoneyImport(), 0, entities);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultList;
	}
}
