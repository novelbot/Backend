package com.novelbot.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(servers = {
        @Server(url = "https://api.novelbot.org", description = "개발 서버"),
        @Server(url = "http://localhost:8080", description = "로컬 서버")
})

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NovelBot API")
                        .version("v1")
                        .description("NovelBot API documentation with Swagger UI"));
    }
}
