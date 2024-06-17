package com.dbt.test.express;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.test.express.param.PreCreateOrderParam;
import com.dbt.test.express.param.ProductType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.vpointsshop.dao.VpointsExchangeDao;

@Service
public class VpointsExpressService  {
    private Log log = LogFactory.getLog(this.getClass());
    private final static long appId = 1661720428;
    private final static String shopId = "3243279847393";
    private  final static String appKey = "8bac0f29960c3718bb8bb8fc5c0ec714";
    private final static String PIRE_CREATE_ORDER_URL = "https://openic.sf-express.com/open/api/external/precreateorder?sign=";




    /**
     * 预创建订单（店铺）
     *
     * {SERVER_HOST}/open/api/external/precreateorder?sign=$sign
     */
    public static void preCreateOrder(String user_lng, String user_lat, String user_address,String city_name,long weight, int lbs_type,int product_type){
//        String user_lng = "116.436967";
//        String user_lat = "39.915516";
//        String user_address = "北京市光华长安大厦";
//        String city_name = "北京市";
//        int lbs_type = 1;
//        int product_type = ProductType.TOBACCO_AND_ALCOHOL_STORE.getCode();
//        long push_time = System.currentTimeMillis() / 1000;
        if(product_type==0){
            product_type = ProductType.TOBACCO_AND_ALCOHOL_STORE.getCode();
        }
        PreCreateOrderParam preParam = new PreCreateOrderParam(appId,shopId,1,user_lng,user_lat,user_address,city_name,weight,lbs_type,product_type);
        try {
            String sign = VpointsExpressService.generateOpenSign(JSONObject.toJSONString(preParam), appId, appKey);
//            requestTemplate.
            Map<String, Object> map = new HashMap<String, Object>();

            // 预创建订单（店铺）
            map.put("dev_id", appId);
            map.put("shop_id", "3243279847393");
            map.put("shop_type", 1);
            map.put("user_lng", "116.436967"); // 用户信息
            map.put("user_lat", "39.915516");
            map.put("user_address", "北京市光华长安大厦");
            map.put("city_name", "北京市");
            map.put("lbs_type", 1); // 百度坐标
            map.put("weight", 1000);
            map.put("product_type", 1);
            map.put("push_time", System.currentTimeMillis() / 1000);
            String sign1 = VpointsExpressService.generateOpenSign(JSON.toJSONString(map), appId, appKey);
            JSONObject revokeJson = HttpReq.handerHttpReq(PIRE_CREATE_ORDER_URL + sign, JSONObject.toJSONString(preParam));
            System.out.println("预创建订单sign："+revokeJson);
            System.out.println(JSONObject.toJSONString(sign1));
            System.out.println(JSONObject.toJSONString(sign));
            System.out.println(JSONObject.toJSONString(preParam));
            System.out.println(JSONObject.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        preCreateOrder("116.436967", "39.915516", "北京市光华长安大厦", "北京市", 1000, 1, 1);

//
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        // 预创建订单（店铺）
//        map.put("dev_id", appId);
//        map.put("shop_id", "3243279847393");
//        map.put("shop_type", "1");
//        map.put("user_lng", "116.436967"); // 用户信息
//        map.put("user_lat", "39.915516");
//        map.put("user_address", "北京市光华长安大厦");
//        map.put("city_name", "北京市");
//        map.put("lbs_type", 1); // 百度坐标
//        map.put("product_type", "47");
//        map.put("push_time", System.currentTimeMillis() / 1000);
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(VpointsExpressService.generateOpenSign(JSON.toJSONString(map), appId, appKey));
//
//        // 创建订单（店铺）
//        map.clear();
//        map.put("dev_id", appId);
//        map.put("shop_id", "3243279847393");
//        map.put("shop_order_id", "V" + System.currentTimeMillis() / 1000);
//        map.put("order_source", "开发测试");
//        map.put("lbs_type", 1);
//        map.put("order_time", System.currentTimeMillis() / 1000);
//        map.put("is_insured", 0);
//        map.put("push_time", System.currentTimeMillis() / 1000);
//        map.put("version", 19);
//        Map<String, String> receiveMap = new HashMap<String, String>();
//        receiveMap.put("user_name", "顺丰同城");
//        receiveMap.put("user_phone", "13621347901");
//        receiveMap.put("user_address", "北京市海淀区学清嘉创大厦A座15层）");
//        receiveMap.put("user_lng", "116.436967");
//        receiveMap.put("user_lat", "39.915516");
//        receiveMap.put("city_name", "北京市");
//        map.put("receive", receiveMap);
//        Map<String, Object> orderMap = new HashMap<String, Object>();
//        orderMap.put("total_price", 100);
//        orderMap.put("product_type", 99);
//        orderMap.put("weight_gram", 100);
//        orderMap.put("product_num", 1);
//        orderMap.put("product_type_num", 1);
//        Map<String, Object> productMap = new HashMap<String, Object>();
//        productMap.put("product_name", "测试商品");
//        productMap.put("product_num", 1);
//        ArrayList<Map> productLst = new ArrayList<Map>();
//        productLst.add(productMap);
//        orderMap.put("product_detail", productLst);
//        map.put("order_detail", orderMap);
//        System.out.println("\n创建订单（店铺）");
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(VpointsExpressService.generateOpenSign(JSON.toJSONString(map), appId, appKey));
//
//        // 取消订单
//        map.clear();
//        map.put("dev_id", appId);
//        map.put("order_id", "JS4157556257386");
//        map.put("order_type", 1);
//        map.put("push_time", System.currentTimeMillis() / 1000);
//        System.out.println("\n取消订单");
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(VpointsExpressService.generateOpenSign(JSON.toJSONString(map), appId, appKey));
//
//        // 实时查询订单状态
//        map.clear();
//        map.put("dev_id", appId);
//        map.put("order_id", "JS4157556257386");
//        map.put("order_type", 1);
//        map.put("push_time", System.currentTimeMillis() / 1000);
//        System.out.println("\n实时查询订单状态");
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(VpointsExpressService.generateOpenSign(JSON.toJSONString(map), appId, appKey));
    }

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

        String ret = Base64.encodeBase64String(buf.toString().getBytes("utf-8"));

        return  ret;
    }
}
