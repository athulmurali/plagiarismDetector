package com.blacksheep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BlackSheepApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackSheepApplication.class, args);
    }

}