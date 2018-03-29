package com.blacksheep.controller;

import com.blacksheep.Cred;
import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

/**
 * This class contains the implementation for the login use case
 */
@RestController
public class LoginController {


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
    public ResponseEntity<Object> process(@RequestBody Cred cred)
            throws Exception {

        logger.info("endpoint : userLogin");

        String userId   = cred.getUser().trim();
        String password = cred.getPassword().trim();

        ResultSet results = null;
        String query = "SELECT  * FROM credentials where userid = ? " + "AND  " + "password = ?";
        IDBConfigUtil dbConfigUtil = new DBConfigUtil();
        try ( Connection con = DriverManager.getConnection( dbConfigUtil.getDbURL(), dbConfigUtil.getDbUser(), dbConfigUtil.getDbPass() ) ;
                PreparedStatement ps = con.prepareStatement( query );          
          )  {
            ps.setString(1, userId);
            ps.setString(2, password);
            results = ps.executeQuery();
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
            } catch (SQLException e) {
                logger.error("ERROR", e);
            }
        }
    }
}