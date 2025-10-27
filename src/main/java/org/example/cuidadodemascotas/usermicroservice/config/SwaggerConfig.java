package org.example.cuidadodemascotas.usermicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8084}")
    private String serverPort;

    @Bean
    public OpenAPI userMicroserviceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort + "/api/v1");
        server.setDescription("User Microservice Local Server");

        Contact contact = new Contact();
        contact.setName("Jazmín Kanami Pavón Shiokawa");
        contact.setEmail("jazmin.pavon@example.com");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Cuidado de Mascotas - User Microservice API")
                .version("1.0.0")
                .description("Microservicio encargado de la gestión de usuarios y roles. " +
                        "Permite crear, actualizar, listar y eliminar usuarios del sistema.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}