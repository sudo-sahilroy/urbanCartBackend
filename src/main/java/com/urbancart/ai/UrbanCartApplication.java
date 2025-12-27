package com.urbancart.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
public class UrbanCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrbanCartApplication.class, args);
    }
}
