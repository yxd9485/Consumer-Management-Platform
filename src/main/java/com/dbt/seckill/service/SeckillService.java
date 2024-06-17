package com.dbt.seckill.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.QrCodeUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.dao.ISysAreaDao;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.autocode.util.RandomDataUtil;
import com.dbt.platform.autocode.util.RandomUtil;
import com.dbt.seckill.bean.SeckillActivityBean;
import com.dbt.seckill.bean.SeckillActivityQuery;
import com.dbt.seckill.bean.SeckillCogMoney;
import com.dbt.seckill.dao.SeckillDao;
import com.dbt.vpointsshop.bean.VpointsCouponBatch;

@Service
public class SeckillService extends BaseService<VpointsCouponBatch>{
    
    /** 秒杀奖项配置项模板字段*/
    public static String[] entities = {"prizeType","money", "amounts", "rangeVal"};
    
	@Autowired
	private SeckillDao secDao;
	@Autowired
	private ISysAreaDao sysAreaDao;
	/**
	 * 电子券批次列表
	 * @param info
	 * @param bean
	 * @return
	 */
	public List<SeckillActivityBean> getSeckillList(PageOrderInfo info, SeckillActivityQuery queryBean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", queryBean);
		return secDao.getSeckillList(map);
	}
	/**
	 * 电子券批次数量
	 * @param info
	 * @param bean
	 * @return
	 */
	public int getSeckillCount(PageOrderInfo info, SeckillActivityQuery queryBean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", queryBean);
		return secDao.getSeckillCount(map);
	}
	public void getSeckillDetail(PageOrderInfo info, SeckillActivityQuery queryBean,Model model) {
		SeckillActivityBean bean = getSeckillList(info,queryBean).get(0);
		changeStringToCode(bean);
		model.addAttribute("bean", bean);
		getRuleList(model,queryBean.getSeckillId());
	}
	public int checkName(String seckillName,String seckillId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillName", seckillName);
		map.put("seckillId", seckillId);
		return secDao.checkName(map);
	}
	/**
	 * 修改状态
	 * @param info
	 * @param queryBean
	 */
	public void  changeStatus(PageOrderInfo info, SeckillActivityQuery queryBean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", queryBean);
		SeckillActivityBean bean= secDao.getSeckillList(map).get(0);
		if(bean.getSeckillStatus().equals("1")) {
			bean.setSeckillStatus("0");
		}else {
			bean.setSeckillStatus("1");
		}
		secDao.updateSeckill(bean);
		String key=RedisApiUtil.CacheKey.seckill.SEC_ACTIVITY+bean.getSeckillVcode();
		RedisApiUtil.getInstance().setObject(true, key, bean);
		logService.saveLog("seckill", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(bean), "更新秒杀配置：" + bean.getSeckillName());
	}
	/**
	 * 删除电子券批次
	 * @param batchKey
	 * @return
	 */
//	public String delBatch(String batchKey){
//		String isSuccess="deleteSuccess";
//		int count=couponDao.getGoodsByBatch(batchKey);//批次是否有商品使用
//		String batchTable=couponDao.getTalbeByBatch(batchKey);//批次对应表
//		Map<String,Object> map =new HashMap<String,Object>();
//		map.put("batchTable", batchTable);
//		map.put("batchKey", batchKey);
//		int hcount=couponDao.getCouponByBatch(map);//批次是否有券被使用
//		if(count>0||hcount>0){
//			return "deleteFalse";
//		}
//		couponDao.delBatchByKey(batchKey);
//		couponDao.delCouponByBatch(map);
//		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3,
//				batchKey, "删除电子券批次:" + batchKey);
//		return isSuccess;
//	}
	/**
	 * 添加批次
	 * @param batch
	 * @return
	 */
	public String addSeckill(SeckillActivityBean bean){
		changeArea(bean);
		newVcode(bean);
		String result="addSuccess";
		secDao.addSeckill(bean);
		//添加到redis
		String key=RedisApiUtil.CacheKey.seckill.SEC_ACTIVITY+bean.getSeckillVcode();
		RedisApiUtil.getInstance().setObject(true, key, bean);
		logService.saveLog("seckill", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(bean), "创建秒杀活动:" + bean.getSeckillName());
		return result;
	}
	/**
	 * 创建二维码
	 * @param bean
	 */
	private void newVcode(SeckillActivityBean bean) {
		String qrcodeUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.QRCODE_URL);
		String projectFlag = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
				DatadicKey.filterCompanyInfo.PROJECT_FLAG);
		String url = qrcodeUrl + projectFlag;
		if(qrcodeUrl.indexOf("vjifen") > -1){
			url += "&v=";
		}else{
			url += "/";
		}
		
		String reStr = "";
		for (int i = 0; i < 9; i++) {
			reStr += RandomUtil.randomString(RandomDataUtil.randomSimple, 1);
		}
		String source = "MS_"+bean.getSeckillType()+reStr;
		
		PageOrderInfo info = new PageOrderInfo(null);
		SeckillActivityQuery q=new SeckillActivityQuery();
		q.setSeckillVcode(source);
		int count=this.getSeckillCount(info,q);
		if(count>0) {
			for (int i = 0; i < 9; i++) {
				reStr += RandomUtil.randomString(RandomDataUtil.randomSimple, 1);
			}
			source = "MS_"+bean.getSeckillType()+reStr;
		}
		url+=source;
		QrCodeUtil.encodeQRCodeImage(url, null, "/data/upload/"+source+".png", 283, 283, null);
		bean.setSeckillVcode(source);
		bean.setSeckillUrl(url);
	}
	/**
	 * 删除规则
	 * @param ruleKey
	 */
	public void delCogMoney(String ruleKey) {
		secDao.delCogMoney(ruleKey);
		
	}
	/**
	 * 删除cogMoney缓存
	 * @param oldBean
	 */
	public void deleCogMoneyCache(SeckillActivityBean oldBean) {
		String redisKey="";
		String dateTpe=oldBean.getSeckillDateType();
		if(dateTpe.equals("1")) {
			redisKey=oldBean.getSeckillId()+":"+oldBean.getSeckillRuleKey();
			String moneyMapKey=RedisApiUtil.CacheKey.seckill.MONEY_MAP+redisKey;
			RedisApiUtil.getInstance().del(true, moneyMapKey);
		}else if(dateTpe.equals("2")) {
			for (int j = 0; j < 2; j++) {
				String[] timeSet=oldBean.getSeckillTimeSet().split("#");
				String nowDate=new DateTime().plusDays(-j).toString("yyyy-MM-dd");
				for (int i = 0; i < timeSet.length; i++) {
					String time=timeSet[i];
					String seckillSession=nowDate+"_"+time;
					redisKey=oldBean.getSeckillId()+":"+oldBean.getSeckillRuleKey()+":"+seckillSession;
					String moneyMapKey=RedisApiUtil.CacheKey.seckill.MONEY_MAP+redisKey;
					System.out.println("delKey:"+moneyMapKey);
					RedisApiUtil.getInstance().del(true, moneyMapKey);
				}
			}
			
		}
	}
	/**
	 * 添加规则
	 * @param batchKey
	 * @param couponList
	 * @param userKey
	 * @return
	 */
	public String addCogMoney(List<VcodeActivityMoneyImport> cogMoneyList,String ruleKey){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleKey", ruleKey);
        map.put("cogMoneyList", cogMoneyList);
        secDao.saveCogMoney(map);
        logService.saveLog("seckill", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(cogMoneyList), "创建红包规则");
		return "addSuccess";
	}
	/**
	 * 更新批次
	 * @param batch
	 * @return
	 */
	public String updateSeckill(SeckillActivityBean bean){
		changeArea(bean);
		String isSuccess="addSuccess";
		secDao.updateSeckill(bean);
		String key=RedisApiUtil.CacheKey.seckill.SEC_ACTIVITY+bean.getSeckillVcode();
		RedisApiUtil.getInstance().setObject(true, key, bean);
		logService.saveLog("seckill", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(bean), "更新秒杀配置：" + bean.getSeckillName());
		return isSuccess;
	}
	private void changeArea(SeckillActivityBean bean) {
		String province = this.transferArea(bean.getProvince());
		String city = this.transferArea(bean.getCity());
		String county = this.transferArea(bean.getCounty());

		if (StringUtils.isNotBlank(province)) {
			bean.setProvince(province);
		} else {
			bean.setProvince(null);
		}

		if (StringUtils.isNotBlank(city)) {
			bean.setCity(city);
		} else {
			bean.setCity(null);
		}

		if (StringUtils.isNotBlank(county)) {
			bean.setCounty(county);
		} else {
			bean.setCounty(null);
		}
	}
	private void changeStringToCode(SeckillActivityBean bean) {
		String province = this.transferString(bean.getProvince(),"1","0");
		String city = this.transferString(bean.getCity(),"2",province);
		String county = this.transferString(bean.getCounty(),"3",city);

		if (StringUtils.isNotBlank(province)) {
			bean.setProvince(province);
		} else {
			bean.setProvince(null);
		}

		if (StringUtils.isNotBlank(city)) {
			bean.setCity(city);
		} else {
			bean.setCity(null);
		}

		if (StringUtils.isNotBlank(county)) {
			bean.setCounty(county);
		} else {
			bean.setCounty(null);
		}
	}
	/**
	 * 转换地区
	 * 
	 * @param areaCode
	 * @return
	 */
	private String transferArea(String areaCode) {
	    String result = "";
	    if (!StringUtils.isEmpty(areaCode) && !areaCode.equals("100000")) {
	    	SysAreaM area = sysAreaDao.findById(areaCode);
	        result = area == null ? null : area.getAreaName().equals("全部")
	                ? null
	               // : area.getAreaName().substring(0,area.getAreaName().length()-1);
	        : area.getAreaName();
	    }
	    return result;
	}
	/**
	 * 转换地区
	 * 
	 * @param areaCode
	 * @return
	 */
	private String transferString(String areaCode,String type,String parent) {
	    String result = "";
	    if (!StringUtils.isEmpty(areaCode)&&!StringUtils.isEmpty(parent)) {
	    	SysAreaM area = sysAreaDao.findChildCode(areaCode,type,parent);
	        result = area == null ? null : area.getAreaCode();
	    }
	    return result;
	}
	public void getRuleList(Model model,String seckillId){
		List<SeckillCogMoney> moneyList=secDao.getRuleList(seckillId);
		int sumNum=secDao.getSumNumById(seckillId);
		double allMoney=0;
		int allCount=0;
		int allRangeVal=0;
		for (SeckillCogMoney seckillCogMoney : moneyList) {
			int rangeVal=seckillCogMoney.getRangeVal();
			allCount+=seckillCogMoney.getCogamounts();
			allMoney+=seckillCogMoney.getVcodeMoney()*seckillCogMoney.getCogamounts();
			allRangeVal+=rangeVal;
			double per = 0;
			if(rangeVal==0){
				per=0.0;
			}else{
				 per = (double)rangeVal / sumNum * 100;
			}
			String percentage = String.valueOf(MathUtil.round(per, 4));
			seckillCogMoney.setPercent(percentage+"%");
		}
		SeckillCogMoney moneyAll=new SeckillCogMoney();
		moneyAll.setVcodeMoney(Double.valueOf(String.format("%.2f",allMoney)));
		moneyAll.setCogamounts(allCount);
		moneyAll.setRangeVal(allRangeVal);
		moneyAll.setPercent("100%");
		model.addAttribute("moneyAll", moneyAll);
		model.addAttribute("moneyList", moneyList);
	}
}
