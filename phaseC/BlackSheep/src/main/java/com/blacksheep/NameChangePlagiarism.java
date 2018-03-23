package com.blacksheep;

import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This class contains the implementation for comparing the syntax tree of source and suspect to get the
 * plagiarism if the variable and methods names have been changed but code order is the same
 */
public class NameChangePlagiarism {

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

    public List<List<String>> check(RuleContext sourceContext, RuleContext suspectContext) {

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
}
