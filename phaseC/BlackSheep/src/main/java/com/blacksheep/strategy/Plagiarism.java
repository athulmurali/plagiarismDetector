package com.blacksheep.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Plagiarism interface
 */
interface Plagiarism {

    /**
     * get the detect result
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(File f1, File f2) throws IOException;

    /**
     * get the percentage of similarity of two files
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a double that indicates the percentage of similarity
     */
    public double getSimilarPercentage(File f1, File f2) throws IOException;

    /**
     * get the line numbers of similar lines in two files
     * @param f1 A RuleContext(ast) that needs to detect
     * @param f1 A RuleContext(ast) that needs to compare with
     * @return a list of two lists contain line numbers of similar codes in two files
     */
    public List<List<Integer>> getSimilarLines(File f1, File f2) throws IOException;
}
