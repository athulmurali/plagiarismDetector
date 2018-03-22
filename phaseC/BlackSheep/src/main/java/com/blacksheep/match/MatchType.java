package com.blacksheep.match;

/**
 * This enum class contains
 * all the types of matches based on which the plagiarism system works
 *
 * <br>Author : Athul
 * <br>version : 1.0
 * <br>Mar 20, 2018
 */
public enum MatchType {
    EXACT,   //  A CRC match or
    MOVED,   // for moved functions or classes
    RENAMED, // token renaming
    COMMENTS, // comments match
}
