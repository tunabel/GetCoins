package com.example.getcoins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.getcoins.configuration")
public class GetCoinsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetCoinsApplication.class, args);
    }

}
