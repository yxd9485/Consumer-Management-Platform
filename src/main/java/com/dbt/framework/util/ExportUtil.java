package com.dbt.framework.util;

import org.apache.commons.io.FileUtils;

import java.io.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RoyFu
 * @version 1.0
 * @createTime 2015年6月12日 下午4:00:59
 * @description 导出的工具类，可持续增加新方法
 */

public class ExportUtil {

	// 防new
	private ExportUtil(){}
	
	/**
	 * 导出TXT文件
	 * @param servOutputStream  需要进行传送的流
	 * @param text 需要导出的内容
	 */
	public static void writeTxtFile(ServletOutputStream servOutputStream, String text) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(servOutputStream);
			outputStream.write(text.getBytes("UTF-8"));
			outputStream.flush();  
			outputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(null != outputStream){
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		 
	}
	/**
	 * 导出TXT文件
	 * @param servOutputStream  需要进行传送的流
	 * @param text 需要导出的内容
	 */
	public static void writeTxtFile(ServletOutputStream servOutputStream, String text , String bytes) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(servOutputStream);
			outputStream.write(text.getBytes(bytes));
			outputStream.flush();  
			outputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(null != outputStream){
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
	}

    public static void download(File file, HttpServletRequest request, HttpServletResponse response) throws IOException{
        byte[] content = FileUtils.readFileToByteArray(file);
        String title=file.getName();
        response.reset();
        InputStream is = new ByteArrayInputStream(content);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ServletOutputStream out = response.getOutputStream();
            // 设置response参数，可以打开下载页面
            response.setHeader("Cache-Control", "private");
            response.setHeader("Pragma", "private");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Type", "application/force-download");
            title = processFileName(request, title);
            response.setHeader("Content-disposition", "attachment;filename=" + title);
            //response.setHeader("Content-Disposition", "attachment;filename="+ new String(title).getBytes(), "iso-8859-1"));
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    /**
     *
     * @Title: processFileName
     *
     * @Description: ie,chrom,firfox下处理文件名显示乱码
     */
    public static String processFileName(HttpServletRequest request, String fileNames) {
        String codedfilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")|| null != agent
                    && -1 != agent.indexOf("Edge")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF-8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等
                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedfilename;
    }
}
