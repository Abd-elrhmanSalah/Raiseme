package com.eprogs.raiseme;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@OpenAPIDefinition(info = @Info(title = "Ecommerce System REST API Documentation",
        description = "Ecommerce System REST API Documentation that is using for test apis",
        version = "v1")
)
public class RaisemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaisemeApplication.class, args);
    }

}
