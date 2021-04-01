package cn.daily.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
	void init();

	void setRedisAddr(String addr);

	void stop();

	String set(String key, String value);

	String mset(String... keyvals);

	String set(String key, String value, int expireSeconds);

	boolean setIfNotExist(String key, String value);

	boolean setIfNotExist(String key, String value, int expireMilliSeconds);

	String get(String key);

	List<String> mget(String... keys);

	long del(String... keys);

	boolean exists(String key);

	String type(String key);

	long ttl(String key);

	long sadd(String key, String... value);

	long srem(String key, String... value);

	/**
	 * 返回集合中key的数量
	 *
	 * @param key
	 * @return
	 */
	long scard(String key);

	Set<String> smembers(String key);

	String srandmember(String key);

	boolean sismember(String key, String value);

	boolean expire(String key, int seconds);

	boolean expireAt(String key, long unixTime);

	String getset(String key, String value);

	long incr(String key);

	long incrby(String key, long increment);

	double incrbyfloat(String key, double increment);

	long decr(String key);

	long decrBy(String key, long decrement);

	long hset(String key, String field, String value);

	String hmset(String key, Map<String, String> hash);

	Set<String> hkeys(String key);

	Set<String> keys(String key);

	boolean hsetIfNotExists(String key, String field, String value);

	String hget(String key, String field);

	boolean hexists(String key, String field);

	List<String> hmget(String key, String... fields);

	Map<String, String> hgetall(String key);

	long hdel(String key, String... fields);

	long hlen(String key);

	long hincrby(String key, String field, long delta);

	double hincrbyfloat(String key, String field, double delta);

	/**
	 * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	long zcount(String key, double min, double max);

	/**
	 * 返回有序集key的数量
	 *
	 * @param key
	 * @return
	 */
	long zcard(String key);

	long zadd(String key, String member, double score);

	long zadd(String key, Map<String, Double> members);

	Set<String> zrange(String key, long start, long end);

	Set<String> zrangeByScore(String key, double min, double max);

	long zrem(String key, String... member);

	long zremrangeByScore(String key, double min, double max);

	Set<String> zreverage(String key, long start, long end);

	long rpush(String key, String... string);

	long rpushx(String key, String... string);

	long lpush(String key, String... string);

	long llen(String key);

	List<String> lrange(String key, long start, long end);

	String ltrim(String key, long start, long end);

	String lindex(String key, long index);

	String lset(String key, long index, String value);

	/**
	 * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作： count >
	 * 0: 从头往尾移除值为 value 的元素。 count < 0: 从尾往头移除值为 value 的元素。 count = 0: 移除所有值为
	 * value 的元素。
	 *
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	long lrem(String key, long count, String value);

	String lpop(String key);

	String rpop(String key);

	/**
	 * cas(compare and set)
	 * 若一个键的旧值为期望的值之一,则将该键的值更改成新值,否则不更改,无论是否将键值成功更新成新值,都将返回该键操作后的值
	 * 此操作为原子操作,可以保证在并发情况下的正确性 NOTE: 该方法必须配合hashtag使用
	 *
	 * @param key
	 *          键
	 * @param expectingOriginVals
	 *          期望的旧值
	 * @param toBe
	 *          期望更新的值
	 * @return cas结果,该次cas是否成功更新成期望的新值可以查看{@link CASResult#isSuccess()}
	 *         ,该键最终的值可以查看{@link CASResult#getFinalResult()}
	 * @throws JedisConnectionException
	 *           若和redis服务器的连接不成功 JedisDataException
	 *
	 *           IllegalArgumentException key中没有包含hashtag
	 */
	CASResult<String> cas(String key, Set<String> expectingOriginVals, String toBe);

	CASResult<String> cas(String key, String expect, String newval);

	/**
	 * 返回minuend与key的原值的差(若key未关联过任何值,则认为key的原值为0),并将key的原值更新为minuend
	 * 此方法可用于在分布式环境下计算连续的增量(例如计时) NOTE：使用此方法必须配合hashtag
	 *
	 * @param minuend
	 *          被减数
	 * @param key
	 *          减数对应的key
	 * @return minuend与key的原值的差
	 * @throws JedisConnectionException
	 *           若和redis服务器的连接不成功 JedisDataException
	 *
	 *           IllegalArgumentException key中没有包含hashtag
	 */
	long substractedAndSet(long minuend, String key);

	int persist(String key);

	Jedis getJedisInstance();

	void sync(Jedis jedis, Pipeline p);

	String rpoplpush(String srckey, String dstkey);

	String brpoplpush(String source, String destination, int timeout);

	long publish(String channel, String message);

	void subscribe(JedisPubSub jedisPubSub, String... channels);
}
