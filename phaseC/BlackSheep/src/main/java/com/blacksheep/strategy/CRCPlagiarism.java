package com.blacksheep.strategy;

import com.blacksheep.strategy.CommentPlagiarism;
import com.blacksheep.strategy.Plagiarism;
import org.antlr.v4.runtime.RuleContext;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

/**
 * This class contains the implementation for CRC32 algorithm to check if the
 * two source files are exactly the same
 */
public class CRCPlagiarism implements Plagiarism {
	/**
	 * Logger instance
	 */
	private final Logger logger = Logger.getLogger(CommentPlagiarism.class);

	/***
	 *
	 * @param input1
	 *            : String of source file 1
	 * @param input2
	 *            : String of source file 2
	 * @return true if both the files have same CRC. False otherwise
	 */
	public List<List<String>> getDetectResult(String input1, String input2) {
		logger.info("CRC plagiarism check started");

		List<List<String>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		result.add(new ArrayList<>());
		result.add(new ArrayList<>());

		CRC32 crc1 = new CRC32();
		CRC32 crc2 = new CRC32();

		crc1.update(input1.getBytes());
		crc2.update(input2.getBytes());

		if (crc1.getValue() == crc2.getValue()) {

			String[] codeLines1 = input1.split("\\r?\\n");

			for (int i = 0; i < codeLines1.length; i++) {
				result.get(0).add(i + 1 + "");
				result.get(1).add(i + 1 + "");
			}
			result.get(2).add(100.00 + "");
			logger.info("CRC plagiarism check ended");
			return result;
		} else {
			logger.info("CRC plagiarism check ended");
			return result;
		}
	}

	/**
	 * get the detect result
	 * 
	 * @param f1
	 *            A file that needs to detect
	 * @param f2
	 *            A file that needs to compare with
	 * @return a list of three string lists that contain the line numbers of similar
	 *         codes in two files and the similar percentage
	 */
	public List<List<String>> getDetectResult(File f1, File f2) throws IOException {
		return null;
	}

	/**
	 * get the detect result
	 * 
	 * @param f1
	 *            Rulecontext of the file that needs to detect
	 * @param f2
	 *            Rulecontext of the file that needs to compare with
	 * @return a list of three string lists that contain the line numbers of similar
	 *         codes in two files and the similar percentage
	 */
	public List<List<String>> getDetectResult(RuleContext f1, RuleContext f2) throws IOException {
		return null;
	}

	/**
	 * get the detect result
	 * 
	 * @param f1
	 *            A file that needs to detect
	 * @param f2
	 *            A file that needs to compare with
	 * @return a list of three string lists that contain the line numbers of similar
	 *         codes in two files and the similar percentage
	 */
	public List<List<String>> getDetectResult(InputStream f1, InputStream f2) throws IOException {
		return null;
	}
}
