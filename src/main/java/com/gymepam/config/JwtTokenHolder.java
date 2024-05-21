package com.gymepam.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtTokenHolder {
    private  String Token;

}
