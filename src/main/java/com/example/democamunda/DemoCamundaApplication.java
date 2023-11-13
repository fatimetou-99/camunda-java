package com.example.democamunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class DemoCamundaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCamundaApplication.class, args);
    }

}
