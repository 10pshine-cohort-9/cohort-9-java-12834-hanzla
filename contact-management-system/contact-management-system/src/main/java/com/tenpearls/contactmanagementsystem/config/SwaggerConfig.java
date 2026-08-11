package com.tenpearls.contactmanagementsystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI contactManagementAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("Contact Management System API")

                        .description(
                                "REST API developed during the 10Pearls Internship")

                        .version("1.0")

                        .contact(new Contact()
                                .name("Hanzla Shehzad")
                                .email("hanzlash2003@gmail.com"))

                        .license(new License()
                                .name("10Pearls Internship")))

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Project Documentation"));
    }
}