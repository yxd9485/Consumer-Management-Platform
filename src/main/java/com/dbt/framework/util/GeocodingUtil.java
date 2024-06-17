package com.dbt.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 文件名： GeocodingUtil.java<br>
 * 版权： Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述： 百度地图坐标帮助类<br>
 * 修改人： HaoQi<br>
 * 修改时间： 2014-07-28<br>
 * 修改内容： 新增<br>
 */
public class GeocodingUtil {
    private static String[] baiduAkAry = null;
	private static Logger log = Logger.getLogger("GeocodingUtil");
    static {
        baiduAkAry = PropertiesUtil.getPropertyValue("baidu_access_key").split(",");
    }
	/**
	 * 依据订单获取百度坐标
	 *
	 * @return lng、lat
	 */
	public static Map<String, String> getGeoCode(String address, String city) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("https://api.map.baidu.com/geocoding/v3/?output=json");
			buffer.append("&ak=").append(PropertiesUtil.getPropertyValue("baidu_access_key"));
			buffer.append("&address=").append(address);
			if (org.apache.commons.lang3.StringUtils.isNotBlank(city)) buffer.append("&city=").append(city);
			URL resjson = new URL(buffer.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), "UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			JSONObject resultObject = JSON.parseObject(str);
			Integer status = resultObject.getInteger("status");
			if(status != null && status == 0){
				JSONObject locationObj = resultObject.getJSONObject("result").getJSONObject("location");
				Set<String> keySet = locationObj.keySet();
				for (String key : keySet) {
					map.put(key, locationObj.get(key).toString());
				}
			} else {
				log.warn("调用百度理编码API失败，status:" + status);
			}
		} catch (Exception e) {
			// 为了不影响主业务流程，现将异常注释  add by jiquanwei 20160613
			// e.printStackTrace();
			log.error(e);
		}
		return map;
	}
	/**
	 * 根据经纬度，返回具体的地址信息
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @return
	 *  province=北京市<br/>
	 *  city=北京市<br/>
	 *  district=东城区<br/>
	 *  street=贡院西街<br/>
	 *  street_number=8-1号<br/>
	 */
	public static Map<String, String> getRealAddress(String latitude, String longitude) {
		Map<String, String> map = new HashMap<String, String>();
		try {
            URL resjson = new URL(PropertiesUtil.getPropertyValue("baidu_geocoder_url")
                    + "&ak=" + baiduAkAry[RandomUtils.nextInt(0, baiduAkAry.length)]
                    + "&location="+latitude+","+longitude);
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), "UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			JSONObject resultObject = JSON.parseObject(str);
			Integer status = resultObject.getInteger("status");
			if(status != null && status == 0){
				JSONObject addressObject = resultObject.getJSONObject("result").getJSONObject("addressComponent");
				Set<String> addressKeySet = addressObject.keySet();
				for (String key : addressKeySet) {
					map.put(key, addressObject.get(key).toString());
				}
			}
		} catch (Exception e) {
			// 为了不影响主业务流程，现将异常注释  add by jiquanwei 20160613
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, String> getTrueAddress(String latitude, String longitude) {
//		double[] d=gaoDeToBaidu(Double.valueOf(latitude),Double.valueOf(longitude));
//		latitude=d[0]+"";
//		longitude=d[1]+"";
		Map<String, String> map = new HashMap<String, String>();
		try {
			URL resjson = new URL("http://api.map.baidu.com/geocoder/v2/?output=json&pois=0"
					+ "&ak=" + "pd5F1YFwkdePYm7nkdNMKeThSgwR81br"
					+ "&location="+latitude+","+longitude);
			//System.out.println(resjson.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), "UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			JSONObject resultObject = JSON.parseObject(str);
			Integer status = resultObject.getInteger("status");
			if(status != null && status == 0){
			    map.put("formatted_address", resultObject.getJSONObject("result").getString("formatted_address"));
			    map.put("sematic_description", resultObject.getJSONObject("result").getString("sematic_description"));
			    map.put("province", resultObject.getJSONObject("result").getJSONObject("addressComponent").getString("province"));
			    map.put("city", resultObject.getJSONObject("result").getJSONObject("addressComponent").getString("city"));
			    map.put("district", resultObject.getJSONObject("result").getJSONObject("addressComponent").getString("district"));
			}else{
				System.out.println(str);
			}
		} catch (Exception e) {
			// 为了不影响主业务流程，现将异常注释  add by jiquanwei 20160613
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 把 GPS设备获取的角度坐标 转换成 百度经纬度坐标
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @return String[纬度, 经度]
	 */
	public static String[] geoConv(String latitude, String longitude){
		try {
            URL resjson = new URL(PropertiesUtil.getPropertyValue("baidu_geoconv_url")
                    + "&ak=" + PropertiesUtil.getPropertyValue("baidu_access_key")
                    + "&coords="+longitude+","+latitude);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			JSONObject resultObject = JSON.parseObject(str);
			Integer status = resultObject.getInteger("status");
			if(status != null && status == 0){
				JSONArray resultArray = resultObject.getJSONArray("result");
				JSONObject result = resultArray.getJSONObject(0);
				//System.out.println("百度api转换后的坐标:"+result.getString("x")+","+result.getString("y"));
				return new String[]{result.getString("y"), result.getString("x")};
			}
		} catch (Exception e) {
			// 为了不影响主业务流程，现将抛异常注释  add by jiquanwei 20160613
//			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据经纬度，返回具体的地址信息
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param ifLocationConv 是否要转换经纬度，微信都必须为true
	 * @return
	 *  province=北京市<br/>
	 *  city=北京市<br/>
	 *  district=东城区<br/>
	 *  street=贡院西街<br/>
	 *  street_number=8-1号<br/>
	 */
	public static Map<String, String> getRealAddress(String latitude, String longitude, boolean ifLocationConv) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			if(StringUtils.isEmpty(latitude)
					|| StringUtils.isEmpty(longitude)){
				return map;
			}
			if(ifLocationConv){
				String[] location = geoConv(latitude, longitude);
				if(location != null && location.length == 2){
					latitude = location[0];
					longitude = location[1];
				}else{
					return map;
				}
			}
			map = getTrueAddress(latitude, longitude);
		} catch (Exception e) {
			e.printStackTrace();
			// 为了不影响主业务流程，现将抛异常注释  add by jiquanwei 20160613
//			e.printStackTrace();
		}
		if (map != null) {
            if (StringUtils.isBlank(map.get("province"))) {
                map.put("province", null);
            }
            if (StringUtils.isBlank(map.get("city"))) {
                map.put("city", null);
            }
            if (StringUtils.isBlank(map.get("district"))) {
                map.put("district", null);
            }
        }
		return map;
	}
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
	public static void main(String args[]) {
		//测试数据
		String latitude = "39.9084";
		String longitude = "116.42454";
		
//		String[] coordinate = "86.088382,44.314487".split(",");
//		String[] coordinate = "116.462184,39.921472".split(",");
//		String[] coordinate = "116.858301,40.396129".split(",");
//		String[] coordinate = "117.221168,39.125444".split(",");
//		String[] coordinate = "117.836328,39.33516".split(",");
//		String[] coordinate = "117.007299,39.894558".split(",");
//		String[] coordinate = "115.67263,37.742883".split(",");
//		String[] coordinate = "94.956888,36.40394".split(",");
//		String[] coordinate = "93.522498,42.826335".split(",");
		//String[] coordinate = "116.439914,39.939972".split(",");
		
		Map<String, String> map = getRealAddress(latitude, longitude, false);
		Set<String> mspKeys = map.keySet();
		for (String key : mspKeys) {
			System.out.println(key + "=" + map.get(key));
		}
	}
}
