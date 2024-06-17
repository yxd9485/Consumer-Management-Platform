package com.dbt.framework.util;

import com.dbt.datasource.util.DbContextHolder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class PosterUtil {

    // public final static String punchinImgPath = "D:/data/";
    public final static String punchinImgPath = "D:/data/";
    public final static String posterPath = "/data/upload/poster/" + DbContextHolder.getDBType() + "/";
    public final static String defaultHeadImg = posterPath + "bgImg/headImg.png";

    /**
     * 缩放图片
     */
    public static BufferedImage resizeImg(int width, int height, BufferedImage img) {
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        return buffImg;
    }

    /**
     * 
     * @param g
     * @param text
     *            文本
     * @param lineHeight
     *            行高
     * @param maxWidth
     *            行宽
     * @param maxLine
     *            最大行数
     * @param left
     *            左边距
     * @param top
     *            上边距
     */
    public static void drawString(Graphics2D g, String text, float lineHeight, float maxWidth, int maxLine, float left,
            float top) {
        if (text == null || text.length() == 0)
            return;

        FontMetrics fm = g.getFontMetrics();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(c);
            int stringWidth = fm.stringWidth(sb.toString());
            if (c == '\n' || stringWidth > maxWidth) {
                if (c == '\n') {
                    i += 1;
                }
                if (maxLine > 1) {
                    g.drawString(text.substring(0, i), left, top);
                    drawString(g, text.substring(i), lineHeight, maxWidth, maxLine - 1, left, top + lineHeight);
                } else {
                    // g.drawString(text.substring(0, i - 1) + "…", left, top);
                    g.drawString(text.substring(0, i), left, top);
                }
                return;
            }
        }
        g.drawString(text, left, top);
    }

    /**
     * 
     * @param g
     * @param text
     *            文本
     * @param lineHeight
     *            行高
     * @param maxHeight
     *            行宽
     * @param maxLine
     *            最大行数
     * @param left
     *            左边距
     * @param top
     *            上边距
     */
    public static void drawVString(Graphics2D g, String text, float lineHeight, float columnWidth, int maxLine,
            int maxColumn, float left, float top) {
        if (text == null || text.length() == 0 || maxColumn == 0)
            return;

        String item = "";
        for (int i = 0; i < text.length(); i++) {
            item = String.valueOf(text.charAt(i));
            if (item == "\n" || i >= maxLine) {
                if (item == "\n") {
                    i += 1;
                }
                drawVString(g, text.substring(i), lineHeight, columnWidth, maxLine, maxColumn - 1, left + columnWidth,
                        top);
                break;
            } else {
                g.drawString(item, left, top + i * lineHeight);
            }
        }
    }
    /**
     * url 转 Ffile
     * */
    public static File UrlToFile(String url){

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            file = File.createTempFile("net_url", UUID.randomUUID().toString());
            URL urlFile = new URL(url);
            inputStream = urlFile.openStream();
            fileOutputStream = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    /**
     * #FFFFFF 转成 ARGB
     * 
     * @param str
     * @return
     */
    public static Color toRGB(String colorStr) {
        String R = colorStr.substring(1, 3);
        String G = colorStr.substring(3, 5);
        String B = colorStr.substring(5, 7);

        int red = Integer.parseInt(R, 16);
        int green = Integer.parseInt(G, 16);
        int blue = Integer.parseInt(B, 16);
        return new Color(red, green, blue);
    }
}
