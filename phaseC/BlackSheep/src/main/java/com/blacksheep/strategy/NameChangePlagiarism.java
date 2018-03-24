package com.blacksheep.strategy;

import com.blacksheep.parser.CreateAST;
import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the implementation for comparing the syntax tree of source and suspect to get the
 * plagiarism if the variable and methods names have been changed but code order is the same
 */
public class NameChangePlagiarism implements Plagiarism {

    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(NameChangePlagiarism.class);

    /**
     * Compares two syntax trees and provides the match percentage between the two source codes
     * @param sourceContext
     * @param suspectContext
     * @return
     */
    public List<List<String>> getDetectResult(RuleContext sourceContext, RuleContext suspectContext) {

        CreateAST sourceAST = new CreateAST();
        CreateAST suspectAST = new CreateAST();

        List<Integer> similarLinesSource = new ArrayList<>();
        List<String> similarLinesSourceStr = new ArrayList<>();


        List<Integer> similarLinesSuspect = new ArrayList<>();
        List<String> similarLinesSuspectStr = new ArrayList<>();


        List<RuleContext> sourceTree = sourceAST.getRuleSyntaxTree(sourceContext);
        List<RuleContext> suspectTree = suspectAST.getRuleSyntaxTree(suspectContext);

        List<List<String>> result = new ArrayList<>();


        int sourceSize = sourceTree.size();
        int suspectSize = suspectTree.size();

        double similarityCount = 0;
        double minimumSize,maximumSize;

        double matchPercent;
        String matchPercentStr;

        List<String> matchPercentStrList = new ArrayList<>();

        if (sourceSize <= suspectSize) {
            minimumSize = sourceSize;
            maximumSize = suspectSize;
        }
        else {
            minimumSize = suspectSize;
            maximumSize = sourceSize;
        }

        for (int i = 0; i < minimumSize; i++) {
            if ((Python3Parser.ruleNames[sourceTree.get(i).getRuleIndex()].equals(Python3Parser.ruleNames[suspectTree.get(i).getRuleIndex()]))) {

                similarLinesSource.add(getLineNumber(sourceTree.get(i)));
                similarLinesSuspect.add(getLineNumber(suspectTree.get(i)));

                similarityCount++;
            }
        }

        matchPercent = (similarityCount / maximumSize) * 100;
        matchPercentStr = String.valueOf(matchPercent);

        matchPercentStrList.add(matchPercentStr);

        for (Integer myInt : similarLinesSuspect) {

            similarLinesSuspectStr.add(String.valueOf(myInt));
        }

        similarLinesSuspectStr = similarLinesSuspectStr.stream().distinct().collect(Collectors.toList());

        for (Integer myInt : similarLinesSource) {
            similarLinesSourceStr.add(String.valueOf(myInt));
        }

        similarLinesSourceStr = similarLinesSourceStr.stream().distinct().collect(Collectors.toList());

        result.add(similarLinesSourceStr);
        result.add(similarLinesSuspectStr);
        result.add(matchPercentStrList);

        logger.info("The match percent is " + matchPercent);
        logger.info("Return " + result);

        return result;


    }

    /**
     * get line number of given RuleContext
     * @return a int that indicates the line number of given RuleContext
     */
    public int getLineNumber(RuleContext ctx) {
        if (ctx instanceof ParserRuleContext) {
            ParserRuleContext pctx = (ParserRuleContext) ctx;
            return pctx.getStart().getLine();
        }
        return -1;
    }

    /**
     * get the detect result
     * @param f1 InputStream of the file that needs to detect
     * @param f2 InputStream of the file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(InputStream f1, InputStream f2) throws IOException{
        return null;
    }

    /**
     * get the detect result
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(File f1, File f2) throws IOException{
        return null;
    }
}
