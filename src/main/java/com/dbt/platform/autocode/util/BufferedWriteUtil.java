package com.dbt.platform.autocode.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang.StringUtils;

/**
 * 项目名称：V积分V码生成工具类
 * 文件名：BufferedWriteUtil
 * 作者：jiquanwei
 * 创建时间：2014-06-17 12:58:20
 * 功能描述: 二维码图片生成工具类
 * 版   本: V 1.0
 */
public class BufferedWriteUtil {
	
	/**
     * 按字符缓冲写入 BufferedWriter
     * @param str   写入字符串
     */
    public static void bufferedWrite(String str,File f) {
        BufferedWriteUtil.bufferedWrite(str, f, null);
    }
    public static void bufferedWrite(String str,File f, String encoding) {
    	
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(f, true);
            if (StringUtils.isNotBlank(encoding)) {
                writer = new OutputStreamWriter(os, encoding);
            } else {
                writer = new OutputStreamWriter(os);
            }
            bw = new BufferedWriter(writer);
            bw.write(str);
            //bw.write(System.getProperty("line.separator"));
            //说明:line.separator是跨系统的写法，在windows=\r\n linux=\r
            //上述写法本身是对的，但是我们的需求是不管在windows还是linux中生成，都在windows中显示。
            bw.write("\r\n");
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
