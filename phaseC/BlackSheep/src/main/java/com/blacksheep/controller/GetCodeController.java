package com.blacksheep.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.models.FileStreams;
import com.blacksheep.models.SourceCodeList;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;
import com.blacksheep.util.GetCodeJson;
import com.blacksheep.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	@RequestMapping(value ="/getCode", method = RequestMethod.GET)
	@ResponseBody
	public List<GetCodeJson> getCode(@RequestParam("userid") String userId) {
		List<GetCodeJson> ljson = new ArrayList<>();
		Map<String, List<FileStreams>> allSubmissionStreams = new HashMap<>();

		try {
			AWSConfigUtil util = new AWSConfigUtil();
			AmazonS3 s3 = AWSConnection.getS3Client();

			String bucketName = util.getAwsBucketName();

			String userFolder = userId + SUFFIX;

			// get the list of objects inside the user folder
			List<S3ObjectSummary> submissions = s3.listObjects(bucketName, userFolder).getObjectSummaries();
			submissions = submissions.stream().filter(p -> AWSConnection.getFileName(p).endsWith(".py"))
					.collect(Collectors.toList());

			for (int i = 0; i < submissions.size(); i++) {
				S3ObjectSummary submission1 = submissions.get(i);
				String filename1 = submission1.getKey();
				filename1 = filename1.substring(filename1.indexOf(SUFFIX) + 1);

				String[] submissionPrefixes1 = filename1.split(SUFFIX);
				String projectName = submissionPrefixes1[0];

				S3Object object1 = s3.getObject(new GetObjectRequest(bucketName, submission1.getKey()));
				InputStream stream = object1.getObjectContent();

				List<FileStreams> fileStream = new ArrayList<>();

				if (allSubmissionStreams.containsKey(projectName)) {
					fileStream = allSubmissionStreams.get(projectName);
				}

				FileStreams file = new FileStreams();
				file.setFileName(filename1);
				file.setProjectName(projectName);
				file.setStream(stream);

				fileStream.add(file);
				allSubmissionStreams.put(projectName, fileStream);
			}

			SourceCodeList sourceCodeList = getFilesForDetection(allSubmissionStreams);
			
			List<String> sourceCodes = sourceCodeList.getSourceCodes();
			List<String> folderNames = sourceCodeList.getFolderNames(); 
			
			for(int i = 0; i < folderNames.size(); i++) {
				GetCodeJson json = new GetCodeJson(folderNames.get(i), sourceCodes.get(i));
				ljson.add(json);				
			}
			
			return ljson;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ljson;
		}
	}

	/**
	 * @param submissionstreams
	 * @throws IOException
	 */
	private SourceCodeList getFilesForDetection(Map<String, List<FileStreams>> submissionstreams) {
		Iterator<Entry<String, List<FileStreams>>> it = submissionstreams.entrySet().iterator();
		SourceCodeList codeList = new SourceCodeList();
		List<String> sourceCodes = new ArrayList<>();
		List<String> folderNames = new ArrayList<>();
		while (it.hasNext()) {
			Map.Entry<String, List<FileStreams>> pair = it.next();
			String projectName = pair.getKey();
			List<FileStreams> streams = pair.getValue();
			StringBuilder fileText = new StringBuilder();

			for (FileStreams file : streams) {
				fileText.append("\n # @@TOPAATMABI@@ ");
				fileText.append(file.getFileName());
				fileText.append(" start\n");

				fileText.append(Utility.streamToText(file.getStream()));

				fileText.append("\n # @@TOPAATMABI@@ ");
				fileText.append(file.getFileName());
				fileText.append(" end\n");
			}

			folderNames.add(projectName);
			sourceCodes.add(fileText.toString());
		}
		codeList.setFolderNames(folderNames);
		codeList.setSourceCodes(sourceCodes);
		return codeList;
	}

}