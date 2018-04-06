/**
 * Author : Athul
 * Version : 2.0
 * Test covers ConfigPlagiarismController.java
 * Latest test coverage for the class : 100 %
 * Updated : 29 March 2018
 */
package Tests;


import com.blacksheep.Types;
import com.blacksheep.controller.ConfigPlagiarismController;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


public class ConfigPlagiarismControllerTest {

    /**
     * Test for percentage update
     */
    @Test
    public void configPercentageController() throws SQLException, IOException {
        ConfigPlagiarismController configController = new ConfigPlagiarismController();
        Types testData = new Types();

        testData.setC2("codeMovement");
        testData.setPercentage(80);

        assertEquals( ResponseEntity.status(HttpStatus.OK).build(),
                configController.configPercentageController((testData)) );

    }

    /**
     * Test for percentage update
     */
    @Test //(expected = NullPointerException.class)
    public void configPercentageControllerThrow() {
        try {
            ConfigPlagiarismController configController = new ConfigPlagiarismController();
            Types testData = new Types();

            testData.setC2("codeMovement");
            testData.setPercentage(80);

            assertEquals( ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                    configController.configPercentageController((null)) );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}