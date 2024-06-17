package com.dbt.framework.util;
/**
 * ASC码转换工具
 * @author hanshimeng
 *
 */
public class AscUtil {

	/**
     * 字符转ASC
     * 
     * @param st
     * @return
     */
    public static int getAsc(String st) {
        byte[] gc = st.getBytes();
        return (int) gc[0];
    }

    /**
     * ASC转字符
     * 
     * @param backnum
     * @return
     */
    public static char backchar(int backnum) {
        return (char) backnum;
    }
}
