package com.dbt.framework.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LabelImage {
	//图片宽度
	private static final  int IMAGE_WIDTH = 2100;
	//图片 高度
	private static final  int IMAGE_HEIGHT = 2970;
	//字体x轴
	private static final  int FONT_X = 30;
	//字体y轴
	private static final  int FONT_Y = 80;
	//标签总数
	private static final  int LABEL_TOTAL = 40;
	
   
    public static  void myGraphicsGeneration(String [] batch ,String path) throws IOException {
    	
    	BufferedImage content = ImageIO.read(new File(batch[4]));
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,BufferedImage.TYPE_INT_RGB);      
        Graphics2D graphics = image.createGraphics();
        //抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, IMAGE_WIDTH, IMAGE_HEIGHT);
        //设置字体间距   
        int x = FONT_X, y = FONT_Y, n = FONT_Y;
        //循环标签数
        for (int i = 0 ; i < LABEL_TOTAL; i++) {
			if (i > 0 && i % 4 == 0) {		
				x = FONT_X;
				n = y += 300;
				graphics.setColor(Color.black);
				graphics.setStroke(new BasicStroke(3.3f));
				graphics.drawLine(0, y-100 ,IMAGE_WIDTH ,y-100); 
			}
			
			//日期
			graphics.setColor(Color.red);
			graphics.setFont(new Font("微软雅黑", Font.PLAIN,30));		
			graphics.drawString(batch[5], x, y-30);

			graphics.setColor(Color.black);
			graphics.setFont(new Font("宋体", 0, 26));
			graphics.drawString(batch[0], x, y += 10);
			graphics.setFont(new Font("宋体", Font.PLAIN, 23));
			graphics.drawString(batch[1], x, y += 40);
			graphics.setFont(new Font("宋体", Font.PLAIN, 23));
			graphics.drawString(batch[2], x, y += 40);
			graphics.setFont(new Font("宋体", Font.PLAIN, 22));
			graphics.drawString(batch[3], x, y += 40);
		
			//后三位编码
			graphics.setColor(Color.blue);
			graphics.setFont(new Font("微软雅黑", Font.PLAIN, 80));		
			graphics.drawString(batch[6], x+280, y-140);
			// 二维码
			graphics.drawImage(content.getScaledInstance(170,170, Image.SCALE_DEFAULT), x + 250, y -120, null);
			x += 530;
			y = n;
		}
		//释放资源
        graphics.dispose();  
	    // 保存图片
	    createImage(image, path);
	    
    }

/**
     * 将图片保存到指定位置
     * @param image 缓冲文件类
     * @param fileLocation 文件位置
     * @throws IOException 
     */
	public static  void createImage(BufferedImage image, String fileLocation) throws IOException {
			File file = new File(fileLocation);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
            }
            if (!file.exists()) { 
            	file.createNewFile();
            }
			ImageIO.write(image, "jpg", file);
	}
	
	
	 public static void main(String[] args) throws Exception {
		String contentImgPath="C:\\Users\\Administrator\\Desktop\\11.png";
		 
		String[] a = { "QPHBTJSKWSHCHBCS500X1220200918001", "山东青啤[测试]", "经典8°500*6纸箱", "天津世凯威三环厂003", contentImgPath,"2020-09-18","003" };
		myGraphicsGeneration(a, "C:\\Users\\Administrator\\Desktop\\myPic.jpg");
		
 }
	 
	 
}