package Tests;

import com.blacksheep.Cred;
import com.blacksheep.UserDetails;
import com.blacksheep.controller.LoginController;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * This class tests login and createAST
 */
public class LoginTests {

    /**
     * Test if the springboot function is running
     */
    @Test
    public void test2() {

        LoginController l = new LoginController();
        assertEquals("hello", l.hello() );

    }

    /**
     * Tests if the login authentication is working
     */
    @Test
    public void test3() throws SQLException, IOException, ClassNotFoundException {

        LoginController l = new LoginController();

        Cred c = new Cred();
        c.setUser("mike");
        c.setPassword("passcode");

        assertEquals(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build(),l.process(c));
    }

    /**
     * Tests if the login authentication throws an error if
     * wrong creds are given
     */
    @Test

    public void test4() throws SQLException, IOException, ClassNotFoundException {
        LoginController l = new LoginController();

        Cred c = new Cred();
        c.setUser("prash");
        c.setPassword("passcode");
        assertEquals(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build(),l.process(c));
    }



    /**
     * Test -  user exists but invalid password
     */
    @Test
    public void ValidUserInvalidPassword() throws SQLException, IOException, ClassNotFoundException {

        LoginController l = new LoginController();

        Cred c = new Cred();
        c.setUser("ab@a.com");
        c.setPassword("abcd"); //  Not the actual password
        assertEquals(ResponseEntity.status(HttpStatus.OK).build(),l.process(c));
    }

    @Test
    public void TestGetconfirmedPassword() {

        UserDetails ud = new UserDetails();
        ud.setConfirmedPassword("abc");

        assertEquals("abc",ud.getConfirmedPassword());

    }


}
