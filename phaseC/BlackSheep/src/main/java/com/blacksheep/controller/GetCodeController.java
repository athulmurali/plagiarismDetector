package com.blacksheep.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;
import com.blacksheep.util.GetCodeJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
public class GetCodeController {

	/**
	 * Suffix for the folder names
	 */
	private static final String SUFFIX = "/";

	/**
	 * Logger instance
	 */
	private final Logger logger = LoggerFactory.getLogger(GetCodeController.class);

	/**
	 * An API to send the eventual results in form of a JSON
	 * 
	 * @return List of CreateJson
	 */
	@RequestMapping("/getCode")
	public List<GetCodeJson> getCode(@RequestParam("userid") String userId) {
		Scanner scanner = null;
		List<GetCodeJson> response = new ArrayList<>();

		try {
			AWSConfigUtil util = new AWSConfigUtil();
			AmazonS3 s3 = AWSConnection.getS3Client();

			String bucketName = util.getAwsBucketName();
			String userFolder = userId + SUFFIX;

			List<S3ObjectSummary> submissions = s3.listObjects(bucketName, userFolder)
					.getObjectSummaries();
			submissions = submissions.stream()
					.filter(p -> AWSConnection.getFileName(p).endsWith(".py"))
					.collect(Collectors.toList());

			for (S3ObjectSummary submission : submissions) {
				String fileName = submission.getKey();

				S3Object object = s3
						.getObject(new GetObjectRequest(bucketName, submission.getKey()));
				InputStream stream = object.getObjectContent();

				scanner = new Scanner(stream);
				String code = scanner.useDelimiter("\\A").next();

				GetCodeJson json = new GetCodeJson(fileName, code);

				response.add(json);
			}
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}
		finally {
			if(scanner != null)
				scanner.close();
		}

	}
}