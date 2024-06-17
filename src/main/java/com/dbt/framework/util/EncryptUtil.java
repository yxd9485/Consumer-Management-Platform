package com.dbt.framework.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * @author RoyFu
 * @createTime 2016年3月30日 上午9:48:06
 * @description
 */

public class EncryptUtil {

	/**
	 * no need to initialize the tool
	 */
	private EncryptUtil(){
		
	}
	
	public static EncryptUtil getInstance(){
		return new EncryptUtil();
	}
	
	public static String[] codeArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n",
        "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", 
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	public static int codeLength = 4;
	
	/**
	 * 创建验证码
	 * @return
	 */
	public static String createValidCode(){
		StringBuffer code = new StringBuffer();
		
		int length = codeArray.length;
		for (int i = 0; i < codeLength; i++) {
			int charNum = (int) Math.floor(Math.random() * 40);
			if(charNum > length - 1) {
				charNum = (int) Math.floor(Math.random() * 40);
			}
			code.append(codeArray[charNum]);
		}
		return code.toString();
	}
	
	/**
	 * <pre>
	 * 通过MD5加密算法.
	 * </pre>
	 * 
	 * @param pstrPassword 密码
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @author 张涛 2011-4-25
	 */
	public static String encrypt(String pstrPassword) throws NoSuchAlgorithmException {
		byte[] bUnencodedPassword = pstrPassword.getBytes();

		MessageDigest objmd = null;

		try {
			objmd = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		objmd.reset();
		objmd.update(bUnencodedPassword);
		byte[] bEncodedPassword = objmd.digest();
		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < bEncodedPassword.length; i++) {
			if ((bEncodedPassword[i] & 0xff) < 0x10) {
				strBuf.append("0");
			}
			strBuf.append(Long.toString(bEncodedPassword[i] & 0xff, 16));
		}

		return strBuf.toString().toUpperCase();
	}
	
	public static String encode(byte[] binaryData) throws UnsupportedEncodingException {
		return new String(Base64.encode(binaryData));
	}
	
	public static String decode(byte[] binaryData) throws UnsupportedEncodingException {
		return new String(Base64.decode(binaryData));
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

	    String pwd = "vjifen";
	    System.out.println(pwd + " " + encode(encrypt(pwd).getBytes()));
	    
	    for (int i = 0; i < 30; i++) {
	        String str = RandomStringUtils.random(9, StringUtils.join(codeArray).replace(",", "").toCharArray()).toUpperCase();
	        System.out.println(str + " " + encode(encrypt(str).getBytes()));
        }
	}
}
