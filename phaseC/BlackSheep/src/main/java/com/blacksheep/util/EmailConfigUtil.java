package com.blacksheep.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



/**
 * FileName : EmailConfigUtil.java
 * Version  : 1
 * Author   : Athul
 * Date create : 22 March,2018
 * Purpose : This class retrieves the data from the emailConfig.properties file
 *   to retrieve the properties of the email config
 */
public class EmailConfigUtil {


    //name of the config file
    private static final String FILE_NAME = "emailConfig.properties";

    //logger instance
    private static final Logger logger = LoggerFactory.getLogger(EmailConfigUtil.class);


    /**
     * Private constructor to hide implicit public constructor
     */
    private EmailConfigUtil(){
    }

    /**
     *
     * @return an obj of Properties required for email config
     */
    public static Properties returnProperties() {

        logger.debug("Loading emailConfig.properties");
        Properties emailProp = new Properties();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            File file = new File(classloader.getResource(FILE_NAME).getFile());
            FileInputStream fileInput = new FileInputStream(file);

            emailProp.load(fileInput);
        } catch ( IOException e) {
            logger.error(e.getMessage());
        }
        return emailProp;
    }
}



