package com.dbt.framework.util;
/**
 * GPS坐标、火星坐标(谷歌、高德)、百度坐标  互转********
 * 
 *  |--gps坐标:gps卫星获取的原始坐标--
 *  |--火星坐标:国内将gps坐标加密后的坐标--
 *  |--百度坐标：火星坐标加密后的坐标--|
 * @author zhaohongtao
 *2017年5月15日
 */
public class GeoChange {
	static double pi = 3.14159265358979324;  
    static double a = 6378245.0;  
    static double ee = 0.00669342162296594323;  
    /**
     * gps转火星坐标
     * @param ggLon
     * @param ggLat
     * @return
     */
	public static double[] gpsToHx(double ggLon,double ggLat){
        double mgLat;  
        double mgLon;  
        if (outOfChina(ggLat, ggLon))  
        {  
            return new double[]{ggLon,ggLat};
        }  
        double dLat = transformLat((ggLon - 105.0), (ggLat - 35.0));  
        double dLon = transformLon((ggLon - 105.0), (ggLat - 35.0));  
        double radLat = ggLat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        mgLat = ggLat + dLat;  
        mgLon = ggLon + dLon;  
        return new double[]{mgLon,mgLat};
	}
	/**
	 * 火星坐标转百度坐标
	 * @param ggLat
	 * @param ggLon
	 * @return
	 */
	public static double[] hxToBd(double hxLon,double hxLat)  
    {  
        double x = hxLon, y = hxLat;  
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);  
        return new double[]{z * Math.cos(theta) + 0.0065,z * Math.sin(theta) + 0.006};  
  
    }
	/**
     * gps转百度坐标
     * @param ggLon
     * @param ggLat
     * @return
     */
	public static double[] gpsToBd(double ggLon,double ggLat){
        double mgLat;  
        double mgLon;  
        if (outOfChina(ggLat, ggLon))  
        {  
            return new double[]{ggLon,ggLat};
        }  
        double dLat = transformLat((ggLon - 105.0), (ggLat - 35.0));  
        double dLon = transformLon((ggLon - 105.0), (ggLat - 35.0));  
        double radLat = ggLat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        mgLat = ggLat + dLat;  
        mgLon = ggLon + dLon;  
        double x = mgLon, y = mgLat;  
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);  
        double blon=z * Math.cos(theta) + 0.0065;
        double blat=z * Math.sin(theta) + 0.006;
        return new double[]{blon,blat};
	}
	/**
	 * 百度坐标转火星坐标
	 * @param bdLat
	 * @param bdLon
	 * @return
	 */
	public static double[] bdToHx(double bdLon,double bdLat)  
    {  
        double x = bdLon - 0.0065, y = bdLat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);  
        return new double[]{z * Math.cos(theta),z * Math.sin(theta)};
    }
	/**
	 * 火星坐标转gps
	 * @param gjLat
	 * @param gjLon
	 * @return
	 */
	public static double[] hxToGps(double hxLon,double hxLat)  
    {  
        double[] hx = gpsToHx(hxLon,hxLat);  
        double dLon = hx[0] - hxLon;  
        double dLat = hx[1]- hxLat;  
        return new double[]{hxLon - dLon,hxLat - dLat};
    }  
	/**
	 * 百度坐标转GPS
	 * @param bdLon
	 * @param bdLat
	 * @return
	 */
	public static double[] bdToGps(double bdLon,double bdLat){
		double x = bdLon - 0.0065, y = bdLat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi); 
        double gjLon=z * Math.cos(theta);
        double gjLat=z * Math.sin(theta);
        double[] hx = gpsToHx(gjLon,gjLat);  
        double dLon = hx[0] - gjLon;  
        double dLat = hx[1]- gjLat;  
        return new double[]{gjLon - dLon,gjLat - dLat};
        
	}
	
    private static double transformLat(double x, double y)  
    {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  
    private static double transformLon(double x, double y)  
    {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;  
        return ret;  
    }  
	private static Boolean outOfChina(double lat, double lon)  
    {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }  
	
	public static void main(String[] args) {
		double bdLon=116.437326;
		double bdLat=39.915267;
		double[] gps=bdToGps(bdLon, bdLat);
		System.out.println(bdLon+","+bdLat);
		System.out.println(gps[0]+","+gps[1]);
		gps=new double[]{116.4246,39.90787};
		double[] bd=gpsToBd(gps[0], gps[1]);
		System.out.println(bd[0]+","+bd[1]);
		
	}
}
