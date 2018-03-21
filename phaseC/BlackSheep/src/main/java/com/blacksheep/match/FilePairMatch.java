package com.blacksheep.match;


/**
 * FilePairMatch - public class
 *
 * * <br>Author : Athul
 * <br>Mar 20, 2018
 * <br>version : 1.0
 *
 * Contains
 * the match type,
 * file1 - String (name of the file)
 * file2 - String (name of another file)
 * matches - an array of type Match; refer the class for more info
 *
 * All the getter and setter methods were auto-generated in IDE
 * Purpose : This class is created in order to be sent to the UI as json response
 *
 * @see Match
 *

 */
public class FilePairMatch {

    /** getFile1()
     *
     * @return the file1 Name
     */
    public String getFile1() {
        return file1;
    }


    /** setFile1(fileName)
     *
     * @param file1   the name of the file
     */
    public void setFile1(String file1) {
        this.file1 = file1;
    }

    /** getFile2()
     *
     * @return the name of the file2
     */
    public String getFile2() {
        return file2;
    }

    /** setFile2(String)
     *
     * @param file2  a String, name of File 2
     */
    public void setFile2(String file2) {
        this.file2 = file2;
    }

    /** get all the matches for this pair
     *
     * @return a Match array obj
     * @see  Match
     */
    public Match[] getMatches() {
        return matches;
    }

    /** setMatches(Match[])
     *
     * @param matches an array of Match Type
     */
    public void setMatches(Match[] matches) {
        this.matches = matches;
    }



    /** getPercentage()
     *
     * @return an int
     * @see  Match
     */
    public int  getPercentage() {
        return percentage;
    }

    /** setPercentage(int)
     *
     * @param percentage an integer
     */
    public void setMatches(int percentage) {
        this.percentage = percentage;
    }

    private String    file1; // name of the file 1
    private String    file2; // name of the file 2
    private Match[]   matches; //  an array of type Match

    private  int percentage; // the percentage of code match in the given file

    //Note for percentage:
    // can be calculated an implemented using another function by
    //  percentage = number of lines plagiarised/number of lines in file1

}
