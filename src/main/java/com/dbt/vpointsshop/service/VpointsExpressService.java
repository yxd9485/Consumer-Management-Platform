package com.dbt.vpointsshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.*;
import com.dbt.vpointsshop.bean.PreCreateOrderParam;
import com.dbt.vpointsshop.bean.ProductType;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.dao.VpointsExchangeDao;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.*;

@Service
public class VpointsExpressService {
    private Log log = LogFactory.getLog(this.getClass());
    private final static String PIRE_CREATE_ORDER_URL = "https://openic.sf-express.com/open/api/external/precreateorder?sign=";
    private final static String PRO_CREATE_ORDER_URL = "https://openic.sf-express.com/open/api/external/createorder?sign=";
    @Autowired
    private VpointsExchangeDao vpointsExchangeDao;
//        String user_lng = "116.436967";
//        String user_lat = "39.915516";
//        String user_address = "北京市光华长安大厦";
//        String city_name = "北京市";
//        int lbs_type = 1;
//        int product_type = ProductType.TOBACCO_AND_ALCOHOL_STORE.getCode();
//        long push_time = System.currentTimeMillis() / 1000;

    public boolean executeSfJds(List<VpointsExchangeLog> sfJsdExchangeCog) {
        ArrayList<Map> productLst = new ArrayList<Map>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> exchangeList = new ArrayList<>();
        JSONObject responseData = null;
        String shopOrderid = "";
        String exchangeIds = "";
        String sign = null;
        long totalPrice = 0;
        long weight = 0;

        try {
            long devId = Long.parseLong(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_dev_id));
            String shopId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_shop_id);
            String appKey = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_appKey);

            for (VpointsExchangeLog vpointsExchangeLog : sfJsdExchangeCog) {
                exchangeIds += vpointsExchangeLog.getExchangeId()+",";
                String goodsSpecification = vpointsExchangeLog.getGoodsSpecification();
                totalPrice += vpointsExchangeLog.getExchangePay();
                if ("ml*".equalsIgnoreCase(goodsSpecification)) {
                    long ml = Long.parseLong(goodsSpecification.split("ml*")[0]);
                    long num2 = 1;
                    long num = 1;
                    if(goodsSpecification.split("ml*")[1].contains("*")){
                        num  = Long.parseLong(goodsSpecification.split("ml*")[1].split("*")[0]);
                        num2 = Long.parseLong(goodsSpecification.split("ml*")[1].split("*")[1]);
                    }else{
                        num = Long.parseLong(goodsSpecification.split("ml*")[1]);
                    }
                    weight += vpointsExchangeLog.getExchangeNum() * ml * num * num2;
                }
                if ("L*".equalsIgnoreCase(goodsSpecification)) {
                    long ml = Long.parseLong(goodsSpecification.split("L*")[0]) * 1000;
                    long num = Long.parseLong(goodsSpecification.split("L*")[1]);

                    weight += vpointsExchangeLog.getExchangeNum() * ml * num;
                }
                Map<String, Object> productMap = new HashMap<String, Object>();
                productMap.put("product_name", vpointsExchangeLog.getGoodsName());
                productMap.put("product_num", vpointsExchangeLog.getExchangeNum());
                productLst.add(productMap);
                exchangeList.add(vpointsExchangeLog.getExchangeId());
            }
            long currentTIme = System.currentTimeMillis() / 1000;
            shopOrderid = "V" + IdWorker.getIdByExchangeType(Constant.exchangeType.TYPE_8);
            map.put("dev_id", devId);
            map.put("shop_id", shopId);
            map.put("shop_order_id", shopOrderid);
            map.put("order_source", "开发测试");
            map.put("lbs_type", 1);
            map.put("order_time", currentTIme);
            map.put("is_insured", 0);
            map.put("push_time", currentTIme);
            map.put("version", 19);
            Map<String, String>  geoCode = GeocodingUtil.getGeoCode(sfJsdExchangeCog.get(0).getAddress(), sfJsdExchangeCog.get(0).getAddress().split("-")[0]);
            Map<String, String> receiveMap = new HashMap<String, String>();
            receiveMap.put("user_name", "顺丰同城");
            receiveMap.put("user_phone", "13621347901");
            receiveMap.put("user_address", sfJsdExchangeCog.get(0).getAddress());
            receiveMap.put("user_lng", geoCode.get("lng"));
            receiveMap.put("user_lat", geoCode.get("lat"));
            receiveMap.put("city_name", sfJsdExchangeCog.get(0).getAddress().split("-")[0]);
            map.put("receive", receiveMap);

            Map<String, Object> orderMap = new HashMap<String, Object>();
            orderMap.put("total_price", totalPrice);
            orderMap.put("product_type", 1);
            orderMap.put("weight_gram", weight);
            orderMap.put("product_num", 1);
            orderMap.put("product_type_num", sfJsdExchangeCog.size());
            orderMap.put("product_detail", productLst);
            map.put("order_detail", orderMap);

            System.out.println("\n商城顺丰极速达创建订单（店铺）");
            System.out.println(JSON.toJSONString(map));

            sign = ShunFengSignUtil.generateOpenSign(JSON.toJSONString(map), devId, appKey);
            responseData = HttpsUtil.doPostForJson(PRO_CREATE_ORDER_URL + sign, JSONObject.toJSONString(map));
            log.warn(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(responseData!=null && responseData.getInteger("error_code") == 0){
            for (VpointsExchangeLog vpointsExchangeLog : sfJsdExchangeCog) {
                JSONObject result = responseData.getJSONObject("result");
                Map<String,Object> resultParam = new HashMap();
                resultParam.put("exchangeId",vpointsExchangeLog.getExchangeId());
                resultParam.put("expressOrderId",  result.getString("sf_order_id"));
                resultParam.put("shopOrderId", shopOrderid);
                resultParam.put("expressCompany", "顺丰");
                resultParam.put("expressNumber", result.getString("sf_bill_id"));
                resultParam.put("expressSendMessage", "");
                resultParam.put("exchangeStatus", "1");
                resultParam.put("expressSendTime", new Date());
                vpointsExchangeDao.updateSfJsdExchangeOrder(result);
            }
            return true;
        }else{
            String errData = "";
            if(responseData == null){
                errData = "下单接口请求失败";
            }else{
                errData = responseData.getString("error_data");
            }
            log.error("serverName:" + DbContextHolder.getDBType() + "商城订单id" + exchangeIds + " 收货人" + sfJsdExchangeCog.get(0).getUserName() + " 收货电话"
                    +  sfJsdExchangeCog.get(0).getPhoneNum() + " 收货地址"+ sfJsdExchangeCog.get(0).getAddress() +" 顺丰极速达下单异常:"+errData);
            return false;
        }

    }
    /**
     * 预创建订单（店铺）
     *
     * {SERVER_HOST}/open/api/external/precreateorder?sign=$sign
     */
    public static void preCreateOrder(String user_lng, String user_lat, String user_address,String city_name,long weight, int lbs_type,int product_type){
        long devId = Long.parseLong(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_dev_id));
        String shopId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_shop_id);
        String appKey = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.VPOINTS_ESTORE_COG, DatadicKey.SfJSDKey.sf_jsd_appKey);
        if(product_type==0){
            product_type = ProductType.TOBACCO_AND_ALCOHOL_STORE.getCode();
        }
        PreCreateOrderParam preParam = new PreCreateOrderParam(devId,shopId,1,user_lng,user_lat,user_address,city_name,weight,lbs_type,product_type);
        try {
            String sign = VpointsExpressService.generateOpenSign(JSONObject.toJSONString(preParam), devId, appKey);
//            requestTemplate.
            Map<String, Object> map = new HashMap<String, Object>();
            // 预创建订单（店铺）
            map.put("dev_id", devId);
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
            String sign1 = VpointsExpressService.generateOpenSign(JSON.toJSONString(map), devId, appKey);
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
