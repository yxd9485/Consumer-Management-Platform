package com.dbt.platform.autocode.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.dbt.framework.util.PropertiesUtil;
import com.dbt.platform.autocode.util.TxtConvertMdbUtil;
import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;
/**	
 * txt转mdb文件工具类
 * @author hanshimeng
 *
 */
public class TxtConvertMdbTools {
	//	 目录格式：
	//	 主目录：/data/upload/autocode/20180908（日期）/20180906153508123（订单编号）/
	//	 源码目录：1source
	//	 合并目录：2merge
	//	 转换目录：3convert
	//	 压缩目录：4compress
	
	static Logger log = Logger.getLogger(TxtConvertMdbTools.class);
	
    /**
     * txt转换mdb文件主方法
     * @param filePath 码源订单所在路径
     * @param orderKey 订单主键
     * @param date 订单日期
     * @param directory 目录：1source,2merge,3convert,4compress
     * @throws Exception
     */
	public static void txtConvertMdb(String filePath, String orderKey, String date, String directory) throws Exception {
		log.warn("txt转换mdb开始");
		new File(filePath + "/3convert").mkdir();
		File[] fileLst = new File(filePath + "/" + directory).listFiles();
		File itemFile;
        for (int i = 0; i < fileLst.length; i++) {
            itemFile = fileLst[i];
            if (itemFile.isFile() && itemFile.getName().contains(".txt")) {
            	importDataByJackess(itemFile, directory);
            }
        }
        log.warn("txt转换mdb结束");
	}
    
    /**
     * 文件转换
     * @param file 源文件（txt格式）
     * @param directory 目录：1source,2merge,3convert,4compress
     * @throws Exception
     */
	public static void importDataByJackess(File file, String directory) throws Exception {
    	try{
	    	String mdbFilePath = file.getPath().replace(directory, "3convert").replace(".txt", ".mdb");
	    	Database db = DatabaseBuilder.create(Database.FileFormat.V2003, new File(mdbFilePath));
	    	Table newTable = new TableBuilder("1t")
	            .addColumn(new ColumnBuilder("SN")
	                       .setSQLType(Types.VARCHAR)
	                       .toColumn())
	            .addColumn(new ColumnBuilder("CT")
	                       .setSQLType(Types.VARCHAR)
	                       .toColumn())
	            .addColumn(new ColumnBuilder("markFlag")
	                       .setSQLType(Types.VARCHAR)
	                       .toColumn())
	            .addColumn(new ColumnBuilder("markTime")
	                    .setSQLType(Types.VARCHAR)
	                    .toColumn())
	            .toTable(db);
	          
	        String line;
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        ArrayList<Object[]> list=new ArrayList<Object[]>();
	        while ((line = br.readLine()) != null) {
	            Object[] obj={line,null, '0', null };
	            list.add(obj);
	        }
	        newTable.addRows(list);
    	}catch(Exception e){
    		throw new Exception("txt转换mdb异常",e);
    	}
    }
    
    /**
     * 文件转换
     * @param file 源文件（txt格式）
     * @throws Exception
     */
    public static void importDataByJackess() throws Exception {
    	System.out.println(System.currentTimeMillis());
    	Database db = DatabaseBuilder.create(Database.FileFormat.V2003, new File("d:\\new2.mdb"));
    	Table newTable = new TableBuilder("1t")
            .addColumn(new ColumnBuilder("SN")
                       .setSQLType(Types.VARCHAR)
                       .toColumn())
            .addColumn(new ColumnBuilder("CT")
                       .setSQLType(Types.VARCHAR)
                       .toColumn())
            .addColumn(new ColumnBuilder("markFlag")
                       .setSQLType(Types.VARCHAR)
                       .toColumn())
            .addColumn(new ColumnBuilder("markTime")
                    .setSQLType(Types.VARCHAR)
                    .toColumn())
            .toTable(db);
          ArrayList<Object[]> list=new ArrayList<Object[]>();
          for (int j = 0; j < 100; j++){
              Object[] obj={j, null, '0', null };
              list.add(obj);
          }
          newTable.addRows(list);
          System.out.println(System.currentTimeMillis());
    }
    
    /**
	 * 文件转换（不支持Linux系統）
	 * @param file 源文件（txt格式）
	 * @throws Exception
	 */
    @Deprecated
    public static void importData(File file) throws Exception {
    	BufferedReader br = null;
    	TxtConvertMdbUtil accessUtil = null;
    	try{
	        accessUtil = TxtConvertMdbUtil.getInstance();
	        accessUtil.setSavedFileName(file);
	        accessUtil.copyBlankMdbFile(PropertiesUtil.getPropertyValue("create_code_common_path"));
	        accessUtil.connetAccessDB();
	        br = new BufferedReader(new FileReader(file));
	        String line;
	        StringBuffer buffer = new StringBuffer();
	        while ((line = br.readLine()) != null) {
	            buffer = new StringBuffer();
	            buffer.append("insert into 1t (SN, CT, MarkFlag, MarkTime) values('");
	            buffer.append(line).append("', null, '0', null )");
	            accessUtil.executeSql(buffer.toString());
	        }
	        accessUtil.closeConnection();
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new Exception("txt转换mdb异常",e);
    	}finally {
			if(null != br){
				br.close();
			}
		}
//	    System.out.println(String.format("%02d", index) + ": " + file.getName() + "完成! -- " + qrcodeNum + "个二维码");
    }

    /**
     * txt转换csv文件主方法
     * @param filePath 码源订单所在路径
     * @param orderKey 订单主键
     * @param date 订单日期
     * @param directory 目录：1source,2merge,3convert,4compress
     * @throws Exception
     */
    public static void txtConvertCSV(String filePath, String orderKey, String date, String directory) throws Exception {
        log.warn("txt转换csv开始");
        new File(filePath + "/3convert").mkdir();
        File[] fileLst = new File(filePath + "/" + directory).listFiles();
        File itemFile;
        String csvFilePath = null;
        for (int i = 0; i < fileLst.length; i++) {
            itemFile = fileLst[i];
            if (itemFile.isFile() && itemFile.getName().contains(".txt")) {
                csvFilePath = itemFile.getPath().replace(directory, "3convert").replace(".txt", ".csv");
                itemFile.renameTo(new File(csvFilePath));
            }
        }
        log.warn("txt转换csv结束");
    }

    /**
     * 测试main方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        System.out.println("导入开始......");
//        File inputDir = new File(TxtConvertMdbTools.class.getResource("").getFile() + "/input");
////      txtConvertMdb("");
//        System.out.println("导入完成......");
    	importDataByJackess();
    }
}
