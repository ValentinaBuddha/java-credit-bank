package ru.neoflex.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Gateway Microservice",
                description = "Redirect requests to other microservices", version = "1.0.0",
                contact = @Contact(
                        name = "Valentina Vakhlamova",
                        email = "valulka@gmail.com",
                        url = "https://github.com/ValentinaBuddha"
                )
        )
)
public class SwaggerConfiguration {
}
