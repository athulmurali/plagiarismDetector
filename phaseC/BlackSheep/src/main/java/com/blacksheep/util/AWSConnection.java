package com.blacksheep.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSConnection {

	/**
	 * Suffix for the folder names
	 */
	private static final String SUFFIX = "/";

	public static AmazonS3 getS3Client() {
		AWSConfigUtil util = new AWSConfigUtil();
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

	private AWSConnection() {

	}
}
