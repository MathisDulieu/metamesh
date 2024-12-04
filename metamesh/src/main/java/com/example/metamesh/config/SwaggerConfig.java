package com.example.metamesh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MetaMesh REST APIs")
                        .description("MetaMesh APIs")
                        .license(new License().name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addTagsItem(new Tag().name("Authentication").description("Endpoints for user authentication and authorization."))
                .addTagsItem(new Tag().name("User Accounts").description("Endpoints for managing user accounts, privacy settings, and preferences."))
                .addTagsItem(new Tag().name("Subscribe/Unsubscribe").description("Endpoints for managing user subscriptions and followers."))
                .addTagsItem(new Tag().name("CRUD Posts").description("Endpoints for creating, retrieving, updating, and deleting posts."))
                .addTagsItem(new Tag().name("Searching Posts").description("Endpoints for searching posts."))
                .addTagsItem(new Tag().name("Notifications").description("Endpoints for managing and retrieving user notifications."))
                .addTagsItem(new Tag().name("Media").description("Endpoints for uploading and retrieving media files."))
                .addTagsItem(new Tag().name("Comments").description("Endpoints for managing comments on posts."));
    }
}
