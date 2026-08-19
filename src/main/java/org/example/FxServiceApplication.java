package org.example;

import org.example.service.ApiClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FxServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(FxServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ApiClientService apiClientService) {
        return args -> {
            apiClientService.fetch();

        };
    }

}
