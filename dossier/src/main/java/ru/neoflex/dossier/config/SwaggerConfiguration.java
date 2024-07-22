package ru.neoflex.dossier.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Credit Dossier",
                description = "The formation of documents and sending an email to the Client", version = "1.0.0",
                contact = @Contact(
                        name = "Valentina Vakhlamova",
                        email = "valulka@gmail.com",
                        url = "https://github.com/ValentinaBuddha"
                )
        )
)
public class SwaggerConfiguration {
}
