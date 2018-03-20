package com.blacksheep.controller;

import com.blacksheep.Cred;
import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

/**
 * This class contains the implementation for the login use case
 */
@RestController
public class LoginController {


    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Dummy method to test the whether the server is running or not
     */
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }


    //Added by Athul
    @RequestMapping(
            value = "/userLogin",
            method = RequestMethod.POST)
    public ResponseEntity<Object> process(@RequestBody Cred cred)
            throws Exception {

        logger.info("endpoint : userLogin");

        String userId   = cred.getUser().trim();
        String password = cred.getPassword().trim();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {

            String CREDENTIALS_QUERY = "SELECT  * FROM credentials where userid = ? " + "AND  " + "password = ?";
            IDBConfigUtil dbConfigUtil = new DBConfigUtil();

            connection = DriverManager.getConnection(dbConfigUtil.getDbURL(), dbConfigUtil.getDbUser(), dbConfigUtil.getDbPass());
            preparedStatement = connection.prepareStatement(CREDENTIALS_QUERY);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);
            results = preparedStatement.executeQuery();
            logger.info("query executed : userId and password check");

            int count = 0;
            while (results.next()) count++;

            if (count == 1) {
                logger.info("userId & password matched");
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            else
            {
                logger.info("login check: userId & password pair not found");
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
            }
        }
        finally {
            try {
                if (results != null)
                    results.close();
                if (results != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                logger.error("ERROR", e);
            }
        }
    }
}
