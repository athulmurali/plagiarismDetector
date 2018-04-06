package com.blacksheep.models;

import java.util.List;

public class SourceCodeList {
	
	List<String> sourceCodes;
	List<String> folderNames;
	
	/**
	 * @return the sourceCodes
	 */
	public List<String> getSourceCodes() {
		return sourceCodes;
	}
	/**
	 * @param sourceCodes the sourceCodes to set
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
	 * @param folderNames the folderNames to set
	 */
	public void setFolderNames(List<String> folderNames) {
		this.folderNames = folderNames;
	}

}
