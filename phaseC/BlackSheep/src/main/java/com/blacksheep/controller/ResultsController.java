package com.blacksheep.controller;

import com.blacksheep.models.FileStreams;
import com.blacksheep.models.SourceCodeList;
import com.blacksheep.services.CreateJson;
import com.blacksheep.services.Matches;
import com.blacksheep.services.ParserFacade;
import com.blacksheep.strategy.*;
import com.blacksheep.util.AWSutil;
import com.blacksheep.util.Utility;
import org.antlr.v4.runtime.RuleContext;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 
 * Controller to get the results of the matches for between submissions
 *
 */
@RestController
public class ResultsController {

	private static final String COMMENT_MATCH = "Comment Match";

	private static final String CODEMOVE_MATCH = "CodeMovement Match";

	private static final String STRUCTURE_MATCH = "Structure Match";

	private static final String CRC = "CRC Match";

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
			allSubmissionStreams = AWSutil.getFileStreamsFromS3(userId);

			List<Matches> listmatches = new ArrayList<>();
			SourceCodeList sourceCodeList = Utility.getFilesForDetection(allSubmissionStreams);

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
						createMatches(crcMatches, CRC, listmatches);
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
	 * Sets the strategy flags
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	private void getConfigFlags() throws SQLException, IOException {
		boolean[] configFlags = Utility.getConfigFlags();
		comment = configFlags[0];
		codemove = configFlags[1];
		structure = configFlags[2];		
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
					STRUCTURE_MATCH, matches);
		} else {
			initMatches(structureMatches);

		}

		List<List<String>> codeMovementMatches = new ArrayList<>();
		if (codemove) {
			codeMovementMatches = checkPlagiarism(sourceContext1, sourceContext2, new CodeMoveDetector(),
					CODEMOVE_MATCH, matches);
		} else {
			initMatches(codeMovementMatches);

		}

		List<List<String>> commentMatches = new ArrayList<>();
		if (comment) {
			commentMatches = checkPlagiarism(string1, string2, new CommentPlagiarism(), COMMENT_MATCH, matches);
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
			logger.error("", exception);
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
	
	public void setFlagsForTesting(boolean comment, boolean codemove, boolean structure) {
		this.comment = comment;
		this.codemove = codemove;
		this.structure = structure;
	}
}