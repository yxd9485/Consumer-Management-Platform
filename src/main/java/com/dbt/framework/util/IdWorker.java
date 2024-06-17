package com.dbt.framework.util;

import org.joda.time.DateTime;

import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;

/**
 * 订单号生成
 * @author zhaohongtao
 *2017年11月24日
 */
public class IdWorker {

	private final long workerId;
	private final static long twepoch = 1288834974657L;
	private long sequence = 0L;
	private final static long workerIdBits = 4L;
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 10L;

	private final static long workerIdShift = sequenceBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;

	public IdWorker(final long workerId) {
		super();
		if (workerId > this.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					this.maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & this.sequenceMask;
			if (this.sequence == 0) {
//				System.out.println("###########" + sequenceMask);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(
						String.format(
								"Clock moved backwards.  Refusing to generate id for %d milliseconds",
								this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift))
				| (this.workerId << this.workerIdShift) | (this.sequence);
//		System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
//				+ timestampLeftShift + ",nextId:" + nextId + ",workerId:"
//				+ workerId + ",sequence:" + sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private synchronized static long timeGen() {
		//同步代码块 暂停1毫秒获取时间，避免ID相同
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}
	
	
	public static void main(String[] args){
//		for (int i = 0; i <100; i++) {
//			System.out.println(getIdByExchangeType("1"));
////			System.out.println(new IdWorker(1).timeGen());
//		}
		System.out.println(new DateTime().toString("yyyyMMddHHmmss"));
	}
	/**
	 * 根据奖品类型生成不同的订单号
	 * @param type 1.实物, 2.电子券 , 3.流量 , 4.积分, 5.现金, 6.谢谢惠顾,7.足球游戏
	 * @return
	 */
	public static String getIdByExchangeType(String type){
		String id = (timeGen()+"").substring(5);
		String date=new DateTime().toString("yyyyMMdd");
		return "E" + type + date + id;
	}
	/**
	 * 根据transtype类型生成不同的订单号
	 * @param type 1.投注, 2.开奖 , 3.查询 
	 * @return
	 */
	public static String getIdByLotteryType(String type){
		String id = (timeGen()+"").substring(5);
		String date=new DateTime().toString("yyyyMMdd");
		return "L" + type + date + id;
	}
	public static String getIdByPay() {
		String id=(timeGen()+"").substring(5);
		String date=new DateTime().toString("yyyyMMddHHmmss");
		return "P" + date + id;
	}
	public static String getIdByRefund() {
		String id=(timeGen()+"").substring(5);
		String date=new DateTime().toString("yyyyMMddHHmmss");
		return "R" + date + id;
	}
	public static String getCommonId(String idType) throws Exception {
		synchronized (IdWorker.class) {
			SysDataDicService dataDicService = (SysDataDicService) BeanFactoryUtil
					.getbeanFromWebContext("sysDataDicService");
			String categoryCode=DatadicKey.dataDicCategory.COMMON_ID;
			SysDataDic dicm=dataDicService.getDatadicByCatCodeAndDataid(categoryCode,idType);
			int next=Integer.parseInt(dicm.getDataValue());
			String nextId=String.format("%04d", next);
			dicm.setDataValue((next+1)+"");
			dataDicService.updateDataDicByDataId(dicm);
			return dicm.getDataAlias()+new DateTime().getYear()+idType+nextId;
		}
	}
}
