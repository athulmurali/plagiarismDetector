package com.blacksheep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * This class contains the implementation for the login use case
 */
@RestController
public class LoginController {
    /**
     * The connection string for the database connection
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/login";

    /**
     * The username for the database connection
     */
    private static final String DB_USERNAME = "root";

    /**
     * The password for the database connection
     */
    private static final String PASSWORD = "root";

    /**
     * The import driver for the database connection
     */
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * The query to get the credentials from the database
     */
    private static final String CREDENTIALS_QUERY = "SELECT passcode FROM credentials";

    /**
     * Key for the valid response
     */
    public static final String PASSCODE = "passcode";

    /**
     * Logger instance
     */
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Dummy method to test the whether the server is running or not
     */
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * Validates the credentials input by the user
     *
     * @param inputPasscode : passcode entered by the user
     */
    @RequestMapping(method = RequestMethod.POST, value = "/passcode")
    public ResponseEntity<String> validateLogin(@RequestBody String inputPasscode) {

        String savedPasscode = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, PASSWORD);
            statement = connection.createStatement();
            results = statement.executeQuery(CREDENTIALS_QUERY);

            while (results.next()) {
                savedPasscode = results.getString(PASSCODE);
                logger.info("Matched {0}", savedPasscode);
            }

            if (savedPasscode != null && savedPasscode.equals(inputPasscode))
                return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ClassNotFoundException|SQLException e) {
            logger.error("ERROR", e);
        } finally {
            try {
                if(results != null)
                    results.close();
                if(results != null)
                statement.close();
                connection.close();
            } catch (SQLException e) {
                logger.error("ERROR", e);
            }
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }


    /**
     * Validates the credentials input by the user, just a string match
     *
     * @param passcode123 : passcode entered by the user
     */
    @RequestMapping(method = RequestMethod.POST, value = "/passcodeString")
    public ResponseEntity<String> validateLoginString(@RequestBody String passcode123) {

        if (PASSCODE.equals(passcode123))
            return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }
}
