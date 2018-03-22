package com.blacksheep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class retrieves the data from the config.properties file
 */
public class ConfigUtil {

    /**
     * Name of the config file
     */
    private static final String fileName = "config.properties";

    /**
     * Logger instance
     */
    private final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * AWS access secret key
     */
    private String awsSecretKey;

    /**
     * AWS access key
     */
    private String awsAccessKey;

    /**
     * AWS bucket name
     */
    private String awsBucketName;

    /**
     * Gets the AWS access key
     *
     * @return AWS access key
     */
    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    /**
     * Sets the AWS access key
     *
     * @param awsAccessKey AWS access key
     */
    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    /**
     * Gets the AWS access secret key
     *
     * @return AWS access secret key
     */
    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    /**
     * Sets the AWS access secret key
     *
     * @param awsSecretKey AWS access secret key
     */
    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    /**
     * Gets the AWS bucket name
     *
     * @return AWS bucket name
     */
    public String getAwsBucketName() {
        return awsBucketName;
    }

    /**
     * Sets the AWS bucket name
     *
     * @param awsBucketName AWS bucket name
     */
    public void setAwsBucketName(String awsBucketName) {
        this.awsBucketName = awsBucketName;
    }

    /**
     * This constructor gets the config keys from config.properties and sets the values of the variables
     */
    public ConfigUtil() {
        logger.debug("Loading config.properties");
        Properties prop = new Properties();

        try {
            File file = new File(fileName);
            FileInputStream fileInput = new FileInputStream(file);

            if (fileInput == null) {
                logger.error("File not found " + fileName);
                return;
            }

            prop.load(fileInput);

            setAwsAccessKey(prop.getProperty("AWS_ACCESS_KEY"));
            setAwsSecretKey(prop.getProperty("AWS_SECRET_KEY"));
            setAwsBucketName(prop.getProperty("AWS_BUCKET_NAME"));

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}