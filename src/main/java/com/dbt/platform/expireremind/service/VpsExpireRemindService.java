package com.dbt.platform.expireremind.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.service.VcodeActivityLibRelationServiceImpl;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;
import com.dbt.platform.expireremind.bean.VpsMsgExpireRemindInfo;
import com.dbt.platform.expireremind.dao.IVpsMsgExpireRemindInfoDao;
import com.dbt.platform.promotion.bean.VpsVcodeBindPromotionCog;
import com.dbt.platform.promotion.service.VpsVcodeBindPromotionCogService;
import com.dbt.platform.signin.bean.VpsVcodeSigninCog;
import com.dbt.platform.signin.service.VpsVcodeSigninCogService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 一万一批次激活人员授权配置表Service
 */
@Service
public class VpsExpireRemindService extends BaseService<VpsMsgExpireRemindInfo> {
	private final static Log log = LogFactory.getLog(VpsExpireRemindService.class);
	@Autowired
	IVpsMsgExpireRemindInfoDao  expireRemindInfoDao;

	@Autowired
	private VcodeActivityRebateRuleCogService ruleCogService;

	@Autowired
	private VcodeQrcodeBatchInfoService batchInfoService;

	@Autowired
	private VpsVcodeDoublePrizeCogService vpsVcodeDoublePrizeCogService;

	@Autowired
	private VpsVcodeSigninCogService signinCogService;

	@Autowired
	private VpsVcodeBindPromotionCogService bindPromotionCogService;

	@Autowired
	private VcodeActivityService activityService;

   /*  public void  createExpireRemindInfoDao (VpsMsgExpireRemindInfo msg){
      	expireRemindInfoDao.insertSelective(msg);
	 }*/

	public List<VpsMsgExpireRemindInfo> selectByActivityParamer(HashMap<String,Object> map){
		return expireRemindInfoDao.selectByActivityParamer(map);
	}

	public List<VpsMsgExpireRemindInfo> selectByActivityParamerCondition(HashMap<String,Object> map){
		return expireRemindInfoDao.selectByActivityParamerCondition(map);
	}

	/* public void updateByPrimaryKeySelective (VpsMsgExpireRemindInfo msg){
		 expireRemindInfoDao.updateByPrimaryKeySelective(msg);
	 }*/

	public VpsMsgExpireRemindInfo selectByTypeAndActivityId(VpsMsgExpireRemindInfo msg){
		return expireRemindInfoDao.selectByTypeAndActivityId(msg);
	}

	public void  updateTotalMsgExpireRemind() throws Exception{
		log.warn("规则/批次到期提醒统计开始======");
		//1、 规则到期信息提醒统计
		List<VcodeActivityRebateRuleCog> ruleList = ruleCogService.queryRuleExpireRemind();

		for (int i = 0; i < ruleList.size(); i++) {
			VcodeActivityRebateRuleCog ruleCog = ruleList.get(i);
			VpsMsgExpireRemindInfo   resultMsg=   setActivityNameAndDate(ruleCog);
			if (StringUtils.isNotEmpty(ruleCog.getTotal()) && !("0").equals(ruleCog.getTotal())&&null!=resultMsg.getVcodeActivityKey()) {
				VpsMsgExpireRemindInfo vmsg = new VpsMsgExpireRemindInfo();
				vmsg.setActivityType(ruleCog.getActivityType());
				vmsg.setMsgType(Constant.MSG_Type.remind_0);// 规则到期
				vmsg.setMsgContent("规则将要到期"+ruleCog.getTotal()+"条");
				vmsg.setVcodeActivityKey(ruleCog.getVcodeActivityKey());
				vmsg.setActivityEndDate(resultMsg.getActivityEndDate());
				vmsg.setActivityNo(resultMsg.getActivityNo());
				vmsg.setActivityStartDate(resultMsg.getActivityStartDate());
				vmsg.setActivityName(resultMsg.getActivityName());
				VpsMsgExpireRemindInfo msgFind = selectByTypeAndActivityId(vmsg);
				if (null != msgFind && null != msgFind.getVcodeActivityKey()) {
				    vmsg.setInfoKey(msgFind.getInfoKey());
                    expireRemindInfoDao.updateByPrimaryKeySelective(vmsg);
				/*	updateByPrimaryKeySelective(vmsg);*/
				} else {
                    vmsg.setTopFlag("0");
                    vmsg.setReadFlag("0");
                    expireRemindInfoDao.insert(vmsg);
				}
			}
		}

		//2、批次到期提醒
		List<VcodeQrcodeBatchInfo> batchInfos=   batchInfoService.queryBatchExpireRemind();
		for (int i =0;i<batchInfos.size();i++){
			VcodeQrcodeBatchInfo batchInfo= batchInfos.get(i);
			VpsMsgExpireRemindInfo vbatchMsg = new VpsMsgExpireRemindInfo();
			vbatchMsg.setVcodeActivityKey(batchInfo.getVcodeActivityKey());
            vbatchMsg.setActivityStartDate(batchInfo.getStartDate());
            vbatchMsg.setActivityEndDate(batchInfo.getEndDate());
            vbatchMsg.setActivityNo(batchInfo.getActivityNo());
            vbatchMsg.setActivityName(batchInfo.getActivityName());
			vbatchMsg.setActivityType(Constant.ACTIVITY_TYPE.activityType_0);
			vbatchMsg.setMsgContent("该活动下有"+batchInfo.getTotal()+"批次将要到期");
			vbatchMsg.setMsgType(Constant.MSG_Type.remind_4);// 规则到期
            VpsMsgExpireRemindInfo msgFind = selectByTypeAndActivityId(vbatchMsg);
            if (null != msgFind && null != msgFind.getVcodeActivityKey()) {
                vbatchMsg.setInfoKey(msgFind.getInfoKey());
                expireRemindInfoDao.updateByPrimaryKeySelective(vbatchMsg);
              /*  updateByPrimaryKeySelective(vbatchMsg);*/
            } else {
                vbatchMsg.setReadFlag("0");
                vbatchMsg.setTopFlag("0");
                expireRemindInfoDao.insert(vbatchMsg);
            }
		}
		log.warn("规则/批次到期提醒统计结束======");
	}

	public  void updateTotalRedPacketMsgExpireRemind() throws Exception{
		log.warn("红包金额/红包个低于阈值统计开始======");
		HashMap<String, List<VcodeActivityRebateRuleCog>> cashAndVpoints=  ruleCogService.queryRedPacketRule();
		List<VcodeActivityRebateRuleCog> moneyRules = cashAndVpoints.get(Constant.MSG_Type.remind_1);
		List<VcodeActivityRebateRuleCog> voptionsRules = cashAndVpoints.get(Constant.MSG_Type.remind_2);
		List<VcodeActivityRebateRuleCog> bottleRules = cashAndVpoints.get(Constant.MSG_Type.remind_3);

		for (int i = 0; i < moneyRules.size(); i++) {

			VcodeActivityRebateRuleCog moneyRule = moneyRules.get(i);
			VpsMsgExpireRemindInfo moneyMsg = new VpsMsgExpireRemindInfo();
			VpsMsgExpireRemindInfo  resultMsg= setActivityNameAndDate(moneyRule);
			if (0 < Integer.parseInt(moneyRule.getTotal())&&null!=resultMsg.getVcodeActivityKey()) {
				moneyMsg.setMsgType(Constant.MSG_Type.remind_1);
				moneyMsg.setActivityType(moneyRule.getActivityType());
				moneyMsg.setVcodeActivityKey(moneyRule.getVcodeActivityKey());
				moneyMsg.setActivityName(resultMsg.getActivityName());
				moneyMsg.setActivityNo(resultMsg.getActivityNo());
				moneyMsg.setActivityStartDate(resultMsg.getActivityStartDate());
				moneyMsg.setActivityEndDate(resultMsg.getActivityEndDate());
				VpsMsgExpireRemindInfo msgFind = selectByTypeAndActivityId(moneyMsg);
				if (null !=msgFind && msgFind.getInfoKey() != null) {
                    moneyMsg.setInfoKey(msgFind.getInfoKey());
                    expireRemindInfoDao.updateByPrimaryKeySelective(moneyMsg);
				/*	updateByPrimaryKeySelective(moneyMsg);*/
				} else {
				    moneyMsg.setMsgContent("钱数低于10%");
                    moneyMsg.setTopFlag("0");
                    moneyMsg.setReadFlag("0");
                    expireRemindInfoDao.insert(moneyMsg);
				/*	createExpireRemindInfoDao(moneyMsg);*/
				}
			}


		}
		for (int i = 0; i < voptionsRules.size(); i++) {
			VpsMsgExpireRemindInfo voptionsMsg = new VpsMsgExpireRemindInfo();
			VcodeActivityRebateRuleCog voptionsRule = voptionsRules.get(i);
			VpsMsgExpireRemindInfo  resultMsg= setActivityNameAndDate(voptionsRule);
			if (0 < Integer.parseInt(voptionsRule.getTotal())&&null!=resultMsg.getVcodeActivityKey()) {
				voptionsMsg.setMsgType(Constant.MSG_Type.remind_2);
				voptionsMsg.setActivityType(voptionsRule.getActivityType());
				voptionsMsg.setVcodeActivityKey(voptionsRule.getVcodeActivityKey());
				voptionsMsg.setActivityName(resultMsg.getActivityName());
				voptionsMsg.setActivityNo(resultMsg.getActivityNo());
				voptionsMsg.setActivityStartDate(resultMsg.getActivityStartDate());
				voptionsMsg.setActivityEndDate(resultMsg.getActivityEndDate());
				VpsMsgExpireRemindInfo msgFind = selectByTypeAndActivityId(voptionsMsg);
				if (null != msgFind && null != msgFind.getInfoKey()  ) {
                    voptionsMsg.setInfoKey(msgFind.getInfoKey());
                    expireRemindInfoDao.updateByPrimaryKeySelective(voptionsMsg);
				/*	updateByPrimaryKeySelective(voptionsMsg);*/
				} else {
				/*	createExpireRemindInfoDao(voptionsMsg);*/
                    voptionsMsg.setMsgContent("积分低于10%");
                    voptionsMsg.setTopFlag("0");
                    voptionsMsg.setReadFlag("0");
                    expireRemindInfoDao.insert(voptionsMsg);
				}
			}
		}
		for (int i = 0; i < bottleRules.size(); i++) {
			VpsMsgExpireRemindInfo bottleMsg = new VpsMsgExpireRemindInfo();
			VcodeActivityRebateRuleCog bottleRule = bottleRules.get(i);
			VpsMsgExpireRemindInfo  resultMsg= setActivityNameAndDate(bottleRule);
			if (0 < Integer.parseInt(bottleRule.getTotal())&&null!=resultMsg.getVcodeActivityKey()) {
				bottleMsg.setMsgType(Constant.MSG_Type.remind_3);
				bottleMsg.setActivityType(bottleRule.getActivityType());
				bottleMsg.setVcodeActivityKey(bottleRule.getVcodeActivityKey());
				bottleMsg.setActivityName(resultMsg.getActivityName());
				bottleMsg.setActivityNo(resultMsg.getActivityNo());
				bottleMsg.setActivityStartDate(resultMsg.getActivityStartDate());
				bottleMsg.setActivityEndDate(resultMsg.getActivityEndDate());
				VpsMsgExpireRemindInfo msgFind = selectByTypeAndActivityId(bottleMsg);
				if (null!=msgFind && msgFind.getInfoKey() != null) {
				    bottleMsg.setInfoKey(msgFind.getInfoKey());
                    expireRemindInfoDao.updateByPrimaryKeySelective(bottleMsg);
				/*	updateByPrimaryKeySelective(bottleMsg);*/
				} else {
                    bottleMsg.setMsgContent("红包个数低于10%");
                    bottleMsg.setReadFlag("0");
                    bottleMsg.setTopFlag("0");
				/*	createExpireRemindInfoDao(bottleMsg);*/
                    expireRemindInfoDao.insert(bottleMsg);
				}
			}
		}
		log.warn("红包金额/红包个低于阈值统计结束======");

	}




	public  VpsMsgExpireRemindInfo  setActivityNameAndDate(VcodeActivityRebateRuleCog ruleCog) throws Exception{

		VpsMsgExpireRemindInfo remindInfo= new VpsMsgExpireRemindInfo();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd ");
		if(Constant.ACTIVITY_TYPE.activityType_0.equals(ruleCog.getActivityType())){
			VcodeActivityCog activityCog= activityService.findById(ruleCog.getVcodeActivityKey());
			if(null !=activityCog && compareToTime(activityCog.getStartDate(),activityCog.getEndDate())) {
                remindInfo.setVcodeActivityKey(ruleCog.getVcodeActivityKey());
                remindInfo.setActivityName(activityCog.getVcodeActivityName());
                remindInfo.setActivityNo(activityCog.getVcodeActivityNo());
                remindInfo.setActivityStartDate(activityCog.getStartDate());
                remindInfo.setActivityEndDate(activityCog.getEndDate());
            }

		}
		if(Constant.ACTIVITY_TYPE.activityType_5.equals(ruleCog.getActivityType())){
			VpsVcodeBindPromotionCog bindPromotionCog= bindPromotionCogService.findById(ruleCog.getVcodeActivityKey());
            if(null !=bindPromotionCog && compareToTime(bindPromotionCog.getStartDate(),bindPromotionCog.getEndDate())) {
                remindInfo.setVcodeActivityKey(ruleCog.getVcodeActivityKey());
                remindInfo.setActivityName(bindPromotionCog.getActivityName());
                remindInfo.setActivityNo(bindPromotionCog.getActivityNo());
                remindInfo.setActivityStartDate(bindPromotionCog.getStartDate());
                remindInfo.setActivityEndDate(bindPromotionCog.getEndDate());
            }
		}
		if(Constant.ACTIVITY_TYPE.activityType_4.equals(ruleCog.getActivityType())){
			VpsVcodeSigninCog signinCog= signinCogService.findById(ruleCog.getVcodeActivityKey());
            if(null !=signinCog && compareToTime(signinCog.getStartDate(),signinCog.getEndDate())) {
                remindInfo.setVcodeActivityKey(ruleCog.getVcodeActivityKey());
                remindInfo.setActivityName(signinCog.getActivityName());
                remindInfo.setActivityNo(signinCog.getActivityNo());
                remindInfo.setActivityStartDate(signinCog.getStartDate());
                remindInfo.setActivityEndDate(signinCog.getEndDate());
            }
		}

		if(Constant.ACTIVITY_TYPE.activityType_6.equals(ruleCog.getActivityType())){
			VpsVcodeDoublePrizeCog doublePrizeCog= vpsVcodeDoublePrizeCogService.findById(ruleCog.getVcodeActivityKey());
            if(null !=doublePrizeCog && compareToTime(doublePrizeCog.getStartDate(),doublePrizeCog.getEndDate())) {
                remindInfo.setVcodeActivityKey(ruleCog.getVcodeActivityKey());
                remindInfo.setActivityName(doublePrizeCog.getActivityName());
                remindInfo.setActivityNo(doublePrizeCog.getActivityNo());
                remindInfo.setActivityStartDate(doublePrizeCog.getStartDate());
                remindInfo.setActivityEndDate(doublePrizeCog.getEndDate());
            }
		}

		return  remindInfo;
	}

	//统计提醒消息
	public int conutMsgExpireReminds(HashMap<String,Object> map) {

	 return	expireRemindInfoDao.selectMsgExpireRemindTotalCondition(map);
	}

	@Transactional
    public int conutMsgExpireRemind() {
        return	expireRemindInfoDao.selectMsgExpireRemindTotal();
    }
    //统计提醒信息数
	public int selectTotalNum() {
	return	expireRemindInfoDao.selectMsgExpireRemindTotal();
	}

	//更新是否已读，
	public void updateStatusForRead(String list,String status) {
     	HashMap<String,Object> map = new HashMap();
     	List lists = getList(list.split(","));
     	map.put("lists",lists);
     	map.put("status",status);
		expireRemindInfoDao.updateStatusForRead(map);
     }

	public  List<String> getList(String[] arr) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}
    /**
	 *   置顶
	 * */
	public void stickMsg(String infoKey, String topFlag) {
		Map<String,Object> map  = new HashMap<>();
		map.put("key",infoKey);
		map.put("topFlag",topFlag);
		expireRemindInfoDao.stickMsg(map);
	}
	
	public boolean compareToTime(String startTime,String endTime) throws Exception{
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarStart= Calendar.getInstance();
        Calendar calendarEnd= Calendar.getInstance();
        Calendar calendarNow= Calendar.getInstance();

        Date now = new Date();
        calendarStart.setTime(sf.parse(startTime));
        calendarEnd.setTime(sf.parse(endTime));
        calendarNow.setTime(now);


        if(-1==calendarNow.compareTo(calendarEnd) && 1==calendarNow.compareTo(calendarStart)){
           return true;
        }else{
         return false;
        }
    
    }

    /***
     * 清理过期提醒消息
     */
    public void deleteCleanMsgExpireRemindInfo() {
        log.warn("清除提醒信息超过日期信息开始======");
        try {
            List<String> list = expireRemindInfoDao.findIdsByTime();
            if(null!=list&&list.size()>0) {
                HashMap map = new HashMap();
              //  map.put("deleteFlag", "1");
                map.put("list", list);
                expireRemindInfoDao.delByInfoKey(map);
            }
        }catch(Exception e){
            log.error("清除提醒信息超过日期信息异常======",e);
        }
        log.warn("清除提醒信息超过日期信息结束======");
        
    }
}

