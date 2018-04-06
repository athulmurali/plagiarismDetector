package Tests.utilTests;

import com.blacksheep.util.EMailer;
import com.blacksheep.util.EmailConfigUtil;

import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;


public class EMailerTest {

    private final EMailer emailerObj = new EMailer();

    public EMailerTest() throws IOException {
    }

    /**
     * Test for emailing one valid id
     */
    @Test
    public void sendAMail() {
        boolean exceptionThrown = false;
        String sender   = "BlackSheepDetector";
        String body     = "Black sheep found! You have copied. This message is sent from Plagiarism Detector ";
        String subject  = "Test mail server";
        String[] validEmailList = new String[]{"athulmurali@gmail.com"};
        try {
            emailerObj.sendEmail(body, subject, sender,validEmailList);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assert(!exceptionThrown); // returns true if no exception is thrown
    }


    /**
     * Test for sending mail to a list of valid users;
     */
    @Test
    public void sendToTeam()  {

        String body = "Black sheep found! You have copied. This message is sent from Plagiarism Detector ";
        String subject = "Test mail server";
        boolean exceptionThrown = false;

        try {
            emailerObj.emailTeam(body,subject);
        } catch (MessagingException e) {
            exceptionThrown = true;
            e.printStackTrace();
        }

        // if no exception is thrown and email is sent to the above list,
        //        assert will be true
        assert (!exceptionThrown);
    }


    /**
     * Test for sending to an invalid recipient
     */

    @Test
    public void invalidRecipientTest() {
    	EmailConfigUtil util = new EmailConfigUtil();
        boolean exceptionThrown = false;
        String sender   = "BlackSheepDetector";
        String[] invalidEMailList = new String[]{"afaddress"}; // list of invalid toAddresses
        String body = "Test for invalid test cases";
        String subject  = "Test mail server";


        try {
            emailerObj.sendEmail(body, subject,
                    sender, invalidEMailList);
        }
        catch (MessagingException e) {
            exceptionThrown = true; //SendFailedException is a Messaging exception
        }
        assert(exceptionThrown);
    }


    /**
     * Test for admin team
     */

    @Test
    public void  adminTeamMailTest()
    {
        String body = "Black sheep system restarted  - test function";
        String subject = "Black Sheep restarted";
        boolean exceptionThrown = false;

        try {
            emailerObj.emailAdminTeam(subject,body);
        } catch (MessagingException e) {
            exceptionThrown = true;
            e.printStackTrace();
        }

        // if no exception is thrown and email is sent to the above list,
        //        assert will be true
        assert (!exceptionThrown);
    }
}