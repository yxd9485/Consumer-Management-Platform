package com.dbt.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.dbt.framework.base.bean.Constant;

public class ImgUploadUtil {
	/**
     * 图片上传
     *
     * @param file
     * @return
     * @throws IOException
     * @throws BaseException
     */
    public static Map<String, Object> uploadFile(MultipartFile file)
            throws IOException {
        String fail = "uploadFail";//上传失败状态
        String success = "uploadSuccess";//上传成功状态
        String errorMsg = "uploadError";//上传错误信息
        String returnPath="/upload/";
        //String filePath = Constant.filePath+"upload/";//上传路径，本来是相对路径，但是在发布后路径会创建在tomcat的bin目录下，所以接口上传的路径是一个难题，我用的是绝对路径，等到发布到Linux环境中，通过配置文件修改路径为Linux中的资源地址【防止工程删除而资源文件不会丢失】，然后重新发布工程时再通过Linux的命令把我们需要的资源文件导入到我们发布的工程项目中。
        /**
         * tomcat server.xml <HOST>
         * 添加下面一条信息 配置虚拟路径，将/DBTVMTSPlatform/upload 映射到服务器 /data/upload下
         * <Context path="/DBTHNQPPlatform/upload" docBase="/data/upload" reloadable="true"></Context>
         */
        String filePath="/data/upload/";
        String size = "10";
         
        long maxFileSize = (Integer.valueOf(size)) * 1024 * 1024;
        String suffix = file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf("."));
        long fileSize = file.getSize();
        Map<String, Object> map = new HashMap<String, Object>();
        if (suffix.equals(".png") || suffix.equals(".jpg")||suffix.equals(".bmp")||suffix.equals(".jpeg")) {
            if (fileSize < maxFileSize) {
                // System.out.println("fileSize"+fileSize);
                String fileName = file.getOriginalFilename();
                fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
                String saveName=new Date().getTime()+suffix;
                File tempFile = new File(filePath, saveName);
                try {
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();//如果是多级文件使用mkdirs();如果就一层级的话，可以使用mkdir()
                    }
                    if (!tempFile.exists()) { 
                        tempFile.createNewFile();
                    }
                    file.transferTo(tempFile);
                } catch (IllegalStateException e) {
                    throw e;
                }
 
                map.put("SUCESS", success);
                map.put("data",  returnPath+saveName);
 
            } else {
                map.put("SUCESS", fail);
                map.put("data", "Image too big");
            }
 
        } else {
            map.put("SUCESS", fail);
            map.put("data", "Image format error");
        }
        return map;
    }
}
