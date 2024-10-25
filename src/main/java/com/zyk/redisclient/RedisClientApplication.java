package com.zyk.redisclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.zyk.redisclient.repo.jpa")
@EnableRedisRepositories(basePackages = "com.zyk.redisclient.repo.redis")
public class RedisClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisClientApplication.class, args);
    }

}
