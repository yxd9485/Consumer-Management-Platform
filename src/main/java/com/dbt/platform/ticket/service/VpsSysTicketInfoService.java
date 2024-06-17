package com.dbt.platform.ticket.service;

import java.util.*;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsSysTicketCategory;
import com.dbt.platform.ticket.bean.VpsVcodeTicketInfo;
import com.dbt.platform.ticket.dao.IVpsSysTicketCategoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.dao.IVpsSysTicketInfoDao;
import org.springframework.web.multipart.MultipartFile;

/**
 * 优惠券基础面额Service
 */
@Service
public class VpsSysTicketInfoService extends BaseService<VpsSysTicketInfo> {

    @Autowired
    private IVpsSysTicketInfoDao sysTicketInfoDao;
    @Autowired
    private VpsSysTicketCategoryService sysTicketCategoryService;
    @Autowired
    private IVpsSysTicketCategoryDao sysTicketCategoryDao;
    @Autowired
    private VpsVcodeTicketActivityCogService ticketActivityCogService;
    @Autowired
    private VpsVcodeTicketLibService ticketLibService;
    @Autowired
    private VpsVcodeTicketInfoService ticketInfoService;

    /**
     * 优惠券类型
     */
    public final static Map<String, String> map = new HashMap();

    static {
        //青啤
        map.put("21b84b19-4c77-11e9-8a79-224943c59de0", "J");
        //京东
        map.put("6fb458dc-26f2-11ea-94d1-6e6d36e3ad65", "H");
        //有赞
        map.put("72fd2ba0-26fb-11ea-94d1-6e6d36e3ad65", "I");
    }

    public final static Map<String, String> ticketTypeMap = new HashMap();

    static {
        //URL
        ticketTypeMap.put("0", "U");
        //CODE
        ticketTypeMap.put("1", "V");
        //PIC
        ticketTypeMap.put("2", "P");
        //PIC
        ticketTypeMap.put("3", "Q");
        //PIC
        ticketTypeMap.put("4", "N");
    }

    public List<VpsSysTicketInfo> localList() {
        List<VpsSysTicketInfo> sysTicketInfoList = sysTicketInfoDao.localList();
        return sysTicketInfoList;
    }

    public Map<String, String> localListForMap() {
        LinkedHashMap<String, String> prizeMap = new LinkedHashMap<>();
        List<VpsSysTicketInfo> sysTicketInfoList = sysTicketInfoDao.localList();
        if(!CollectionUtils.isEmpty(sysTicketInfoList)){

            StringBuilder builder = new StringBuilder();
            for (VpsSysTicketInfo item : sysTicketInfoList) {
                // 优惠券明细
                builder.append(item.getCategoryType());
                builder.append(item.getCategoryName());
                builder.append("-");
                builder.append(item.getTicketName());
                builder.append("（");
                if (Constant.TICKET_TYPE.ticketType_0.equals(item.getTicketType())) {
                    builder.append("链接");
                } else if (Constant.TICKET_TYPE.ticketType_1.equals(item.getTicketType())) {
                    builder.append("券码");
                } else if (Constant.TICKET_TYPE.ticketType_2.equals(item.getTicketType())) {
                    builder.append("图片");
                } else if (Constant.TICKET_TYPE.ticketType_3.equals(item.getTicketType())) {
                    builder.append("动态券码");
                } else if (Constant.TICKET_TYPE.ticketType_4.equals(item.getTicketType())) {
                    builder.append("活动编码");
                }
                builder.append("）");
                prizeMap.put(item.getTicketNo(), builder.toString());
                builder.setLength(0);
            }
        }
        return prizeMap;
    }

    public List<VpsSysTicketCategory> findTicketDenomination(VpsSysTicketCategory queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);
        return sysTicketCategoryDao.findTicketDenomination(queryMap);
    }

    public int countTicketList(VpsSysTicketCategory queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return sysTicketCategoryDao.countTicketActivityList(queryMap);
    }


    public Map<String, String> writeTicketDenomination(VpsSysTicketInfo vpsSysTicketInfo, MultipartFile batchFile) {
        if ("".equals(vpsSysTicketInfo.getEndDate())) {
            vpsSysTicketInfo.setEndDate(null);
        }
        Map<String, String> resurtMap = new HashMap<String, String>();
        Map<String, Object> map = null;
        VpsSysTicketCategory ticketCategoryInfoById = sysTicketCategoryService.findById(vpsSysTicketInfo.getCategoryKey());
        // 类型appid不为空则跳转类型为小程序
        if (StringUtils.isNotBlank(ticketCategoryInfoById.getJumpId())) {
            vpsSysTicketInfo.setJumpFlag("0");
        } else {
            vpsSysTicketInfo.setJumpFlag("1");
        }

        // 组装优惠券编号
        String code = ticketCategoryInfoById.getCategoryType() + ticketTypeMap.get(vpsSysTicketInfo.getTicketType());
        String ticketNo = "";
        //查询所有匹配类型的list
        List<VpsSysTicketInfo> ticketList = sysTicketInfoDao.getTicketList(vpsSysTicketInfo);
        //结果集中按照code+3位编码进行写入，ticketList存在进行编码不存在从001开始编码

        if (CollectionUtils.isNotEmpty(ticketList)) {
            //查询同类型编码中数值最大的数
            List<Integer> numList = new ArrayList<>();
            for (VpsSysTicketInfo s : ticketList) {
                numList.add(Integer.parseInt(s.getTicketNo().substring(s.getTicketNo().length() - 3)));
            }
            ticketNo = code + String.format("%03d", Collections.max(numList) + 1);
        } else {
            ticketNo = code + "001";
        }
        vpsSysTicketInfo.setTicketNo(ticketNo);
        vpsSysTicketInfo.setInfoKey(UUIDTools.getInstance().getUUID());

        // 生成优惠券库名称
        String libName = "vps_vcode_ticket_lib" + Constant.DBTSPLIT + vpsSysTicketInfo.getTicketNo();
        // 链接格式
        if (Constant.TICKET_TYPE.ticketType_0.equals(vpsSysTicketInfo.getTicketType())
                || Constant.TICKET_TYPE.ticketType_2.equals(vpsSysTicketInfo.getTicketType())) {
            vpsSysTicketInfo.setTicketCount(0L);
        }
        // 券码格式
        else if (Constant.TICKET_TYPE.ticketType_1.equals(vpsSysTicketInfo.getTicketType())) {
            // 检验是否重复
            String fileName = batchFile.getOriginalFilename();
            /*if ("1".equals(checkBussionName("vcodeTicketInfo", "TICKET_KEY", null, "FILE_NAME", fileName))) {
                resurtMap.put("errMsg", "导入失败，请不要重复导入");
                return resurtMap;
            }*/

            // 解析券码文件
            map = ticketActivityCogService.checkTicketFileByTxt(batchFile, vpsSysTicketInfo.getInfoKey(), DateUtil.getDateTime());
            if ((int) map.get("ticketCount") == 0) {
                resurtMap.put("errMsg", "导入失败，优惠券文件为空");
                return resurtMap;
            }
            vpsSysTicketInfo.setFileName(fileName);
            vpsSysTicketInfo.setLibName(libName);

            // 创建券码表
            ticketLibService.createTicketTable(libName);
        }


        // 保存券码
        if (Constant.TICKET_TYPE.ticketType_1.equals(vpsSysTicketInfo.getTicketType())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("filePath", map.get("filePath"));
            paramMap.put("libName", libName);
            int count = ticketLibService.addTicketCodeToData(paramMap);
            resurtMap.put("filePath", map.get("filePath").toString());
            resurtMap.put("errMsg", "导入成功-券码总量：" +
                    map.get("fileCount").toString() + ",成功导入：" + count);
            vpsSysTicketInfo.setTicketCount((long) count);
        } else {
            resurtMap.put("errMsg", "保存成功");
        }

        sysTicketInfoDao.writeTicketDenomination(vpsSysTicketInfo);
        return resurtMap;
    }

    public String delTicketDenomination(String infoKey) {
        VpsSysTicketInfo ticketInfo = sysTicketInfoDao.findById(infoKey);
        if (null != ticketInfoService.findByTicketNo(ticketInfo.getTicketNo())){
            return "删除失败!此优惠券已投放活动无法删除";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        sysTicketInfoDao.delTicketDenomination(map);
        return "删除成功";
    }

    public VpsSysTicketInfo findTicketDenominationByKey(String infoKey) {
        return sysTicketInfoDao.findTicketDenominationByKey(infoKey);
    }

    public void updateTicketDenominationEdit(VpsSysTicketInfo vpsSysTicketInfo) throws Exception {
        if ("".equals(vpsSysTicketInfo.getEndDate())){
            vpsSysTicketInfo.setEndDate(null);
        }
        sysTicketInfoDao.updateTicketDenominationEdit(vpsSysTicketInfo);
        String key = RedisApiUtil.CacheKey.ticket
                .KEY_SYS_TICKET_INFO + Constant.DBTSPLIT + vpsSysTicketInfo.getTicketNo();
        // 清除优惠券面额缓存
        CacheUtilNew.removeByKey(key);
        // 清除优惠券活动缓存
    }

    public String writeTicketCategory(VpsSysTicketCategory vpsSysTicketCategory) {
        //查询优惠券类型名称是否重复
        if (sysTicketCategoryDao.queryTicketCategoryByName(vpsSysTicketCategory.getCategoryName()) != null) {
            return "优惠券类型名称重复!";
        }
        String categoryType = null;
        // 查询全部类型
        List<VpsSysTicketCategory> vpsSysTicketCategoryList = sysTicketCategoryService.loadTicketCategory();
        if (CollectionUtils.isNotEmpty(vpsSysTicketCategoryList)) {
            List<Integer> numList = new ArrayList<>();
            for (VpsSysTicketCategory ticketCategory : vpsSysTicketCategoryList) {
                if (ticketCategory.getCategoryType().length() > 1) {
                    int num = Integer.parseInt(ticketCategory.getCategoryType().substring(1));
                    if (num<950){
                        numList.add(num);
                    }
                }
            }
            if (numList.size() == 0) {
                numList.add(0);
            }
            int maxNum = Collections.max(numList) + 1;
            if (maxNum>=950){
                return "优惠券类型编码已触及预留限定编码区间!";
            }
            categoryType = "M" + String.format("%03d", maxNum);
        } else {
            categoryType = "M" + "001";
        }
        vpsSysTicketCategory.setCategoryType(categoryType);
        sysTicketCategoryDao.create(vpsSysTicketCategory);
        return "编辑成功";
    }

    public String updateTicketCategoryEdit(VpsSysTicketCategory vpsSysTicketCategory) throws Exception {
        //查询优惠券类型名称是否重复
        VpsSysTicketCategory vpsSysTicketCategoryInfo = sysTicketCategoryDao.queryTicketCategoryByName(vpsSysTicketCategory.getCategoryName());
        if (vpsSysTicketCategoryInfo != null && !vpsSysTicketCategory.getCategoryKey().equals(vpsSysTicketCategoryInfo.getCategoryKey())) {
            return "优惠券类型名称重复!";
        }
        sysTicketCategoryDao.update(vpsSysTicketCategory);
        // 查询此类型下的所有优惠券面额
        List<VpsSysTicketInfo> ticketInfoList = sysTicketInfoDao.findByCategoryKey(vpsSysTicketCategory.getCategoryKey());
        String key = "";
        for (VpsSysTicketInfo vpsSysTicketInfo : ticketInfoList) {
            key = RedisApiUtil.CacheKey.ticket
                    .KEY_SYS_TICKET_INFO + Constant.DBTSPLIT + vpsSysTicketInfo.getTicketNo();
            // 清除优惠券面额缓存
            CacheUtilNew.removeByKey(key);
        }
        RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.ticket.KEY_VCODE_TICKET_ACTIVITY_LIST + Constant.DBTSPLIT + vpsSysTicketCategory.getCategoryType()+ Constant.DBTSPLIT+DateUtil.getDate());
        return "编辑成功";
    }

    public void doTicketCategoryDel(String categoryKey1) {
        sysTicketCategoryDao.doTicketCategoryDel(categoryKey1);
    }

    public Map<String, String> importTicketFile(MultipartFile batchFile, String infoKey, SysUserBasis currentUser) {
        Map<String, String> resurtMap = new HashMap<String, String>();
        Map<String, Object> map = null;
        VpsSysTicketInfo vpsSysTicketInfo = sysTicketInfoDao.findById(infoKey);
        if (!"1".equals(vpsSysTicketInfo.getTicketType())) {
            resurtMap.put("errMsg", "导入失败，此优惠券非券码类型!");
            return resurtMap;
        }
        // 检验是否重复
        String fileName = batchFile.getOriginalFilename();
        /*String[] ticketFileName = vpsSysTicketInfo.getFileName().split(",");
        for (String file : ticketFileName) {
            if (fileName.equals(file)) {
                resurtMap.put("errMsg", "导入失败，请不要重复导入");
                return resurtMap;
            }
        }*/


        // 解析券码文件
        map = ticketActivityCogService.checkTicketFileByTxt(batchFile, vpsSysTicketInfo.getInfoKey(), DateUtil.getDateTime());
        if ((int) map.get("ticketCount") == 0) {
            resurtMap.put("errMsg", "导入失败，优惠券文件为空");
            return resurtMap;
        }

        String libName = "vps_vcode_ticket_lib" + Constant.DBTSPLIT + vpsSysTicketInfo.getTicketNo();
        // 保存券码
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("filePath", map.get("filePath"));
        paramMap.put("libName", libName);
        int count = ticketLibService.addTicketCodeToData(paramMap);
        if ((int)map.get("fileCount") > 0 && count == 0){
            resurtMap.put("errMsg", "导入失败! 券码总量：" +
                    map.get("ticketCount").toString() + ",成功导入：" + count + ",请检查导入内容是否重复!");
        } else {
            resurtMap.put("errMsg", "导入成功-券码总量：" +
                    map.get("fileCount").toString() + ",成功导入：" + count);
            vpsSysTicketInfo.setFileName(vpsSysTicketInfo.getFileName() + "," + fileName);
            vpsSysTicketInfo.setTicketCount(vpsSysTicketInfo.getTicketCount() + count);
            vpsSysTicketInfo.setUpdateUser(currentUser.getUserName());
            vpsSysTicketInfo.setUpdateTime(DateUtil.getDateTime());
            sysTicketInfoDao.updateTicketDenominationEdit(vpsSysTicketInfo);
        }

        return resurtMap;
    }


    public Map<String, String> queryAllTicket() {
        List<VpsSysTicketInfo> sysTicketInfoList = sysTicketInfoDao.queryAllTicket();
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(sysTicketInfoList)){
            String categoryType = "";
            StringBuilder builder = new StringBuilder();
            for (VpsSysTicketInfo item : sysTicketInfoList) {

                // 优惠券明细
                builder.append(item.getCategoryType());
                builder.append(item.getCategoryName());
                builder.append("-");
                builder.append(item.getTicketName());
                builder.append("（");
                if (Constant.TICKET_TYPE.ticketType_0.equals(item.getTicketType())) {
                    builder.append("链接");
                } else if (Constant.TICKET_TYPE.ticketType_1.equals(item.getTicketType())) {
                    builder.append("券码");
                } else if (Constant.TICKET_TYPE.ticketType_2.equals(item.getTicketType())) {
                    builder.append("图片");
                } else if (Constant.TICKET_TYPE.ticketType_3.equals(item.getTicketType())) {
                    builder.append("动态券码");
                } else if (Constant.TICKET_TYPE.ticketType_4.equals(item.getTicketType())) {
                    builder.append("活动编码");
                }
                builder.append("）");
                resultMap.put(item.getTicketNo(), builder.toString());
                builder.setLength(0);
            }
        }
        return resultMap;
    }
}
