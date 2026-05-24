package com.visordocs.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.visordocs.backend")
public class VisorDocsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisorDocsBackendApplication.class, args);
    }
}