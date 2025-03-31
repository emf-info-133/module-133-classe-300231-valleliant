package com.monprojet.service1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tournament - Service 1")
                        .description("API REST pour la gestion des tournois, équipes, matches et utilisateurs.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe de développement")
                                .email("contact@monprojet.com"))
                        .license(new License()
                                .name("Licence MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 