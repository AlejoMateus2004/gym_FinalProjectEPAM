package com.gymepam.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("database.elastic")
@Data
public class ElasticSearchProperties {

    private String port;
    private String hostname;
    private String user;
    private String password;


}
