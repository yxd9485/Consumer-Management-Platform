package com.dbt.vpointsshop.util.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.vpointsshop.util.PunchinImg;

import net.coobird.thumbnailator.Thumbnails;

public class GoodsShareImgType extends PunchinImg {

    @Override
    public String initImg(String goodsImg, String... tips) throws Exception {
        int bgImgW = 250, bgImgH = 250;
        
        // 获取背景图片
        BufferedImage bgImg = resizeImg(bgImgW, bgImgH, ImageIO.read(
        		new File(PropertiesUtil.getPropertyValue("goods_share_img_path") + "background.jpg")));
        
        //商品图片
        BufferedImage itemImg = ImageIO.read(new URL(PropertiesUtil.getPropertyValue("image_server_url") + goodsImg));
        Graphics2D bgGraphics = bgImg.createGraphics();
        bgGraphics.drawImage(itemImg, 25, 35, 200, 150, null);
        
        // 文字背景及文字 
        bgGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bgGraphics.setColor(Color.white);
        bgGraphics.setFont(new Font("微软雅黑",Font.BOLD,20));
        bgGraphics.drawString(tips[0], 8, 24);
        
        bgGraphics.setFont(new Font("微软雅黑",Font.BOLD,20));
        bgGraphics.drawString(tips[1], 6, 238);
        
        bgGraphics.dispose();
        
        // 保存临时文件
        String tempPath = PropertiesUtil.getPropertyValue("goods_share_img_path") + System.currentTimeMillis() + ".jpg"; 
        
        // 压缩图片
        Thumbnails.of(bgImg).scale(1f).outputQuality(0.85F).toFile(new File(tempPath));
        
        // 上传图片服务器
        File file = new File(tempPath);
//        FileItemFactory factory = new DiskFileItemFactory(16, null);
//        FileItem item=factory.createItem(file.getName(),"text/plain",true,file.getName());
//        MultipartFile multipartFile = new CommonsMultipartFile(item);
        
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        String imgUrl = HttpReq.uploadImgFile(multipartFile);
        file.delete();
        return imgUrl;
    }
    
    @Override
	public String initImg(String openid, String punchinBgImg, String nickName, String headImgUrl, String... tips)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
    public static void main(String[] args) throws Exception {
        String[] tips = {"拼单立省88%","￥200.00+9999积分"};
        PunchinImg punchinImg = new GoodsShareImgType();
        String punchinImgPath = punchinImg.initImg("D://456.png", tips);
        System.out.println(punchinImgPath);
    }
}
