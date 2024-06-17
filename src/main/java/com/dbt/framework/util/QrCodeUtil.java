/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 上午11:46:13 by hanshimeng create
 * </pre>
 */
package com.dbt.framework.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dbt.datasource.util.DbContextHolder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeUtil {

	private static Logger log = Logger.getLogger(QrCodeUtil.class);
	
	/**
	 * 解析二维码
	 * @param sweepstr </br> 
	 * @return Map<String,String> </br>
	 */
	public static Map<String, String> analysisQrcode(String sweepstr){
		Map<String, String> sweepCodeMap=null;
	    if(sweepstr.length()>30){
	        //二次反向解析混排
	        sweepCodeMap = antiMixedGetVCode(sweepstr, true);
	    }else{
	        if(sweepstr.length()>15){
	            //一次反向解析混排
	            sweepCodeMap = antiMixedGetVCode(sweepstr, false);
	        } else {
	            //无需解析
	            sweepCodeMap=simpleGetVCode(sweepstr);
	        }
	    }
	    return sweepCodeMap;
	}
	
	/**
     * 反混排得到明文字符串
     * 
     * @param sweepStr
     * @param flag
     *            true:需要反混排两次
     * @return
     */
    public static Map<String, String> antiMixedGetVCode(String sweepStr, boolean flag) {
        if (StringUtils.isBlank(sweepStr)) {
            log.info("扫码内容为空!");
            return null;
        }
        // 第一次反混排结果
        StringBuilder stringBuilder = null;
        if (flag) {
            stringBuilder = new StringBuilder(32);
            char[] charArr = sweepStr.toCharArray();
            if (null != charArr && charArr.length > 0) {
                for (int i = 0, len = charArr.length; i < len; i++) {
                    if (i % 2 != 0) {
                        stringBuilder.append(charArr[i]);
                    }
                }
            }
        }
        char[] finalCharArr = sweepStr.toCharArray();
        if (flag) {
            finalCharArr = stringBuilder.toString().toCharArray();
        }
        StringBuilder finalBuilder = new StringBuilder();
        if (null != finalCharArr && finalCharArr.length > 0) {
            List<String> list = new ArrayList<String>();
            for (int i = 0, len = finalCharArr.length; i < len; i++) {
                if ((3 * i - 1) < len) {
                    list.add(String.valueOf(3 * i - 1));
                }
                if (!list.contains(String.valueOf(i))) {
                    finalBuilder.append(finalCharArr[i]);
                }
            }
        }
        // 最终字符码
        String finalStr = finalBuilder.toString();
        
        // 广西、浙江
        if ("guangxi".equals(DbContextHolder.getDBType())) {
            finalStr=finalStr.substring(0, 11);
            
        } else {
            finalStr=finalStr.substring(0, 12);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        if (finalStr.length() >= 3) {
            map.put("activitycode", finalStr.substring(0, 3));
            map.put("vcode", finalStr.substring(3));
        }
//        log.info("解析出的批次活动标识" + map.get("activitycode") + "码的内容："
//                + map.get("vcode"));
        return map;
    }
    
    
    /**
     * 简化提取11位参数中3位活动标志和8位串码
     * @param sweepStr
     * @return
     */
    public static Map<String, String> simpleGetVCode(String sweepStr) {
        if (StringUtils.isBlank(sweepStr)) {
            log.info("扫码内容为空!");
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        if (sweepStr.length() >= 3) {
            map.put("activitycode", sweepStr.substring(0, 3));
            map.put("vcode", sweepStr.substring(3));
        }
//        log.info("解析出的批次活动标识" + map.get("activitycode") + "码的内容："
//                + map.get("vcode"));
        return map;
    }
    
    /**
	 * 生成二维码
	 * 
	 * @param content
	 *            二维码内容
	 * @param charset
	 *            编码二维码内容时采用的字符集(传null时默认采用UTF-8编码)
	 * @param imagePath
	 *            二维码图片存放路径(含文件名)
	 * @param width
	 *            生成的二维码图片宽度
	 * @param height
	 *            生成的二维码图片高度
	 * @param logoPath
	 *            logo头像存放路径(含文件名,若不加logo则传null即可)
	 * @return 生成二维码结果(true or false)
	 */
	public static boolean encodeQRCodeImage(String content, String charset,
			String imagePath, int width, int height, String logoPath) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定编码格式
		// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 指定纠错级别(L--7%,M--15%,Q--25%,H--30%)
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 设置白边的大小0-4
        hints.put(EncodeHintType.MARGIN, 0);
		// 编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(new String(content
					.getBytes(charset == null ? "UTF-8" : charset),
					"ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);
			
		} catch (Exception e) {
			System.out.println("编码待生成二维码图片的文本时发生异常,堆栈轨迹如下");
			e.printStackTrace();
			return false;
		}
		// 生成的二维码图片默认背景为白色,前景为黑色,但是在加入logo图像后会导致logo也变为黑白色,至于是什么原因还没有仔细去读它的源码
		// 所以这里对其第一个参数黑色将ZXing默认的前景色0xFF000000稍微改了一下0xFF000001,最终效果也是白色背景黑色前景的二维码,且logo颜色保持原有不变
		MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001,0xFFFFFFFF);
		// 这里要显式指定MatrixToImageConfig,否则还会按照默认处理将logo图像也变为黑白色(如果打算加logo的话,反之则不须传MatrixToImageConfig参数)
		try {
			MatrixToImageWriter.writeToFile(bitMatrix, imagePath
					.substring(imagePath.lastIndexOf(".") + 1), new File(
					imagePath), config);
		} catch (IOException e) {
			System.out.println("生成二维码图片[" + imagePath + "]时遇到异常,堆栈轨迹如下");
			e.printStackTrace();
			return false;
		}
		// 此时二维码图片已经生成了,只不过没有logo头像,所以接下来根据传入的logoPath参数来决定是否加logo头像
		if (null == logoPath) {
			return true;
		} else {
			// 如果此时最终生成的二维码不是我们想要的,那么可以扩展MatrixToImageConfig类(反正ZXing提供了源码)
			// 扩展时可以重写其writeToFile方法,令其返回toBufferedImage()方法所生成的BufferedImage对象(尽管这种做法未必能解决为题,故需根据实际情景测试)
			// 然后替换这里overlapImage()里面的第一行BufferedImage image = ImageIO.read(new
			// File(imagePath));
			// 即private static void overlapImage(BufferedImage image, String
			// imagePath, String logoPath)
			try {
				// 这里不需要判断logoPath是否指向了一个具体的文件,因为这种情景下overlapImage会抛IO异常
				overlapImage(imagePath, logoPath);
				return true;
			} catch (IOException e) {
				System.out.println("为二维码图片[" + imagePath + "]添加logo头像["
						+ logoPath + "]时遇到异常,堆栈轨迹如下");
				e.printStackTrace();
				return false;
			}
		}
	}
    private static void overlapImage(String imagePath, String logoPath)
			throws IOException {
		BufferedImage image = ImageIO.read(new File(imagePath));
		int logoWidth = image.getWidth() / 5; // 设置logo图片宽度为二维码图片的五分之一
		int logoHeight = image.getHeight() / 5; // 设置logo图片高度为二维码图片的五分之一
		int logoX = (image.getWidth() - logoWidth) / 2; // 设置logo图片的位置,这里令其居中
		int logoY = (image.getHeight() - logoHeight) / 2; // 设置logo图片的位置,这里令其居中
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(ImageIO.read(new File(logoPath)), logoX, logoY, logoWidth, logoHeight, null);
		graphics.dispose();
		ImageIO.write(image, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath));
	}
    
}
