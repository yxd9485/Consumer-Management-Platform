package com.dbt.smallticket.action;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.smallticket.service.VpsTicketWineShopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("poster")
public class PosterAction extends BaseAction {
    
    @Autowired
    private VpsTicketWineShopUserService wineShopUserService;
    
    /**
     * 酒行海报
     */
    @ResponseBody
    @RequestMapping("/initWineShopPoster")
    public String initWineShopPoster(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, String> requestMap = parseRequest(request, HashMap.class);
            if (requestMap != null && requestMap.containsKey("projectServerName")) {
                DbContextHolder.setDBType(requestMap.get("projectServerName"));
                resultMap.put("posterImgUrl", wineShopUserService.initWineShopPoster(requestMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 酒行大奖海报
     */
    @ResponseBody
    @RequestMapping("/initPrizePorster")
    public String initPrizePorster(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, String> requestMap = parseRequest(request, HashMap.class);
            if (requestMap != null && requestMap.containsKey("projectServerName")) {
                DbContextHolder.setDBType(requestMap.get("projectServerName"));
                resultMap.put("posterImgUrl", wineShopUserService.initPrizePorster(requestMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 河北礼品卡小程序分享海报
     */
    @ResponseBody
    @RequestMapping("/initGiftCardPorster")
    public String initGiftCardPorster(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, String> requestMap = parseRequest(request, HashMap.class);
            if (requestMap != null && requestMap.containsKey("projectServerName")) {
                DbContextHolder.setDBType(requestMap.get("projectServerName"));
                resultMap.put("posterImgUrl", wineShopUserService.initGiftCardPorster(requestMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 中粮长城海报
     */
    @ResponseBody
    @RequestMapping("/initZlCNYPorster")
    public String initZlCNYPorster(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, String> requestMap = parseRequest(request, HashMap.class);
            if (requestMap != null && requestMap.containsKey("projectServerName")) {
                DbContextHolder.setDBType(requestMap.get("projectServerName"));
                resultMap.put("posterImgUrl", wineShopUserService.initZlCNYPorster(requestMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }
}
