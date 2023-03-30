package com.example.configurations;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    @Value("${redisson.endpointUrl}") private String endpointUrl;
//
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redissonClient() {
//        Config config = new Config();
////        config.useClusterServers().addNodeAddress(endpointUrl);
//        config.useClusterServers().addNodeAddress("127.0.0.1:7001");
//        return Redisson.create(config);
//    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(endpointUrl);
        return Redisson.create(config);
    }
}
