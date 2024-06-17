package com.dbt.platform.autocode.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.io.Files;

/**
 * txt文件合并工具
 * @author hanshimeng
 *
 */
public class MergeFileTools {

	static Logger log = Logger.getLogger(MergeFileTools.class);
	
	//	 目录格式：
	//	 主目录：/data/upload/autocode/20180908（日期）/20180906153508123（订单编号）/
	//	 源码目录：1source
	//	 合并目录：2merge
	//	 转换目录：3convert
	//	 压缩目录：4compress
	
	/**
	 * 合并码源文件（两个50万文件合并为1个100万）
	 * @param filePath 码源订单所在路径
	 * @param orderKey 订单主键
	 * @param date 订单日期
	 * @throws Exception 
	 */
	public static void FileMerge(String filePath, String orderKey, String date) throws Exception{
		log.warn("码源合并开始");
		if(StringUtils.isBlank(orderKey)){
			throw new Exception("合并文件异常：参数不能为空orderKey=" + orderKey);
		}

		// 测试数据
		// String filePath = "E:/temp";
		// File file = new File(filePath + "/1source");
		
		try{
			new File(filePath + "/2merge").mkdir();
			File file = new File(filePath + "/1source");
			if(!file.exists()){
				throw new Exception("合并文件异常：1source源码文件不存在");
			}
			
			int idx = 0;
			int successCount = 0;
			File toFile = null;
			File[] fileList = file.listFiles();
			if(fileList.length == 0){
        		throw new Exception("合并文件异常：没有可处理的文件");
			}
			if(fileList.length == 1){
				idx++;
				successCount++;
        		toFile = new File(filePath + "/2merge/" + date + "-" + idx + ".txt");
        		Files.copy(fileList[0], toFile);
			}else{
				// 处理多个文件
				BufferedWriter out = null;   
				BufferedReader bufferedReader = null; 
		        for (int i = 0; i < fileList.length; i++) {
		        	if (fileList[i].isFile() && fileList[i].getName().contains(".txt")) {
		        		try{
			        		// 创建拷贝合并的码源文件
				        	if(i % 2 == 0){
				        		idx++;
				        		toFile = new File(filePath + "/2merge/" + date + "-" + idx + ".txt");
				        		Files.copy(fileList[i], toFile);
				        	}
				        	// 追加合并的码源文件
				        	else{
				        		bufferedReader = new BufferedReader(new FileReader(fileList[i]));
				        		//bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				        		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile, true)));   
				        		String lineTxt = null;
				        		while ((lineTxt = bufferedReader.readLine()) != null) {
				        			out.write(lineTxt + "\n");
				        		}
				        	}
				        	successCount++;
	        			}catch(Exception e){
//			        		log.error("合并文件异常", e);
	        				throw new Exception("合并文件异常", e);
		        		}finally {
							if(bufferedReader != null){
								bufferedReader.close();
							}
							if(out != null){
								out.close();
							}
						}
			        }
		        }
			}
	        if(fileList.length > successCount){
	        	throw new Exception("合并文件异常：还有未合并的文件");
	        }
		}catch(Exception e){
//			log.error("合并文件异常", e);
			throw new Exception("合并文件异常", e);
		}
		log.warn("码源合并结束");
	}
	
	/**
	 * 合并码源文件（Rio通道专用）
	 * @param filePath 码源订单所在路径
	 * @param orderKey 订单编号
	 * @param date 订单日期
	 * @param count 通道数量
	 * @param totalCount 二维码总量
	 * @param txtCount 指定合并文件二维码数量，如果是0则需要计算：totalCount / count
	 * @throws Exception 
	 */
	public static void FileMerge(String filePath, String orderKey, String date, int count, int totalCount, int txtCount) throws Exception{
		log.warn("码源合并开始（通道专用）");
		if(StringUtils.isBlank(orderKey) || count <= 0 || totalCount <= 0){
			throw new Exception("合并文件异常：参数异常orderKey=" + orderKey +",count=" + count + ",totalCount=" + totalCount);
		}
		
		// 测试数据
		// String filePath = "E:/temp";
		// File file = new File(filePath + "/1source");
		
		try{
			new File(filePath + "/2merge").mkdir();
			File file = new File(filePath + "/1source");
			if(!file.exists()){
				throw new Exception("合并文件异常：1source源码文件不存在");
			}
			
			int idx = 0;
			File toFile = null;
			File[] fileList = file.listFiles();
			if(fileList.length == 0){
				throw new Exception("合并文件异常：没有可处理的文件");
			}

			// 每个文件二维码的计数
			int txtIdx = 0;
			// 每个文件的二维码数量
			if(txtCount == 0){
				txtCount = totalCount / count;
			}
			
			
			// 处理多个文件
			int textFileCount = 0;
			int successCount = 0;
			BufferedWriter out = null;   
			BufferedReader bufferedReader = null; 
	        for (int i = 0; i < fileList.length; i++) {
	        	if (fileList[i].isFile() && fileList[i].getName().contains(".txt")) {
	        	    textFileCount++;
	        		try{
		        		// 创建第一个合并的码源文件
	        			if(idx == 0){
	        				idx++;
	        				toFile = new File(filePath + "/2merge/" + date + "-" + idx + ".txt");
	        			}
	        			
		        		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileList[i]), "UTF-8"));
		        		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile, true), "UTF-8"));
		        		String lineTxt = null;
		        		while ((lineTxt = bufferedReader.readLine()) != null) {
		        			out.write(lineTxt + "\n");
		        			txtIdx++;
		        			
		        			// 该文件的二维码数量已经达到txtCount，则创建下一个文件
		        			if(txtIdx == txtCount && idx < count){
		        				out.close();
		        				txtIdx = 0;
		        				
		        				idx++;
				        		toFile = new File(filePath + "/2merge/" + date + "-" + idx + ".txt");
				        		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile, true), "UTF-8"));
		        			}
		        		}
			        	successCount++;
        			}catch(Exception e){
        				throw new Exception("合并文件异常", e);
	        		}finally {
						if(bufferedReader != null){
							bufferedReader.close();
						}
						if(out != null){
							out.close();
						}
					}
		        }
	        }
	        if(textFileCount > successCount){
	        	throw new Exception("合并文件异常：还有未合并的文件");
	        }
		}catch(Exception e){
			throw new Exception("合并文件异常", e);
		}
		log.warn("码源合并结束（通道专用）");
	}
	
	public static void main(String[] args) {
		try {
//			FileMerge("1");
//			FileMerge("1", 4, 35);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
