package com.blacksheep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * This class launches the BlackSheep plagiarism detector
 */
@SpringBootApplication
public class BlackSheepApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BlackSheepApplication.class, args);
    }
}
