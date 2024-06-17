package com.dbt.vpointsshop.util;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.DateUtil;

public abstract class PunchinImg {
    
    private final static Logger log = LoggerFactory.getLogger(PunchinImg.class);
    
    protected final static String punchinImgPath = "/data/upload/punchin/";
    protected final static String defaultHeadImg = punchinImgPath + "bgImg/headImg.png";

    /**
     * 合成图片
     * 
     * @param punchinBgImg  背景图片
     * @param tips          提示消息
     * @return  合成图片的保存路径
     * @throws Exception
     */
    public abstract String initImg(String punchinBgImg, String... tips) throws Exception;
    
    /**
     * 合成图片
     * 
     * @param openid        用户昵称
     * @param punchinBgImg  背景图片
     * @param nickName      用户昵称
     * @param headImgUrl    用户头像
     * @param tips          提示消息
     * @return  合成图片的保存路径
     * @throws Exception
     */
    public abstract String initImg(String openid, String punchinBgImg,
            String nickName, String headImgUrl, String... tips) throws Exception;
    
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
     * @param text 文本
     * @param lineHeight 行高
     * @param maxWidth 行宽
     * @param maxLine 最大行数
     * @param left 左边距
     * @param top 上边距
     */
    public void drawString(Graphics2D g, String text, 
            float lineHeight, float maxWidth, int maxLine, float left,float top) {
        if (text == null || text.length() == 0) return;
        
        FontMetrics fm = g.getFontMetrics();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(c);
            int stringWidth = fm.stringWidth(sb.toString());
            if (c == '\n' || stringWidth > maxWidth) {
                if(c == '\n') {
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
     * @param text 文本
     * @param lineHeight 行高
     * @param maxHeight 行宽
     * @param maxLine 最大行数
     * @param left 左边距
     * @param top 上边距
     */
    public void drawVString(Graphics2D g, String text, 
            float lineHeight, float columnWidth, int maxLine, int maxColumn, float left,float top) {
        if (text == null || text.length() == 0 || maxColumn == 0) return;

        String item = "";
        for (int i = 0; i < text.length(); i++) {
            item = String.valueOf(text.charAt(i));
            if (item == "\n" || i >= maxLine) {
                if(item == "\n") {
                    i += 1;
                }
                drawVString(g, text.substring(i), lineHeight, columnWidth, maxLine, maxColumn - 1, left + columnWidth, top);
                break;
            } else {
                g.drawString(item, left, top + i * lineHeight );
            }
        }
    }
    
    /**
     * 随机获取当天打卡的背景图片
     * @return
     */
    public static String randomBgImg() {
        
        // 背景图片
        File bgImgFile = null;

        // 获取当前日期
        String currDate = DateUtil.getDateTime("yyyyMMdd");
        
        List<File> imgFileLst = new ArrayList<>();
        File bgPathDir = new File(punchinImgPath + "bgImg/");
        if (bgPathDir.isDirectory() && bgPathDir.exists()) {
            for (File file : bgPathDir.listFiles()) {
                if (file.isFile() && (file.getName().toLowerCase()
                        .endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg"))) {
                    // 当天图片
                    if (file.getName().startsWith(currDate)) {
                        bgImgFile = file;
                        break;
                        
                        // 非特定日期时：!(20190815*.png)
                    } else if (!file.getName().equals("headImg.png") 
                            && !(file.getName().startsWith("20") && file.getName().length() >= 12)){
                        imgFileLst.add(file);
                    }
                }
            }
        }
        if (bgImgFile == null && !imgFileLst.isEmpty()) {
            bgImgFile = imgFileLst.get(RandomUtils.nextInt(imgFileLst.size()));
            imgFileLst.clear();
        }
        String bgImgPath = "";
        if(bgImgFile != null) {
            bgImgPath = bgImgFile.getPath();
        } else {
            log.warn("未获取到打卡背景图片，请检查打卡背景图片配置：" + punchinImgPath + "bgImg/");
        }
        return bgImgPath;
    }
}
