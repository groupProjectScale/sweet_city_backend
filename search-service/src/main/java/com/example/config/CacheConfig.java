package com.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public RedissonClient redissonClient() {
        // test
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:7001").setPassword("yourpassword");
        return Redisson.create(config);
    }
}
