package com.dbt.framework.util;

import java.io.InputStream;
import java.util.Properties;

/**	
 * 项目名称：V积分V码生成工具类
 * 文件名：CommonPropertiesUtils
 * 作者：jiquanwei
 * 创建时间：2014-06-16 13:23:20
 * 功能描述: 取得公共配置信息
 * 版   本: V 1.0
 */
public class CommonPropertiesUtils {
	
    private static Properties properties;

	// URL
    private static String url;
    
    // 密钥key
    private static String key;
    
    // 生成时间
    private static String generationTime;
    
    //生成字符码文件夹名
    private static String characterFolder;
    
    // 生成字符码文件名
    private static String characterCodeName;
    
    // 生成二维码文件夹名
    private static String qrFolder;
    
    // 生成二维码文件名
    private static String twoDimensionalCodeName;
    
    // 生成AB码文件夹名 
    private static String abFolder;
    
    // 生成AB码文件名
    private static String abCodeName;
    
    // 储存时间限制 以天为单位
    private static String storageTimeLimit;
    
    // 储存位置
    private static String storageLocation;
    
    //加密字符串
    private static String encrypt;
    
    //字符产生个数
    private static String generated;
    
    //二维码logo地址
    private static String logoPath;
    
    //实现全排列
    private static String fullArray;
    
    //微信生成二维码
    private static String weiXinFoldName;
    
    //自定义文件夹
    private static String vjifenFolder;
    
    // mecmache地址
    private static String memcache_address;
	static{
        properties = new Properties();
        InputStream inStream = CommonPropertiesUtils.class.getClassLoader().getResourceAsStream("common.properties");
        try{
            properties.load(inStream);
            url = properties.getProperty("url");
            key = properties.getProperty("key");
            generationTime = properties.getProperty("generationTime");
            characterFolder = properties.getProperty("characterFoldName");
            characterCodeName = properties.getProperty("characterCodeName");
            qrFolder = properties.getProperty("twoDimensionalFoldName");
            twoDimensionalCodeName = properties.getProperty("twoDimensionalCodeName");
            abFolder = properties.getProperty("abCodeFoldName");
            abCodeName = properties.getProperty("abCodeName");
            storageTimeLimit = properties.getProperty("storageTimeLimit");
            storageLocation = properties.getProperty("storageLocation");
            encrypt = properties.getProperty("encrypt");
            generated = properties.getProperty("generated");
            logoPath = properties.getProperty("logoPath");
            fullArray = properties.getProperty("fullArray");
            weiXinFoldName = properties.getProperty("weiXinFoldName");
            vjifenFolder = properties.getProperty("vjifenFolder");
            memcache_address = properties.getProperty("memcache_address");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		CommonPropertiesUtils.properties = properties;
	}


    public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		CommonPropertiesUtils.url = url;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		CommonPropertiesUtils.key = key;
	}

	public static String getGenerationTime() {
		return generationTime;
	}

	public static void setGenerationTime(String generationTime) {
		CommonPropertiesUtils.generationTime = generationTime;
	}

	public static String getCharacterCodeName() {
		return characterCodeName;
	}

	public static void setCharacterCodeName(String characterCodeName) {
		CommonPropertiesUtils.characterCodeName = characterCodeName;
	}

	public static String getTwoDimensionalCodeName() {
		return twoDimensionalCodeName;
	}

	public static void setTwoDimensionalCodeName(String twoDimensionalCodeName) {
		CommonPropertiesUtils.twoDimensionalCodeName = twoDimensionalCodeName;
	}

	public static String getAbCodeName() {
		return abCodeName;
	}

	public static void setAbCodeName(String abCodeName) {
		CommonPropertiesUtils.abCodeName = abCodeName;
	}

	public static String getStorageTimeLimit() {
		return storageTimeLimit;
	}

	public static void setStorageTimeLimit(String storageTimeLimit) {
		CommonPropertiesUtils.storageTimeLimit = storageTimeLimit;
	}

	public static String getStorageLocation() {
		return storageLocation;
	}

	public static void setStorageLocation(String storageLocation) {
		CommonPropertiesUtils.storageLocation = storageLocation;
	}

	public static String getCharacterFolder() {
		return characterFolder;
	}

	public static void setCharacterFolder(String characterFolder) {
		CommonPropertiesUtils.characterFolder = characterFolder;
	}

	public static String getQrFolder() {
		return qrFolder;
	}

	public static void setQrFolder(String qrFolder) {
		CommonPropertiesUtils.qrFolder = qrFolder;
	}

	public static String getAbFolder() {
		return abFolder;
	}

	public static void setAbFolder(String abFolder) {
		CommonPropertiesUtils.abFolder = abFolder;
	}

	public static String getEncrypt() {
		return encrypt;
	}

	public static void setEncrypt(String encrypt) {
		CommonPropertiesUtils.encrypt = encrypt;
	}

	public static String getGenerated() {
		return generated;
	}

	public static void setGenerated(String generated) {
		CommonPropertiesUtils.generated = generated;
	}

	public static String getLogoPath() {
		return logoPath;
	}

	public static void setLogoPath(String logoPath) {
		CommonPropertiesUtils.logoPath = logoPath;
	}

	public static String getFullArray() {
		return fullArray;
	}

	public static void setFullArray(String fullArray) {
		CommonPropertiesUtils.fullArray = fullArray;
	}
	
	public static String getWeiXinFoldName() {
		return weiXinFoldName;
	}

	public static void setWeiXinFoldName(String weiXinFoldName) {
		CommonPropertiesUtils.weiXinFoldName = weiXinFoldName;
	}
	
	public static String getVjifenFolder() {
		return vjifenFolder;
	}

	public static void setVjifenFolder(String vjifenFolder) {
		CommonPropertiesUtils.vjifenFolder = vjifenFolder;
	}
	
	public static String getMemcache_address() {
		return memcache_address;
	}

	public static void setMemcache_address(String memcacheAddress) {
		memcache_address = memcacheAddress;
	}

	public static void main(String[] args) {
        System.out.println("url    " + CommonPropertiesUtils.url);
        System.out.println("密钥key    " + CommonPropertiesUtils.key);
        System.out.println("生成时间    " + CommonPropertiesUtils.generationTime);
        System.out.println("生成字符码文件夹名    " + CommonPropertiesUtils.characterFolder);
        System.out.println("生成字符码文件名    " + CommonPropertiesUtils.characterCodeName);
        System.out.println("生成二维码文件夹名     " + CommonPropertiesUtils.qrFolder);
        System.out.println("生成二维码文件名     " + CommonPropertiesUtils.twoDimensionalCodeName);
        System.out.println("生成AB码文件夹名    " + CommonPropertiesUtils.abFolder);
        System.out.println("生成AB码文件名    " + CommonPropertiesUtils.abCodeName);
        System.out.println("储存时间限制 以天为单位    " + CommonPropertiesUtils.storageTimeLimit);
        System.out.println("储存位置    " + CommonPropertiesUtils.storageLocation);
        System.out.println("加密字符串    " + CommonPropertiesUtils.encrypt);
        System.out.println("字符产生个数    " + CommonPropertiesUtils.generated);
        System.out.println("二维码logo地址    " + CommonPropertiesUtils.logoPath);
        System.out.println("实现全排列    " + CommonPropertiesUtils.fullArray);
        System.out.println("微信生成二维码    " + CommonPropertiesUtils.weiXinFoldName);
        System.out.println("自定义文件夹    " + CommonPropertiesUtils.vjifenFolder);
    }
}