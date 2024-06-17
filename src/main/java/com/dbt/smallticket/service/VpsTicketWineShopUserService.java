package com.dbt.smallticket.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.*;
import com.dbt.framework.util.HttpReq.PATH_TYPE;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 酒行老板注册信息Service
 */
@Service
public class VpsTicketWineShopUserService {
    
    private Logger log = Logger.getLogger(this.getClass());

    
    /**
     * 生成酒行海报
     * @param wineShopMap
     * @throws IOException 
     */
    public String initWineShopPoster(Map<String, String> wineShopMap) throws IOException {
        if (wineShopMap == null) return null;
        
        // 海报尺寸
        int bgImgW = 690, bgImgH = 1748;
        
        // 获取背景图片
        BufferedImage bgImg = PosterUtil.resizeImg(bgImgW, bgImgH, ImageIO.read(new File(PosterUtil.posterPath + "bgImg/wineShopUser.png")));
        Graphics2D bgGraphics = bgImg.createGraphics();
        
        // 门店信息
        bgGraphics.setColor(PosterUtil.toRGB("#FFF0C6"));
        bgGraphics.setFont(new Font("宋体",Font.PLAIN + Font.BOLD, 22));
        PosterUtil.drawString(bgGraphics, "门店名称：" + wineShopMap.get("wineShopName"), 36, 510, 1, 92, 1298);
        PosterUtil.drawString(bgGraphics, "订购热线：" + wineShopMap.get("hotPhoneNum"), 36, 510, 1, 92, 1334);
        PosterUtil.drawString(bgGraphics, "门店地址：" + wineShopMap.get("wineShopAddress"), 36, 510, 2, 92, 1368);

        // 生成二维码 
        String qrcodeContent = "http://vj1.tv/zlchangchenghui/ecjx/" + wineShopMap.get("infoKey");
        String qrcodeImageUrl = PosterUtil.posterPath + "poster/" + wineShopMap.get("infoKey") + "_qrcode.png";
        QrCodeUtil.encodeQRCodeImage(qrcodeContent, null, qrcodeImageUrl, 144, 144, null);
        File qrcodeFile = new File(qrcodeImageUrl);
        DpiUtil dpiUtil = new DpiUtil();
        dpiUtil.saveGridImage(qrcodeFile);
        
        // 二维码图片
        BufferedImage qrcodeImg = ImageIO.read(qrcodeFile);
        bgGraphics.drawImage(qrcodeImg, 280, 1448, 144, 144, null);
        FileUtils.deleteQuietly(qrcodeFile);
        
        // 释放资源
        bgGraphics.dispose();
        
        // 压缩图片
        String posterUrl = PosterUtil.posterPath + "/poster/" + wineShopMap.get("infoKey") + ".jpg";
        Thumbnails.of(bgImg).scale(1f).outputQuality(0.5F).toFile(new File(posterUrl));
        
        // 上传图片服务器
        File file = new File(posterUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        String imgUrl = HttpReq.uploadImgFile(multipartFile, PATH_TYPE.IPURL);
        
        // 删除临时图片
        file.delete();
        qrcodeFile.delete();
        
        return imgUrl;
    }
    
    /**
     * 初始化大奖海报
     * 
     * @param wineShopUser
     * @param prizeRecord
     * @return
     * @throws IOException 
     */
    public String initPrizePorster(Map<String, String> wineShopMap) throws IOException {
        
        if (wineShopMap == null) return null;
        
        // 海报尺寸
        int bgImgW = 690, bgImgH = 1573;
        
        // 获取背景图片
        BufferedImage bgImg = PosterUtil.resizeImg(bgImgW, bgImgH, ImageIO.read(new File(PosterUtil.posterPath + "bgImg/wineShopPrize.png")));
        Graphics2D bgGraphics = bgImg.createGraphics();
        
        // 中奖信息
        bgGraphics.setColor(PosterUtil.toRGB("#FDEBBD"));
        bgGraphics.setFont(new Font("宋体",Font.PLAIN + Font.BOLD, 28));
        FontMetrics fm = bgGraphics.getFontMetrics();
        int fontW = fm.stringWidth(wineShopMap.get("wineShopName"));
        int left = fontW > 510 ? 70 : (510 - fontW) / 2 + 100;
        PosterUtil.drawString(bgGraphics, wineShopMap.get("wineShopName"), 60, 510, 1, left, 590);
        String prizeDesc = "";
        if (StringUtils.isNotBlank(wineShopMap.get("prizeCity"))) {
            prizeDesc += wineShopMap.get("prizeCity");
        }
        if (StringUtils.isNotBlank(wineShopMap.get("userPhoneNum"))) {
            prizeDesc = prizeDesc + " " + wineShopMap.get("userPhoneNum").substring(0, 3) + "****" + wineShopMap.get("userPhoneNum").substring(7);
        }
        prizeDesc += " 消费者中出大奖";
        fontW = fm.stringWidth(prizeDesc);
        left = fontW > 510 ? 70 : (510 - fontW) / 2 + 100;
        PosterUtil.drawString(bgGraphics, prizeDesc, 60, 510, 1, left, 630);
        
        // 门店信息
        bgGraphics.setColor(PosterUtil.toRGB("#FFF0C6"));
        bgGraphics.setFont(new Font("宋体",Font.PLAIN + Font.BOLD, 22));
        PosterUtil.drawString(bgGraphics, "门店名称：" + wineShopMap.get("wineShopName"), 36, 510, 1, 100, 1118);
        PosterUtil.drawString(bgGraphics, "订购热线：" + wineShopMap.get("hotPhoneNum"), 36, 510, 1, 100, 1156);
        PosterUtil.drawString(bgGraphics, "门店地址：" + wineShopMap.get("wineShopAddress"), 36, 510, 2, 100, 1192);

        // 生成二维码 
        String qrcodeContent = "http://vj1.tv/zlchangchenghui/ecjx/" + wineShopMap.get("infoKey");
        String qrcodeImageUrl = PosterUtil.posterPath + "poster/" + wineShopMap.get("infoKey") + "_qrcode.png";
        QrCodeUtil.encodeQRCodeImage(qrcodeContent, null, qrcodeImageUrl, 144, 144, null);
        File qrcodeFile = new File(qrcodeImageUrl);
        DpiUtil dpiUtil = new DpiUtil();
        dpiUtil.saveGridImage(qrcodeFile);
        
        // 二维码图片
        BufferedImage qrcodeImg = ImageIO.read(qrcodeFile);
        bgGraphics.drawImage(qrcodeImg, 280, 1272, 144, 144, null);
        FileUtils.deleteQuietly(qrcodeFile);
        
        // 释放资源
        bgGraphics.dispose();
        
        // 压缩图片
        String posterUrl = PosterUtil.posterPath + "/poster/" + wineShopMap.get("prizeInfoKey") + ".jpg";
        Thumbnails.of(bgImg).scale(1f).outputQuality(0.5F).toFile(new File(posterUrl));
        
        // 上传图片服务器
        File file = new File(posterUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        String imgUrl = HttpReq.uploadImgFile(multipartFile, PATH_TYPE.IPURL);
        
        // 删除临时图片
        file.delete();
        qrcodeFile.delete();
        
        return imgUrl;
    }

    public static void main(String[] args) {
        VpsTicketWineShopUserService ts = new VpsTicketWineShopUserService();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("shareOpenid", "shareOpenid");
            map.put("oyshoKey", "oyshoKey");
            map.put("bottomAddress", "加压查勘阿斯蒂芬阿斯蒂芬加加压查勘阿斯蒂芬阿斯蒂芬加加压查勘阿斯蒂芬阿斯蒂芬加");
            map.put("ticketChannel", "1");
            map.put("headPicture", "https://img.vjifen.com/images/vma/online/20220112/zhongliangCNY/vjf20220112165217807.png");
            String s = ts.initZlCNYPorster(map);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String initZlCNYPorster(Map<String, String> requestMap) throws IOException, URISyntaxException {
        if (requestMap == null) return null;
        //内容图片
        String shareOpenid = requestMap.get("shareOpenid");
        String oyshoKey = requestMap.get("oyshoKey");
        String contentImgPath = requestMap.get("headPicture");
        String bottomAddress = requestMap.get("bottomAddress");
        String code = requestMap.get("code");

        String path = PosterUtil.posterPath;
        String juxingyuanjiao = path + "bgImg/zlccCNY/yuanjiaoBG.png";
        String qrcodeBg = path + "bgImg/zlccCNY/qrcodeBG.png";
        // 海报尺寸
        int bgImgW = 615, bgImgH = 1512;
        File contentFile = PosterUtil.UrlToFile(contentImgPath);
        BufferedImage content = ImageIO.read(contentFile);
        BufferedImage bufferedImage = modifyImageScale(content, 497);
        int promotionUserImgH = bufferedImage.getHeight();
        BufferedImage writed = new BufferedImage(615, 1512+promotionUserImgH-497, 1);
        // 获取背景图片
        BufferedImage bgImg = PosterUtil.resizeImg(bgImgW, bgImgH, ImageIO.read(new File(path + "bgImg/zlccCNY/zlcccnyBG.png")));
        Graphics2D graphics = writed.createGraphics();
        int baseLine = 855;
        graphics.drawImage(bgImg, 0, 0, bgImgW, bgImgH, null);
        
        // 中间延展背景
        BufferedImage itemImg = bgImg.getSubimage(0, bgImgH - 2, bgImgW, 1); 
        graphics.drawImage(itemImg, 0, bgImgH - 1, bgImgW, writed.getHeight() - bgImgH + 1, null);
        
        //中间图片背景板
        graphics.setColor(new Color(247, 230, 196));
        graphics.fillRect(57,baseLine,502,promotionUserImgH+6);

        //添加内容图片
        graphics.drawImage(bufferedImage, 60, baseLine + 3, null);
        
        //编号背景
        BufferedImage juxingyuanjiaoImage = ImageIO.read(new File(juxingyuanjiao));
        graphics.drawImage(juxingyuanjiaoImage.getScaledInstance(122, 50, Image.SCALE_DEFAULT), 60, baseLine + 3, null);
        //写编号
        //消除文字锯齿
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(new Font("宋体",Font.PLAIN + Font.BOLD, 24));
        graphics.setColor(new Color(112, 35, 33));
        PosterUtil.drawString(graphics, code+"号", 24, 90, 24, 76, baseLine + 32);
        
        // 餐饮角标
        if (Constant.ticketChannel.CHANNEL1.equals(requestMap.get("ticketChannel"))) {
            BufferedImage tipsImg = ImageIO.read(new File(path + "bgImg/zlccCNY/tips_canyin.png"));
            graphics.drawImage(tipsImg, bgImgW - tipsImg.getWidth() - 58, baseLine + 3, null);
        }
        
        //二维码背景板
        File qrcodeBgFile = new File(qrcodeBg);
        BufferedImage qrcodeBgImage = ImageIO.read(qrcodeBgFile);
        graphics.drawImage(qrcodeBgImage.getScaledInstance(154, 189, Image.SCALE_DEFAULT), 403, baseLine + promotionUserImgH -184, null);

        //写图片右下角二维码 http://x.vjifen.com/v/tp/d=shareOpenid&k=oyshoKey
        String propertyValue = PropertiesUtil.getPropertyValue("zlcc_cny_porster_share_path");
        String qrcodeContent = propertyValue+"d="+shareOpenid+"&k="+oyshoKey;
        String qrcodeImageUrl = path + "poster/"+shareOpenid+"_qrcode.png";
        QrCodeUtil.encodeQRCodeImage(qrcodeContent, null, qrcodeImageUrl, 144, 144, null);
        File qrcodeFile = new File(qrcodeImageUrl);
        BufferedImage qrcodeImage = ImageIO.read(qrcodeFile);
        graphics.drawImage(qrcodeImage.getScaledInstance(144, 144, Image.SCALE_DEFAULT), 410, baseLine + promotionUserImgH-170, null);


        //写底部文字内容
        graphics.setFont(new Font("宋体",Font.PLAIN + Font.BOLD, 24));
        graphics.setColor(new Color(51, 51, 51));
        PosterUtil.drawString(graphics, bottomAddress, 30, 469, 53, 73, baseLine + promotionUserImgH+45);
        // 完成模板修改
//        bgGraphics.dispose();
        graphics.dispose();

        // 压缩图片
        String posterUrl = path + "/poster/" + UUID.randomUUID()+"_"+shareOpenid + ".jpeg";
        Thumbnails.of(writed).scale(1f).outputQuality(0.8F).toFile(new File(posterUrl));

//         上传图片服务器
        File file = new File(posterUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        String imgUrl = HttpReq.uploadImgFile(multipartFile, PATH_TYPE.IPURL);

//         删除临时图片
        file.delete();
        qrcodeFile.delete();
        return imgUrl;
    }
    public Object initGiftCardPorster(Map<String, String> requestMap) throws IOException {
        if (requestMap == null){
            return null;
        }
        // 海报尺寸
        int bgImgW = 1004, bgImgH = 804;

        String path = PosterUtil.posterPath;
        // 获取背景图片
        BufferedImage background = PosterUtil.resizeImg(bgImgW, bgImgH, ImageIO.read(new File(path + "giftCard/background.png")));
        File contentFile = PosterUtil.UrlToFile(requestMap.get("goodsImageUrl"));
        BufferedImage content = ImageIO.read(contentFile);
        Graphics2D bgGraphics = background.createGraphics();
        //消除文字锯齿
        bgGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        bgGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画商品
        bgGraphics.drawImage(content, 42, 40, 556, 636,null );
        // 门店信息

        bgGraphics.setColor(new Color(252, 241, 203));
        bgGraphics.setFont(new Font("宋体",Font.PLAIN+Font.BOLD, 52));
        PosterUtil.drawString(bgGraphics,requestMap.get("exchangeNum")+"件",52,144,1,278,730);
        // 释放资源
        bgGraphics.dispose();

        // 压缩图片
        String posterUrl = path + "/poster/" + requestMap.get("exchangeId") + "giftcard.jpg";
        Thumbnails.of(background).scale(1f).outputQuality(0.5F).toFile(new File(posterUrl));

        // 上传图片服务器
        File file = new File(posterUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        String imgUrl = HttpReq.uploadImgFile(multipartFile, PATH_TYPE.IPURL);
        // 删除临时图片
        file.delete();
        return imgUrl;
    }
    /**
     *
     * @param mini 贴图
     * @param Scale 缩放宽度
     * @return
     */
    public BufferedImage modifyImageScale(BufferedImage mini,double width) {
        double scale =  width/mini.getWidth() ;

        int w = (int)Math.round(mini.getWidth()*scale);
        int h = (int)Math.round(mini.getHeight()*scale);
        //设置生成图片宽*高，色彩
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //创建画布
        Graphics2D g2 = bi.createGraphics();
        //设置图片透明  注********：只有png格式的图片才能设置背景透明，jpg设置图片颜色变的乱七八糟
        bi = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        //重新创建画布
        g2 = bi.createGraphics();
        //画图
        g2.drawImage(mini, 0,0,w,h, null);
        return bi;
    }
    // 合并图片
    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d,int x,int y) {

        try {
            int w = b.getWidth();
            int h = b.getHeight();
            Graphics2D g = d.createGraphics();
            g.drawImage(b, x, y, w, h, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return d;
    }

}
