package com.gzk;

import com.gzk.redis.config.XRedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication(scanBasePackages = "com.gzk.redis")
@EnableConfigurationProperties(XRedisConfig.class)
public class RedisDemoStarter {
    public static void main( String[] args ) {
        SpringApplication.run(RedisDemoStarter.class, args);
    }
}
