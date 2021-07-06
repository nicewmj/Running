package com.example.redisUtils;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.*;


public class RedisUtils {

    public static final String R1 = "R1";
    public static final String R2 = "R2";
    public static final String R3 = "R3";
    public static final String R4 = "R4";

    private static JedisPool poolsR1 = null;
    private static JedisPool poolsR2 = null;
    private static JedisPool poolsR3 = null;
    private static JedisPool poolsR4 = null;


    private static synchronized void createJedisPool(String dbName) {
        Properties prop = new Properties();
        try {
            InputStream fis = new Object() {
                public Class getClassForStatic() {
                    return this.getClass();
                }
            }.getClassForStatic().getResourceAsStream("/db.properties");
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (R1.equals(dbName)) {
//            if (poolsR1 == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            // 最小空闲连接数, 默认0
            config.setMinIdle(Integer.valueOf(prop.getProperty("redis.list.minIdle")));
            // 设置最大连接数
            config.setMaxTotal(Integer.valueOf(prop.getProperty("redis.list.maxActive")));
            // 设置空闲连接
            config.setMaxIdle(Integer.valueOf(prop.getProperty("redis.list.maxIdle")));
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
            config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("redis.list.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("redis.list.testOnBorrow")));
            poolsR1 = new JedisPool(config, prop.getProperty("redis.list.host"),
                    Integer.valueOf(prop.getProperty("redis.list.port")),
                    Integer.valueOf(prop.getProperty("redis.list.timeOut")),
                    prop.getProperty("redis.list.password"));
//            }
        } else if (R2.equals(dbName)) {
            if (poolsR2 == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                // 最小空闲连接数, 默认0
                config.setMinIdle(Integer.valueOf(prop.getProperty("redis.coupon.minIdle")));
                // 设置最大连接数
                config.setMaxTotal(Integer.valueOf(prop.getProperty("redis.coupon.maxActive")));
                // 设置空间连接
                config.setMaxIdle(Integer.valueOf(prop.getProperty("redis.coupon.maxIdle")));
                // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
                config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("redis.coupon.maxWait")));
                config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("redis.coupon.testOnBorrow")));
                poolsR2 = new JedisPool(config, prop.getProperty("redis.coupon.host"),
                        Integer.valueOf(prop.getProperty("redis.coupon.port")),
                        Integer.valueOf(prop.getProperty("redis.coupon.timeOut")),
                        prop.getProperty("redis.coupon.password"));
            }
        } else if (R3.equals(dbName)) {
            if (poolsR3 == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                // 最小空闲连接数, 默认0
                config.setMinIdle(Integer.valueOf(prop.getProperty("redis.initdb.minIdle")));
                // 设置最大连接数
                config.setMaxTotal(Integer.valueOf(prop.getProperty("redis.initdb.maxActive")));
                // 设置空间连接
                config.setMaxIdle(Integer.valueOf(prop.getProperty("redis.initdb.maxIdle")));
                // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
                config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("redis.initdb.maxWait")));
                config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("redis.initdb.testOnBorrow")));
                poolsR3 = new JedisPool(config, prop.getProperty("redis.initdb.host"),
                        Integer.valueOf(prop.getProperty("redis.initdb.port")),
                        Integer.valueOf(prop.getProperty("redis.initdb.timeOut")),
                        prop.getProperty("redis.initdb.password"));
            }
        } else if (R4.equals(dbName)) {
            if (poolsR4 == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                // 最小空闲连接数, 默认0
                config.setMinIdle(Integer.valueOf(prop.getProperty("redis.log.minIdle")));
                // 设置最大连接数
                config.setMaxTotal(Integer.valueOf(prop.getProperty("redis.log.maxActive")));
                // 设置空间连接
                config.setMaxIdle(Integer.valueOf(prop.getProperty("redis.log.maxIdle")));
                // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
                config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("redis.log.maxWait")));
                config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("redis.log.testOnBorrow")));
                poolsR4 = new JedisPool(config, prop.getProperty("redis.log.host"),
                        Integer.valueOf(prop.getProperty("redis.log.port")),
                        Integer.valueOf(prop.getProperty("redis.log.timeOut")), prop.getProperty("redis.log.password"));
            }
        } else {
            try {
                throw new Exception("数据源名称输入有误！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static Jedis getResource(String dbName) {
        Jedis jedis = null;
        if (R1.equals(dbName)) {
            if (poolsR1 == null) {
                createJedisPool(dbName);
            }
            jedis = poolsR1.getResource();
        } else if (R2.equals(dbName)) {
            if (poolsR2 == null) {
                createJedisPool(dbName);
            }
            jedis = poolsR2.getResource();
        } else if (R3.equals(dbName)) {
            if (poolsR3 == null) {
                createJedisPool(dbName);
            }
            jedis = poolsR3.getResource();
        } else if (R4.equals(dbName)) {
            if (poolsR4 == null) {
                createJedisPool(dbName);
            }
            jedis = poolsR4.getResource();
        } else {
            try {
                throw new Exception("数据源名称输入有误！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jedis;
    }

    public static void returnResource(final Jedis resource, String dbName) {
        if (resource != null) {
            resource.close();
        }
    }

    public static void setList(String key, List entitys, Integer seconds, String dbName) {
        Jedis jedis = getResource(dbName);
        for (Object object : entitys) {
            jedis.lpush(key, JSON.toJSONString(object));
        }
        jedis.expire(key, seconds);
        returnResource(jedis, dbName);
    }

    public static void setList(String key, List entitys, String dbName) {
        Jedis jedis = getResource(dbName);
        for (Object object : entitys) {
            jedis.lpush(key, JSON.toJSONString(object));
        }
        returnResource(jedis, dbName);
    }

    public static void setListRpush(String key, List entitys, String dbName) {
        Jedis jedis = getResource(dbName);
        for (Object object : entitys) {
            jedis.rpush(key, JSON.toJSONString(object));
        }
        returnResource(jedis, dbName);
    }

    public static void setListToOne(String key, Object object, Integer seconds, String dbName) {
        Jedis jedis = getResource(dbName);
        jedis.lpush(key, JSON.toJSONString(object));
        jedis.expire(key, seconds);
        returnResource(jedis, dbName);
    }

    public static List getAllList(String key, Class c, String dbName) {
        Jedis jedis = getResource(dbName);
        List<String> entitys = jedis.lrange(key, 0, -1);
        List list = new ArrayList<Object>();
        for (String entity : entitys) {
            Object object = JSON.parseObject(entity, c);
            list.add(object);
        }
        returnResource(jedis, dbName);
        return list;
    }

    public static List getList(String key, Integer start, Integer end, Class c, String dbName) {
        Jedis jedis = getResource(dbName);
        List<String> entitys = jedis.lrange(key, start, end);
        List list = new ArrayList<Object>();
        for (String entity : entitys) {
            Object object = JSON.parseObject(entity, c);
            list.add(object);
        }
        returnResource(jedis, dbName);
        return list;
    }

    /**
     * 获取 value 根据 key -fieId
     *
     * @param key        redis key
     * @param fieId      hash key
     * @param serverName
     * @return
     */
    public static String hget(String key, String fieId, String serverName) {
        String result = null;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.hget(key, fieId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis, serverName);
        }
        return result;
    }

    /**
     * 删除 value 根据 key -fieId
     *
     * @param key        redis key
     * @param fieId      hash key
     * @param serverName
     * @return
     */
    public static Long hdel(String key, String fieId, String serverName) {
        Long result = 0L;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.hdel(key, fieId);
        returnResource(jedis, serverName);
        return result;
    }

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public static String get(String key, String dbName) {

        Jedis jedis = getResource(dbName);
        String result = jedis.get(key);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 设置单个值
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value, String serverName) {
        String result = null;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis, serverName);
        }
        return result;
    }

    /**
     * 查询集合长度
     *
     * @param key
     * @return
     */
    public static Long llen(String key, String dbName) {

        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return 0L;
        }
        Long result = jedis.llen(key);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 查询key否存在
     *
     * @param key
     * @return static boolean exists(java.lang.String key)
     */
    public static boolean exists(String key, String dbName) {

        Jedis jedis = getResource(dbName);
        boolean result = jedis.exists(key);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 集合 [key]中是否存在 成员 [value]
     *
     * @param key
     * @param value
     * @param dbName
     * @return
     */
    public static Boolean sismember(String key, String value, String dbName) {

        Jedis jedis = getResource(dbName);
        boolean result = jedis.sismember(key, value);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 获取一个HashMap的值
     *
     * @param key
     * @return 根据key去查找值，若HashMap字段不存在，则其值为空。
     */
    public static Map hgetAll(String key, String dbName) {
        Jedis jedis = getResource(dbName);
        Map result = jedis.hgetAll(key);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 存储一个HashMap的值
     */
    public static String hmset(String key, Map map, String dbName) {
        Jedis jedis = getResource(dbName);
        String result = jedis.hmset(key, map);
        returnResource(jedis, dbName);

        return result;
    }

    /**
     * 存储一个HashMap的值,并设置过期时间 设置一个key的过期时间 time_expire 过期时间，秒为单位
     */
    public static Long expireMap(String key, Map map, int time_expire, String dbName) {
        Jedis jedis = getResource(dbName);

        jedis.hmset(key, map);

        Long result = jedis.expire(key, time_expire);
        returnResource(jedis, dbName);

        return result;
    }

    /*
     * 存储一个HashMap的值
     */
    public static Long hset(String key, String field, String value, String dbName) {
        Jedis jedis = getResource(dbName);
        Long result = jedis.hset(key, field, value);
        returnResource(jedis, dbName);
        return result;
    }

    /**
     * 在list尾部增加一个值
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rpush(String key, String value, String serverName) {
        Long result = null;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.rpush(key, value);
        returnResource(jedis, serverName);
        return result;
    }

    /**
     * 名称为key的string增1操作
     *
     * @param key
     * @param serverName
     * @return
     */
    public static Long incr(String key, String serverName) {
        Long result = 0L;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.incr(key);
        returnResource(jedis, serverName);
        return result;
    }

    /**
     * 返回并删除名称为key的list中的首元素
     *
     * @param key
     * @return
     */
    public static String lpop(String key, String serverName) {
        String result = null;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.lpop(key);
        returnResource(jedis, serverName);
        return result;
    }

    /**
     * 名称为key的string减1操作
     *
     * @param key
     * @param serverName
     * @return
     */
    public static Long decr(String key, String serverName) {
        Long result = 0L;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.decr(key);
        returnResource(jedis, serverName);
        return result;
    }

    /**
     * 根据单个KEY删除
     *
     * @param key
     * @return
     */
    public static Long del(String key, String serverName) {
        Long result = 0L;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        result = jedis.del(key);
        returnResource(jedis, serverName);
        return result;
    }

//    public static void main(String[] args) {
//        while (true) {
//            Jedis jedis = RedisUtils.getResource(R2);
//            String ss = null;
//            if (jedis.exists(ss)) {
//                System.out.println(111);
//            } else {
//                System.out.println(222);
//            }
//        }
//
//    }

    public static String setex(String key, String value, int secods, String serverName) {
        String result = null;
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.setex(key, secods, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis, serverName);
        }
        return result;
    }

    /**
     * 设置一个key的过期时间
     * time_expire 过期时间，秒为单位
     */
    public static Long expire(String key, int time_expire, String dbName) {
        Jedis jedis = getResource(dbName);
        Long result = jedis.expire(key, time_expire);
        returnResource(jedis, dbName);
        return result;
    }


    /**
     * 返回并名称为key的list中的指定位数的值
     *
     * @param key
     * @return
     */
    public static List<String> lrange(String key, int start, int end, String serverName) {
        List<String> logs = new ArrayList<String>();
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return logs;
        }
        logs = jedis.lrange(key, start, end);
        returnResource(jedis, serverName);
        return logs;
    }

    /**
     * 删除名称为key的list中的相同的值
     *
     * @param key
     * @return
     */
    public static void lrem(String key, String value, String serverName) {
        Jedis jedis = getResource(serverName);
        if (jedis == null) {
            return;
        }
        jedis.lrem(key, 0, value);
        returnResource(jedis, serverName);
    }

    /**
     * 设值 集合Set
     */
    public static void sadd(String key, String value, String dbName) {
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return;
        }
        jedis.sadd(key, value);
        returnResource(jedis, dbName);
    }

    /**
     * 集合Set 获取count个值
     */
    public static List<String> srandmember(String key, int count, String dbName) {
        List<String> list = new ArrayList<String>();
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return list;
        }
        list = jedis.srandmember(key, count);
        returnResource(jedis, dbName);
        return list;
    }

    /**
     * 集合Set 删除数据
     */
    public static void srem(String key, String[] members, String dbName) {
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return;
        }
        jedis.srem(key, members);
        returnResource(jedis, dbName);
    }

    /**
     * 查询集合set长度
     *
     * @param key
     * @return
     */
    public static Long scard(String key, String dbName) {
        Long result = 0L;
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return 0L;
        }
        result = jedis.scard(key);
        returnResource(jedis, dbName);
        return result;
    }
    //====================================================================

    /**
     * 获取所有的键值
     *
     * @param dbName
     * @return
     */
    public static Set<String> getKeys(String dbName) {
        Set<String> result = null;
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return null;
        }
        result = jedis.keys("*");
        returnResource(jedis, dbName);
        return result;
    }

    /**
     * 根据键值获取剩余时间
     *
     * @param dbName
     * @return
     */
    public static Long getTime(String dbName, String key) {
        Long result = null;
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return null;
        }
        result = jedis.ttl(key);
        returnResource(jedis, dbName);
        return result;
    }

    /**
     * 获取数据类型
     *
     * @param dbName
     * @param key
     * @return
     */
    public static String getTypes(String dbName, String key) {
        String result = null;
        Jedis jedis = getResource(dbName);
        if (jedis == null) {
            return null;
        }
        result = jedis.type(key);
        returnResource(jedis, dbName);
        return result;
    }

    /**
     * list获取所以数据
     *
     * @param key
     * @param dbName
     * @return
     */
    public static List<String> getList(String key, String dbName) {
        Jedis jedis = getResource(dbName);
        List<String> entitys = jedis.lrange(key, 0, -1);
        returnResource(jedis, dbName);
        return entitys;
    }

    /**
     * 将一个值 value 插入到列表 key 的表尾
     *
     * @param key
     * @param entitys
     * @param dbName
     */
    public static void rpush(String key, List<String> entitys, String dbName) {
        Jedis jedis = getResource(dbName);
        for (String string : entitys) {
            jedis.rpush(key, string);
        }
        returnResource(jedis, dbName);
    }
}
