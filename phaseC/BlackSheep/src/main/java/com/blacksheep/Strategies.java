package com.blacksheep;

public class Strategies {

	/**
	 * Flag for structure similarity plagiarism
	 */
    private String structure;
    
    /**
	 * Flag for code movement plagiarism
	 */
    private String codeMove;
    
    /**
	 * Flag for comment similarity plagiarism
	 */
    private String comment;
    
    /**
	 * Flag for plagiarism percentage
	 */
    private int percentage;
    
	/**
	 * @return the structure
	 */
	public String getStructure() {
		return structure;
	}
	/**
	 * @param structure the structure to set
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}
	/**
	 * @return the codeMove
	 */
	public String getCodeMove() {
		return codeMove;
	}
	/**
	 * @param codeMove the codeMove to set
	 */
	public void setCodeMove(String codeMove) {
		this.codeMove = codeMove;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the percentage
	 */
	public int getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
}
