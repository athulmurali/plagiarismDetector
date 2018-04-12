package com.blacksheep.controller;

import com.blacksheep.models.FileStreams;
import com.blacksheep.models.SourceCodeList;
import com.blacksheep.util.AWSutil;
import com.blacksheep.util.GetCodeJson;
import com.blacksheep.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * The controller to get the text of the uploaded submissions
 *
 */
@RestController
public class GetCodeController {
	/**
	 * Logger instance
	 */
	private final Logger logger = LoggerFactory.getLogger(GetCodeController.class);

	/**
	 * An API to send the eventual results in form of a JSON
	 * 
	 * @return List of CreateJson
	 */
	@RequestMapping(value = "/getCode", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<GetCodeJson>> getCode(@RequestParam("userid") String userId) {
		List<GetCodeJson> ljson = new ArrayList<>();
		Map<String, List<FileStreams>> allSubmissionStreams = new HashMap<>();

		try {
			allSubmissionStreams = AWSutil.getFileStreamsFromS3(userId);
			SourceCodeList sourceCodeList = Utility.getFilesForDetection(allSubmissionStreams);

			List<String> sourceCodes = sourceCodeList.getSourceCodes();
			List<String> folderNames = sourceCodeList.getFolderNames();

			for (int i = 0; i < folderNames.size(); i++) {
				GetCodeJson json = new GetCodeJson(folderNames.get(i), sourceCodes.get(i));
				ljson.add(json);
			}

			return new ResponseEntity<>(ljson,HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ljson,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}