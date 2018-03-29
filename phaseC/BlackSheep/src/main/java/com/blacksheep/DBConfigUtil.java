package com.blacksheep;

import com.blacksheep.controller.LoginController;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class DBConfigUtil implements IDBConfigUtil {

    // please refer to the bottom of the class for data fields
    // all the getters and setters are self explanatory
    // Make sure the config.properties file is present else, an exception will be thrown.
    // Watch the console for file related exceptions.

    // Location of the db config with fileName:
    private static final String fileName = "config.properties";

    private final Logger logger = Logger.getLogger(LoginController.class);

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getDbUser() {
        return dbUser;
    }

    private void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    private void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    /**
     * Constructor for DBConfigUtil
     *  Extracts the properties of the remote DB
     *  Stores it in its private variables
     *  Can Be instantiated and values can be obtained using the above get methods
     */
    public DBConfigUtil() {
        logger.info("DBConfigUtil.js : loading dbConfig properties");
        Properties prop = new Properties();

        try {
            File file = new File(fileName);
            FileInputStream fileInput = new FileInputStream(file);

            /*if (fileInput == null) {
                logger.info("Sorry, unable to find " + fileName);
                return;
            }*/
            
            //load a properties file from class path, inside static method
            prop.load(fileInput);

            setDbURL(prop.getProperty("dbURL"));
            setDbUser(prop.getProperty("dbUser"));
            setDbPass(prop.getProperty("dbPass"));

            //get the property value and print it out
            logger.info(prop.getProperty("dbURL"));
            ;

        } catch (FileNotFoundException e) {
            logger.error( e.getMessage());
        } catch (IOException e) {
            logger.error( e.getMessage());
            // e.printStackTrace();
        }
    }

    //    the following are the field names in the propertyFile
    private  String dbURL;  //  URL of the db with DB name
    private  String dbUser; //  dbUser Name
    private  String dbPass; //  db Password

}
