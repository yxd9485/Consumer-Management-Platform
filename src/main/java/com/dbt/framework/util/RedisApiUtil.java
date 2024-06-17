package com.dbt.framework.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;

import com.google.common.collect.Maps;
import com.vjifen.server.base.datasource.DDS;
import com.vjifen.server.base.datasource.redis.RedisUtils;

import redis.clients.jedis.Tuple;

public class RedisApiUtil {
    private RedisApiUtil() {
    }

    private static class RedisHelper {
        static RedisApiUtil redisApiUtil = new RedisApiUtil();
    }
    public static RedisApiUtil getInstance() {
        return RedisHelper.redisApiUtil;
    }

    /**
     * 添加到Set中
     * @param key   唯一键
     * @param value 数据值
     */
    public void addSet(String key, String... value) {
        addSet(true, key, value);
    }

    /**
     * 添加到Set中
     * @param isDynamic 是否动态获取
     * @param key   唯一键
     * @param value 数据值
     */
    public void addSet(boolean isDynamic, String key, String... value) {
        if(key == null || value == null)
            return;
        RedisUtils redisUtils = getRedisUtils(isDynamic);
        key = getKey(isDynamic, key);
        redisUtils.STRING.SET.sAdd(key, value);
    }

    /**
     * 判断值是否包含在set中
     * @param key   唯一键
     * @param value 数据值
     * @return  存在返回true
     */
    public boolean containsInSet(String key, String value) {
        if(key == null || value == null)
            return false;
        RedisUtils redisUtils = getRedisUtils(true);
        key = getKey(true, key);
        return redisUtils.STRING.SET.sIsMember(key, value);
    }

    public static String getProjectServerName(){
        return DDS.get() + ":";
    }

    /**
     * 获取Set
     * @param key   唯一键
     * @return  Set对象
     */
    public Set<String> getSet(String key) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.SET.sMembers(key);
    }

    /**
     * 从set中删除value
     * @param key   唯一键
     */
    public void removeSetValue(String key,String... value) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        Object[] v = Arrays.stream(value).map(e -> (Object)e).toArray();
        redisUtils.STRING.SET.sRem(key, v);
    }

    /**
     * 添加到List
     * @param key   唯一键
     * @param value 数据值
     */
    public void addList(String key, String... value) {
        if(key == null || value == null)
            return;
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.LIST.lPush(key, value);
    }

    /**
     * getList
     * @param key   唯一键
     */
    public List<String> getList(String key, long start, long end) {
        if(key == null)
            return new ArrayList<>();
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.LIST.lRange(key,start,end);
    }
    /**
     * 删除value值
     * @param key   唯一键
     * @param value 数据值
     */
    public void removeList(String key, String value) {
        if(key == null || value == null)
            return;
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.LIST.lRem(key,1,value);
    }
    /**
     * 获取数组长度
     * @param key   唯一键
     */
    public long lLen(String key) {
        if(key == null)
            return 0;
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
       return redisUtils.STRING.LIST.lLen(key);
    }
    /**
     * 设置HashSet对象
     * @param domain 域名
     * @param key   键值
     * @param value  Json String or String value
     */
    public void setHSet(String domain, String key, String value) {
        if (value == null)
            return;
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.HASH.hSet(domain, key, value);
    }

    /**
     * 设置HashSet对象
     * @param domain 域名
     * @param keyVal   键-值
     */
    public void setHSet(String domain, Map<String, String> keyVal) {
        if (keyVal == null)
            return;
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.HASH.hMSet(domain, keyVal);
    }

    /**
     * 设置HashSet对象
     * @param domain 域名
     * @param key   键值
     * @param value  long
     */
    public void setHincrBySet(String domain, String key, long value) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.HASH.hIncrBy(domain, key, value);
    }

    /**
     * 获得HashSet对象
     * @param domain 域名
     * @param key   键值
     * @return Json String or String value
     */
    public String getHSet(String domain, String key) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        Object value = redisUtils.STRING.HASH.hGet(domain, key);
        if(value instanceof String)
            return (String) value;
        return null;
    }

    /**
     * 获得HashSet对象
     * @param domain 域名
     * @return 域对应的所有key-value
     */
    public Map<String, String> getHAll(String domain) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        Map<String, Object> valve = redisUtils.STRING.HASH.hGetAll(domain);
        Map<String, String> v = Maps.newHashMap();
        for(String key: valve.keySet()) {
            if(valve.get(key) instanceof String)
                v.put(key, (String) valve.get(key));
        }
        return v;
    }

    /**
     * 删除HashSet对象
     *
     * @param domain 域名
     * @param key   键值
     */
    public void delHSet(String domain, String key) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.HASH.hDel(domain, key);
    }

    /**
     * 删除HashSet对象
     * @param domain 域名
     * @param key   键值
     */
    public void delHSet(String domain, String... key) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.HASH.hDel(domain, key);
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的key值
     * @param domain    域名
     * @return  所有键的set
     */
    public Set<String> hkeys(String domain) {
        domain = getKey(true, domain);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.HASH.hKeys(domain);
    }


    /**
     * Redis Rpop 命令用于移除并返回列表的最后一个元素
     * @param key 缓存KEY</br>
     * @return void </br>
     */
    public String rpop(String key) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.LIST.rPop(key);
    }
	    
	public static class CacheKey {

	    /**分隔符**/
	    public static final String splitKey=":";
	    
        /**
         * token
         */
        public static final String token = "token:";
	    
	    /**
	     *  酒王排名相关
	     */
	    public static interface Ranking {
	        /** 用户季度瓶数 */
	        public static final String QUARTER_NUM = "quarterNum";
	        /** 季度排行榜 **/
	        public static final String QUARTER_RANK = "quarter:";
	        /** 排行榜中用户的昵称及头像 **/
	        public static final String QUARTER_RANK_USEINFO = "quarterforuserinfo";
            
            /** 月度城市排行榜 **/
            public static final String MONTH_CITY_RANK = "monthCityRank:";
	        
	    }
	    
	    /**
	     * 自然周签到红包
	     */
	    public static interface WeekSign {
	        public static final String WEEKSIGN_FORUSER = "weekSignForUser";
	    }
	    
	    /**
         * 提现专用
         */
        public static interface EXTRACT {
            /** 进行中 **/
            public static final String EXTRACT_PROCESSING_LIST = "extractProcessingList";
        }
        
        /**
         * 防黑客缓存key
         */
        public static interface Hacker {
            /** IP类型黑名单集合 **/
            public static final String HACKER_TYPE_IP = "hackerTypeIpSet";
            /** IP每分钟错误次数据 **/
            public static final String HACKER_TYPE_IP_ERRNUM = "hackerTypeIpErrNum:";
        }

        /**
         * 奖项配置项
         */
        public static interface ActivityVpointsCog {
            /** 奖项配置项剩余数量**/
            public static final String VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM = "vpsVcodeActivityVpointCogNum";
            /** 奖项配置项待扣除缓存key队列**/
            public static final String ACTIVITY_FORVCODE_GET_POINTS_QUEUE = "ActivityForCodeGetPointsQueue";
            /** 奖项配置项待扣除缓存key**/
            public static final String ACTIVITY_FORVCODE_GET_POINTS_MAP = "ActivityForCodeGetPointsMAP";
            /** 奖项配置项待扣除占比预警规则缓存key**/
            public static final String ACTIVITY_FORVCODE_GET_POINTS_WARN = "ActivityForCodeGetPointsWarn";
        }
        /**
	     * 商城相关
	     */
        public static interface vpointsShop{
        	public static final String ALL_CATEGORY="allCategory";
        	public static final String COUPON="Coupon:";
        	public static final String GOODS_LIMIT="GoodsLimit";
        }
        /**
         * 秒杀
         * @author zhaohongtao
         *
         * 2018年12月6日
         */
        public static interface seckill{
        	public static final String SEC_ACTIVITY="secActivity:";
        	public static final String MONEY_MAP="secMoneyMap:";
        }
        
        public static interface ticket{
        	/** 优惠券活动**/
            public static final String VPS_VCODE_TICKET_ACTIVITY_COG = "vpsVcodeTicketActivityCog";
            /** 优惠券活动List**/
            public static final String KEY_VCODE_TICKET_ACTIVITY_LIST = "keyVcodeTicketActivityList";
            /** 优惠券面额**/
            public static final String VPS_VCODE_TICKET_INFO = "vpsVcodeTicketInfo";
            /** 未审核数量*/
            public static final String TICKET_UNCHECKED_NUM = "ticketUnCheckedNum";
            /** 优惠券面额**/
            public static final String KEY_SYS_TICKET_INFO = "keySysTicketInfo";
            /** 优惠券兑换次数**/
            public static final String TICKET_EXCHANGE_COUNT = "ticketExchangeCount";
            /** 优惠券兑换消耗总积分**/
            public static final String TICKET_EXCHANGE_CONSUME_VPOINTS = "ticketExchangeConsumeVpoints";
        }
        
        public static interface drinkCapacityPk{
            /** PK比赛开始20小时后提醒队列**/
            public static final String DRINK_CAPACITY_PK_DELAYQUEUE_20HOUR = "drinkCapacityPkDelayQueue20hour";
            /** PK比赛延时队列**/
            public static final String DRINK_CAPACITY_PK_DELAYQUEUE = "drinkCapacityPkDelayQueue";
            /** PK查看用户**/
            public static final String DRINK_CAPACITY_PK_VIEW = "drinkCapacityPkView:";
            /** 点赞用户集合*/
            public static final String DRINK_CAPACITY_PK_THUMBUP = "drinkCapacityPkThumbUp:";
        }
        
        /**
         * 商城秒杀
         * @author Administrator
         *
         */
        public static interface vpointsSeckill{
            public static final String SECKILL_PERIODS_NUM="seckillPeriodsNum";
            public static final String SECKILL_TOTAL_NUM="seckillTotalNum";
        }
        
        /**
         * 蒙牛高印
         */
        public static interface mengniuGY{
        	public static final String BATCH_UPLOAD_TASK ="batchUpladTask:"; // 高印码源回传
        	public static final String SMALL_ORDER = "smallOrder:"; // 高印班闪小订单
        }

        /**
         * 码包下载token
         */
        public static interface codeToken{
            public static final String VJIFENCOM_TOKEN ="token:VjifenCOM";
        }

        /**
         * V店惠邀请有礼
         */
        public static interface vdhInvitationActivity{
            // 邀请有礼投放总金额
            public static final String  VDH_INVITATION_ACTIVITY_TOTAL_MONEY = "VdhInvitationActivityTotalMoney";

        }

        /**
         * 天降红包活动
         */
        public static interface waitActivation{
            // 天降红包活动投放金额缓存
            public static final String  WAIT_ACTIVATION_POSTED_AMOUNT = "WaitActivationPostedAmount";
            // 天降红包活动倍数中出规则发放红包数量缓存
            public static final String  WAIT_ACTIVATION_PRIZE_GRANT_NUM = "WaitActivationGrantNum";
            // 天降红包活动 激活待激活红包后第N次抽奖中N元  发放个数
            public static final String  WAIT_ACTIVATION_USER_PC_GRANT_NUM = "WaitActivationUserPcGrantNum";
        }
        // 扫码获取待激活红包上限
 		public static final String  SQWA_MONEY_LIMIT = "sqwa_money_limit";
        
        /** 风控用户信息 **/
		public static final String RISK_USER="riskUserInfo";

        /**
         * 邀请有礼
         */
        public static interface invitationActivity{
            // 邀请有礼投放总金额
            public static final String  INVITATION_ACTIVITY_TOTAL_MONEY = "InvitationActivityTotalMoney";
            // 邀请有礼投放总积分
            public static final String  INVITATION_ACTIVITY_TOTAL_VPOINTS = "InvitationActivityTotalVpoints";
        }
        
        // 扫码获取待激活红包无效配置KEY缓存
     	public static final String  SQWA_INVALID_PRIZE_KEY = "sqwa_invalid_prize_key";
	}
	
	/**
	 * 设置一个key在某个时间点过期
	 * @param key key值
	 * @param unixTimestamp unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
	 */
	public void expireAt(String key, long unixTimestamp) {
	    expireAt(true, key, unixTimestamp);
	}

    /**
     * 设置一个key在某个时间点过期
     * @param key key值
     * @param unixTimestamp unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     */
    public void expireAt(boolean isDynamic, String key, long unixTimestamp) {
        key = getKey(isDynamic, key);
        RedisUtils redisUtils = getRedisUtils(isDynamic);
        redisUtils.STRING.expireAt(key, new Date(unixTimestamp * 1000));
    }

    /**
     * 删除排序集合
     * @param key   唯一键
     * @param value 数据值
     */
    public void delSortedSet(String key, String value) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.Z_SET.zRem(key, value);
    }

    /**
     * 获得排名用户及排名
     * orderByDesc 为true时从大到小排，false时从小到大
     * 返回Set<Tuple>集合，Tuple.getElement()获取用户，tuple.getScore()获取排名
     * @param key           唯一键
     * @param startRange    开始下标
     * @param endRange      结束下标
     * @param orderByDesc   分数是否从高到低
     * @return              符合范围的集合，包含值和分数
     */
    public Set<Tuple> getSoredSetByRangeWithScort(String key, int startRange, int endRange, boolean orderByDesc) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);

        Set<ZSetOperations.TypedTuple<String>> tuples = orderByDesc ?
                redisUtils.STRING.Z_SET.zRevRangeWithScores(key, startRange, endRange) :
                redisUtils.STRING.Z_SET.zRangeWithScores(key, startRange, endRange);
        if(tuples == null)
            return null;
        else if(tuples.size() == 0)
            return Collections.emptySet();
        return tuples.stream().filter(e -> e.getValue() != null)
                .map(e -> new Tuple(e.getValue(), e.getScore())).collect(Collectors.toSet());
    }

    /**
     * 获得Set集合长度
     * @param key   唯一键
     * @return      集合长度
     */
    public long getSetNum(String key) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.SET.sCard(key);
    }

    /**
     * 指定key设置value,设置过期时间second
     * @param key       唯一键
     * @param value     数据值
     * @param second    过期秒数
     * @return          是否添加成功
     */
    public boolean set(String key, String value, int second) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.VALUE.set(key, value, second, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 指定key设置value
     * @param key       唯一键
     * @param value     数据值
     * @return          是否添加成功
     */
    public boolean set(String key, String value) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.VALUE.set(key, value);
        return true;
    }

    /**
     * 获取值
     * @param key   唯一键
     * @return      数据值
     */
    public String get(String key) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.VALUE.get(key);
    }

    /**
     * 指定key设置对象value
     * @param key   唯一键
     * @param value Object对象
     */
    public void setObject(boolean isDynamic, String key, Object value) {
        key = getKey(isDynamic, key);
        RedisUtils redisUtils = getRedisUtils(isDynamic);
        redisUtils.JDK.VALUE.set(key, value);
    }

    /**
     * 获取对象
     * @param key   唯一键
     * @return      Object对象
     */
    public Object getObject(boolean isDynamic, String key) {
        key = getKey(isDynamic, key);
        RedisUtils redisUtils = getRedisUtils(isDynamic);
        return redisUtils.JDK.VALUE.get(key);
    }

    /**
     * 获取值，如果没有返回默认defaultValue
     * @param key           唯一键
     * @param defaultValue  默认值
     * @return              数据值
     */
    public String get(String key, String defaultValue) {
        String v = get(key);
        return v == null ? defaultValue : v;
    }

    /**
     * 删除key
     * @param key   唯一键
     * @return      是否删除成功
     */
    public boolean del(boolean isDynamic, String key) {
        key = getKey(isDynamic, key);
        RedisUtils redisUtils = getRedisUtils(isDynamic);
        return redisUtils.STRING.del(key);
    }

    /**
     * Incrby 命令将 key 中储存的数字加上指定的增量值。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令
     * @param key   唯一键
     */
    public void incrBy(String key, long value) {
        key = getKey(true, key);
        RedisUtils redisUtils = getRedisUtils(true);
        redisUtils.STRING.VALUE.incrBy(key, value);
    }

    /**
     * 执行Redis脚本(注：相关的Key要添加项目前缀常量DbContextHolder.getDBType())
     *
     * @param script       脚本
     * @param keyCount     参数个数
     * @param params       参数集合
     * @return              脚本结果
     */
    public Object eval(String script, int keyCount, String... params) {
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.eval(script, String.class, keyCount, params);
    }

    /**
     * 执行Redis脚本(注：相关的Key要添加项目前缀常量DbContextHolder.getDBType())
     *
     * @param script       脚本
     * @param keyCount     参数个数
     * @param params       参数集合
     */
    public void evalEx(String script, int keyCount, String... params) {
        eval(script, keyCount, params);
    }
    /**
     * 根据sha值(注：相关的Key要添加项目前缀常量DbContextHolder.getDBType())
     *
     * @param script       脚本
     * @param keyCount     参数个数
     * @param params       参数集合
     * @return              脚本执行返回值
     */
    public Object evalsha(String script, int keyCount, String... params) {
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.evalSha(script, keyCount, params);
    }
    /**
     * 准备Redis脚本(注：相关的Key要添加项目前缀常量DbContextHolder.getDBType())
     * @param script       脚本
     * @return              脚本sha1串
     */
    public String scriptLoad(String script) {
        RedisUtils redisUtils = getRedisUtils(true);
        return redisUtils.STRING.scriptLoad(script);
    }

    /* redis 分布式锁 start*/
	/**
	 * 获取锁
	 * @param key			锁key
	 * @param expireTime	超时时间
	 * @return	生成的锁id,释放锁需要此id
	 */
	public String tryGetDistributedLock(String key, String projectServerName,int expireTime) {
	    if(StringUtils.isNotBlank(projectServerName))
			key = projectServerName + ":" + key;
		String value = UUID.randomUUID().toString();

        RedisUtils redisUtils = RedisUtils.get();

        Boolean res = redisUtils.STRING.VALUE.setNX(key, value, expireTime, TimeUnit.MILLISECONDS);
        if(res)
            return value;
        else {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            return null;
        }
	}

	/**
	 * 释放分布式锁
	 * @param key 		锁key
	 * @param lockId 	锁id
	 */
	public void releaseDistributedLock(String key, String lockId, String projectServerName) {
		if(StringUtils.isNotBlank(projectServerName))
			key = projectServerName + ":" + key;
        RedisUtils redisUtils = RedisUtils.get();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        System.out.println(redisUtils.STRING.eval(script, Long.class, 1, key, lockId));
	}

    private RedisUtils getRedisUtils(boolean isDynamic) {
        if(isDynamic && DDS.get() != null) {
            if(DDS.get().equals("hebei"))
                return RedisUtils.get(DDS.get());
            else if(DDS.get().equals("quechao") || DDS.get().equals("zhongLJH") || DDS.get().equals("mengniu")
                    || DDS.get().equals("beixiao2022") || DDS.get().equals("jiangsu2022") || DDS.get().equals("shanxi2022")
                    || DDS.get().equals("hubei2022") || DDS.get().equals("zhejiang22") || DDS.get().equals("hainan22")
                    || DDS.get().equals("xian2022") || DDS.get().equals("hansi23") || DDS.get().equals("xinjiang")
                    || DDS.get().equals("zhongLWX") || DDS.get().equals("mengniuzhi"))
                return RedisUtils.get("quechao");
        }
        return RedisUtils.get();
    }

    private String getKey(boolean isDynamic, String key) {
        if(isDynamic && StringUtils.isNotBlank(key) && DDS.get() != null && !key.startsWith(DDS.get())) {
            return DDS.get() + ":" + key;
        }
        return key;
    }
}
