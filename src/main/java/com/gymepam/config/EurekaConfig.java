package com.gymepam.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true")
@EnableDiscoveryClient
public class EurekaConfig {
}
