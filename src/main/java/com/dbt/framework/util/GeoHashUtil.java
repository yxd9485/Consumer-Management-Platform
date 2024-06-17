package com.dbt.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * GEOHASH工具类
 * @author zhaohongtao
 *2017年8月24日
 */
public class GeoHashUtil {
	/**
	 * geohash长度与精度对比
     * 1 2500km;2 630km;3 78km;4 30km
     * 5 2.4km; 6 610m; 7 76m; 8 19m
     */
//    private static int hashLength = 8; //经纬度转化为geohash长度
    private static int latLength = 20; //纬度转化为二进制长度
    private static int lngLength = 20; //经度转化为二进制长度
    private static final double MINLAT = -90;  
    private static final double MAXLAT = 90;  
    private static final double MINLNG = -180;  
    private static final double MAXLNG = 180; 
    private static final  double EARTH_RADIUS = 6371000;//赤道半径(单位m)
    private static double minLat;//每格纬度的单位大小
    private static double minLng;//每个经度的单位大小
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', 
                '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static HashMap<Character,Integer> CHARSMAP;
     
    static {
        CHARSMAP = new HashMap<Character,Integer>();
        for (int i = 0; i < CHARS.length; i++) {
            CHARSMAP.put(CHARS[i], i);
        }
    }
	/**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei  
     * @Description: 获取经纬度的base32字符串
     */
    public static String encode(double lng,double lat) {
        boolean[] bools = getGeoBinary(lat, lng);
        if (bools == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bools.length; i = i + 5) {
            boolean[] base32 = new boolean[5];
            for (int j = 0; j < 5; j++) {
                base32[j] = bools[i + j];
            }
            char cha = getBase32Char(base32);
            if (' ' == cha) {
                return null;
            }
            sb.append(cha);
        }
        return sb.toString();
    }
    /**
     * @param geoHash
     * @return
     * @Author:lulei  
     * @Description: 返回geoHash 对应的坐标
     */
    public static double[] decode(String geoHash) {
        String geoHashBinaryStr = getGeoHashBinaryString(geoHash);
        if (geoHashBinaryStr == null) {
            return null;
        }
        StringBuffer lat = new StringBuffer();
        StringBuffer lng = new StringBuffer();
        for (int i = 0; i < geoHashBinaryStr.length(); i++) {
            if (i % 2 != 0) {
                lat.append(geoHashBinaryStr.charAt(i));
            } else {
                lng.append(geoHashBinaryStr.charAt(i));
            }
        }
        double latValue = Double.valueOf(String .format("%.6f",getGeoHashMid(lat.toString(), MINLAT, MAXLAT)));
        double lngValue = Double.valueOf(String .format("%.6f",getGeoHashMid(lng.toString(), MINLNG, MAXLNG)));
        double[] re=new double[]{lngValue,latValue};
        return re;
    }
    /**
     * @return  
     * @Description: 求所在坐标点及周围点组成的九个
     */
    public static List<String> getGeoHashBase32For9(double lon,double lat) {
    	setMinLatLng();
        double leftLat = lat - minLat;
        double rightLat = lat + minLat;
        double upLng = lon - minLng;
        double downLng = lon + minLng;
        List<String> base32For9 = new ArrayList<String>();
        //左侧从上到下 3个
        String leftUp = encode(upLng,leftLat);
        if (!(leftUp == null || "".equals(leftUp))) {
            base32For9.add(leftUp);
        }
        String leftMid = encode(lon,leftLat);
        if (!(leftMid == null || "".equals(leftMid))) {
            base32For9.add(leftMid);
        }
        String leftDown = encode(downLng,leftLat);
        if (!(leftDown == null || "".equals(leftDown))) {
            base32For9.add(leftDown);
        }
        //中间从上到下 3个
        String midUp = encode(upLng,lat);
        if (!(midUp == null || "".equals(midUp))) {
            base32For9.add(midUp);
        }
        String midMid = encode(lon,lat);
        if (!(midMid == null || "".equals(midMid))) {
            base32For9.add(midMid);
        }
        String midDown = encode(downLng,lat);
        if (!(midDown == null || "".equals(midDown))) {
            base32For9.add(midDown);
        }
        //右侧从上到下 3个
        String rightUp = encode(upLng,rightLat);
        if (!(rightUp == null || "".equals(rightUp))) {
            base32For9.add(rightUp);
        }
        String rightMid = encode(lon,rightLat);
        if (!(rightMid == null || "".equals(rightMid))) {
            base32For9.add(rightMid);
        }
        String rightDown = encode(downLng,rightLat);
        if (!(rightDown == null || "".equals(rightDown))) {
            base32For9.add(rightDown);
        }
        return base32For9;
    }
    /**
     * @Author:lulei  
     * @Description: 设置经纬度的最小单位
     */
    private static void setMinLatLng() {
        minLat = MAXLAT - MINLAT;
        for (int i = 0; i < latLength; i++) {
            minLat /= 2.0;
        }
        minLng = MAXLNG - MINLNG;
        for (int i = 0; i < lngLength; i++) {
            minLng /= 2.0;
        }
    }
    /**
     * @param binaryStr
     * @param min
     * @param max
     * @return
     * @Author:lulei  
     * @Description: 返回二进制对应的中间值
     */
    private static double getGeoHashMid(String binaryStr, double min, double max) {
        if (binaryStr == null || binaryStr.length() < 1) {
            return (min + max) / 2.0;
        }
        if (binaryStr.charAt(0) == '1') {
            return getGeoHashMid(binaryStr.substring(1), (min + max) / 2.0, max);
        } else {
            return getGeoHashMid(binaryStr.substring(1), min, (min + max) / 2.0);
        }
    } 
    /**
     * @param geoHash
     * @return
     * @Author:lulei  
     * @Description: 将geoHash转化为二进制字符串
     */
    private static String getGeoHashBinaryString(String geoHash) {
        if (geoHash == null || "".equals(geoHash)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < geoHash.length(); i++) {
            char c = geoHash.charAt(i);
            if (CHARSMAP.containsKey(c)) {
                String cStr = getBase32BinaryString(CHARSMAP.get(c));
                if (cStr != null) {
                    sb.append(cStr);
                }
            }
        }
        return sb.toString();
    }
    /**
     * @param i
     * @return
     * @Author:lulei  
     * @Description: 将数字转化为二进制字符串
     */
    private static String getBase32BinaryString(int i) {
        if (i < 0 || i > 31) {
            return null;
        }
        String str = Integer.toBinaryString(i + 32);
        return str.substring(1);
    }
    /**
     * @param base32
     * @return
     * @Author:lulei  
     * @Description: 将五位二进制转化为base32
     */
    private static char getBase32Char(boolean[] base32) {
        if (base32 == null || base32.length != 5) {
            return ' ';
        }
        int num = 0;
        for (boolean bool : base32) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return CHARS[num % CHARS.length];
    }
    /**
     * @param value
     * @param min
     * @param max
     * @return
     * @Author:lulei  
     * @Description: 将数字转化为geohash二进制字符串
     */
    private static boolean[] getHashArray(double value, double min, double max, int length) {
        if (value < min || value > max) {
            return null;
        }
        if (length < 1) {
            return null;
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            double mid = (min + max) / 2.0;
            if (value > mid) {
                result[i] = true;
                min = mid;
            } else {
                result[i] = false;
                max = mid;
            }
        }
        return result;
    }
    /**
	 * 转化为弧度(rad)
	 * */
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
    /**
	 * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
	 * @param lon1 第一点的精度
	 * @param lat1 第一点的纬度
	 * @param lon2 第二点的精度
	 * @param lat3 第二点的纬度
	 * @return 返回的距离，单位m
	 * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lon1) - rad(lon2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei  
     * @Description: 获取坐标的geo二进制字符串
     */
    private static boolean[] getGeoBinary(double lat, double lng) {
        boolean[] latArray = getHashArray(lat, MINLAT, MAXLAT, latLength);
        boolean[] lngArray = getHashArray(lng, MINLNG, MAXLNG, lngLength);
        return merge(latArray, lngArray);
    }
    /**
     * @param latArray
     * @param lngArray
     * @return
     * @Author:lulei  
     * @Description: 合并经纬度二进制
     */
    private static boolean[] merge(boolean[] latArray, boolean[] lngArray) {
        if (latArray == null || lngArray == null) {
            return null;
        }
        boolean[] result = new boolean[lngArray.length + latArray.length];
        Arrays.fill(result, false);
        for (int i = 0; i < lngArray.length; i++) {
            result[2 * i] = lngArray[i];
        }
        for (int i = 0; i < latArray.length; i++) {
            result[2 * i + 1] = latArray[i];
        }
        return result;
    }
    /**
     * 高德坐标转百度坐标
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
	    double[] bd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = gd_lon, y = gd_lat;
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
	    bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
	    bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
	    return bd_lat_lon;
	}
    /**
     * 百度坐标转高德坐标
     * @param bd_lat
     * @param bd_lon
     * @return
     */
	public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
	    double[] gd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
	    gd_lat_lon[0] = z * Math.cos(theta);
	    gd_lat_lon[1] = z * Math.sin(theta);
	    return gd_lat_lon;
	 }
    public static void main(String[] args) {
    	double lon=116.430745;//经度
    	double lat=39.909110;//纬度
    	double[] d0=gaoDeToBaidu(lon,lat);
    	System.out.println("原始坐标为:"+lon+","+lat);
//    	39.9091107806,116.4302730560
    	// 使用百度地图接口获取详细地理位置
    	Map<String, String> map = GeocodingUtil.getTrueAddress(d0[1] + "", d0[0] + "");
		System.out.println(map.get("formatted_address"));
		System.out.println(map.get("sematic_description"));
		
    	String geoHash=GeoHashUtil.encode(lon,lat);//将经纬度转换为为geohash
		System.out.println("转换后的geohash:"+geoHash);
		double[] d=GeoHashUtil.decode(geoHash);//转换geohash为经纬度
		System.out.println("geohash反查的坐标:"+d[0]+","+d[1]);
		double[] d1=gaoDeToBaidu(d[0],d[1]);
//		// 使用百度地图接口获取详细地理位置
    	Map<String, String> map1 = GeocodingUtil.getTrueAddress(d1[1] + "", d1[0] + "");
		System.out.println(map1.get("formatted_address"));
		System.out.println(map1.get("sematic_description"));
		
		
		System.out.println("两坐标相差距离"+GeoHashUtil.GetDistance(lon, lat, d[0], d[1])+"m");//获取坐标相差距离
		
		List<String> hashList=GeoHashUtil.getGeoHashBase32For9(lon,lat);//查询一个坐标geohash及周围8个坐标
		for (int i = 0; i < hashList.size(); i++) {
			System.out.println(hashList.get(i));
		}
	}
}
