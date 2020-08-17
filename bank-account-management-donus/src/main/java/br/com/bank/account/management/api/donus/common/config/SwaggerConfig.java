package br.com.bank.account.management.api.donus.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Donus - Bank Account Management",
        version = "1.0.0",
        description = "This API has some of the most important functions for managing a bank account.\n" +
            "With it it is possible to create a new account and perform functions such as deposit, \n" +
            "withdraw and transfers. ",
        contact = @Contact(
            name = "Maisa Rissi",
            url = "https://github.com/mairissi",
            email = "maisa.rissi@gmail.com"
        )),
    servers = @Server(url = "http://localhost:8080")
)

@Configuration
public class SwaggerConfig {
}
