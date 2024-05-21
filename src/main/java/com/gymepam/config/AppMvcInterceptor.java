package com.gymepam.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AppMvcInterceptor implements HandlerInterceptor {
    
    private String TRACE_HEADER = "X-Trace-Id";
    
    public AppMvcInterceptor() {
        MDC.put("transactionId", UUID.randomUUID().toString());
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceID = UUID.randomUUID().toString();
        if (request.getHeader(TRACE_HEADER) != null && !request.getHeader(TRACE_HEADER).isEmpty()) {
            traceID = request.getHeader(TRACE_HEADER);
        }
        MDC.put("transactionId", traceID);
        return true;
    }
}