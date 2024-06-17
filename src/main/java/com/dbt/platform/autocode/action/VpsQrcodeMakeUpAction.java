package com.dbt.platform.autocode.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.util.QrCodeUtil;
import com.dbt.platform.autocode.service.VpsQrcodeMakeService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码补录
 * @author shuDa
 * @date 2021年12月2日
 **/
@Controller
@RequestMapping("/qrcodeMakeUp")
public class VpsQrcodeMakeUpAction extends BaseAction {

    @Autowired
    private VpsQrcodeMakeService vpsQrcodeMakeService;

    /**
     * 跳转二维码补录页面
     */
    @RequestMapping("/showQrcodeMakeUpView")
    public String showQrcodeMakeUpView(){
        return "vcode/autocode/makeUp/showQrcodeMakeUpView";
    }

    /**
     * 跳转二维码查询页面
     */
    @RequestMapping("/showQrcodeQueryView")
    public String showQrcodeQueryView(){
        return "vcode/autocode/makeUp/showQrcodeQueryView";
    }
    /**
     * 跳转二维码补录页面
     */
    @ResponseBody
    @RequestMapping("/makeUp")
    public String makeUp(HttpSession session, @RequestParam("qr") String qr){
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        SysUserBasis userInfo = this.getUserBasis(session);
        Map<String, String> analysisQrcodeMap = new HashMap<>();
        try {
            //1、参数非空判断
            if(userInfo==null){
                baseResult.initReslut("0", "未登录");
                return JSON.toJSONString(baseResult);
            }
            if(!StringUtils.isNotBlank(qr)){
                baseResult.initReslut("0", "二维码参数不能为空");
                return JSON.toJSONString(baseResult);
            }
            //2、解密二维码，不符合格式直接返回告知
             analysisQrcodeMap = QrCodeUtil.analysisQrcode(qr.trim());
            //只能输入12位串码
            if((analysisQrcodeMap.get("activitycode").length() + analysisQrcodeMap.get("vcode").length()) != 12 ){
                baseResult.initReslut("0", "请输入12位串码");
                return JSON.toJSONString(baseResult);
            }
            if (!ifVcodeMap(analysisQrcodeMap)) {
                baseResult.initReslut("0", "非活动二维码");
                return JSON.toJSONString(baseResult);
            }
            vpsQrcodeMakeService.executeMakeUp(userInfo,analysisQrcodeMap,baseResult);
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "解析异常");
        }

        return JSON.toJSONString(baseResult);
    }

    /**
     * 跳转二维码补录页面
     */
    @ResponseBody
    @RequestMapping("/query")
    public String query(HttpSession session, @RequestParam("col5") String col5,@RequestParam("queryNotes")  String queryNotes){
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        SysUserBasis userInfo = this.getUserBasis(session);
        try {
            //1、参数非空判断
            if(userInfo==null){
                baseResult.initReslut("0", "未登录");
                return JSON.toJSONString(baseResult);
            }
            if(!StringUtils.isNotBlank(col5)){
                baseResult.initReslut("0", "序号不能为空");
                return JSON.toJSONString(baseResult);
            }
            Map<String, String> analysisCol5Map = analysisCol5(col5.trim());
            analysisCol5Map.put("queryNotes", queryNotes);
            if(analysisCol5Map.size() < 1){
                baseResult.initReslut("0", "序号错误");
                return JSON.toJSONString(baseResult);
            }
            //2、解密二维码，不符合格式直接返回告知
            vpsQrcodeMakeService.executeQuery(userInfo,analysisCol5Map,baseResult,null);
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "解析异常");
        }
        return JSON.toJSONString(baseResult);
    }
private static Map<String,String> analysisCol5(String col5){
    Map<String, String> resultMap = new HashMap<>();
    if(col5 != null && col5.length() > 3){
        resultMap.put("code",col5.substring(0, 3));
        resultMap.put("col",col5.substring(3));
        resultMap.put("col5",col5);
    }
    return resultMap;
}


    /**
     * 判断V码是否正常
     *
     * @param vcodeMap
     * @return
     */
    public boolean ifVcodeMap(Map<String, String> vcodeMap) {
        boolean flag = true;
        // 不是正常的v码
        if (null == vcodeMap || vcodeMap.isEmpty() || org.apache.commons.lang3.StringUtils.isBlank(vcodeMap.get("vcode"))
                || org.apache.commons.lang3.StringUtils.isBlank(vcodeMap.get("activitycode"))) {
            flag = false;
            log.info("解析不出批次活动标识及码的内容!");
        }
        return flag;
    }
}
