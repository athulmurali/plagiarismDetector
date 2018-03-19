package com.blacksheep;

import com.blacksheep.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class DBConfigUtil implements IDBConfigUtil {

    // Location of the db config with fileName:
    private static final String fileName = "config.properties";

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);


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
     */
    public DBConfigUtil(){
        logger.info("DBConfigUtil.js : loading dbConfig properties");
        Properties prop = new Properties();
        InputStream input = null;

        try {
            File file = new File(fileName);
            // uncomment for loading it from class loader
            //            input =  getClass().getResourceAsStream(filename);
            try (FileInputStream fileInput = new FileInputStream(file)) {

                if(input==null){
                    logger.info("Sorry, unable to find " + fileName);
                    return ;
                }
                //load a properties file from class path, inside static method

                prop.load(fileInput);
            }
            setDbURL(prop.getProperty("dbURL"));
            setDbUser(prop.getProperty("dbUser"));
            setDbPass(prop.getProperty("dbPass"));

            //get the property value and print it out
            logger.info(prop.getProperty("dbURL"));;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //    the following are the field names in the propertyFile
    private  String dbURL;  //  URL of the db with DB name
    private  String dbUser; //  dbUser Name
    private  String dbPass; //  db Password

}
