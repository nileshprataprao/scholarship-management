package edu.ua.scholarship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonJacksonConfig {


    @Bean
    public ObjectMapper getObjectMapper() {

        return new ObjectMapper();
    }

}
