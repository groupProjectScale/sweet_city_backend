package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/** The type Sweet city application. */
@SpringBootApplication
@RestController
public class SweetCityApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SweetCityApplication.class, args);
    }
}
