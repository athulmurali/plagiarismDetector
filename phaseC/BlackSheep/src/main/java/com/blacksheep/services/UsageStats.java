package com.blacksheep.services;


import com.blacksheep.DBConfigUtil;
import com.blacksheep.ErrorCodes;
import com.blacksheep.IDBConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.*;


@RestController
public class UsageStats {


    private static final Logger logger = Logger.getLogger(UsageStats.class);
    //    Table name Usage :
    //   UPDATE `usage` SET `value` = `value` + 1 WHERE `user = "allUsers"

    // select * from usage WHERE `user = 'allUsers'


    private static final String COUNT_VALUE_COLUMN  =  "value";

    private static final String INC_USAGE_QUERY     =
            "UPDATE `usage` SET `value` = `value` + 1 WHERE user = 'allUsers'";

    private static final String DEC_USAGE_QUERY     =
            "UPDATE `usage` SET `value` = `value` - 1 WHERE user = 'allUsers'";


    private static final String GET_COUNT_QUERY     =
            "SELECT value from  `usage` WHERE user = 'allUsers'";


/**
* This class contains the implementation for the login use case
*/



/**
 * Dummy method to test the whether the server is running or not
 */
    @RequestMapping(  value = "/usageStats", method = RequestMethod.POST)
    public static int  usageStats() throws SQLException, IOException {
        int count = 0; // will be returned as the count of the usages

        IDBConfigUtil dbConfigUtil = new DBConfigUtil();

        try (Connection connection =
                     DriverManager.getConnection(
                             dbConfigUtil.getDbURL(),
                             dbConfigUtil.getDbUser(),
                             dbConfigUtil.getDbPass())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_QUERY)) {
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    // getting  the value ( the count of usage from db table column)
                    // The following line will be executed only once.

                    while (resultSet.next()) {
                        logger.info("query executed : getting value for usage stats!");
                        count = Integer.parseInt(resultSet.getString(COUNT_VALUE_COLUMN));
                    }
                }
                logger.info("Count  : " + count);
            }
        }
        return count;
    }


    /**
     * This function  is to be called in the user Login or Results method.
     * Only one place in the system.
     * The call should be based on the decision, if the count reflects the login
     * or submissions or getting results.
     *
     * @return
     */

    public  static int incrementUsageCount() throws SQLException, IOException
    {
        int status = ErrorCodes.DB_UPDATE_FAILURE;
        IDBConfigUtil dbConfigUtil = new DBConfigUtil();

        try (Connection connection =
                     DriverManager.getConnection(
                             dbConfigUtil.getDbURL(),
                             dbConfigUtil.getDbUser(),
                             dbConfigUtil.getDbPass())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INC_USAGE_QUERY)) {
                preparedStatement.executeUpdate();
                status = ErrorCodes.SUCCESS;

                    // getting  the value ( the count of usage from db table column)
                    // The following line will be executed only once.

                        logger.info("query executed :incrementing values");
                }
            }
        return status;
    }

    /**
     * Decrement the usage count
     * This function built specifically for testing.
     * When, the usage count incremented by 1, it should be decremented by 1.
     * Hence the consistency of the table will be maintained.
     * @return the error code or success code of the operation
     * @see ErrorCodes
     */

    public  static int decrementUsageCount() throws SQLException, IOException {
        int status = ErrorCodes.DB_UPDATE_FAILURE;
        IDBConfigUtil dbConfigUtil = new DBConfigUtil();

        try (Connection connection =
                     DriverManager.getConnection(
                             dbConfigUtil.getDbURL(),
                             dbConfigUtil.getDbUser(),
                             dbConfigUtil.getDbPass())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DEC_USAGE_QUERY)) {
                preparedStatement.executeUpdate();
                status = ErrorCodes.SUCCESS;

                // getting  the value ( the count of usage from db table column)
                // The following line will be executed only once.

                logger.info("query executed :decrementing values");
            }
        }
        return status;
    }
}



