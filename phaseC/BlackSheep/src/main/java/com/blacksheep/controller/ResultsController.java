package com.blacksheep.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import com.blacksheep.models.FileStreams;
import com.blacksheep.models.SourceCodeList;
import com.blacksheep.services.CreateJson;
import com.blacksheep.services.Matches;
import com.blacksheep.services.ParserFacade;
import com.blacksheep.strategy.*;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;
import com.blacksheep.util.Utility;
import org.antlr.v4.runtime.RuleContext;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RestController
public class ResultsController {

	/**
	 * Suffix for the folder names
	 */
	private static final String SUFFIX = "/";

	/**
	 * Logger instance
	 */
	private final Logger logger = Logger.getLogger(ResultsController.class);

	/**
	 * Flag to get see if structure match is enabled
	 */
	private boolean structure = false;

	/**
	 * Flag to get see if comment match is enabled
	 */
	private boolean comment = false;

	/**
	 * Flag to get see if code movement is enabled
	 */
	private boolean codemove = false;

	/**
	 * An API to send the eventual results in form of a JSON
	 * 
	 * @return List of CreateJson
	 *  added RequestBody to get response
	 */
	@RequestMapping(value = "/getResults3",  method = RequestMethod.GET)
	@ResponseBody
	public List<CreateJson> initPlagiarismDetection(@RequestParam("userid") String userId) {
		List<CreateJson> ljson = new ArrayList<>();
		Map<String, List<FileStreams>> allSubmissionStreams = new HashMap<>();

		try {
			getConfigFlags();
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

			List<Matches> listmatches = new ArrayList<>();
			SourceCodeList sourceCodeList = getFilesForDetection(allSubmissionStreams);

			List<String> sourceCodes = sourceCodeList.getSourceCodes();
			List<String> folderNames = sourceCodeList.getFolderNames();

			for (int i = 0; i < sourceCodes.size(); i++) {
				String submission1 = sourceCodes.get(i);
				ParserFacade parser1 = new ParserFacade();
				RuleContext ctx1 = parser1.parse(submission1);

				for (int j = i + 1; j < sourceCodes.size(); j++) {
					String submission2 = sourceCodes.get(j);
					ParserFacade parser2 = new ParserFacade();
					RuleContext ctx2 = parser2.parse(submission2);

					Context context = new Context(new CRCPlagiarism());
					List<List<String>> crcMatches = context.executeStrategy(submission1, submission2);
					double percentage = 0;

					if (!crcMatches.get(0).isEmpty()) {
						percentage = 100.0;
						createMatches(crcMatches, "CRC Match", listmatches);
					} else {

						percentage = getAllPlagiarism(submission1, ctx1, submission2, ctx2, listmatches);
					}
					if (!listmatches.isEmpty()) {
						CreateJson cj1 = new CreateJson(folderNames.get(i), folderNames.get(j), percentage,
								listmatches);
						ljson.add(cj1);
					}
				}

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

				fileText.append("\n @@TOPAATMABI@@# ");
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

	/**
	 * @param string1
	 * @param sourceContext1
	 * @param string2
	 * @param sourceContext2
	 * @param matches
	 * @return
	 * @throws IOException
	 */
	private double getAllPlagiarism(String string1, RuleContext sourceContext1, String string2,
			RuleContext sourceContext2, List<Matches> matches) throws IOException {
		double percentage;
		List<List<String>> structureMatches = new ArrayList<>();
		if (structure) {
			structureMatches = checkPlagiarism(sourceContext1, sourceContext2, new NameChangePlagiarism(),
					"Structure Match", matches);
		} else {
			initMatches(structureMatches);

		}

		List<List<String>> codeMovementMatches = new ArrayList<>();
		if (codemove) {
			codeMovementMatches = checkPlagiarism(sourceContext1, sourceContext2, new CodeMoveDetector(),
					"CodeMovement Match", matches);
		} else {
			initMatches(codeMovementMatches);

		}

		List<List<String>> commentMatches = new ArrayList<>();
		if (comment) {
			commentMatches = checkPlagiarism(string1, string2, new CommentPlagiarism(), "Comment Match", matches);
		} else {
			initMatches(commentMatches);

		}

		percentage = calculateWeightedPercentage(Double.parseDouble(structureMatches.get(2).get(0)),
				Double.parseDouble(codeMovementMatches.get(2).get(0)),
				Double.parseDouble(commentMatches.get(2).get(0)));
		return percentage;
	}

	/**
	 * @param sourceContext1
	 * @param sourceContext2
	 * @param plagiarism
	 * @param name
	 * @param listmatches
	 * @param flag
	 * @throws IOException
	 */
	private List<List<String>> checkPlagiarism(RuleContext sourceContext1, RuleContext sourceContext2,
			Plagiarism plagiarism, String name, List<Matches> listmatches) throws IOException {

		Context context = new Context(plagiarism);
		List<List<String>> matches = context.executeStrategy(sourceContext1, sourceContext2);
		createMatches(matches, name, listmatches);

		return matches;
	}

	/**
	 * @param string1
	 * @param string2
	 * @param plagiarism
	 * @param name
	 * @param listmatches
	 * @throws IOException
	 */
	private List<List<String>> checkPlagiarism(String string1, String string2, Plagiarism plagiarism, String name,
			List<Matches> listmatches) {

		Context context = new Context(plagiarism);
		List<List<String>> matches = context.executeStrategy(string1, string2);
		createMatches(matches, name, listmatches);

		return matches;
	}

	/**
	 * @param matches
	 */
	private void initMatches(List<List<String>> matches) {
		matches.add(new ArrayList<>());
		matches.add(new ArrayList<>());
		matches.add(new ArrayList<>());
		matches.get(2).add(0.0 + "");
	}

	/**
	 * @param s3
	 * @param bucketName
	 * @param file1
	 * @return
	 * @throws IOException
	 */
	private ByteArrayOutputStream getAWSFile(AmazonS3 s3, String bucketName, S3ObjectSummary file1) throws IOException {
		ByteArrayOutputStream baos;
		S3Object object1 = s3.getObject(new GetObjectRequest(bucketName, file1.getKey()));
		InputStream stream1 = object1.getObjectContent();
		baos = Utility.backupInput(stream1);
		return baos;
	}

	/**
	 * Helper Method to Create Match Array in JSON
	 * 
	 * @param list1
	 *            An List of List containing line matches and percentage match
	 * @param type
	 *            The type of match
	 * @param matches
	 *            The List of matches passed as reference
	 */
	public String createMatches(List<List<String>> list1, String type, List<Matches> matches) {

		try {
			List<String> l1 = list1.get(0);
			List<Integer> intl1 = new ArrayList<>();

			for (String s : l1)
				intl1.add(Integer.valueOf(s));

			List<String> l2 = list1.get(1);
			List<Integer> intl2 = new ArrayList<>();

			for (String s : l2)
				intl2.add(Integer.valueOf(s));

			if (!intl1.isEmpty() && !intl2.isEmpty()) {
				Matches match1 = new Matches(type, intl1, intl2);
				matches.add(match1);
			}

			return "All went fine";
		} catch (ArrayIndexOutOfBoundsException exception) {
			logger.error("Error:", exception);
			throw exception;
		}

	}

	/**
	 * calculateWeightedPercentage calculates the weighted percentage
	 * 
	 * @param value1
	 *            the percentage from first comparison strategy
	 * @param value2
	 *            the percentage from second comparison strategy
	 * @return double, returns the weighted percentage
	 */
	public double calculateWeightedPercentage(double value1, double value2, double value3) {
		return (0.33 * value1) + (0.33 * value2) + (0.33 * value3);
	}

	/**
	 *
	 * @param config
	 *            : Configuration values selected by the user
	 * @throws MessagingException
	 */
	private void getConfigFlags() throws SQLException, IOException {
		IDBConfigUtil dbConfigUtil = new DBConfigUtil();

		String updateTableSQL = "SELECT * FROM configPlagiarism";

		try (Connection connection = DriverManager.getConnection(dbConfigUtil.getDbURL(), dbConfigUtil.getDbUser(),
				dbConfigUtil.getDbPass())) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL)) {

				try (ResultSet results = preparedStatement.executeQuery()) {
					while (results.next()) {
						if (results.getString("comment") != null)
							comment = true;
						else
							comment = false;

						if (results.getString("codeMovement") != null)
							codemove = true;
						else
							codemove = false;

						if (results.getString("structure") != null)
							structure = true;
						else
							structure = false;
					}
				}
			}
		}
	}
	
	public void setFlagsForTesting(boolean comment, boolean codemove, boolean structure) {
		this.comment = comment;
		this.codemove = codemove;
		this.structure = structure;
	}
}