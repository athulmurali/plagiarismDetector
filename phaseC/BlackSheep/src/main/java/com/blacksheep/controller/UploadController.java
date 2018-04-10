package com.blacksheep.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blacksheep.util.AWSutil;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

/**
 * This class contains the implementation for the file upload use case
 */
@RestController
public class UploadController {
	/**
	 * Suffix for the folder names
	 */
	private static final String SUFFIX = "/";

	/**
	 * Logger instance
	 */
	private final Logger logger = Logger.getLogger(UploadController.class);

	/**
	 * Receives the student1 files and sends them to the upload method
	 *
	 * @param files
	 *            : source files to be uploaded
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ResponseEntity<?> uploadFileSource(@RequestParam("userid") String userId,
			@RequestParam("project") String projectName, @RequestParam("files[]") MultipartFile[] files) {

		if (files.length == 0)
			return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);

		return uploadFiles(userId, projectName, files);
	}

	/**
	 * Uploads the files to the AWS S3 instance
	 *
	 * @param userId
	 *            : prefix of the folder on AWS
	 * 
	 * @param projectName
	 *            : Name of the submission
	 *
	 * @param files
	 *            : files to the saved
	 */
	private ResponseEntity<?> uploadFiles(String userId, String projectName, MultipartFile[] files) {
		try {
			AWSutil util = new AWSutil();
			AmazonS3 s3 = AWSutil.getS3Client();

			String bucketName = util.getAwsBucketName();

			// create folder into bucket
			String folder = userId + SUFFIX + projectName;

			AWSutil.deleteFolder(bucketName, folder + SUFFIX, s3);
			AWSutil.createFolder(bucketName, folder + SUFFIX, s3);

			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}
				// upload file to folder and set it to public
				String fileName = folder + SUFFIX + file.getOriginalFilename();
				ObjectMetadata metaData = new ObjectMetadata();
				byte[] bytes = file.getBytes();
				metaData.setContentLength(bytes.length);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
				PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream,
						metaData).withCannedAcl(CannedAccessControlList.PublicRead);
				s3.putObject(putObjectRequest);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("Upload success");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
