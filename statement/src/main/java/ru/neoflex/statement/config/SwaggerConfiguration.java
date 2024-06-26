package ru.neoflex.statement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Statement",
                description = "Prescoring, calculation of loan offers and choosing one of them", version = "1.0.0",
                contact = @Contact(
                        name = "Valentina Vakhlamova",
                        email = "valulka@gmail.com",
                        url = "https://github.com/ValentinaBuddha"
                )
        )
)
public class SwaggerConfiguration {
}
