package com.dbt.framework.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
  
/**
 * 项目名称：V积分V码生成工具类
 * 文件名：DpiUtil
 * 作者：jiquanwei
 * 创建时间：2014-12-06 19:26:20
 * 功能描述: 生成固定dpi工具类
 * 版   本: V 1.0
 */
public class DpiUtil {  
	
   
   private BufferedImage gridImage ;
   /**
    * 根据传入的路径生成固定dpi大小的图片
    * @param output
    * @throws IOException
    */
   public void saveGridImage(File output) throws IOException {
	    final String formatName = "png";  
	    
	    for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {  
	       ImageWriter writer = iw.next();  
	       ImageWriteParam writeParam = writer.getDefaultWriteParam();  
	       ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);  
	       IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);  
	       if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {  
	          continue;  
	       }  
	  
	       this.setDPI(metadata);  
	       gridImage = ImageIO.read(output);
	       final ImageOutputStream stream = ImageIO.createImageOutputStream(output);  
	       try {  
	          writer.setOutput(stream);  
	          writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam);  
	       } finally {  
	          stream.close();  
	       }  
	       break;  
	    }  
	 }  
	  
	 private void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
	  
	    // for PMG, it's dots per millimeter  
	    double dotsPerMilli = 1.0 * 300 / 10 / 2.54;  
	  
	    IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");  
	    horiz.setAttribute("value", Double.toString(dotsPerMilli));  
	  
	    IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");  
	    vert.setAttribute("value", Double.toString(dotsPerMilli));  
	  
	    IIOMetadataNode dim = new IIOMetadataNode("Dimension");  
	    dim.appendChild(horiz);  
	    dim.appendChild(vert);  
	  
	    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");  
	    root.appendChild(dim);  
	  
	    metadata.mergeTree("javax_imageio_1.0", root);  
	 }  
	 public static void main(String[] args) {
		 DpiUtil cfl = new DpiUtil();
		 File output  = new File("G:\\myQRCodeImage25.png");
		 try {
			cfl.saveGridImage(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}  
