package com.blacksheep.controller;

import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import com.blacksheep.util.EMailer;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.sql.*;
import java.util.Map;

/**
 * This class contains the implementation for Plagiarism Configuration
 */
@RestController
public class ConfigPlagiarismController {

//    will removed once user data is passed from browser
    private static final  String TEMP_USERNAME = "mike";
    private static final String QUERY = "update credentials set percentage = ? where userid = ? " ;

    private final Logger logger = Logger.getLogger(LoginController.class);

    //Added by Athul
    /**
     *
     * @param payload contains the percentage to be set for the user account
     * @return a response entity object representing success or failure
     * @throws MessagingException
     */
    @RequestMapping( value = "/configPercentage", method = RequestMethod.POST)
    public ResponseEntity<Object> configPercentageController( @RequestBody Map<String, Object> payload)
            throws MessagingException{
        logger.info("Entering endpoint : /configPercentage");
        logger.info(payload.get("percentage"));
        Integer percentageToSet = (Integer) payload.get("percentage");

        if(updatePlagiarismPercentage(TEMP_USERNAME,percentageToSet))
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

    private  boolean updatePlagiarismPercentage(String userId, int percentage) throws MessagingException {
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
                preparedStatement.executeUpdate();

                logger.info("query executed successfully: update Table ");
                logger.info("plagiarism percentage updated for userId" + userId);
                return true;
            }
        }
        catch (SQLException e) {
            logger.error("ERROR", e);
            EMailer eMailer = new EMailer();
            eMailer.emailTeam(e.toString(),"Login security breach!");
            return false;
        }
    }

}
