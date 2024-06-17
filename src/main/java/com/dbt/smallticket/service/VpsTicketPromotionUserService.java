package com.dbt.smallticket.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.oyshoVote.bean.VpsVoteOyshoInfo;
import com.dbt.platform.oyshoVote.dao.IVpsVoteOyshoInfoDao;
import com.dbt.platform.oyshoVote.dao.IVpsVoteOyshoRecordDao;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.smallticket.bean.VpsTicketPromotionUser;
import com.dbt.smallticket.bean.VpsTicketWararea;
import com.dbt.smallticket.dao.IVpsTicketPromotionUserDao;

@Service
public class VpsTicketPromotionUserService extends BaseService<VpsTicketPromotionUser> {
    private Logger log = Logger.getLogger(this.getClass());
    
    @Autowired
    private IVpsTicketPromotionUserDao promotionUserDao;
    @Autowired
	private IVpsVoteOyshoInfoDao oyshoInfoDao;
	@Autowired
	private IVpsVoteOyshoRecordDao oyshoRecordDao;
	
    
    public List<VpsTicketPromotionUser> queryForLst(VpsTicketPromotionUser queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		return promotionUserDao.queryForLst(queryMap);
	}

	public int queryForCount(VpsTicketPromotionUser queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return promotionUserDao.queryForCount(queryMap);
	}
    
    public VpsTicketPromotionUser findByUserKey(String userKey) {
        return promotionUserDao.findByUserKey(userKey);
    }
    
    public VpsTicketPromotionUser findByPhoneNum(String phoneNum) {
        return promotionUserDao.findByPhoneNum(phoneNum);
    }

	public VpsTicketPromotionUser findById(String infoKey) {
		return promotionUserDao.findById(infoKey);
	}

	public void insertPromotionUser(VpsTicketPromotionUser promotionUser) {
	    promotionUser.setTerminalName(StringUtils.defaultIfBlank(promotionUser.getTerminalName(), "").trim());
		promotionUserDao.create(promotionUser);
	}
	
	/**
	 * 	修改
	 * @param promotionUser
	 */
	public void updatePromotionUser(VpsTicketPromotionUser promotionUser) {
		// 报名结束时间
		String endTime = null;
		
		// 查询投票报名时间范围
		String oushoVoteTime = DatadicUtil.getDataDicValue(
				DatadicUtil.dataDicCategory.TICKET_COG,
				DatadicUtil.dataDic.ticketCog.OYSHO_APPLY_BEGIN_END_DATE);
		 if(StringUtils.isNotBlank(oushoVoteTime)) {
			 endTime  = oushoVoteTime.split(",")[1];
		 }
		
		// 查询促销员数据库信息
		VpsTicketPromotionUser dBpromotionUser = promotionUserDao.findById(promotionUser.getInfoKey());
		if("1".equals(dBpromotionUser.getIsJoinVote()) && StringUtils.isNotBlank(endTime)) {
			// 报名时间未截止（报名表信息删除，重置促销员，保留促销员表的报名数据，并且审核状态改为驳回）
			if(endTime.compareTo(DateUtil.getDate()) >= 0) {
				// 删除报名表信息
				if(Constant.COMMON_CHECK_STATUS.CHECK_STATUS_1.equals(dBpromotionUser.getCheckStatus())) {
					deletePromotionVote(dBpromotionUser.getUserKey());
				}
				
				// 审核状态改为驳回
				promotionUser.setCheckStatus(Constant.COMMON_CHECK_STATUS.CHECK_STATUS_3);
				promotionUser.setCheckOpinion("您的信息已被修改，请重新检查并提交报名信息");
			}
			// 报名时间已截止（报名表信息不做更改，只重置促销员基础信息）
			else {
				promotionUser.setCheckStatus(dBpromotionUser.getCheckStatus());
				promotionUser.setCheckOpinion(dBpromotionUser.getCheckOpinion());
			}
		}else {
			// 时间未配置时默认只重置促销员信息
			promotionUser.setCheckOpinion(dBpromotionUser.getCheckOpinion());
		}
		
		// 更新
		promotionUser.setTerminalName(StringUtils.defaultIfBlank(promotionUser.getTerminalName(), "").trim());
		promotionUserDao.update(promotionUser);
	}
	
	/**
	 * 	审核
	 * @param promotionUser
	 */
	public void updatePromotionUserForCheck(VpsTicketPromotionUser promotionUser) {
		promotionUserDao.updatePromotionUserForCheck(promotionUser);

		// 审核通过，需要添加评选相关记录
		if(Constant.COMMON_CHECK_STATUS.CHECK_STATUS_1.equals(promotionUser.getCheckStatus())) {
			VpsTicketPromotionUser dbPromotionUser = findById(promotionUser.getInfoKey());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tableSchema", PropertiesUtil.getPropertyValue("run_env").equals("TEST") ? "vjien_common_zlcny" : "vjifen_zlcny");
			String oyshoNo = oyshoInfoDao.findAutoId(params) + "";
			VpsVoteOyshoInfo voteOyshoInfo = new VpsVoteOyshoInfo(oyshoNo,
					dbPromotionUser.getUserKey(), dbPromotionUser.getUserName(), 
					dbPromotionUser.getUploadPic().split(",")[0], dbPromotionUser.getPhoneNum(), 
					dbPromotionUser.getProvince(), dbPromotionUser.getCity(), dbPromotionUser.getCounty(), 
					dbPromotionUser.getTerminalName(), dbPromotionUser.getIntroduce(), 
					dbPromotionUser.getUploadPic(), dbPromotionUser.getWarAreaName(), dbPromotionUser.getFirstImgH(), dbPromotionUser.getTicketChannel());
			oyshoInfoDao.create(voteOyshoInfo);
		}
	}

	public List<VpsTicketWararea> queryWarareaAll() {
		return promotionUserDao.queryWarareaAll();
	}

	/**
	 * 	删除
	 * @param id
	 */
	public void deleteById(String id) {
		VpsTicketPromotionUser promotionUser = findById(id);
		if(null != promotionUser) {
			// 删除报名投票信息
			deletePromotionVote(promotionUser.getUserKey());
			
			// 删除促销员
			promotionUserDao.deleteById(id);
		}
	}

	/**
	 * 	删除评选信息
	 * @param id
	 */
	private void deletePromotionVote(String userKey) {
		oyshoInfoDao.deleteByUserKey(userKey);
		oyshoRecordDao.deleteByUserKey(userKey);
	}

	public String savePromotionUser(List<List<Object>> readExcel, SysUserBasis currentUser) {

        // 校验表头及属性名称
        List<Object> objects = readExcel.get(0);
        String[] headers = {"省", "市", "区", "姓名", "手机号", "外勤365编码", "所属战区","所属渠道", "终端系统", "门店编号", "门店名称", "门店地址"};
        if (objects.size() != headers.length) {
            return "导入失败，请检查Excel模板是否是最新版本！！";
        }
        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].equals(objects.get(i))) {
                return "导入失败，请检查Excel模板是否是最新版本！！";
            }
        }

        List<String> phoneList = new ArrayList<String>();
        List<VpsTicketPromotionUser> list = new ArrayList<VpsTicketPromotionUser>();
        
        String dateTime = DateUtil.getDateTime();
        for (int i = 0; i < readExcel.size(); i++) {
            if (i == 0) {
                continue;
            }
            List<Object> promotionUser = readExcel.get(i);
            if (promotionUser.size() <= 0) {
            	break;
            }
            VpsTicketPromotionUser instance = new VpsTicketPromotionUser();
            instance.setProvince(promotionUser.get(0).toString());
            instance.setCity(promotionUser.get(1).toString());
            instance.setCounty(promotionUser.get(2).toString());
            instance.setUserName(promotionUser.get(3).toString().trim().replace(".00", ""));
            instance.setPhoneNum(promotionUser.get(4).toString().trim().replace(".00", ""));
            instance.setUserCode(promotionUser.get(5).toString().trim().replace(".00", ""));
            instance.setWarAreaName(promotionUser.get(6).toString().trim().replace(".00", ""));
            instance.setTicketChannel(promotionUser.get(7).toString().trim().replace(".00", ""));
            instance.setTerminalSystem(promotionUser.get(8).toString().trim().replace(".00", ""));
            instance.setTerminalCode(promotionUser.get(9).toString().trim().replace(".00", ""));
            instance.setTerminalName(promotionUser.get(10).toString().trim().replace(".00", ""));
            instance.setTerminalAddress(promotionUser.get(11).toString().trim().replace(".00", ""));
            
            if ("酒行渠道".equals(instance.getTicketChannel())) {
                instance.setTicketChannel(Constant.ticketChannel.CHANNEL0);
            } else if ("餐饮渠道".equals(instance.getTicketChannel())) {
                instance.setTicketChannel(Constant.ticketChannel.CHANNEL1);
            } else if ("KA渠道".equals(instance.getTicketChannel())) {
                instance.setTicketChannel(Constant.ticketChannel.CHANNEL2);
            } else if ("电商渠道".equals(instance.getTicketChannel())) {
                instance.setTicketChannel(Constant.ticketChannel.CHANNEL3);
            }
            
            if (StringUtils.isBlank(instance.getUserCode())) {
                return "第" + (i + 1) + "行," + "促销员外勤365编号不能为空，请校验上传文件";
            }
            if (StringUtils.isBlank(instance.getUserName())) {
                return "第" + (i + 1) + "行," + "促销员名称不能为空，请校验上传文件";
            }
            if (StringUtils.isBlank(instance.getPhoneNum())) {
                return "第" + (i + 1) + "行," + "促销员手机号不能为空，请校验上传文件";
            }
            if (!StringUtil.isNumericByPattern(instance.getPhoneNum()) || instance.getPhoneNum().length() != 11) {
                return "第" + (i + 1) + "行," + "促销员手机号格式有误，请校验上传文件";
            }
            if(phoneList.contains(instance.getPhoneNum())) {
            	return "第" + (i + 1) + "行," + "导入文件存在重复手机号，请校验上传文件";
            }
            if (StringUtils.isBlank(instance.getWarAreaName())) {
                return "第" + (i + 1) + "行," + "促销员战区不能为空，请校验上传文件";
            }
            if (StringUtils.isBlank(instance.getTicketChannel())) {
                return "第" + (i + 1) + "行," + "促销员所属渠道不能为空，请校验上传文件";
            }
            phoneList.add(instance.getPhoneNum());
            
            instance.setDeleteFlag("0");
            instance.setCreateTime(dateTime);
            instance.setCreateUser(currentUser.getUserKey());
            list.add(instance);
        }

        List<String> repetitionList = null;
        if(list.size() > 0) {
        	repetitionList = promotionUserDao.queryRepetitionList(list);
            List<VpsTicketPromotionUser> delList = new ArrayList<VpsTicketPromotionUser>();
            for (String phoneNum : repetitionList) {
    			for (VpsTicketPromotionUser item : list) {
    				if(phoneNum.equals(item.getPhoneNum())) {
    					delList.add(item);
    				}
    			}
    		}
            
            list.removeAll(delList);
            if(list.size() > 0) {
            	promotionUserDao.batchInsert(list);
            }
        }
        
        StringBuffer buffer = new StringBuffer();
        if (CollectionUtils.isNotEmpty(repetitionList)) {
            buffer.append("共导入：").append(list.size()).append("个,有").append(repetitionList.size()).append("个未导入成功:\n");
            buffer.append(repetitionList.toString()).append("，失败原因：促销员手机号已存在");
            return buffer.toString();
        } else {
            String msg = "";
            if ((readExcel.size() - 1) <= 0) {
                msg = "共导入：" + (readExcel.size() - 1) + "个" + ",导入表格中未检测到数据，请检查表格数据是否存在";
            } else {
                msg = "共导入：" + (readExcel.size() - 1) + "个" + ",导入成功";
            }
            return msg;
        }

    }

	/**
	 * 	导出
	 * @param queryBean
	 * @param response
	 * @throws IOException 
	 */
	public void exportPromotionUser(VpsTicketPromotionUser queryBean, HttpServletResponse response) throws IOException {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();
        
        // 获取导出信息
        List<VpsTicketPromotionUser> promotionUserLst = queryForLst(queryBean, null);
        for (VpsTicketPromotionUser item : promotionUserLst) {
        	item.setStatus("1".equals(item.getStatus()) ? "已激活" : "未激活");
        	item.setIsJoinVote("1".equals(item.getIsJoinVote()) ? "已参与" : "未参与");
        	item.setCheckStatus(Constant.COMMON_CHECK_STATUS.CHECK_STATUS_1.equals(item.getCheckStatus()) ? "已通过" 
        			: Constant.COMMON_CHECK_STATUS.CHECK_STATUS_2.equals(item.getCheckStatus()) ? "未通过" 
        					: Constant.COMMON_CHECK_STATUS.CHECK_STATUS_3.equals(item.getCheckStatus()) ? "已驳回" : "未审核");
        	if (Constant.ticketChannel.CHANNEL0.equals(item.getTicketChannel())) {
        	    item.setTicketChannel("酒行渠道");
        	} else if (Constant.ticketChannel.CHANNEL1.equals(item.getTicketChannel())) {
                item.setTicketChannel("餐饮渠道");
            } else if (Constant.ticketChannel.CHANNEL2.equals(item.getTicketChannel())) {
                item.setTicketChannel("KA渠道");
            } else if (Constant.ticketChannel.CHANNEL3.equals(item.getTicketChannel())) {
                item.setTicketChannel("电商渠道");
            }
        }

        String bookName = "促销员导出";
        String[] headers = new String[] {"省", "市", "区", "姓名", "手机号", "外勤365编号", "所属战区", "所属渠道", "终端系统", "门店编号", "门店名称", "门店地址", "激活状态", "是否参与评选", "审核状态", "审核备注"};
        String[] valueTags = new String[] {"province", "city", "county", "userName", "phoneNum", "userCode", "warAreaName", "ticketChannel", "terminalSystem", "terminalCode", "terminalName", "terminalAddress", "status", "isJoinVote", "checkStatus", "checkOpinion"};

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpsTicketPromotionUser> excel = new ExcelUtil<VpsTicketPromotionUser>(); 
        excel.writeExcel(bookName, headers, valueTags, promotionUserLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }

}
