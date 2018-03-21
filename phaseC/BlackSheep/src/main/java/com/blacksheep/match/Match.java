package com.blacksheep.match;

/**
 * Match - public class
 *
 * * <br>Author : Athul
 * <br>Mar 20, 2018
 * <br>version : 1.0
 *
 * Contains
 * the type of Match,
 * the  matched line numbers in file1 and file 2 correspondingly
 *
 * This class is to be instantiated and
 * an object array  of this type is supposed to be
 * encapsulated in the class FilePairMatch
 *

 */
public class Match {
    private MatchType matchType;
    private int[]     file1Lines;
    private int[]     file2Lines;

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public int[] getFile1Lines() {
        return file1Lines;
    }

    public void setFile1Lines(int[] file1Lines) {
        this.file1Lines = file1Lines;
    }

    public int[] getFile2Lines() {
        return file2Lines;
    }

    public void setFile2Lines(int[] file2Lines) {
        this.file2Lines = file2Lines;
    }
}
