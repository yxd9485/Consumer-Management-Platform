package com.dbt.platform.drinkcapacity.util;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Tuple;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.drinkcapacity.service.VpsVcodeDrinkCapacityPkRecordService;

/**
 * 酒量1V1PK延迟任务队列监听类
 */
@Component
public class DrinkCapacityPkDelayQueue implements ApplicationContextAware, DisposableBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    @Autowired
    private VpsVcodeDrinkCapacityPkRecordService pkRecordService;
    

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if ("shandongagt".equals(DbContextHolder.getDBType())) {
            // 比赛开始20小时的提醒
            executeDrinkCapacityPkDelayQueue20hour();
            
            // 比赛判定结果提醒
            executeDrinkCapacityPkDelayQueue();
        }
    }
    
    private void executeDrinkCapacityPkDelayQueue20hour() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                log.warn("DrinkCapacityPkDelayQueue20hour启动...");
                
                int emptyNum = 0;
                Tuple item = null;
                String[] valAry = null;
                DbContextHolder.setDBType("shandongagt");
                while (true) {
                    try {
                        Set<Tuple> tupleSet = RedisApiUtil.getInstance().getSoredSetByRangeWithScort(RedisApiUtil
                                .CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_DELAYQUEUE_20HOUR, 0, 1, false);
                        if (tupleSet == null|| tupleSet.isEmpty()) {
                            try {
                                if (emptyNum++ == 600) {
                                    emptyNum = 0;
                                    log.warn("暂无要提醒的1V1PK比赛");
                                }
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // 缓存值格式：infoKey,beginTime
                            item = tupleSet.iterator().next();
                            if (Calendar.getInstance().getTimeInMillis() >= item.getScore()) {
                                emptyNum = 0;
                                valAry = item.getElement().split(",");
                                executorService.execute(new DrinkCapacityPkRunable("0", valAry[0], valAry[1], pkRecordService));
                                
                                // 处理成功后删除当前记录
                                RedisApiUtil.getInstance().delSortedSet(RedisApiUtil.CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_DELAYQUEUE_20HOUR, item.getElement());
                            } else {
                                if (emptyNum++ == 600) {
                                    emptyNum = 0;
                                    log.warn("暂无要提醒的1V1PK比赛");
                                }
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }).start();
    }
    private void executeDrinkCapacityPkDelayQueue() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                log.warn("DrinkCapacityPkDelayQueue启动...");
                
                int emptyNum = 0;
                Tuple item = null;
                String[] valAry = null;
                while (true) {
                    try {
                        Set<Tuple> tupleSet = RedisApiUtil.getInstance().getSoredSetByRangeWithScort(RedisApiUtil
                                .CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_DELAYQUEUE, 0, 1, false);
                        if (tupleSet == null|| tupleSet.isEmpty()) {
                            try {
                                if (emptyNum++ == 600) {
                                    emptyNum = 0;
                                    log.warn("暂无要判定结果的1V1PK比赛");
                                }
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // 缓存值格式：infoKey,beginTime
                            item = tupleSet.iterator().next();
                            if (Calendar.getInstance().getTimeInMillis() >= item.getScore()) {
                                emptyNum = 0;
                                valAry = item.getElement().split(",");
                                executorService.execute(new DrinkCapacityPkRunable("1", valAry[0], valAry[1], pkRecordService));
                                
                                // 处理成功后删除当前记录
                                RedisApiUtil.getInstance().delSortedSet(RedisApiUtil.CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_DELAYQUEUE, item.getElement());
                            } else {
                                if (emptyNum++ == 600) {
                                    emptyNum = 0;
                                    log.warn("暂无要判定结果的1V1PK比赛");
                                }
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }).start();
    }

    @Override
    public void destroy() throws Exception {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}


class DrinkCapacityPkRunable implements Runnable {
    private VpsVcodeDrinkCapacityPkRecordService pkRecordService;
    // 消息类型：0结束前提醒消息、1比赛结果提醒消息
    private String msgType;
    private String infoKey;
    private String beginTime;
    
    public DrinkCapacityPkRunable(String msgType, String infoKey, 
            String beginTime, VpsVcodeDrinkCapacityPkRecordService pkRecordService) {
        this.msgType = msgType;
        this.infoKey = infoKey;
        this.beginTime = beginTime;
        this.pkRecordService = pkRecordService;
    }

    @Override
    public void run() {
    	DbContextHolder.setDBType("shandongagt");
        if ("0".equals(msgType)) {
            pkRecordService.send20HourTips(infoKey, beginTime);
            
        } else if ("1".equals(msgType)) {
            pkRecordService.executePkStatus(infoKey, beginTime);
        }
        
    }
    
}
