package com.dbt.platform.autocode.action;

import ch.qos.logback.core.db.DBHelper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.autocode.bean.VpsBatchSerialQr;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.autocode.dto.TitleHandler;
import com.dbt.platform.autocode.dto.VpsBatchSerialQrExcelTemplate;
import com.dbt.platform.autocode.dto.VpsBatchSerialQrVO;
import com.dbt.platform.autocode.service.ConverterDataListener;
import com.dbt.platform.autocode.service.VpsBatchSerialQrService;
import com.dbt.platform.autocode.service.VpsQrcodeMakeService;
import com.dbt.platform.autocode.tools.CompressFileTools;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量二维码查询
 * @author shuDa
 * @date 2023年06月29日
 **/
@Controller
@RequestMapping("/batchSerialQr")
public class VpsBatchSerialQrQueryAction extends BaseAction {
    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");
    @Autowired
    private VpsBatchSerialQrService vpsBatchSerialQrService;
    @Autowired
    private VpsQrcodeMakeService vpsQrcodeMakeService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 跳转二维码查询页面
     */
    @RequestMapping("/showbatchSerialQrList")
    public String showbatchSerialQrList(HttpSession session, String queryParam, String pageParam, Model model){
        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        VpsBatchSerialQrVO queryBean = new VpsBatchSerialQrVO(queryParam);
        IPage<VpsBatchSerialQr> vpsBatchSerialQrIPage = vpsBatchSerialQrService.queryPage(pageInfo, queryBean);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resultList", vpsBatchSerialQrIPage.getRecords());
        model.addAttribute("showCount", vpsBatchSerialQrIPage.getTotal());
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        return "vcode/autocode/batchSerialQr/showbatchSerialQrList";
    }

    /**
     * 下载模板
     */
    @RequestMapping("/downTemplate")
    @ResponseBody
    public void downTemplate(HttpServletResponse  response){
        try {
            //response输出文件流
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("批量二维码序号查询模板", "UTF-8")+".xls");
            String selectMenu[] = new String[]{
                    "二维码喷码变形",
                    "二维码喷码折叠",
                    "二维码喷码重叠",
                    "二维码喷码不清晰",
                    "二维码定位快周边有白色点",
                    "二维码定位点位扭曲",
                    "无二维码"
            };
            //写入
            TitleHandler titleHandler = new TitleHandler(new HashMap<Integer, String[]>() {{
                put(1, selectMenu);
            }});
            VpsBatchSerialQrExcelTemplate template = new VpsBatchSerialQrExcelTemplate();
            List<VpsBatchSerialQrExcelTemplate> data = new ArrayList<>();
            EasyExcel.write(response.getOutputStream()).registerWriteHandler(titleHandler)
                    .head(template.getClass()).sheet("Sheet1").doWrite(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 下载上传文件
     */
    @RequestMapping("/downloadQrImage")
    public void downloadExcel(String filePath,
                              HttpServletRequest request, HttpServletResponse response){
        try {
            File file=new File(filePath);
            //文件判断
            if(!file.exists()){
                System.out.println("file not found");
                outPrint(request, response);
            }else{
                response.setContentType("application/msexcel;charset=UTF-8");
                response.setHeader("Content-disposition", "attachment;");
                ExportUtil.download(file, request, response);
            }
        } catch (Exception e) {
            try {
                outPrint(request, response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }



    /**
     * 上传
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public String importExcel(HttpSession session, @RequestParam("filePath") MultipartFile file){
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        String excelPath = COMMON_PATH+"/queryQrcode/"+DbContextHolder.getDBType();
        try {
            if(file!=null){
                String id = vpsBatchSerialQrService.save(excelPath, "", 0, currentUser.getPhoneNum());
                List<VpsBatchSerialQrExcelTemplate> list = EasyExcel.read(file.getInputStream(), VpsBatchSerialQrExcelTemplate.class, new ConverterDataListener(currentUser,id,vpsQrcodeMakeService)).sheet().doReadSync();
                vpsBatchSerialQrService.updateQueryNumber(id, list.size());
                File saveFilePath = new File(excelPath );
                // 如果文件夹不存在md
                if (!saveFilePath.exists()) {
                    saveFilePath.mkdirs();
                }
                resultMap.put("errMsg", "成功");
//                file.transferTo(saveFilePath);
                EasyExcel
                        .write(saveFilePath+"/"+ id+".xls")
                        .autoCloseStream(Boolean.TRUE)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .head(VpsBatchSerialQrExcelTemplate.class)
                        .sheet(id)
                        .doWrite(list);
                taskExecutor.execute(new CreateQrImage(currentUser.getProjectServerName(), id,vpsBatchSerialQrService));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errMsg", "失败");
        }
        return JSONObject.toJSONString(resultMap);
    }

    public void outPrint(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        //去除页面乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            StringBuilder builder = new StringBuilder();
            builder.append("<script type=\'text/javascript\' charset=\'UTF-8\'>");
            builder.append("alert(\'暂无数据\');window.close();");
            builder.append("</script>");
            out.print(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
@Service
class CreateQrImage implements Runnable{
    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");
    private String projectServerName;
    private String id;

    private VpsBatchSerialQrService vpsBatchSerialQrService;
    CreateQrImage(){}
    CreateQrImage(String projectServerName,String id,VpsBatchSerialQrService vpsBatchSerialQrService){
        this.projectServerName = projectServerName;
        this.vpsBatchSerialQrService = vpsBatchSerialQrService;
        this.id = id;
    }
    @Override
    public void run() {
        DbContextHolder.setDBType(projectServerName);
        String fileName = id + "_" + projectServerName + "_" + "labeImage";
        String qrImageZipPath =  COMMON_PATH + "/queryQrcode/" + projectServerName;
        vpsBatchSerialQrService.updateQrImageUrl(id, qrImageZipPath+ "/4compress/" +fileName+".zip");
        String directory = id;
        try {
            CompressFileTools.compressFile(qrImageZipPath, id, null,
                    fileName, directory, null);
            FileOperateUtil.delFolder(qrImageZipPath+"/"+id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
