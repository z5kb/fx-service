package org.example.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FxServiceApplicationConfiguration {

    @Bean
    Gson gson() {
        return new Gson();
    }
}
