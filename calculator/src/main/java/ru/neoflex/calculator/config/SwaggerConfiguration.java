package ru.neoflex.calculator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Calculator Microservice",
                description = "Credit parameters calculation and data scoring service", version = "1.0.0",
                contact = @Contact(
                        name = "Valentina Vakhlamova",
                        email = "valulka@gmail.com",
                        url = "https://github.com/ValentinaBuddha"
                )
        )
)
public class SwaggerConfiguration {
}
