package com.blacksheep.models;

import java.util.List;

/**
 * 
 * Data structure to store the list of files contained in each submission
 *
 */
public class SourceCodeList {

	/***
	 * List of contents of each of the files in the submission
	 */
	List<String> sourceCodes;

	/***
	 * Names of each the submissions
	 */
	List<String> folderNames;

	/**
	 * @return the sourceCodes
	 */
	public List<String> getSourceCodes() {
		return sourceCodes;
	}

	/**
	 * @param sourceCodes
	 *            the sourceCodes to set
	 */
	public void setSourceCodes(List<String> sourceCodes) {
		this.sourceCodes = sourceCodes;
	}

	/**
	 * @return the folderNames
	 */
	public List<String> getFolderNames() {
		return folderNames;
	}

	/**
	 * @param folderNames
	 *            the folderNames to set
	 */
	public void setFolderNames(List<String> folderNames) {
		this.folderNames = folderNames;
	}

}
