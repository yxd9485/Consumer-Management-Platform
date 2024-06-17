package com.dbt.platform.vdhInvitation.service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.autocode.tools.CompressFileTools;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationCode;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationOrder;
import com.dbt.platform.vdhInvitation.dao.VdhInvitationCodeDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VdhInvitationCodeService extends BaseService<VpsVcodeVdhInvitationCode> {
    @Autowired
    private VdhInvitationCodeDao vdhInvitationCodeDao;
    @Autowired
    private VdhInvitationOrderService vdhInvitationOrderService;


    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");

    /**
     * 根据查询条件查询订单
     * @param queryBean
     * @param pageInfo
     * @return
     */
    public List<VpsVcodeVdhInvitationCode> queryForList(VpsVcodeVdhInvitationCode queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return vdhInvitationCodeDao.queryForList(map);
    }

    /**
     * 分页总数
     * @param queryBean
     * @return
     */
    public int queryForCount(VpsVcodeVdhInvitationCode queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vdhInvitationCodeDao.queryForCount(map);
    }

    /**
     * 新增注册码
     * @param orderInfo
     * @param currentUser
     */
    public void addCode(SysUserBasis currentUser, VpsVcodeVdhInvitationOrder orderInfo) throws Exception {
        String orderKey = orderInfo.getInfoKey();
        String orderName = orderInfo.getOrderName();
        String date = orderInfo.getCreateTime().split(" ")[0].replaceAll("-", "");

        // 生成码源订单路径，格式：/data/upload/autocode/vdhInviteActivity/shandongagt/订单主键
        String filePath = COMMON_PATH + "/" + "vdhInviteActivity" + "/" + DbContextHolder.getDBType() + "/" + orderKey;

        // 失败时重新生成，需删除之前的数据
        if("3".equals(orderInfo.getStatus())){
            // 删除订单生成的文件
            FileOperateUtil.delAllFile(filePath);
            // 物理删除订单相关的邀请码
            vdhInvitationCodeDao.removeCodeByOrderKey(orderKey);
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

        String url = qrcodeUrl + projectFlag + "/" + Constant.qrcodeBusinessType.TYPE_VINV;
        if (qrcodeUrl.contains("vjifen")) {
            url += "&v=";
        } else {
            url += "/";
        }

        // 生成邀请码内容相关信息
        Map<String, String> resultMap = queryMaxCode(projectFlag);
        String encodPrefix = resultMap.get("encodPrefix");//编码前缀 门店-d  00000001
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
            for (int i = 0; i < orderInfo.getOrderNum(); i++) {
                // 码内容
                codeContent = UUIDTools.getInstance().getUUID().replace("-", "").toUpperCase();
                // 码编号：码类型 + 省区标志 + 8位数编号
                String codeNo = encodPrefix + StringUtil.lPad((startNo + i)+"", "0", 8);
                // 码链接
                codeAllUrl = url + codeContent;

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
                        .append("+").append(orderInfo.getCompanyKey())
                        .append("+").append(orderKey)
                        .append("+").append(orderName)
                        .append("+").append(codeNo)
                        .append("+").append(codeContent)
                        .append("+").append("0")
                        .append("+").append(currentUser.getUserName())
                        .append("+").append(createTime)
                        .append("+").append(currentUser.getUserName())
                        .append("+").append(createTime).append(separator);
                dataFile.write(buffer.toString().getBytes());
            }

        } catch (Exception e) {
            log.error("V店惠邀请有礼活动，生成V店惠门店码异常", e);
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
        vdhInvitationCodeDao.writeQrcodeToData(map);

        // 压缩文件
        String fileName = orderName + "_"+ orderInfo.getOrderNum();
        CompressFileTools.compressFile(filePath, orderKey, date, fileName, "1source", null);

        // 修改订单状态为成功
        vdhInvitationOrderService.updateOrderStatus(orderInfo,currentUser,"2");

        // 删除入库文件 临时码源文件
        FileOperateUtil.deleteFile(dataFileName);
        FileOperateUtil.delFolder(filePath + "/1source");
    }

    /**
     * 获取当前活动下二维码编码最大值
     * 门店-d00000001
     * @return
     */
    public Map<String, String> queryMaxCode(String projectFlag){
        Map<String, String> resultMap = new HashMap<>();

        String queryStr = "门店-"+projectFlag;
        resultMap.put("encodPrefix",queryStr);//编码前缀

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("queryStr",queryStr);
        String maxCodeNo = vdhInvitationCodeDao.queryMaxCode(queryMap);
        if (StringUtils.isNotBlank(maxCodeNo)){
            resultMap.put("maxCodeNo",maxCodeNo);//最大编号编码
            resultMap.put("maxNo",maxCodeNo.replaceAll(queryStr,""));//最大编号
        }else{
            resultMap.put("maxCodeNo",queryStr+"00000000");
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
    public void uploadCode(HttpServletResponse response, VpsVcodeVdhInvitationCode queryBean, String uploadCodeType) throws Exception {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();

        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("uploadCodeType", uploadCodeType);
        List<VpsVcodeVdhInvitationCode> vpsVcodeVdhInvitationCodes = vdhInvitationCodeDao.uploadCode(map);

        String bookName = "0".equals(uploadCodeType) ? "已注册邀请码导出":"未注册邀请码导出";
        String[] headers = new String[]{"订单名称", "邀请码编号", "注册门店Key", "注册时间"};
        String[] valueTags = new String[]{"orderName", "codeNo", "registrantKey", "registerTime"};

        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xlsx");
        ExcelUtil<VpsVcodeVdhInvitationCode> excel = new ExcelUtil<>();
        excel.writeSXSSFWorkExcel(bookName, headers, valueTags, vpsVcodeVdhInvitationCodes, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }

    /**
     * 根据主键查询注册码
     * @param infoKey
     * @return
     */
    public VpsVcodeVdhInvitationCode findById(String infoKey) {
        return vdhInvitationCodeDao.findById(infoKey);
    }

}
