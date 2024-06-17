package com.dbt.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadExcel {
    /**
     * 对外提供读取excel 的方法
     */
    public static List<List<Object>> readExcel(InputStream is, String extension) throws IOException {
        if ("xls".equals(extension)) {
            return read2003Excel(is);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(is);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 对外提供读取excel 的方法
     */
    public static List<List<Object>> readExcel(File file) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
                .substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return read2003Excel(new FileInputStream(file));
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(new FileInputStream(file));
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 对外提供读取excel 的方法
     */
    public static List<List<Object>> readExcel(InputStream is, String extension, int cellSize) throws IOException {
        if ("xls".equals(extension)) {
            return read2003Excel(is, cellSize);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(is, cellSize);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    private static List<List<Object>> read2003Excel(InputStream is) throws IOException {
        return read2003Excel(is, 0);
    }

    private static List<List<Object>> read2007Excel(InputStream is) throws IOException {
        return read2007Excel(is, 0);
    }

    /**
     * 读取 office 2003 excel
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<List<Object>> read2003Excel(InputStream is, int cellSize) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        HSSFWorkbook hwb = new HSSFWorkbook(is);
        HSSFSheet sheet = hwb.getSheetAt(0);
        Object value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        int counter = 0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }

            if (cellSize == 0) {
                cellSize = row.getLastCellNum();
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <= cellSize; j++) {
                cell = row.getCell(j);
                String strCell = "";
                if (cell != null) {
                    String str = row.getCell(j).toString();
                    if (Pattern.compile("^[+-]?\\d+\\.?\\d*[Ee][+-]?\\d+$").matcher(str).find()) {
                        strCell = new BigDecimal(str).toPlainString();
                    }
                }
                if (cell == null) {
                    linked.add("");
                    continue;
                }
                try {
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String
                    // 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    if (strCell.equals("")) {
                        
                        
                        switch (cell.getCellType()) {
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                    value = df.format(cell.getNumericCellValue());
                                } else if ("General".equals(cell.getCellStyle()
                                        .getDataFormatString())) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else {
                                    value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                            .getNumericCellValue()));
                                }
                                break;
                            case BOOLEAN:
                                value = cell.getBooleanCellValue();
                                break;
                            case BLANK:
                                value = "";
                                break;
                            default:
                                value = cell.toString();
                        }
                    } else {
                        value = strCell;
                    }

                    if (value == null || "".equals(value)) {
                        linked.add("");
                        continue;
                    }
                    linked.add(value);
                } catch (Exception e) {
                    linked.add(cell.toString());
                }

            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 读取Office 2007 excel
     */
    private static List<List<Object>> read2007Excel(InputStream is, int cellSize) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(is);
        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        int counter = 0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }

            if (cellSize == 0) {
                cellSize = row.getLastCellNum();
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <= cellSize; j++) {
                cell = row.getCell(j);
                if (cell == null || cell.equals("")) {
                    linked.add("");
                    continue;
                }
                try {
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String
                    // 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add("");
                        continue;
                    }
                    linked.add(value);
                } catch (Exception e) {
                    linked.add(cell.toString());
                }
            }
            list.add(linked);
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            long t1 = System.currentTimeMillis();
            List<List<Object>> readExcel = readExcel(new File("d:\\export2003_a.xls"));
            long end = System.currentTimeMillis();
            // readExcel(new File("C:\Users\lees\Desktop"));
            for (List<Object> obj : readExcel) {
                for (Object o : obj) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> newList = new ArrayList<String>(9);
    }
}
