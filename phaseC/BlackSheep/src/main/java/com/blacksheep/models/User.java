/**
 * Author : Athul
 * Version : 2.0
 * Models contains the user functions and data for user registration
 */


package com.blacksheep.models;
import com.blacksheep.DBConfigUtil;
import com.blacksheep.ErrorCodes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.log4j.Logger;
import org.apache.commons.validator.*;


import java.io.IOException;
import java.sql.*;

import static java.sql.DriverManager.*;


//For role auth and check
//https://gist.github.com/athulmurali/52c63b415737492016ade599c6ace5c7
public class User
{

    private static final String PROFESSOR = "professor";
    private static final String TA = "ta";

    private String email;
    private String password;
    private String confirmedPassword;

    private Role        role;
    private Integer     percentage;

    private static final  BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final  Logger logger = Logger.getLogger(User.class);

    public User(String email,String password,
                String confirmedPassword,
                String role,Integer percentage){
        this.email = email;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        setRoleEnum(role);
        this.percentage = percentage;

    }


    /**
     * Sets the role for the user object
     * if the supplied user role is
     * professor then assign role professor
     * if the supplied user role is TA, then assign TA
     * else null
     * @param role
     */
    private void setRoleEnum(String role)
    {
        if (role.equals(PROFESSOR))
            this.role = Role.PROFESSOR;
        else if  (role.equals(TA))
            this.role= Role.TA;
        else  this.role = null;
    }


    /**
     *
     * @param user This user to be added to the database
     * @return
     * @throws SQLException
     * End point access to be given

     */
    public static  int createUser(User user) throws SQLException, IOException {

        // if - email ID format is not right
        if (! EmailValidator.getInstance().isValid(user.email))
            return ErrorCodes.EMAIL_INVALID;

        // if - email ID is already registered in the system
        if (isEmailTaken(user.email))
            return ErrorCodes.EMAIL_TAKEN;

        // if password and confirm Password are not same.
        if (!user.password.equals(user.confirmedPassword))
            return ErrorCodes.PASSWORD_MISMATCH;

        // if- role is anything other than the TA or professor
        // This happens only if some tries to break into the system.
        if (user.password.length() < 4)
            return ErrorCodes.PASSWORD_TOO_SHORT;

        // if- role is anything other than the ta or professor
        //the following occurs
        if( user.role == null)
            return ErrorCodes.ROLE_INVALID;

        // percentages should be accepted only between 0 to 100
        if(user.percentage.intValue() < 0  || user.percentage.intValue() > 100 )
            return ErrorCodes.PERCENTAGE_INVALID;

        createUserRecord(user);
        return 0;
    }

    /**
     *
     * @param userId that is to be deleted from the database
     * @return an error code for the result
     * @see ErrorCodes
     *
     *   End point access must not be given ! Never
     *   As deletion of user requires eleveated previlages
     *   which is not in the scope of this project

     * End point access must not be given ! Never

     */
    public static  int deleteUser(String  userId) throws SQLException, IOException {
        DBConfigUtil dbConfigUtil = new DBConfigUtil();

        try (Connection connection = getConnection(dbConfigUtil.getDbURL(),
                dbConfigUtil.getDbUser(),
                dbConfigUtil.getDbPass())) {
            {
                String query = "DELETE FROM  credentials where userid = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, userId);
                    int result = preparedStatement.executeUpdate();
                    if (result == 0) {
                        logger.info("query executed : User account delete | But Invalid User ID");
                        return ErrorCodes.USERID_INVALID;
                    } else if (result == 1) {
                        logger.info("query executed : 1 User account delete");
                        return ErrorCodes.SUCCESS;

                    }
                }

            }
        }

        return ErrorCodes.CONNECTION_FAILURE;
    }


    /**
     *
     * @param email
     * @return a boolean result
     * if the email is taken - returns true
     * else false ( otherwise called 'useriD available'
     * for registration
     * @throws SQLException
     * Endpoint to be created
     * Helps users to check if an user account
     * is already created with this id
     *
     *    End point access to be given

     */

    public static boolean isEmailTaken(String email) throws SQLException, IOException {

        DBConfigUtil dbConfigUtil = new DBConfigUtil();
        String query = "select * from credentials WHERE userid =?";

        try (Connection connection = getConnection
                (dbConfigUtil.getDbURL(),
                        dbConfigUtil.getDbUser(),
                        dbConfigUtil.getDbPass())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                int count;
                try (ResultSet results = preparedStatement.executeQuery()) {
                    logger.info("SQL : executed query check if username exists");

                    count = 0;
                    while (results.next()) count++;
                }

                return (count >0);
            }
        }

    }


    /**
     * Creates an user Record
     * Designed to be triggered using createUser()
     * Note : No direct invocation of this method is encouraged
     * to avoid sql error code tracing mystery missions
     *
     * End point access must not be given ! Never
     * @param user
     */
    private static void createUserRecord(User user) throws SQLException, IOException {

        DBConfigUtil dbConfigUtil = new DBConfigUtil();
        String createUserQuery = "INSERT into  credentials" +
                "(userid, password, role, percentage) " +
                "VALUES(?,?,?,?)";

        try (Connection connection =
                     getConnection(dbConfigUtil.getDbURL(),
                             dbConfigUtil.getDbUser(),
                             dbConfigUtil.getDbPass()))
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(createUserQuery))
            {
                preparedStatement.setString(1, user.email);
                preparedStatement.setString(2,
                        PASSWORD_ENCODER.encode(user.password));
                preparedStatement.setString(3, user.role.toString());
                preparedStatement.setInt(4, user.percentage);
                int result = preparedStatement.executeUpdate();
                logger.info("query executed : User account created");
                logger.info("creation result ; " + result);
            }
        }
    }

}
