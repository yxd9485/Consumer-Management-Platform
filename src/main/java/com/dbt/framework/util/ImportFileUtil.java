/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午4:32:20 by hanshimeng create
 * </pre>
 */
package com.dbt.framework.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportFileUtil {

	/**
	 * 压缩包解压后保存硬盘
	 * @param inputStreamFile 压缩包文件流</br>
	 * @param path 保存文件路径</br>  
	 * @return List<String> 文件名称列表 </br>
	 */
	public static List<String> unZipSave(InputStream fileInputStream, String path) throws Exception{
		List<String> fileNameList = new ArrayList<String>();
        InputStreamFactory inputStreamFactory = new InputStreamFactory(fileInputStream);
        ZipInputStream Zin=new ZipInputStream(inputStreamFactory.newInputStream(), Charset.forName("GBK"));//源zip
		 BufferedInputStream Bin=new BufferedInputStream(Zin);
         File Fout=null;
         ZipEntry entry;
         try {  
             while((entry = Zin.getNextEntry())!=null){
             	if(!entry.isDirectory()){
             		 String fileName = entry.getName();
             		 Fout=new File(path,fileName);  
                      if(!Fout.exists()){  
                          (new File(Fout.getParent())).mkdirs();  
                      }  
                      FileOutputStream out=new FileOutputStream(Fout);  
                      BufferedOutputStream Bout=new BufferedOutputStream(out);  
                      int b;  
                      while((b=Bin.read())!=-1){  
                          Bout.write(b);  
                      }  
                      fileNameList.add(fileName);
                      Bout.close();  
                      out.close();  
                      System.out.println(Fout+"解压并保存文件成功");  
             	}
             }  
         } catch (IOException e) {  
        	 throw e;
         }
         catch (IllegalArgumentException e){
             return unZipSaveUTF8(inputStreamFactory.newInputStream(),path);
         }finally{
        	 try {
				Bin.close();
				Zin.close();
			} catch (IOException e) {
				throw e;
			}  
         } 
         return fileNameList;
     }


    public static List<String> unZipSaveUTF8(InputStream fileInputStream, String path) throws Exception{
        List<String> fileNameList = new ArrayList<String>();
        ZipInputStream Zin=new ZipInputStream(fileInputStream);//源zip,默认UTF-8
        BufferedInputStream Bin=new BufferedInputStream(Zin);
        File Fout=null;
        ZipEntry entry;

        try {
            while((entry = Zin.getNextEntry())!=null){
                if(!entry.isDirectory()){
                    String fileName = entry.getName();
                    Fout=new File(path,fileName);
                    if(!Fout.exists()){
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out=new FileOutputStream(Fout);
                    BufferedOutputStream Bout=new BufferedOutputStream(out);
                    int b;
                    while((b=Bin.read())!=-1){
                        Bout.write(b);
                    }
                    fileNameList.add(fileName);
                    Bout.close();
                    out.close();
                    System.out.println(Fout+"解压并保存文件成功");
                }
            }
        } catch (IOException e) {
            throw e;
        }finally{
            try {
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                throw e;
            }
        }
        return fileNameList;
    }


    public static class InputStreamFactory {

        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private byte[]                buffer                = new byte[1024];

        public InputStreamFactory(InputStream input) throws IOException {
            int len;
            while ((len = input.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        }

        public InputStream newInputStream() {
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }

    }

}
