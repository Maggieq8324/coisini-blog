package com.coisini.config;

/**
 * 配置redis的存取
 *
 * @blame mqpearth
 */
public class RedisConfig {

    /**
     * redis中存放 最新博客 数量 的最大值
     */
    public static final int REDIS_NEW_BLOG_COUNT = 10;

    /**
     * redis中存放 热门博客 数量 的最大值
     */
    public static final int REDIS_HOT_BLOG_COUNT = 6;

    /**
     * redis中存放 热门博客 的 key
     */
    public static final String REDIS_HOT_BLOG = "HOTBLOG";


    /**
     * redis中存放 最新博客 的 key
     */
    public static final String REDIS_NEW_BLOG = "NEWBLOG";

    /**
     * redis中存放blog的前缀
     */
    public static final String REDIS_BLOG_PREFIX = "BLOG_";


    /**
     * 博客归档缓存key
     */
    public static final String REDIS_STATISTICAL = "STATISTICAL";

    /**
     * IP_127.0.0.1
     */
    public static final String REDIS_IP_PREFIX = "IP_";


    /**
     * 请求频率限制 缓存时间
     */
    public static final long REDIS_LIMIT_REQUEST_FREQUENCY_TIME = 1000L;
}