package com.dbt.platform.invitationActivity.service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.autocode.tools.CompressFileTools;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationCode;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.dao.InvitationCodeDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class InvitationCodeService extends BaseService<VpsVcodeInvitationCode> {
    @Autowired
    private InvitationCodeDao invitationCodeDao;
    @Autowired
    private InvitationOrderService invitationOrderService;
    @Autowired
    private InvitationActivityService invitationService;

    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");

    public List<VpsVcodeInvitationCode> queryForList(VpsVcodeInvitationCode queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return invitationCodeDao.queryForList(map);
    }

    public int queryForCount(VpsVcodeInvitationCode queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return invitationCodeDao.queryForCount(map);
    }

    /**
     * 新增注册码
     * @param orderInfo
     * @param currentUser
     */
    public void addCode(SysUserBasis currentUser, VpsVcodeInvitationOrder orderInfo) throws Exception {
        // 活动主键
        String activityKey = orderInfo.getActivityKey();
        // 订单主键
        String orderKey = orderInfo.getInfoKey();
        // 订单创建时间
        String date = orderInfo.getCreateTime().split(" ")[0].replaceAll("-", "");
        // 生成码源订单路径，格式：/data/upload/autocode/inviteActivity/shandongagt/活动主键/订单主键
        String filePath = COMMON_PATH + "/" + "inviteActivity" + "/" + DbContextHolder.getDBType() + "/" + activityKey + "/" + orderKey;

        // 失败时重新生成，需删除之前的数据
        if("3".equals(orderInfo.getStatus())){
            // 删除订单生成的文件
            FileOperateUtil.delAllFile(filePath);
            // 物理删除订单相关的邀请码
            invitationCodeDao.removeCodeByOrderKey(orderKey);
        }

        // 邀请码url
        // 测试：http://xt.vjifen.com/qr?c=p&v=二维码
        // 线上：HTTP://VJ1.TV/p/二维码
        String qrcodeUrl = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.QRCODE_URL);
        String projectFlag = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_FLAG);

        String url = qrcodeUrl + projectFlag + "/" + Constant.qrcodeBusinessType.TYPE_AIVT;
        if (qrcodeUrl.contains("vjifen")) {
            url += "&v=";
        } else {
            url += "/";
        }

        // 生成邀请码内容相关信息
        Map<String, String> resultMap = queryMaxCode(orderInfo, projectFlag);
        String activityFlag = resultMap.get("activityFlag");//活动标识
        String encodPrefix = resultMap.get("encodPrefix");//编码前缀
        int startNo = Integer.parseInt(resultMap.get("maxNo"))+1;//生成码开始序号

        // 生成码源文件
        String createTime = DateUtil.getDateTime();
        String separator = System.getProperty("line.separator");
        DpiUtil dpiUtil = new DpiUtil();

        String terminalQrcodePath = filePath + "/1source";
        File file = new File(terminalQrcodePath); file.mkdirs();
        String dataFileName = filePath + "/" + orderKey + ".txt";
        FileOutputStream dataFile = new FileOutputStream(dataFileName);//入库文件
        BufferedWriter qrcodeFile = new BufferedWriter(new FileWriter(terminalQrcodePath + "/" + orderKey + ".txt")) ;//码源文件
        StringBuffer buffer;
        String codeAllUrl;
        String codeContent;
        try {
            for (int i = 0; i < orderInfo.getQrcodeNum(); i++) {
                // 码内容
                codeContent = UUIDTools.getInstance().getUUID().replace("-", "").toUpperCase();
                // 码编号：码类型 + 省区标志 + 几几年第几个活动 + 7位数编号
                String codeNo = encodPrefix + StringUtil.lPad((startNo + i)+"", "0", 7);
                // 码链接
                codeAllUrl = url + activityFlag + codeContent;

                // 生成码源文件
                qrcodeFile.write(codeAllUrl+","+codeNo);
                qrcodeFile.newLine();

                // 生成二维码
                /*String imageUrl = terminalQrcodePath + "/" + codeNo + ".png";
                QrCodeUtil.encodeQRCodeImage(codeAllUrl, null, imageUrl, 283, 283, null);
                dpiUtil.saveGridImage(new File(imageUrl));*/

                // 写入data文件
                buffer =  new StringBuffer();
                buffer.append(UUIDTools.getInstance().getUUID())
                        .append("+").append(orderInfo.getActivityKey())
                        .append("+").append(orderKey)
                        .append("+").append(orderInfo.getOrderName())
                        .append("+").append(codeContent)
                        .append("+").append(codeNo)
                        .append("+").append(orderInfo.getQrcodeType())
                        .append("+").append("0")
                        .append("+").append(currentUser.getUserName())
                        .append("+").append(createTime)
                        .append("+").append(currentUser.getUserName())
                        .append("+").append(createTime).append(separator);
                dataFile.write(buffer.toString().getBytes());
            }

        } catch (Exception e) {
            log.error("生成终端门店码异常", e);
            throw e;
        } finally {
            dataFile.flush();
            dataFile.close();

            qrcodeFile.flush();
            qrcodeFile.close();
        }

        // 入库文件入库
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tmpFilePath", dataFileName);
        invitationCodeDao.writeQrcodeToData(map);

        // 压缩文件
        String fileName = orderInfo.getOrderName() + "_"+ orderInfo.getQrcodeNum();
        CompressFileTools.compressFile(filePath, orderKey, date, fileName, "1source", null);

        // 修改订单状态为成功
        VpsVcodeInvitationOrder tempOrder = new VpsVcodeInvitationOrder();
        tempOrder.setInfoKey(orderKey);
        tempOrder.setStatus("2");
        invitationOrderService.updateOrder(tempOrder);

        // 删除入库文件 临时码源文件
        FileOperateUtil.deleteFile(dataFileName);
        FileOperateUtil.delFolder(filePath + "/1source");
    }

    /**
     * 获取当前活动下二维码编码最大值
     * @return
     */
    public Map<String, String> queryMaxCode(VpsVcodeInvitationOrder orderInfo,String projectFlag){

        Map<String, String> resultMap = new HashMap<>();

        VpsVcodeInvitationActivityCog activityCog = invitationService.findById(orderInfo.getActivityKey());
        String activityFlag = activityCog.getActivityFlag();
        resultMap.put("activityFlag",activityFlag);//活动标记
        String activityNo = activityCog.getActivityNo();//QPSD2023IV0012
        String activityStr = activityNo.substring(6).replace("IV", "");//230012
        String queryStr = "";//SHP230012
        if ("0".equals(orderInfo.getQrcodeType())){
            //门店
            queryStr = "门店-"+projectFlag+activityStr;
        }else{
            //导购
            queryStr = "导购-"+projectFlag+activityStr;
        }
        resultMap.put("encodPrefix",queryStr);//编码前缀
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("activityKey",orderInfo.getActivityKey());
        queryMap.put("queryStr",queryStr);
        String maxCodeNo = invitationCodeDao.queryMaxCode(queryMap);
        if (StringUtils.isNotBlank(maxCodeNo)){
            resultMap.put("maxCodeNo",maxCodeNo);//最大编号编码
            resultMap.put("maxNo",maxCodeNo.replaceAll(queryStr,""));//最大编号
        }else{
            resultMap.put("maxCodeNo",queryStr+"0000000");
            resultMap.put("maxNo","0");
        }

        return resultMap;
    }

    /**
     * 导出邀请码
     * @param queryBean
     * @param uploadCodeType 0 已注册 1未注册
     * @return
     */
    public void uploadCode(HttpServletResponse response, VpsVcodeInvitationCode queryBean, String uploadCodeType) throws Exception {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();

        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("uploadCodeType", uploadCodeType);
        List<VpsVcodeInvitationCode> vpsVcodeInvitationCodes = invitationCodeDao.uploadCode(map);

        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "0".equals(uploadCodeType) ? "已注册邀请码导出":"未注册邀请码导出";
        if ("0".equals(queryBean.getCodeType())){
            headers = new String[]{"订单名称", "邀请码编号", "门店名称", "手机号", "注册时间"};
            valueTags = new String[]{"orderName", "codeNo", "registrantShopName", "registrantPhoneNum", "registerTime"};
        }else{
            headers = new String[]{"订单名称", "邀请码编号", "姓名", "手机号", "注册时间"};
            valueTags = new String[]{"orderName", "codeNo", "registrantName", "registrantPhoneNum", "registerTime"};
        }

        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xlsx");
        ExcelUtil<VpsVcodeInvitationCode> excel = new ExcelUtil<>();
        excel.writeSXSSFWorkExcel(bookName, headers, valueTags, vpsVcodeInvitationCodes, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }

    /**
     * 根据主键查询注册码
     * @param infoKey
     * @return
     */
    public VpsVcodeInvitationCode findById(String infoKey) {
        return invitationCodeDao.findById(infoKey);
    }

    /**
     * 物理删除订单根据活动主键
     * @param activityKey
     */
    public void removeCodeByActivityKey(String activityKey){
        invitationCodeDao.removeCodeByActivityKey(activityKey);
    }

    /**
     * 物理删除订单根据活动主键订单主键
     * @param activityKey
     */
    public void removeCodeByActivityKeyAndOrderKey(String activityKey,String orderKey){
        invitationCodeDao.removeCodeByActivityKeyAndOrderKey(activityKey,orderKey);
    }
}
