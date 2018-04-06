package com.blacksheep;

import com.blacksheep.util.EMailer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * This class launches the BlackSheep plagiarism detector
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan()
public class BlackSheepApplication extends SpringBootServletInitializer {

    private static final String SUBJECT_RESTART = "Plagiarism System restarted!";
    private static final String RESTART_MESSAGE = "Plagiarism system has been restarted!";

    public static void main(String[] args) throws IOException, MessagingException {

        SpringApplication.run(BlackSheepApplication.class, args);
        EMailer emailer = new EMailer();
        emailer.emailAdminTeam(SUBJECT_RESTART,RESTART_MESSAGE);


    }
}
