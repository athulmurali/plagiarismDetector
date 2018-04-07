package com.blacksheep.strategy;

import org.antlr.v4.runtime.RuleContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Plagiarism interface
 */
public interface Plagiarism {

    /**
     * get the detect result
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(File f1, File f2) throws IOException;

    /**
     * get the detect result
     * @param f1 Rulecontext of the file that needs to detect
     * @param f2 Rulecontext of the file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(RuleContext f1, RuleContext f2) throws IOException;

    /**
     * get the detect result
     * @param f1 InputStream of the file that needs to detect
     * @param f2 InputStream of the file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(InputStream f1, InputStream f2) throws IOException;
    
    /**
     * get the detect result
     * @param f1 String of the file that needs to detect
     * @param f2 String of the file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(String f1, String f2);

}
