package com.gymepam.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "feign.client.enabled", havingValue = "true")
@EnableFeignClients
public class FeignConfig {
}
