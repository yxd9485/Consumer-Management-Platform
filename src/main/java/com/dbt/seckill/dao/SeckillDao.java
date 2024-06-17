package com.dbt.seckill.dao;

import java.util.List;
import java.util.Map;

import com.dbt.seckill.bean.SeckillActivityBean;
import com.dbt.seckill.bean.SeckillCogMoney;

public interface SeckillDao {
	
	public List<SeckillActivityBean> getSeckillList(Map<String,Object> map);
	public int getSeckillCount(Map<String,Object> map);
	public int checkName(Map<String,Object> map);
	public void addSeckill(SeckillActivityBean bean);
	public void delCogMoney(String ruleKey);
	public void saveCogMoney(Map<String, Object> map);
	public void updateSeckill(SeckillActivityBean batch);
	public List<SeckillCogMoney> getRuleList(String sedkillId);
	public int getSumNumById(String sedkillId);
}

