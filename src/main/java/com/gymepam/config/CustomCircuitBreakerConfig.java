package com.gymepam.config;

import feign.Feign;
import feign.Target;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class CustomCircuitBreakerConfig {

    @Bean
    public CircuitBreakerNameResolver circuitBreakerNameResolver() {
        return (String feignClientName, Target<?> target, Method method) ->
                Feign.configKey(target.type(), method)
                        .replaceAll("[#(,]+", "_")
                        .replaceAll("\\)+", Strings.EMPTY)
                        .replaceAll("_+$", Strings.EMPTY);
    }
}
