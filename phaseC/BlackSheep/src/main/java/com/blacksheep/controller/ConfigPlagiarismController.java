package com.blacksheep.controller;

import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.sql.*;
import java.util.Map;

/**
 * Author : Athul
 * Version : 2.0
 * Data updated : 29 March 2018
 * This class contains the implementation for Plagiarism Configuration
 */
@RestController
public class ConfigPlagiarismController {


    //    will removed once user data is passed from browser
    private static final int ONE_ROW_AFFECTED = 1;
    private static final String QUERY = "update credentials set percentage = ? where userid = ? " ;
    private final Logger logger = Logger.getLogger(LoginController.class);

    /**
     *
     * @param payload contains the percentage to be set for the user account
     *                user and percentage must be present in the json data of request
     * @return a response entity object representing success or failure
     * @throws SQLException
     */
    @RequestMapping( value = "/configPercentage", method = RequestMethod.POST)
    public ResponseEntity<Object> configPercentageController( @RequestBody Map<String, Object> payload)
            throws  SQLException {
        logger.info("Entering endpoint : /configPercentage");
        logger.info("userId" + payload.get("user"));
        logger.info(payload.get("percentage: " + "percentage"));

        Integer percentageToSet = (Integer) payload.get("percentage");
        String  userID          = (String) payload.get("user");

        if(updatePlagiarismPercentage(userID,percentageToSet))
            return ResponseEntity.status(HttpStatus.OK).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message");
    }


    /**
     *
     * @param userId the user Id of the user for which the percentage has to be changed
     * @param percentage the percentage to which the plagiarism level has to be set.
     * @return a Response entity Object
     * @throws MessagingException
     */

    private  boolean updatePlagiarismPercentage(String userId, int percentage) throws  SQLException {
        IDBConfigUtil dbConfigUtil = new DBConfigUtil();

        try (Connection connection =
                     DriverManager.getConnection(
                             dbConfigUtil.getDbURL(),
                             dbConfigUtil.getDbUser(),
                             dbConfigUtil.getDbPass()))
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY))
            {
                preparedStatement.setString(1,String.valueOf(percentage));
                preparedStatement.setString(2,userId);
                if (preparedStatement.executeUpdate() == ONE_ROW_AFFECTED)
                {
                    logger.info("query executed successfully: update Table ");
                    logger.info("plagiarism percentage updated for userId" + userId);
                    return true;
                }
                else{
                    logger.info("Plagiarism configuration not applied.");
                    logger.info("Invalid UserID");
                    return false;
                }

            }
        }
    }

}
