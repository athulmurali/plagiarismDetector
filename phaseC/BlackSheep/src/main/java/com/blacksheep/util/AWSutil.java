package com.blacksheep.util;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.models.FileStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This class retrieves the data from the config.properties file
 */
public class AWSutil {

    /**
     * Name of the config file
     */
    private static final String FILENAME = "awsconfig.properties";

    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(AWSutil.class);

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
    public AWSutil() {
        logger.debug("Loading config.properties");
        Properties prop = new Properties();

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            File file = new File(classloader.getResource(FILENAME).getFile());
            FileInputStream fileInput = new FileInputStream(file);

            prop.load(fileInput);

            setAwsAccessKey(prop.getProperty("aws.access.key"));
            setAwsSecretKey(prop.getProperty("aws.secret.key"));
            setAwsBucketName(prop.getProperty("aws.bucket.name"));

        } catch (IOException|NullPointerException e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
	 * Suffix for the folder names
	 */
	private static final String SUFFIX = "/";

	public static AmazonS3 getS3Client() {
		AWSutil util = new AWSutil();
		AWSCredentials credentials = new BasicAWSCredentials(util.getAwsAccessKey(),
				util.getAwsSecretKey());

		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("us-east-2").build();
	}

	/**
	 * This method first creates the folder with the specified name in the specified
	 * AWS bucket
	 *
	 * @param bucketName:
	 *            Name of the AWS bucket
	 * @param folderName
	 *            : Name of the prefix in the AWS bucket
	 * @param client:
	 *            AWS S3 client
	 */
	public static void createFolder(String bucketName, String folderName,
			AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName,
				emptyContent, metadata);

		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	/**
	 * This method first deletes all the files in given folder and than the folder
	 * itself
	 *
	 * @param bucketName
	 *            : Name of the AWS bucket
	 * @param folderName
	 *            : Name of the prefix in the AWS bucket
	 * @param client
	 *            : AWS S3 client
	 */
	public static void deleteFolder(String bucketName, String folderName,
			AmazonS3 client) {
		List<S3ObjectSummary> fileList = client.listObjects(bucketName, folderName)
				.getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
	}

	/**
	 * @param summaryObject
	 * @return
	 */
	public static String getFileName(S3ObjectSummary summaryObject) {
		return summaryObject.getKey()
				.substring(summaryObject.getKey().lastIndexOf(SUFFIX) + 1);
	}
	
	/**
	 * @param userId
	 * @param allSubmissionStreams
	 * @throws IOException
	 */
	public static Map<String, List<FileStreams>> getFileStreamsFromS3(String userId) throws IOException {
		Map<String, List<FileStreams>> allSubmissionStreams = new HashMap<>();
		AWSutil util = new AWSutil();
		AmazonS3 s3 = AWSutil.getS3Client();

		String bucketName = util.getAwsBucketName();

		String userFolder = userId + SUFFIX;

		// get the list of objects inside the user folder
		List<S3ObjectSummary> submissions = s3.listObjects(bucketName, userFolder).getObjectSummaries();
		submissions = submissions.stream().filter(p -> AWSutil.getFileName(p).endsWith(".py"))
				.collect(Collectors.toList());

		for (int i = 0; i < submissions.size(); i++) {
			S3ObjectSummary submission1 = submissions.get(i);
			String filename1 = submission1.getKey();
			filename1 = filename1.substring(filename1.indexOf(SUFFIX) + 1);

			String[] submissionPrefixes1 = filename1.split(SUFFIX);
			String projectName = submissionPrefixes1[0];

			ByteArrayOutputStream baos1 = getAWSFile(s3, bucketName, submission1);
			InputStream parserStream1 = new ByteArrayInputStream(baos1.toByteArray());

			List<FileStreams> fileStream = new ArrayList<>();

			if (allSubmissionStreams.containsKey(projectName)) {
				fileStream = allSubmissionStreams.get(projectName);
			}

			FileStreams file = new FileStreams();
			file.setFileName(filename1);
			file.setProjectName(projectName);
			file.setStream(parserStream1);

			fileStream.add(file);
			allSubmissionStreams.put(projectName, fileStream);
		}
		
		return allSubmissionStreams;
	}
	
	/**
	 * @param s3
	 * @param bucketName
	 * @param file1
	 * @return
	 * @throws IOException
	 */
	private static ByteArrayOutputStream getAWSFile(AmazonS3 s3, String bucketName, S3ObjectSummary file1) throws IOException {
		ByteArrayOutputStream baos;
		S3Object object1 = s3.getObject(new GetObjectRequest(bucketName, file1.getKey()));
		InputStream stream1 = object1.getObjectContent();
		baos = Utility.backupInput(stream1);
		return baos;
	}

}
