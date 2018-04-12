/**
 * 
 */
package com.blacksheep.models;

import java.io.InputStream;

/**
 * The data structure to read data from the AWS S3 bucket
 *
 */
public class FileStreams {

	/***
	 * Name of the uploaded file
	 */
	private String fileName;

	/***
	 * Contents of the uploaded file
	 */
	private InputStream stream;

	/***
	 * Name of the uploaded submission containing the file
	 */
	private String projectName;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the stream
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * @param stream
	 *            the stream to set
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
