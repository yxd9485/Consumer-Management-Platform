package com.dbt.platform.autocode.tools;

import java.io.File;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 文件压缩
 * @author hanshimeng
 *
 */
public class CompressFileTools {
	static Logger log = Logger.getLogger(CompressFileTools.class);
	
	//	 目录格式：
	//	 主目录：/data/upload/autocode/20180908（日期）/20180906153508123（订单编号）/
	//	 源码目录：1source
	//	 合并目录：2merge
	//	 转换目录：3convert
	//	 压缩目录：4compress
	
	/**
	 * 文件压缩Zip（把2merge或3convert目录下的文件压缩为zip）
	 * @param filePath 码源订单所在路径
	 * @param orderKey 订单主键
	 * @param date 订单日期
	 * @param fileName zip名称（订单名称）
	 * @param directory 目录：1source,2merge,3convert,4compress
	 * @param password 压缩包密码
	 * @throws Exception 
	 */
	public static void compressFile(String filePath, String orderKey, String date, String fileName, String directory, String password) throws Exception{
		log.warn("文件压缩Zip开始：" + fileName);
		if(StringUtils.isBlank(orderKey)){
			throw new Exception("文件压缩Zip异常：参数不能为空orderKey=" + orderKey);
		}
		
        try {
			File srcFile = new File(filePath + "/" + directory);
			if(!srcFile.exists()){
				throw new Exception("文件压缩Zip异常："+directory+"文件不存在");
			}
			
            //创建压缩文件
			fileName = fileName.indexOf(".zip") > -1 ? fileName : fileName + ".zip";
			new File(filePath + "/4compress").mkdir();
            ZipFile zipFile = new ZipFile(filePath + "/4compress/" + fileName);
            ArrayList<File> files = new ArrayList<>();
            File[] tempFile = srcFile.listFiles();  
            for (int i = 0; i < srcFile.list().length; i++) {
            	files.add(tempFile[i]);
			}

            //设置压缩文件参数
            ZipParameters parameters = new ZipParameters();
            //设置压缩方法
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            //设置压缩级别
            //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
            //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
            //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
            //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
            //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			if (StringUtils.isNotBlank(password)) {
				// 设置压缩文件加密
				parameters.setEncryptFiles(true);

				// 设置加密方法
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

				// 设置aes加密强度
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

				// 设置密码
				parameters.setPassword(password);
			}

            //添加文件到压缩文件
            zipFile.addFiles(files, parameters);
        } catch (ZipException e) {
            throw new Exception("文件压缩Zip异常：" + fileName, e);
        }
        log.warn("文件压缩Zip结束：" + fileName);
    }
}
