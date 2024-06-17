package com.dbt.test.express;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbt.framework.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 文件名： GeocodingUtil.java<br>
 * 版权： Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述： 百度地图坐标帮助类<br>
 * 修改人： HaoQi<br>
 * 修改时间： 2014-07-28<br>
 * 修改内容： 新增<br>
 */
public class GeocodingUtil {

    private static Logger log = Logger.getLogger("GeocodingUtil");

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
            if (StringUtils.isNotBlank(city)) buffer.append("&city=").append(city);
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
            if (status != null && status == 0) {
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

}
