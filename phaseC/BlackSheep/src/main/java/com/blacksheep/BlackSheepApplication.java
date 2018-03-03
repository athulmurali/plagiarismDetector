package com.blacksheep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class launches the BlackSheep plagiarism detector
 */
@SpringBootApplication
@RestController
public class BlackSheepApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackSheepApplication.class, args);
    }

}