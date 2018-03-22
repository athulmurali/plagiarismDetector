package com.blacksheep.match;
/**
 * IMatch - public interface
 *
 *  <br>Author : Athul
 * <br>Mar 20, 2018
 * <br>version : 1.0
 **/

public interface IMatch {

    /** getMatchTyoe
     *
     * @return a enum value from the enum MatchType
     */
    MatchType getMatchTyoe();

    /** getFile1Name()
     *
     * @return a String with the file1Name
     */
    String getFile1Name();

    /**
     *
     * @return a String with the file2Name
     */
    String getFile2Name();

    /**
     *
     * @return an int array of numbers,
     * where each element in the array represents the appropriate line number for file1
     * example [0,1,3,5]
     */
    String getFile1Lines();


    /** getFile2Lines
     *
     * @return  where each element in the array represents the appropriate line number for file2
     * example [0,1,3,5]
     */
    String getFile2Lines();



}
