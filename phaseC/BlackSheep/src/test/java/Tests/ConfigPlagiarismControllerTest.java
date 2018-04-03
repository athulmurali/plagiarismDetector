/**
 * Author : Athul
 * Version : 2.0
 * Test covers ConfigPlagiarismController.java
 * Latest test coverage for the class : 100 %
 * Updated : 29 March 2018
 */
package Tests;

import com.blacksheep.controller.ConfigPlagiarismController;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class ConfigPlagiarismControllerTest {

    /**
     * Test for percentage update
     */
    @Test
    public void configPercentageController() throws SQLException, IOException {
        ConfigPlagiarismController configController = new ConfigPlagiarismController();
        Map<String,Object> testData = new HashMap<>();

        String  validUserID = "mike";
        testData.put("user",validUserID);

        testData.put("percentage",50);

        assertEquals( ResponseEntity.status(HttpStatus.OK).build(),
                configController.configPercentageController((testData)) );

    }



    /**
     * Test for percentage update
     */
    @Test
    public void testConfigPercentageControllerFailure() throws SQLException, IOException {
        String invalidUserId = "NON_EXISTING_USR";
        ConfigPlagiarismController configController = new ConfigPlagiarismController();
        Map<String,Object> testData = new HashMap<>();
        testData.put("user",invalidUserId);
        testData.put("percentage",50);


        assertEquals( ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message"),
                configController.configPercentageController((testData)) );

    }
}