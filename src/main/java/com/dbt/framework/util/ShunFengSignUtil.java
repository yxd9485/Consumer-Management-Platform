package com.dbt.framework.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * 顺丰同城api接口-签名
 *  https://openic.sf-express.com/#/homepage
 */
public class ShunFengSignUtil {
    public static String generateOpenSign(String postData, Long appId, String appKey) throws Exception {
        StringBuilder sb =  new StringBuilder();
        sb.append(postData);
        sb.append("&" + appId + "&" + appKey);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5 = md.digest(sb.toString().getBytes("utf-8"));
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < md5.length; offset++) {
            i = md5[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return Base64.encodeBase64String(buf.toString().getBytes("utf-8"));
    }
}
