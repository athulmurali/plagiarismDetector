package com.blacksheep;

import com.blacksheep.util.EMailer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class launches the BlackSheep plagiarism detector
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan()
public class BlackSheepApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BlackSheepApplication.class, args);
    }
}
