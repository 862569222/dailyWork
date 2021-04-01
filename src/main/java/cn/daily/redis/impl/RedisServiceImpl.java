package cn.daily.redis.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import cn.daily.redis.CASResult;
import cn.daily.redis.RedisService;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.Pool;

public class RedisServiceImpl implements RedisService {
	// redis lua scripts(supportted since redis 2.6)
	private static final String CAS_CMD = "local v=redis.call('get',KEYS[1]);local r=v;local n=#ARGV-1;local tb=ARGV[#ARGV];local succ='F';for i=1, n do if(v==ARGV[i]) then redis.call('set',KEYS[1],tb); r=tb; succ='T' break;end end;return {succ,r}";
	private static final String SUBS_CMD = "local v=redis.call('incrby',KEYS[1],0);local r=ARGV[1]-v;redis.call('set',KEYS[1],ARGV[1]);return r;";
	private String redisAddr;
	private final List<Pool<Jedis>> jedisPoolList = new ArrayList<>();
	private AtomicInteger inc;
	private int poolSize;
	private int maxActive = 10;
	private int maxIdle = 5;
	private int timeout = 2000;
	private String password = null;
	private int database = 0;
	private static final RedisServiceImpl redisService = new RedisServiceImpl();

	@Override
	public void setRedisAddr(String addr) {
		this.redisAddr = addr;
	}

	public String setRedisAddr() {
		return redisAddr;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	@Override
	public void init() {
		if (redisAddr == null || "".equals(redisAddr.trim())
				|| "NULL".equals(redisAddr.trim())) {
			throw new IllegalArgumentException("invalid redisAddr");
		}
		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxTotal(maxActive);
		cfg.setMaxIdle(maxIdle);

		String[] addrs = redisAddr.split(",");

		for (int i = 0; i < addrs.length; i++) {
			String[] addrInfo = addrs[i].split(":");
			jedisPoolList.add(new JedisPool(cfg, addrInfo[0], Integer
					.parseInt(addrInfo[1]), timeout, password, database));
		}

		poolSize = jedisPoolList.size();

		if (poolSize == 0) {
			throw new IllegalArgumentException("invalid redisAddr");
		}

		inc = new AtomicInteger(Math.abs(UUID.randomUUID().hashCode()) % poolSize);
	}

	public void stop() {
		for (int i = 0; i < poolSize; i++) {
			jedisPoolList.get(i).destroy();
		}
	}

	private Pool<Jedis> getJedisPool() {
		int k = Math.abs(inc.incrementAndGet() % poolSize);
		return jedisPoolList.get(k);
	}

	@Override
	public String set(String key, String value) {
		Pool<Jedis> jedisPool = getJedisPool();

		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.set(key, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String mset(String... keyvals) {
		Pool<Jedis> jedisPool = getJedisPool();

		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.mset(keyvals);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String set(String key, String value, int expireSeconds) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.setex(key, expireSeconds, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean setIfNotExist(String key, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			long result = jedis.setnx(key, value);
			return result > 0;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean setIfNotExist(String key, String value, int expireMilliSeconds) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			String result = jedis.set(key, value, "nx", "px", expireMilliSeconds);
			if (result != null && result.equalsIgnoreCase("OK")) {
				return true;
			}
			return false;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String get(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.get(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public List<String> mget(String... keys) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.mget(keys);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String getset(String key, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.getSet(key, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long del(String... keys) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.del(keys);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean exists(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.exists(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String type(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.type(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long ttl(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.ttl(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean expire(String key, int seconds) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.expire(key, seconds) > 0;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean expireAt(String key, long unixTime) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.expireAt(key, unixTime) > 0;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long sadd(String key, String... values) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.sadd(key, values);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long srem(String key, String... values) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.srem(key, values);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long scard(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.scard(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> smembers(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.smembers(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String srandmember(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.srandmember(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean sismember(String key, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.sismember(key, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long incr(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.incr(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long incrby(String key, long increment) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.incrBy(key, increment);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public double incrbyfloat(String key, double increment) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.incrByFloat(key, increment);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long decr(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.decr(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long decrBy(String key, long decrement) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.decrBy(key, decrement);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long hset(String key, String field, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hset(key, field, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hmset(key, hash);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> hkeys(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.keys(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> keys(String key) {
		return null;
	}

	@Override
	public boolean hsetIfNotExists(String key, String field, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hsetnx(key, field, value) > 0;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public boolean hexists(String key, String field) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hexists(key, field);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String hget(String key, String field) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hget(key, field);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hmget(key, fields);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Map<String, String> hgetall(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hgetAll(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long hdel(String key, String... fields) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hdel(key, fields);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long hlen(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hlen(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long hincrby(String key, String field, long delta) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hincrBy(key, field, delta);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public double hincrbyfloat(String key, String field, double delta) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.hincrByFloat(key, field, delta);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zcount(String key, double min, double max) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zcount(key, min, max);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zcard(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zcard(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zadd(String key, String member, double score) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zadd(key, score, member);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zadd(String key, Map<String, Double> members) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zadd(key, members);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zrange(key, start, end);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zrangeByScore(key, min, max);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zrem(String key, String... member) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zrem(key, member);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long zremrangeByScore(String key, double min, double max) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zremrangeByScore(key, min, max);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Set<String> zreverage(String key, long start, long end) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.zrevrange(key, start, end);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long rpush(String key, String... string) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.rpush(key, string);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long rpushx(String key, String... string) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.rpushx(key, string);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long lpush(String key, String... string) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lpush(key, string);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long llen(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.llen(key);

		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lrange(key, start, end);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String ltrim(String key, long start, long end) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.ltrim(key, start, end);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String lindex(String key, long index) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lindex(key, index);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String lset(String key, long index, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lset(key, index, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long lrem(String key, long count, String value) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lrem(key, count, value);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String lpop(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.lpop(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String rpop(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.rpop(key);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public CASResult<String> cas(String key, Set<String> expects, String newval) {
		int tagStart = key.indexOf('{');
		int tagEnd = key.indexOf('}');
		if (tagStart == -1 || tagEnd == -1 || tagStart >= tagEnd) {
			throw new IllegalArgumentException("not found hashtag in key");
		}

		Pool<Jedis> jedisPool = getJedisPool();
		List<String> args = new ArrayList<String>(expects.size() + 2);
		args.add(key);
		args.addAll(expects);
		args.add(newval);
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			@SuppressWarnings("unchecked")
			List<String> response = (List<String>) jedis.eval(CAS_CMD, 1,
					args.toArray(new String[0]));
			String succStr = response.get(0);
			String val = response.get(1);
			CASResult<String> result = new CASResult<String>("T".equals(succStr), val);
			return result;
		} catch (JedisDataException e) {
			throw e;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public CASResult<String> cas(String key, String expect, String newval) {
		Set<String> originVals = new HashSet<>();
		originVals.add(expect);
		return cas(key, originVals, newval);
	}

	@Override
	public long substractedAndSet(long minuend, String key) {
		int tagStart = key.indexOf('{');
		int tagEnd = key.indexOf('}');
		if (tagStart == -1 || tagEnd == -1 || tagStart >= tagEnd) {
			throw new IllegalArgumentException("not found hashtag in key");
		}

		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			Long response = (Long) jedis.eval(SUBS_CMD, 1, key,
					String.valueOf(minuend));
			return response;
		} catch (JedisDataException e) {
			throw e;
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public int persist(String key) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.persist(key).intValue();
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public Jedis getJedisInstance() {
		Pool<Jedis> jedisPool = jedisPoolList.get(0);
		return jedisPool.getResource();
	}

	@Override
	public void sync(Jedis jedis, Pipeline p) {
		if (jedis == null || p == null) {
			return;
		}

		Pool<Jedis> jedisPool = jedisPoolList.get(0);
		boolean broken = false;

		try {
			p.sync();
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (!broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.rpoplpush(srckey, dstkey);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		Pool<Jedis> jedisPool = getJedisPool();
		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.brpoplpush(source, destination, timeout);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public long publish(String channel, String message) {
		Pool<Jedis> jedisPool = getJedisPool();

		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			return jedis.publish(channel, message);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		Pool<Jedis> jedisPool = getJedisPool();

		Jedis jedis = jedisPool.getResource();
		boolean broken = false;
		try {
			jedis.subscribe(jedisPubSub, channels);
		} catch (JedisConnectionException e) {
			jedisPool.returnBrokenResource(jedis);
			broken = true;
			throw e;
		} finally {
			if (jedis != null && !broken) {
				jedisPool.returnResource(jedis);
			}
		}
	}
}
