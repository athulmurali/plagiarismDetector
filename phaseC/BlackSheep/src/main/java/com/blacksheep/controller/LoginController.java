package com.blacksheep.controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.blacksheep.Cred;
import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.*;

import static java.sql.DriverManager.*;

/**
 * This class contains the implementation for the login use case
 */
@RestController
public class LoginController {

    private static final  BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final Logger logger = Logger.getLogger(LoginController.class);

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
    public ResponseEntity<Object> process(@RequestBody Cred cred) throws SQLException, IOException {

        logger.info("endpoint : userLogin");

        String userId   = cred.getUser().trim();
        String password = cred.getPassword().trim();

            IDBConfigUtil dbConfigUtil = new DBConfigUtil();
            try (Connection connection = getConnection(dbConfigUtil.getDbURL(),
                    dbConfigUtil.getDbUser(), dbConfigUtil.getDbPass()))
            {
                String query = "SELECT  * FROM credentials where userid = ?";

                try (PreparedStatement preparedStatement =
                             connection.prepareStatement(query)) {

                    preparedStatement.setString(1, userId);
                    try (ResultSet results = preparedStatement.executeQuery())
                    {

                        String hashedPassword = null;
                        while (results.next()) {
                            logger.info("query executed :  User account with given userId exits");
                            hashedPassword = results.getString("password");
                        }
                        if ( PASSWORD_ENCODER.matches(password, hashedPassword))
                        {
                            logger.info("userId & password matched ");
                            return ResponseEntity.status(HttpStatus.OK).build();
                        }
                        else {
                            logger.info("login check: userId & password pair not found");
                            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
                        }
                    }

                }
            }
        }
    }