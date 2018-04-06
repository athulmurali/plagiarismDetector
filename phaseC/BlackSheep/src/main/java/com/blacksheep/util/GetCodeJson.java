package com.blacksheep.util;

public class GetCodeJson {

    private String fileName;
    private String code;

    public GetCodeJson(String fileName, String code) {
        this.fileName = fileName;
        this.code = code;
    }

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}    
}
