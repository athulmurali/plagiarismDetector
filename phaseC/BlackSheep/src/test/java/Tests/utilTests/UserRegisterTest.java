/**
 * File             : UserRegisterTest.java
 *
 * Author           : Athul Muralidharan
 * Created          : April 2, 6 :00 pm   2018
 * Version          : 1
 */
package Tests.utilTests;

import com.blacksheep.ErrorCodes;
import com.blacksheep.controller.UserRegister;
import com.blacksheep.models.User;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserRegisterTest {

    private static final String TAKEN_EMAIL     = "mike@husky.neu.edu";
    private static final String AVAILABLE_EMAIL = "testUser1234test@gmail.com";
    private static final String VALID_PASSWORD  = "password1234";
    private static final String PROFESSOR       = "professor";

    /**
     * Test for the endpoint - /userRegister
     *
     * Without deleting the user registered, tests cannot be built.
     * hence adding delete user into this tests.
     *
     * If the following tests fail,
     * check if an account is present in the db with following credentials.
     */
    @Test
    public void userRegister() throws IOException, SQLException {

        UserRegister ur = new UserRegister();

        String email               = "testUser1234test@gmail.com";
        String password             = "password1234";
        String confirmedPassword    = "password1234";
        String role                 = "professor";

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("email", "testUser1234test@gmail.com");
        jsonObj.put("password", "password1234");
        jsonObj.put("confirmedPassword","password1234");
        jsonObj.put("role","professor");

        assertEquals(ResponseEntity.status(HttpStatus.OK).build(),ur.userRegister(jsonObj));
        assertEquals(ErrorCodes.SUCCESS, User.deleteUser("testUser1234test@gmail.com"));
    }

    /**
     * Test for the endpoint - /userRegister
     * Return failure as an account exists with the given name
     *
     * If the following tests fail,
     * there is something wrong with the validation part
     * @see UserRegister
     * @see User
     *
     */
    @Test
    public void userRegisterFailure() throws IOException, SQLException {

        UserRegister ur = new UserRegister();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("email", TAKEN_EMAIL);
        jsonObj.put("password",VALID_PASSWORD );
        jsonObj.put("confirmedPassword",VALID_PASSWORD);
        jsonObj.put("role",PROFESSOR);
        assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).build(),ur.userRegister(jsonObj));
    }

    /**
     * Test for taken email : return conflict
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void isEmailTakenTaken() throws IOException, SQLException {
        UserRegister ur = new UserRegister();
        assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).build(),
                ur.isEmailTaken(TAKEN_EMAIL));
    }

    /**
     * Test for available email : return OK
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void isEmailTakenAvailable() throws IOException, SQLException {
        UserRegister ur = new UserRegister();
        assertEquals(ResponseEntity.status(HttpStatus.OK).build(),
                ur.isEmailTaken(AVAILABLE_EMAIL));
    }
}