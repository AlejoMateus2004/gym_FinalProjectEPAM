package com.gymepam.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private JwtTokenHolder jwtTokenHolder;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println(jwtTokenHolder.getToken());
        String token = jwtTokenHolder.getToken();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
            log.info("Token added to request header: {}", token);
        } else {
            log.warn("No token found in JwtTokenHolder");
        }
    }
}
