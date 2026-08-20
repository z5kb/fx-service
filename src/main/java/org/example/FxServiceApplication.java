package org.example;

import org.example.service.FxService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class FxServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(FxServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(FxService fxService) {
        return args -> {};
    }
}
