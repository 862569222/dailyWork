package cn.daily.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaibo
 * @date 2021/2/23 16:16
 * @description : 分布式锁
 *				getRejectLock()   获取不到锁 返回false
 *				getWaitLock()     获取不到锁 进行尝试等待 不停地获取锁， 直到获取锁或者等待超时
 */
public class RedisLock {
	private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

	/**
	 * 24天的毫秒数
	 */
	private final static int MAX_CACHE_MILLI_SEC = 2073600000;// 24 * 24 * 60 * 60 * 1000  2073600000  24天

	/**
	 * 24天的秒数
	 */
	private final static int MAX_CACHE_SEC = 2073600;// 24 * 24 * 60 * 60  2073600 24天

	/**
	 * 一秒的毫秒数
	 */
	private final static int MIN_CACHE_MILLI_SEC = 1000;// 1000  一秒

	private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

	private RedisService redisService;

	private String lockKey;

	private volatile boolean locked = false;

	/**
	 * 锁的过期时间，默认5s
	 */
	private int expireMilliSeconds = 5000;

	/**
	 * 锁等待时间，防止线程饥饿 默认值为5000 毫秒 = 5秒
	 */
	private int timeoutMilliSeconds = 5000;

	public RedisLock(RedisService redisService, String lockKey) {
		this.redisService = redisService;
		this.lockKey = lockKey + "lock";
	}

	public RedisLock(RedisService redisService, String lockKey, int expireSeconds) {
		this(redisService, lockKey);
		this.expireMilliSeconds = getRealCacheTime(expireSeconds);
	}

	/**
	 * 过期时间最短 1s，最长24天
	 * @param expireSeconds
	 * @return
	 */
	private int getRealCacheTime(int expireSeconds) {
		if(expireSeconds == -1 || expireSeconds > MAX_CACHE_SEC){
			return MAX_CACHE_MILLI_SEC;
		}

		int milliSec = expireSeconds * MIN_CACHE_MILLI_SEC;

		// 最短也要1秒
		return Math.max(milliSec, MIN_CACHE_MILLI_SEC);
	}

	/**
	 * redis 分布式锁 获取不到锁 采取拒绝策略 getRejectLock()会直接返回false
	 * <p>
	 * 在一定时间内 保证只有一个线程才能获得锁，其他的全部拒绝，
	 * 如果获得锁的进程调用unlock()方法 释放锁，其他线程又可以尝试获得锁，或者在锁的过期时间之后  其他线程也可以尝试获得锁
	 * <p>
	 * 使用场景：
	 * 避免用户数据的重复提交
	 *
	 * @return
	 */
	public boolean getRejectLock(){
		locked = redisService.setIfNotExist(lockKey,String.valueOf(System.currentTimeMillis()), expireMilliSeconds);
		logger.info("lock-key:{}-expireMilliSeconds:{}-result:{}", lockKey, expireMilliSeconds, locked);
		return locked;
	}

	/**
	 * redis 分布式锁 获取不到锁 采取等待策略  会一直等待锁的释放，如果超时 返回resultIfTimeout
	 * <p>
	 * 在一定时间内 保证只有一个线程才能获得锁，其他的全部在等待锁的释放
	 * <p>
	 * 使用场景：
	 * 多个业务场景对同一值进行修改， 用该锁， 保证请求的顺序性
	 *
	 * @param resultIfTimeout
	 * @return
	 */
	public boolean getWaitLock(boolean resultIfTimeout) {
		int timeout = timeoutMilliSeconds;
		while (timeout >= 0) {

			/**
			 * jedis.set(key, value, "nx", "px", expireMilliSeconds);
			 * 保证 setnx 和 expire 的原子性
			 */
			locked = redisService.setIfNotExist(lockKey, String.valueOf(System.currentTimeMillis()), expireMilliSeconds);
			logger.info("lock-key:{}-expireMilliSeconds:{}-result:{}", lockKey, expireMilliSeconds, locked);
			if (locked) {
				return true;
			}

			timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
			/**
			 * 延迟100 毫秒
			 */
			try {
				Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
			} catch (InterruptedException e) {
				// ignore
				logger.error("Thread.sleep-error:{}", e);
			}
		}
		return resultIfTimeout;
	}

	/**
	 * 释放锁
	 */
	public void unlock() {
		if (locked) {
			logger.info("释放锁-key:{}", lockKey);
			//只有获得锁的线程  才能释放锁
			redisService.del(lockKey);
			locked = false;
		}
	}


}
