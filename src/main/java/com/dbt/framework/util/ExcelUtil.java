package com.dbt.framework.util;

import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.healthmarketscience.jackcess.impl.IndexCodes;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 *
 * @author leno
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
@Component
public class ExcelUtil<T> {

	protected Log log = LogFactory.getLog(getClass());
	@Autowired
	private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
	public static ExcelUtil excelUtil;
	private  static final short shortNum  = 0;
	@PostConstruct
	public void init(){
		excelUtil=this;   //必需
		excelUtil.vcodeActivityBigPrizeService = this.vcodeActivityBigPrizeService;
	}

	/**
	 * 读取Excel
	 *
	 * @param entity
	 *            导入Excel对应的Bean
	 * @param path
	 *            路径
	 * @param page
	 *            所在Excel的第几页
	 * @param entityNames
	 *            Excel表头顺序对应的Bean字段
	 * @return
	 */
	public List<T> readExcel(T entity, String path, int page, String[] entityNames) {
		List<T> resultList = new ArrayList<T>();
		Workbook workBook = null;

		try {
			File file = new File(path);
			FileInputStream fileStream = new FileInputStream(file);

			if (path.endsWith("xlsx")) {
				workBook = new XSSFWorkbook(fileStream);
			} else {
				workBook = new HSSFWorkbook(fileStream);
			}
			resultList = this.readContent(workBook, resultList, entity, page, entityNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> readContent(Workbook workBook, List<T> resultList, T entity, int page,
            String[] entityNames) throws Exception {
        Sheet entitySheet = workBook.getSheetAt(page);
        if (null != entitySheet) {
            // 循环获取行
            List<Object> objectList = new ArrayList<Object>();
            Row fileNameRow = entitySheet.getRow(1);
            Cell fileNameCell = null;
            String fieldVal = null; // 读取的值
            inportEnd:
            for (int rowNum = 2; rowNum <= entitySheet.getLastRowNum(); rowNum++) {
                Row row = entitySheet.getRow(rowNum);
                if (row == null)
                    continue;
                // 获取实体对应的Class
                Class entityClass = entity.getClass();
                // 创建新的实例
                Object entityObject = entityClass.newInstance();

                // 循环获取单元格
                for (int entityCount = 0; entityCount < entityNames.length; entityCount++) {
                    // 获取set方法中参数的字段
                    String fieldName = entityNames[entityCount];
                    Field field = entityClass.getDeclaredField(fieldName);
                    fieldVal = null;

                    // 校验模板版本
                    fileNameCell = fileNameRow.getCell(entityCount);
                    if (!fieldName.equals(fileNameCell.getStringCellValue())) {
                        throw new BusinessException("请检查奖项Excel模板是否是最新版本！");
                    }

                    // 获取set方法
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    Method setMethod = entityClass
                            .getDeclaredMethod(setMethodName, field.getType());
                    // 获取Cell
                    Cell cell = row.getCell(entityCount);
					Map<String, VcodePrizeBasicInfo> prizeMap =  ExcelUtil.excelUtil.vcodeActivityBigPrizeService.getPrizeMap();
                    if (cell != null) {
                        if (cell.getCellType() == CellType.BOOLEAN) {
                            fieldVal = String.valueOf(cell.getBooleanCellValue());

                        } else if (cell.getCellType() == CellType.NUMERIC) {
                            fieldVal = new BigDecimal(cell.getNumericCellValue()).toPlainString();

                        } else {
                            fieldVal = String.valueOf(cell.getStringCellValue());
                            if (!StringUtils.isEmpty(fieldVal) && !StringUtils.isEmpty(entityObject)) {
                                // 奖品类型（不为空）
                                if("prizeType".equals(fieldName)){
                                    if (fieldVal.contains("等奖") && null != prizeMap) {
                                    	//updateBy zhangbin
										fieldVal = prizeMap.get(fieldVal.substring(0, fieldVal.indexOf("等奖"))).getPrizeNo();
                                    }else if (fieldVal.contains("优惠券")) {
                                    	if(fieldVal.split("-").length == 2){
                                    		fieldVal = Constant.prizeTypeMap.get(fieldVal.substring(0, 4));
                                    	}else if(fieldVal.split("-").length == 3){
                                    		fieldVal = fieldVal.split("-")[2];
                                    	}
                                    }else {
                                        fieldVal = Constant.prizeTypeMap.get(fieldVal);
                                    }
                                
                                // 卡类型（不为空）
                                } else if ("cardNo".equals(fieldName)) {
                                    fieldVal = fieldVal.substring(0, 1);
                                
                                // 扫码类型（不为空）
                                } else if ("scanType".equals(fieldName)) {
                                    if ("首扫".equals(fieldVal)) {
                                        fieldVal = Constant.ScanType.type_0;
                                    } else if ("普通扫码".equals(fieldVal)) {
                                        fieldVal = Constant.ScanType.type_1;
                                    }
                                
                                // 随机类型（不为空）
                                } else if ("randomType".equals(fieldName)) {
                                    if ("随机".equals(fieldVal)) {
                                        fieldVal = Constant.PrizeRandomType.type_0;
                                    } else if ("固定".equals(fieldVal)) {
                                        fieldVal = Constant.PrizeRandomType.type_1;
                                    }
                                }else if("allowanceType".equals(fieldName)) {
                                	fieldVal = Constant.allowanceTypeMap.get(fieldVal);
								}
                            }
                        }

                        if (!StringUtils.isEmpty(fieldVal) && !StringUtils.isEmpty(entityObject)) {
                            if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal).intValue());
                            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal).longValue());
                            } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                                setMethod.invoke(entityObject, Float.valueOf(fieldVal));
                            } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal));
                            } else if (field.getType().equals(BigDecimal.class)) {
                                setMethod.invoke(entityObject, BigDecimal.valueOf(Double.valueOf(fieldVal)));
                            } else {
                                // 处理纯数字的字符串问题
                                if (Pattern.matches("^\\d+\\.0+$", fieldVal)) {
                                    setMethod.invoke(entityObject, fieldVal.substring(0, fieldVal.indexOf(".")));
                                } else {
                                    setMethod.invoke(entityObject, fieldVal);
                                }
                            }
                        }
                        
                    } else {
                        setMethod.invoke(entityObject, "");
                    }

                    // 第一个单元格为空，则跳至锚点"inportEnd"结束导入
                    if (entityCount == 0 && StringUtils.isEmpty(fieldVal)) {
                       break inportEnd;
                    }

                }
                objectList.add(entityObject);
            }

            for (Object object : objectList) {
                resultList.add((T) object);
            }
        }
        return resultList;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> readContent(Workbook workBook, List<T> resultList, T entity, int page,
                                    String[] entityNames, String[] titleNames) throws Exception {
        Sheet entitySheet = workBook.getSheetAt(page);
        if (null != entitySheet) {

	        //            // 校验表头
            Cell fileNameCell = null;
            if (ArrayUtils.isNotEmpty(titleNames)) {
                Row fileNameRow = entitySheet.getRow(0);
                for (int colNum = 0; colNum < titleNames.length; colNum++) {
                    // 校验模板版本
                    fileNameCell = fileNameRow.getCell(colNum);
                    if (!titleNames[colNum].equals(fileNameCell.getStringCellValue())) {
                        throw new BusinessException("导入失败，请检查奖项Excel模板是否是最新版本！");
                    }
                }
            }

            // 循环获取行
            Class entityClass = entity.getClass(); //当前类对象
            Object entityObject = null; // 当前类的实例对象
            Field field = null; // 实例对象的属性字段
            String setMethodName = null; // 属性字段的set方法名称
            Method setMethod = null; // 属性字段的set方法
            Row row = null; // 行
            Cell cell = null; // 单元格对象
            String fieldName = ""; // 实例属性名称
            String fieldVal = null; // 读取的值
            List<Object> objectList = new ArrayList<Object>();
            inportEnd:
            for (int rowNum = 1; rowNum <= entitySheet.getLastRowNum(); rowNum++) {
                row = entitySheet.getRow(rowNum);
                if (row == null) continue;
                // 创建新的实例
                entityObject = entityClass.newInstance();

                // 循环获取单元格
                for (int entityCount = 0; entityCount < entityNames.length; entityCount++) {
                    // 获取set方法中参数的字段
                    fieldName = entityNames[entityCount];
                    field = entityClass.getDeclaredField(fieldName);
                    fieldVal = null; // 读取的值

                    // 获取set方法
                    setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    setMethod = entityClass.getDeclaredMethod(setMethodName, field.getType());
                    // 获取Cell
                    cell = row.getCell(entityCount);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.BOOLEAN) {
                            fieldVal = String.valueOf(cell.getBooleanCellValue());

                        } else if (cell.getCellType() == CellType.NUMERIC) {
                            fieldVal = new BigDecimal(cell.getNumericCellValue()).toPlainString();

                        } else {
                            fieldVal = String.valueOf(cell.getStringCellValue());
                        }

                        if (!StringUtils.isEmpty(fieldVal) && !StringUtils.isEmpty(entityObject)) {
                            if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal).intValue());
                            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal).longValue());
                            } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                                setMethod.invoke(entityObject, Float.valueOf(fieldVal));
                            } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                                setMethod.invoke(entityObject, Double.valueOf(fieldVal));
                            } else if (field.getType().equals(BigDecimal.class)) {
                                setMethod.invoke(entityObject, BigDecimal.valueOf(Double.valueOf(fieldVal)));
                            } else {
                                // 处理纯数字的字符串问题
                                if (Pattern.matches("^\\d+\\.0+$", fieldVal)) {
                                    setMethod.invoke(entityObject, fieldVal.substring(0, fieldVal.indexOf(".")));
                                } else {
                                    setMethod.invoke(entityObject, fieldVal);
                                }
                            }
                        }
                    } else {
                        setMethod.invoke(entityObject, "");
                    }

                    // 第一个单元格为空，则跳至锚点"inportEnd"结束导入
                    if (entityCount == 0 && StringUtils.isEmpty(fieldVal)) {
                       break inportEnd;
                    }

                }
                objectList.add(entityObject);
            }

            for (Object object : objectList) {
                resultList.add((T) object);
            }
        }
        return resultList;
    }

	/**
	 * 导出EXCEL
	 *
	 * @param bookName
	 *            标签名称
	 * @param headers
	 *            内容标题
	 * @param dataList
	 *            内容数据
	 * @param timePattern
	 *            时间格式(默认"yyyy-MM-dd")
	 * @param outStream
	 *            输出流
	 */
	public void writeExcel(String bookName, String[] headers, List<T> dataList, String timePattern,
			OutputStream outStream) {
		try {
			// 创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建sheet
			HSSFSheet sheet = workbook.createSheet(bookName);
			// 为每列指定一个默认的宽度
			for (int i = 0; i < headers.length; i++) {
				sheet.setColumnWidth(i, 30 * 256);
			}
			// 生成标题
			createTitle(workbook, sheet, headers, 0);
			// 生成内容
			createContent(workbook, sheet, headers.length, dataList, timePattern);
			// 写文件
			workbook.write(outStream);
		} catch (Exception e) {
			log.error("ExcelUtil exception");
		}
	}

	/**
	 * 导出EXCEL
	 *
	 * @param bookName
	 *            标签名称
	 * @param headers
	 *            内容标题
	 * @param valueTags
	 *            内容对应字段
	 * @param dataList
	 *            内容数据
	 * @param timePattern
	 *            时间格式(默认"yyyy-MM-dd")
	 * @param outStream
	 *            输出流
	 */
	public void writeExcel(String bookName, String[] headers, String[] valueTags, List<T> dataList,
	        String timePattern, OutputStream outStream) {
	    try {
	        // 创建工作簿
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // 创建sheet
	        HSSFSheet sheet = workbook.createSheet(bookName);
	        // 为每列指定一个默认的宽度
	        for (int i = 0; i < headers.length; i++) {
	            sheet.setColumnWidth(i, 30 * 256);
	        }
	        // 生成标题
	        createTitle(workbook, sheet, headers, 0);
	        // 生成内容
	        createContent(workbook, sheet, headers.length, valueTags, dataList, timePattern, 1);
	        // 写文件
	        workbook.write(outStream);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeSXSSFWorkExcel(String bookName, String[] headers, String[] valueTags, List<T> dataList,
	        String timePattern, OutputStream outStream) {
	    try {
	        // 创建工作簿
	    	SXSSFWorkbook workbook = new SXSSFWorkbook();
	        // 创建sheet
	        Sheet sheet = workbook.createSheet(bookName);
	        // 为每列指定一个默认的宽度
	        for (int i = 0; i < headers.length; i++) {
	            sheet.setColumnWidth(i, 30 * 256);
	        }
	        // 生成标题
	        createSXSSTitle(workbook, sheet, headers, 0);
	        // 生成内容
	        createSXSSContent(workbook, sheet, headers.length, valueTags, dataList, timePattern, 1);
	        // 写文件
	        workbook.write(outStream);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void createSXSSTitle(SXSSFWorkbook workbook, Sheet sheet, String[] headers, int rowNum) {
		// 根据标题得出共有多少列
		int totalSheetCols = headers.length;
		// 生成标题样式
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置标题字体样式
		Font titleFont = workbook.createFont();
		titleFont.setColor(IndexedColors.WHITE.index);
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);

		// 生成标题
		Row titleRow = sheet.createRow(rowNum);
		for (int i = 0; i < totalSheetCols; i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(headers[i]);
		}
	}
	private void createSXSSContent(SXSSFWorkbook workbook, Sheet sheet, int titleLength, String[] valueTags,
			List<T> dataList, String timePattern, int rowNum) {
		// 生成内容样式
		CellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBorderLeft(BorderStyle.THIN);
		contentStyle.setBorderRight(BorderStyle.THIN);
		contentStyle.setBorderTop(BorderStyle.THIN);
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容字体样式
		Font contentFont = workbook.createFont();
		contentFont.setColor(IndexedColors.BLACK.index);
		contentFont.setFontHeightInPoints((short) 10);
		contentStyle.setFont(contentFont);

		CellStyle moneyCellStyle = workbook.createCellStyle();
		moneyCellStyle.cloneStyleFrom(contentStyle);
		moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); // 小数点后保留两位

		Row contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				rowIndex = i + rowNum;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				// Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < valueTags.length; j++) {
					// 创建单元格
					Cell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					String field = valueTags[j];

					String getMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()
								&& (value instanceof Double || value instanceof Float || value instanceof Integer)) {
							if (getMethodName.contains("Money")) {
								cell.setCellStyle(moneyCellStyle);
							}
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							cell.setCellValue(textValue);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 导出EXCEL
	 *
	 * @param bookName
	 *            标签名称
	 * @param headers
	 *            内容标题
	 * @param valueTags
	 *            内容对应字段
	 * @param dataList
	 *            内容数据
	 * @param timePattern
	 *            时间格式(默认"yyyy-MM-dd")
	 * @param outStream
	 *            输出流      title
	 * @param title
	 *          表格抬头     writeExcelAndCaption
	 */
	public void writeExcelIncludeCaption(String bookName, String[] headers, String[] valueTags, List<T> dataList,
						   String timePattern, OutputStream outStream,String title) {
		try {
			// 创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建sheet
			HSSFSheet sheet = workbook.createSheet(bookName);
			// 为每列指定一个默认的宽度
			for (int i = 0; i < headers.length; i++) {
				sheet.setColumnWidth(i, 30 * 256);
			}
			// 生成标题
			createTitleAndCaption(workbook, sheet, headers,title);
			// 生成内容
			createContentAndCaption(workbook, sheet, headers.length, valueTags, dataList, timePattern);
			// 写文件
			workbook.write(outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 导出EXCEL
	 *
	 * @param bookName
	 *            标签名称
	 * @param headers
	 *            内容标题
	 * @param valueTags
	 *            内容对应字段
	 * @param dataList
	 *            内容数据
	 * @param timePattern
	 *            时间格式(默认"yyyy-MM-dd")
	 * @param outStream
	 *            输出流
	 */
	public void writeExcel(HSSFWorkbook workbook, String bookName, String summary, String[] headers,
	                        String[] valueTags, List<T> dataList, String timePattern, OutputStream outStream) {
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(bookName);
        // 为每列指定一个默认的宽度
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 30 * 256);
        }

        // 开始行号
        int startRow = 0;

        // 概要标题
        if (summary != null && !StringUtils.isEmpty(summary)) {
            startRow++;
            createSummary(workbook, sheet, summary, headers.length);
        }
        // 生成标题
        createTitle(workbook, sheet, headers, startRow++);
        // 生成内容
        createContent(workbook, sheet, headers.length, valueTags, dataList, timePattern, startRow++);
	}
	/**
	 * 导出EXCEL
	 *
	 * @param bookName
	 *            标签名称
	 * @param headers
	 *            内容标题
	 * @param valueTags
	 *            内容对应字段
	 * @param dataList
	 *            内容数据
	 * @param timePattern
	 *            时间格式(默认"yyyy-MM-dd")
	 * @param outStream
	 *            输出流
	 */
	public void writeExcel(String bookName, String[] headers, String[] valueTags, List<T> dataList,
			String timePattern, OutputStream outStream, short[] colorTags) {
		try {
			// 创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建sheet
			HSSFSheet sheet = workbook.createSheet(bookName);
			// 为每列指定一个默认的宽度
			for (int i = 0; i < headers.length; i++) {
				sheet.setColumnWidth(i, 30 * 256);
			}
			// 生成标题
			createTitle(workbook, sheet, headers, 0);
			// 生成内容
			createContent(workbook, sheet, headers.length, valueTags, dataList, timePattern,
					colorTags);
			// 写文件
			workbook.write(outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createSummary(HSSFWorkbook workbook, HSSFSheet sheet, String summary, int colNum) {

	    // 生成标题样式
	    HSSFCellStyle summaryStyle = workbook.createCellStyle();
	    summaryStyle.setBorderBottom(BorderStyle.THIN);
	    summaryStyle.setBorderLeft(BorderStyle.THIN);
	    summaryStyle.setBorderRight(BorderStyle.THIN);
	    summaryStyle.setBorderTop(BorderStyle.THIN);
	    summaryStyle.setAlignment(HorizontalAlignment.CENTER);

	    // 设置标题字体样式
	    HSSFFont summaryFont = workbook.createFont();
	    summaryFont.setColor(IndexedColors.BLACK.index);
	    summaryFont.setFontHeightInPoints((short) 13);
	    summaryFont.setBold(true);
	    summaryStyle.setFont(summaryFont);

	    // summary内容
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell cell = titleRow.createCell(0);
        cell.setCellStyle(summaryStyle);
        cell.setCellValue(summary);

	    // 合并单元格
        CellRangeAddress cra =new CellRangeAddress(0, 0, 0, colNum-1); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);

        // 使用RegionUtil类为合并后的单元格添加边框
        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet); // 有边框
	}

    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet, String[] headers, int rowNum) {
		// 根据标题得出共有多少列
		int totalSheetCols = headers.length;
		// 生成标题样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置标题字体样式
		HSSFFont titleFont = workbook.createFont();
		titleFont.setColor(IndexedColors.WHITE.index);
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);

		// 生成标题
		HSSFRow titleRow = sheet.createRow(rowNum);
		for (int i = 0; i < totalSheetCols; i++) {
			HSSFCell cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
	}
    
	private void createTitleAndCaption(HSSFWorkbook workbook, HSSFSheet sheet, String[] headers,String title) {
		// 根据标题得出共有多少列
		int totalSheetCols = headers.length;
		// 生成标题样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置标题字体样式
		HSSFFont titleFont = workbook.createFont();
		titleFont.setColor(IndexedColors.WHITE.index);
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);

		// 生成标题
		HSSFRow titleRowTitle = sheet.createRow(0);
		HSSFCell celltitle = titleRowTitle.createCell(0);
		celltitle.setCellValue(title);
	    int size =	headers.length;
		CellRangeAddress region=new CellRangeAddress(0, 0, 0, size-1);
		sheet.addMergedRegion(region);
		celltitle.setCellStyle(titleStyle);

		HSSFRow titleRowInformation =  sheet.createRow(1);
		for (int i = 0; i < totalSheetCols; i++) {
			HSSFCell cell = titleRowInformation.createCell(i);
			cell.setCellStyle(titleStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContent(HSSFWorkbook workbook, HSSFSheet sheet,
	                            int titleLength, List<T> dataList, String timePattern) {
		// 生成内容样式
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBorderLeft(BorderStyle.THIN);
		contentStyle.setBorderRight(BorderStyle.THIN);
		contentStyle.setBorderTop(BorderStyle.THIN);
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容字体样式
		HSSFFont contentFont = workbook.createFont();
		contentFont.setColor(IndexedColors.BLACK.index);
		contentFont.setFontHeightInPoints((short) 10);
		contentStyle.setFont(contentFont);
		
        HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
        moneyCellStyle.cloneStyleFrom(contentStyle);
        moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); //小数点后保留两位
        
		HSSFRow contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				rowIndex = i + 1;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < fields.length; j++) {
					// 创建单元格
					HSSFCell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					Field field = fields[j];

					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()
                                && (value instanceof Double || value instanceof Float || value instanceof Integer)) {
                            if (getMethodName.contains("Money")) {
                                cell.setCellStyle(moneyCellStyle);
                            }
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContent(HSSFWorkbook workbook, HSSFSheet sheet, int titleLength,
			        String[] valueTags, List<T> dataList, String timePattern, int rowNum) {
		// 生成内容样式
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBorderLeft(BorderStyle.THIN);
		contentStyle.setBorderRight(BorderStyle.THIN);
		contentStyle.setBorderTop(BorderStyle.THIN);
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容字体样式
		HSSFFont contentFont = workbook.createFont();
		contentFont.setColor(IndexedColors.BLACK.index);
		contentFont.setFontHeightInPoints((short) 10);
		contentStyle.setFont(contentFont);
		
		HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
		moneyCellStyle.cloneStyleFrom(contentStyle);
        moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); //小数点后保留两位
		
		HSSFRow contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				rowIndex = i + rowNum;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				// Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < valueTags.length; j++) {
					// 创建单元格
					HSSFCell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					String field = valueTags[j];

					String getMethodName = "get" + field.substring(0, 1).toUpperCase()
							+ field.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches() 
						        && (value instanceof Double || value instanceof Float || value instanceof Integer)) {
							if (getMethodName.contains("Money")) {
							    cell.setCellStyle(moneyCellStyle);
							}
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContentAndCaption(HSSFWorkbook workbook, HSSFSheet sheet, int titleLength,
							   String[] valueTags, List<T> dataList, String timePattern) {
		// 生成内容样式
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBorderLeft(BorderStyle.THIN);
		contentStyle.setBorderRight(BorderStyle.THIN);
		contentStyle.setBorderTop(BorderStyle.THIN);
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容字体样式
		HSSFFont contentFont = workbook.createFont();
		contentFont.setColor(IndexedColors.BLACK.index);
		contentFont.setFontHeightInPoints((short) 10);
		contentStyle.setFont(contentFont);
        
        HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
        moneyCellStyle.cloneStyleFrom(contentStyle);
        moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); //小数点后保留两位
        
		HSSFRow contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				rowIndex = i + 2;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				// Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < valueTags.length; j++) {
					// 创建单元格
					HSSFCell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					String field = valueTags[j];

					String getMethodName = "get" + field.substring(0, 1).toUpperCase()
							+ field.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()
                                && (value instanceof Double || value instanceof Float || value instanceof Integer)) {
                            if (getMethodName.contains("Money")) {
                                cell.setCellStyle(moneyCellStyle);
                            }
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContent(HSSFWorkbook workbook, HSSFSheet sheet, int titleLength,
			String[] valueTags, List<T> dataList, String timePattern, short[] colorTags) {

		HSSFRow contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				// 生成内容样式
				HSSFCellStyle contentStyle = workbook.createCellStyle();
				contentStyle.setBorderBottom(BorderStyle.THIN);
				contentStyle.setBorderLeft(BorderStyle.THIN);
				contentStyle.setBorderRight(BorderStyle.THIN);
				contentStyle.setBorderTop(BorderStyle.THIN);
				contentStyle.setAlignment(HorizontalAlignment.CENTER);

				// 设置内容字体样式
				HSSFFont contentFont = workbook.createFont();
				contentFont.setFontHeightInPoints((short) 10);

				contentFont.setColor(colorTags[i]);
				contentStyle.setFont(contentFont);
		        
		        HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
		        moneyCellStyle.cloneStyleFrom(contentStyle);
		        moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); //小数点后保留两位

				rowIndex = i + 1;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				// Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < valueTags.length; j++) {

					// 创建单元格
					HSSFCell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					String field = valueTags[j];

					String getMethodName = "get" + field.substring(0, 1).toUpperCase()
							+ field.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()
                                && (value instanceof Double || value instanceof Float || value instanceof Integer)) {
                            if (getMethodName.contains("Money")) {
                                cell.setCellStyle(moneyCellStyle);
                            }
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void createContent(HSSFWorkbook workbook, HSSFSheet sheet, int titleLength,
			String[] valueTags, List<T> dataList, String timePattern, String formulae) {
		int rowNum = 0;
		String formula = "";
		boolean hasFormula = false;
		if (!StringUtils.isEmpty(formulae)) {
			String[] formulaArr = formulae.split("##");
			rowNum = Integer.parseInt(formulaArr[0]);
			formula = formulaArr[1];
		}

		// 生成内容样式
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBorderLeft(BorderStyle.THIN);
		contentStyle.setBorderRight(BorderStyle.THIN);
		contentStyle.setBorderTop(BorderStyle.THIN);
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容字体样式
		HSSFFont contentFont = workbook.createFont();
		contentFont.setColor(IndexedColors.BLACK.index);
		contentFont.setFontHeightInPoints((short) 10);
		contentStyle.setFont(contentFont);
        
        HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
        moneyCellStyle.cloneStyleFrom(contentStyle);
        moneyCellStyle.setDataFormat(workbook.createDataFormat().getFormat("##0.00")); //小数点后保留两位
        
		//
		HSSFRow contentRow;
		try {
			int rowIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				rowIndex = i + 1;
				// 创建行
				contentRow = sheet.createRow(rowIndex);
				// 获取对象
				T entity = dataList.get(i);
				Field[] fields = entity.getClass().getDeclaredFields();
				for (int j = 0; j < fields.length; j++) {
					// 创建单元格
					HSSFCell cell = contentRow.createCell(j);
					cell.setCellStyle(contentStyle);
					Field field = fields[j];

					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);

					Class tCls = entity.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});

					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value == null)
						continue;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat format = new SimpleDateFormat(timePattern);
						textValue = format.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()
                                && (value instanceof Double || value instanceof Float || value instanceof Integer)) {
                            if (getMethodName.contains("Money")) {
                                cell.setCellStyle(moneyCellStyle);
                            }
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}