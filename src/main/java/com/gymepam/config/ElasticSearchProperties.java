package com.gymepam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "database.elastic")
@Data
public class ElasticSearchProperties {

    private String port;
    private String hostname;
    private String user;
    private String password;


}
