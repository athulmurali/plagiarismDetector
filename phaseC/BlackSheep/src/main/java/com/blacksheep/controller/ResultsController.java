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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to get the results of the matches for between submissions
 */
@RestController
public class ResultsController {

	/**
	 * String for comment match in the response json
	 */
	private static final String COMMENT_MATCH = "Comment Match";

	/**
	 * String for code movement match in the response json
	 */
	private static final String CODEMOVE_MATCH = "CodeMovement Match";

	/**
	 * String for structure match in the response json
	 */
	private static final String STRUCTURE_MATCH = "Structure Match";

	/**
	 * String for the CRC match in the response json
	 */
	private static final String CRC = "CRC Match";

	/**
	 * Weight for code movement match in the response json
	 */
	private static final double CODEMOVE_WEIGHT = 0.68;

	/**
	 * Weight for structure match in the response json
	 */
	private static final double STRUCTURE_WEIGHT = 0.20;

	/**
	 * Weight for comment match in the response json
	 */
	private static final double COMMENT_WEIGHT = 0.12;

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
	 * @return List of CreateJson added RequestBody to get response
	 */
	@RequestMapping(value = "/getResults3", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<CreateJson>> initPlagiarismDetection(@RequestParam("userid") String userId) {
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
			return new ResponseEntity<>(ljson,HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(ljson,HttpStatus.INTERNAL_SERVER_ERROR);
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
	 * Starts plagiarism detection
	 * 
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
	 * Checks plagiarism using ASTS
	 * 
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
	 * Checks plagiarism using strings
	 * 
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
	 * Initializes the response arrays
	 * @param matches
	 */
	private void initMatches(List<List<String>> matches) {
		matches.add(new ArrayList<>());
		matches.add(new ArrayList<>());
		matches.add(new ArrayList<>());
		matches.get(2).add(0.0 + "");
	}

	/**
	 * Creates Match Array in JSON
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
		} catch (Exception exception) {
			logger.error("", exception);
			throw exception;
		}

	}

	/**
	 * Calculates the weighted percentage
	 * 
	 * @param structureMatch
	 *            the percentage from first comparison strategy
	 * @param codeMatch
	 *            the percentage from second comparison strategy
	 * @param commentMatch
	 *            : percentage from comment match strategy
	 * 
	 * @return the weighted percentage
	 */
	public double calculateWeightedPercentage(double structureMatch, double codeMatch, double commentMatch) {

		double codeWeight = CODEMOVE_WEIGHT;
		double commentWeight = COMMENT_WEIGHT;
		double structWeight = STRUCTURE_WEIGHT;
		
		if(!codemove && !structure && !comment) {
			codeWeight = 0;
			commentWeight = 0;
			structWeight = 0;
		}
		else if(codemove && !structure && !comment) {
			codeWeight = 1;
			commentWeight = 0;
			structWeight = 0;
		}
		else if(!codemove && structure && !comment) {
			codeWeight = 0;
			commentWeight = 0;
			structWeight = 1;
		}
		else if(!codemove && !structure && comment) {
			codeWeight = 0;
			commentWeight = 1;
			structWeight = 0;
		}
		else if(codemove && structure && !comment) {
			codeWeight += COMMENT_WEIGHT/2;
			commentWeight = 0;
			structWeight += COMMENT_WEIGHT/2;
		}
		else if(comment && codemove && !structure) {
			codeWeight += STRUCTURE_WEIGHT/2;
			commentWeight += STRUCTURE_WEIGHT/2;
			structWeight = 0;
		}
		else if(structure && comment && !codemove) {
			codeWeight = 0;
			commentWeight += CODEMOVE_WEIGHT/2;
			structWeight += CODEMOVE_WEIGHT/2;
		}
		
		return (codeWeight * codeMatch) + (structWeight * structureMatch) + (commentWeight * commentMatch);
	}
}