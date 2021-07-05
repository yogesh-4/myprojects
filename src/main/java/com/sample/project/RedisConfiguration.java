package com.sample.project;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	
	
	@Value("${spring.cache.time-to-live}")
	private long cacheTimeOut;
	
	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	
	/* @Bean
	    public static JedisConnectionFactory redisConnectionFactory() {
	        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
	        jedisConnectionFactory.setHostName("localhost");
	        jedisConnectionFactory.setPort(6379);
	        jedisConnectionFactory.afterPropertiesSet();
	        //jedisConnectionFactory.setTimeout(timeout);
	        return jedisConnectionFactory;
	    }*/
	 
	 @Bean
	  RedisCacheConfiguration redisCache() {
		 return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues().entryTtl(Duration.ofMillis(cacheTimeOut));
	 }
	
	  @Bean
	 CacheManager cacheManager() {
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCache()).transactionAware().build();
	}
	 
	
	/* @Bean
	   RedisTemplate<String, String> redisTemplate() {
		 RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		    redisTemplate.setConnectionFactory(redisConnectionFactory);
		    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		    redisTemplate.setKeySerializer(new StringRedisSerializer());
		    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		    redisTemplate.setHashValueSerializer(hashValueSerializer);
		    redisTemplate.afterPropertiesSet();
		    return redisTemplate;
		}*/

}
