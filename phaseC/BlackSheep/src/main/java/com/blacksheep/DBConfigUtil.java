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

    // Location of the db config with FILE_NAME:
    private static final String FILE_NAME = "config.properties";

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
    public DBConfigUtil() throws IOException{
        logger.info("DBConfigUtil.js : loading dbConfig properties");

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        File file = new File(classloader.getResource(FILE_NAME).getFile());
        try(FileInputStream fileInput = new FileInputStream(file))
        {
            Properties prop = new Properties();

            //load a properties file from class path, inside static method
            prop.load(fileInput);

            setDbURL(prop.getProperty("dbURL"));
            setDbUser(prop.getProperty("dbUser"));
            setDbPass(prop.getProperty("dbPass"));

            //get the property value and print it out
            logger.info(prop.getProperty("dbURL"));
        }
    }

    //    the following are the field names in the propertyFile
    private  String dbURL;  //  URL of the db with DB name
    private  String dbUser; //  dbUser Name
    private  String dbPass; //  db Password

}
