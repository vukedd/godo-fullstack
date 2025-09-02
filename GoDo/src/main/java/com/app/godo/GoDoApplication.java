package com.app.godo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GoDoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoDoApplication.class, args);
    }

}
