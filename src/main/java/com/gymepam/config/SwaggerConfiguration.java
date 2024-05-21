package com.gymepam.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Application Rest API")
                        .description("Principal Gym Service API")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Alejandro Mateus")
                                .url("")
                                .email("alejandromateus711@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));


    }
}
