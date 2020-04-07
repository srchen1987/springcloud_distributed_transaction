package com.pttl.distributed.transaction.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pttl.distributed.transaction.aspetct.DistributedTransactionInterceptor;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 
 * @ClassName:  RedisConfig   
 * @Description:   redis配置
 * @author: srchen    
* @date:   2019年11月02日 上午0:05:52
 */
@ConditionalOnBean(DistributedTransactionInterceptor.class)
@Configuration("distributedRedisConfig")
//@PropertySource("classpath:application.properties")
public class RedisConfig {
 
    @Value("${spring.redissett}")
    private String host;
 
    @Value("${spring.redis.port}")
    private int port;
 
    @Value("${spring.redis.timeout}")
    private int timeout;
 
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
 
    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;
 
    @Value("${spring.redis.password}")
    private String password;
 
    @Value("${spring.redis.block-when-exhausted}")
    private boolean blockWhenExhausted;
    
		@Value("${spring.redis.database}")
	  private int database;
 
    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(false);
        
        if("".equals(password))password=null;
        		 
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password,database);
        return jedisPool;
    }

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}
 
}
