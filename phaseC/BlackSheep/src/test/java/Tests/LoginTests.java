package Tests;

import com.blacksheep.Cred;
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
     * @throws IOException 
     */
    @Test
    public void test3() throws SQLException, IOException {

        LoginController l = new LoginController();

        Cred c = new Cred();
        c.setUser("mike");
        c.setPassword("passcode");

        assertEquals(ResponseEntity.status(HttpStatus.OK).build(),l.process(c));
    }

    /**
     * Tests if the login authentication throws an error if
     * wrong creds are given
     * @throws IOException 
     */
    @Test

    public void test4() throws SQLException, IOException {
        LoginController l = new LoginController();

        Cred c = new Cred();
        c.setUser("prash");
        c.setPassword("passcode");
        assertEquals(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build(),l.process(c));
    }

}
