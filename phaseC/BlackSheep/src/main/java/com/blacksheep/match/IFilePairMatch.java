package com.blacksheep.match;

/**
 * IFilePairMatch - public interface
 *
 * * <br>Author : Athul
 * <br>Mar 20, 2018
 * <br>version : 1.0
 * **/

public interface IFilePairMatch {


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
     * getmatchType()
     * @return a match with matchType
     */
    Match[] getMatches();

}
