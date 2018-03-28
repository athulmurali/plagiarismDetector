package Tests.utilTests;

import com.blacksheep.controller.ConfigPlagiarismController;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConfigPlagiarismControllerTest {

    /**
     * Test for percentage update
     */
    @Test
     public void configPercentageController() {
        ConfigPlagiarismController configController = new ConfigPlagiarismController();
        Map<String,Object> testPercentage = new HashMap<>();
        testPercentage.put("percentage",50);

        try {
            assertEquals( ResponseEntity.status(HttpStatus.OK).build(),
                    configController.configPercentageController((testPercentage)) );
        }
        catch (MessagingException e) {
            e.printStackTrace();
            assert false;
            }
    }
}