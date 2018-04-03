/**
 * File :UserTest.java
 *
 * Author : Athul Muralidhran
 * Version : 2
 * Updated : 6: 30 pm, April 3, 2018 //Added test case for invalid userId
 */
package Tests.userAccountsTest;

import com.blacksheep.ErrorCodes;
import com.blacksheep.models.User;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static com.blacksheep.ErrorCodes.*;
import static com.blacksheep.models.User.deleteUser;
import static org.junit.Assert.assertEquals;

public class UserTest {

    // a valid userId or email is supposed to be in the format of email
    // abcd, 1234 cannot be a valid user Id for creation unless directly created from the backend
    // mike is an exception

    private static final String INVLAID_EMAIL = "user";

    /**
     * Test  for create user : invalid userId or emailId passed (An id that doesn't exist)
     * Note : Once a user is created must be deleted
     * Hence  adding delete function to createUser Test
     * is unavoidable
     */
    @Test
    public void createDeleteUser() throws SQLException, IOException {
        String username = "newUserCreated@gmail.com";
        String password = "1234";
        String confirmPassword = "1234";
        String role = "professor";
        Integer percentage = 0;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.SUCCESS,User.createUser(user));
        assertEquals(ErrorCodes.SUCCESS, deleteUser(username));
    }

    /**
     * Test  for create user : invalid userId or emailId passed (An id that doesn't exist)
     * Note : Once a user is created must be deleted
     * Hence  adding delete function to createUser Test
     * is unavoidable:
     *
     * Second test for creating user, to achieve branch of user Role as ta
     */
    @Test
    public void createDeleteUser2() throws SQLException, IOException {
        String username = "rando@gmail.com";
        String password = "1234";
        String confirmPassword = "1234";
        String role = "ta";
        Integer percentage = 0;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.SUCCESS,User.createUser(user));
        assertEquals(ErrorCodes.SUCCESS, deleteUser(username));
    }

    /**
     * Test  for existing email : invalid userId or emailId passed (An id that doesn't exist)
     */
    @Test
    public void createUserTakenEmail() throws SQLException, IOException {
        String username         = "ak123@gmail.com";
        String password         = "1234";
        String confirmPassword  = "1234";
        String role             = "ta";
        Integer percentage      = 0;
        User user = new User(username,password,confirmPassword,role,percentage);
        assertEquals(ErrorCodes.EMAIL_TAKEN, User.createUser(user));
    }

    /**
     * Test for deleting a non existing user ID (invalid)
     */
    @Test
    public void deleteInvalidUserId() throws SQLException, IOException {
        String invalidUser = "INVLAID@GMAIL.COM";
        assertEquals(deleteUser(invalidUser), USERID_INVALID);
    }

    /**
     * Test for a taken user ID
     * @throws SQLException
     */
    @Test
    public void isEmailTaken() throws SQLException, IOException {
        assert (User.isEmailTaken("mike"));
    }

    @Test
    public void passwordTooShort() throws SQLException, IOException {
        String username         = "ak1234987@gmail.com"; // new userID
        String password         = "123";
        String confirmPassword  = "123";
        String role             = "ta";
        Integer percentage      = 0;
        User user = new User(username,password,confirmPassword,role,percentage);
        assertEquals(ErrorCodes.PASSWORD_TOO_SHORT,User.createUser(user));
    }

    /**
     * Test to check if createUser returns
     * error code PASSWORD_MISMATCH
     * if the password is not matching with
     * confirm password
     * @throws SQLException
     */
    @Test
    public void passwordMismatch() throws SQLException, IOException {
        String username         = "ak1234987@gmail.com"; // new userID
        String password         = "12345";
        String confirmPassword  = "1234";
        String role             = "ta";
        Integer percentage      = 0;
        User user = new User(username,password,confirmPassword,role,percentage);
        assertEquals(ErrorCodes.PASSWORD_MISMATCH,User.createUser(user) );
    }

    /**
     * Test for invalid role in User
     * The user is assigned the default role as TA
     * User account should  be created
     * @throws SQLException
     */
    @Test
    public void roleInvalid() throws SQLException, IOException {
        String username         = "ak1234987@gmail.com"; // new userID
        String password         = "12345";
        String confirmPassword  = "12345";
        String role             = "rando";// invalid role
        Integer percentage      = 0;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.ROLE_INVALID,User.createUser(user));

    }

    /**
     * Test for invalid percentage
     */
    @Test
    public void testSubZeroPercentage() throws SQLException, IOException {

        String username         = "ak1234987@gmail.com"; // new userID
        String password         = "12345";
        String confirmPassword  = "12345";
        String role             = "professor";// invalid role
        Integer percentage      = -1;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.PERCENTAGE_INVALID,User.createUser(user));
    }

    /**
     * Test for invalid percentage
     */
    @Test
    public void testAbove100Percentage() throws SQLException, IOException {

        String username         = "ak1234987@gmail.com"; // new userID
        String password         = "12345";
        String confirmPassword  = "12345";
        String role             = "professor";// invalid role
        Integer percentage      = 101;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.PERCENTAGE_INVALID,User.createUser(user));
    }


    /**
     * Test added for invalid user Id or email format
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void createUserInvalidEmailId() throws IOException, SQLException {
        String username = "abcd";
        String password = "1234";
        String confirmPassword = "1234";
        String role = "professor";
        Integer percentage = 0;
        User user = new User(username,password,confirmPassword,role,percentage);

        assertEquals(ErrorCodes.EMAIL_INVALID,User.createUser(user));
    }
}