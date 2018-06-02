package com.stampbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
