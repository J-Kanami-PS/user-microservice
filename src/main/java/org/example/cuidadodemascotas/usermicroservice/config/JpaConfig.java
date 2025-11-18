package org.example.cuidadodemascotas.usermicroservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "org.example.cuidadodemascota.commons.entities")
@EnableJpaRepositories(basePackages = "org.example.cuidadodemascotas.usermicroservice.apis.repository")
public class JpaConfig {
    // Esta clase solo contiene anotaciones de configuración
}