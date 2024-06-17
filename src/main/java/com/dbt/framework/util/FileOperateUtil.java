/**
 * 项目名称：V积分
 * 文件名：FileOperateUtil.java
 * 作者：@王建
 * 创建时间：2014/1/1
 * 功能描述: 文件操作工具类
 * 版本: V1.0
 */
package com.dbt.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.log4j.Logger;

public class FileOperateUtil {

	//文件步进size
	public static final int BUFFER_SIZE = 16 * 1024;
	/**
	 * 判断文件是否存在
	 * 
	 * @param strFile
	 *            文件名
	 * @return 存在返回True,不存在返回False
	 */
	public static boolean checkFile(String strFile) {

		// 文件名为null或空时返回false
		if (strFile == null || strFile.length() == 0) {
			return false;
		}

		// 构造文件
		File fileCheck = new File(strFile);

		// 判断文件是否存在
		if (!fileCheck.exists() || !fileCheck.isFile() || !fileCheck.canRead()
				|| fileCheck.length() == 0) {
			return false;
		}

		// 文件存在返回
		return true;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param strFile
	 *            文件名
	 * @return 存在返回True,不存在返回False
	 */
	public static boolean checkFile(File file) {

		// 文件名为null或空时返回false
		if (file == null) {
			return false;
		}

		// 判断文件是否存在
		if (!file.exists() || !file.isFile() || !file.canRead()
				|| file.length() == 0) {
			return false;
		}

		// 文件存在返回
		return true;
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return 删除成果返回TRUE 否则返回FALSE
	 */
	public static boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				bea = true;
			}
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			// 删除完里面所有内容
			delAllFile(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);

			// 删除空文件夹
			myFilePath.delete();
		} catch (Exception e) {
			e.printStackTrace();
			// message = ("删除文件夹操作出错");
		}
	}

	/**
	 * 取得文件名称
	 * 
	 * @param strSelSrc
	 *            文件全路径
	 * @return 文件名
	 */
	public static String getFileName(String strSelSrc) {

		// 字符集的长度
		int len;

		// 存放截取的字符集
		String[] strUrl;

		// 经过处理的地址
		String strSelUrl;

		// 根据"/"符号截取
		strUrl = strSelSrc.split("/");
		len = strUrl.length;
		strSelUrl = strUrl[len - 1];

		// 返回文件名
		return strSelUrl;
	}

	/**
	 * 检查目录并是否存在不存在就新建
	 * 
	 * @param folderPath
	 *            目录地址
	 * @param 文件路径
	 */
	public static String checkAndCreateFolder(String folderPath) {
		String folder = folderPath;
		try {
			File myFilePath = new File(folder);
			folder = folderPath;

			// 如果文件夹不存在md
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folder;
	}

	/**
	 * 取得文件后缀(不保留.)
	 * 
	 * @param strFileName
	 *            文件名称
	 * @return 文件后缀
	 */
	public static String getExtension(String strFileName) {
		// 返回值
		String strResult = "";

		// 判断文件名是否为空
		if ((strFileName != null) && (strFileName.length() > 0)) {
			int i = strFileName.lastIndexOf('.');

			// 取得文件后缀
			if ((i > -1) && (i < (strFileName.length() - 1))) {
				strResult = strFileName.substring(i + 1);
			}
		}

		// 返回文件后缀
		return strResult;
	}

	/**
	 * 取得文件后缀(保留.)
	 * 
	 * @param strFileName
	 *            文件名称
	 * @return 文件后缀
	 */
	public static String getExtensionBeDot(String strFileName) {
		// 返回值
		String strResult = "";

		// 判断文件名是否为空
		if ((strFileName != null) && (strFileName.length() > 0)) {
			int i = strFileName.lastIndexOf('.');

			// 取得文件后缀
			if ((i > -1) && (i < (strFileName.length() - 1))) {
				strResult = strFileName.substring(i);
			}
		}

		// 返回文件后缀
		return strResult;
	}

	/**
	 * 根据后缀取得唯一文件名 (避免重名)
	 * 
	 * @param strDepFileName
	 *            文件后缀
	 * @return 文件改名后字符
	 */
	public static String fileRename(String strDepFileName) {

		// 返回值
		String strResult = "";
		Random random = new Random(1000);

		// 系统时间纳秒加随机数
		strResult += System.nanoTime()
				+ Long.parseLong((random.nextInt()) + "");
		strResult += getExtensionBeDot(strDepFileName);

		// 返回
		return strResult;
	}

	/**
	 * 生成临时文件夹(唯一性)
	 * 
	 * @return 文件夹名
	 */
	public static String getFolderName() {

		// 返回值
		String strResult = "";
		Random random = new Random(1000);

		// 系统时间纳秒加随机数
		strResult += System.nanoTime()
				+ Long.parseLong((random.nextInt()) + "");

		// 返回
		return strResult;
	}
	
	private static final Logger log = Logger.getLogger(FileOperateUtil.class);
	
	/**
	 * 复制单个文件
	 * @param srcFileName 待复制的文件名
	 * @param descFileName 目标文件名
	 * @param coverlay 如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName,
			String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				log.debug("目标文件已存在，准备删除!");
				if (!FileOperateUtil.delFile1(descFileName)) {
					log.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				log.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					log.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
					+ "成功!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile1(String fileName) {
 		File file = new File(fileName);
		if (!file.exists()) {
			log.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileOperateUtil.deleteFile(fileName);
			} else {
				return FileOperateUtil.deleteDirectory(fileName);
			}
		}
	}
	
	/**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除单个文件 " + fileName + " 成功!");
				return true;
			} else {
				log.debug("删除单个文件 " + fileName + " 失败!");
				return false;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return true;
		}
	}

	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName 被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileOperateUtil.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileOperateUtil.deleteDirectory(files[i]
						.getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			log.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			log.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 复制文件
	 * 
	 * @param DepartFile
	 *            原文件
	 * @param DestinationFile
	 *            目标文件
	 */
	public static void copyFile(File DepartFile, File DestinationFile) {

		// 文件不存在时忽略
		if (!checkFile(DepartFile)) {
			return;
		}

		try {
			// 文件输入流
			InputStream in = null;

			// 文件输出流
			OutputStream out = null;
			try {
				// 取得输入文件并转换为数据流
				in = new BufferedInputStream(new FileInputStream(DepartFile),
						BUFFER_SIZE);

				// 输出文件流到目标文件
				out = new BufferedOutputStream(new FileOutputStream(
						DestinationFile), BUFFER_SIZE);

				// 文件转换为二进制流
				byte[] buffer = new byte[BUFFER_SIZE];

				// 执行写操作
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static boolean delFile(String filePathAndName) {
		boolean flag = false ;
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);
			flag =  myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}
		return flag;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public static void moveFile(String oldPath, String newPath) {
		copyFile(new File(oldPath), new File(newPath));
		delFile(oldPath);

	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);

	}

	/**
	 * 依UTF-8的编码格式写文件
	 * 
	 * @param path
	 *            待写路径
	 * @param content
	 *            写内容
	 */
	public static void writeFileUTF8(String path, String content) {
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
			out.write(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 依UTF-8的编码格式写文件
	 * 
	 * @param path
	 *            待写路径
	 * @param content
	 *            写内容
	 */
	public static void writeFileGBK(String path, String content) {
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(path), "GBK");
			out.write(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}