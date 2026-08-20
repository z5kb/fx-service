package org.example;

import org.example.service.FxService;
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
    public CommandLineRunner run(FxService fxService) {
        return args -> { // TODO remove this and use FxController exclusively
            fxService.prepareFxRatesData("EUR");
            fxService.getConversionRate("USD", "EUR");
            fxService.getBalances(1L);
        };
    }

}
