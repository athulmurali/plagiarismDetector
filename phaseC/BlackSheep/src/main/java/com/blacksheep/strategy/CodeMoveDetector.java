package com.blacksheep.strategy;

import com.blacksheep.parser.ParserFacade;
import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * a detector to detect plagiarism by moving code around
 * only if the hierarchy of code (like abstract codes into another
 *   method), plagiarism can be detected
 */
public class CodeMoveDetector implements Plagiarism{
    List<List<Integer>> similarLines;

    public CodeMoveDetector() {
        similarLines = new ArrayList<>();
    }

    /**
     * get the detect result
     * @param f1 InputStream of the file that needs to detect
     * @param f2 InputStream of the file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    @Override
    public List<List<String>> getDetectResult(InputStream f1, InputStream f2) throws IOException {
        // not to be implemented
        return null;
    }

    /**
     * get the detect result
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    @Override
    public List<List<String>> getDetectResult(File f1, File f2) throws IOException {
        ParserFacade parserFacade = new ParserFacade();
        RuleContext t1 = parserFacade.parse(f1);
        RuleContext t2 = parserFacade.parse(f2);
        return getDetectResult(t1, t2);
    }

    /**
     * get the detect result
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(RuleContext t1, RuleContext t2) {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());

        List<List<Integer>> similarLines = getSimilarLines(t1, t2);
        double similarPercentage = getSimilarPercentage(t1, t2);

        for(int line: similarLines.get(0)) {
            result.get(0).add(line + "");
        }

        for(int line: similarLines.get(1)) {
            result.get(1).add(line + "");
        }

        result.get(2).add(similarPercentage + "");

        return result;
    }


    /**
     * get the percentage of similarity of two files
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return a double that indicates the percentage of similarity
     */
    public double getSimilarPercentage(RuleContext t1, RuleContext t2) {
        return detect(t1, t2);
    }

    /**
     * get the line numbers of similar lines in two files
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return a list of two lists contain line numbers of similar codes in two files
     */
    public List<List<Integer>> getSimilarLines(RuleContext t1, RuleContext t2) {
        similarLines.add(new ArrayList<>());
        similarLines.add(new ArrayList<>());

        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());

        getMachedLines(t1, t2);

        HashSet<Integer> set = new HashSet<>();
        for (int line : similarLines.get(0)) set.add(line);
        result.get(0).addAll(set);
        set.clear();
        for (int line : similarLines.get(1)) set.add(line);
        result.get(1).addAll(set);
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
     * getMachedLines among two files
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return
     */
    private List<List<Integer>> getMachedLines(RuleContext t1, RuleContext t2) {
        List<List<Integer>> lines = new ArrayList<>();
        lines.add(new ArrayList<>());
        lines.add(new ArrayList<>());

        String ruleName1 = Python3Parser.ruleNames[t1.getRuleIndex()];
        String ruleName2 = Python3Parser.ruleNames[t2.getRuleIndex()];

        if (!isWrapper(t1) && !isWrapper(t2)) { // if has no wrapper

            if (!ruleName1.equals(ruleName2)) { // if have different rule name
                return lines;
            }
            else {
                lines.get(0).add(getLineNumber(t1));
                lines.get(1).add(getLineNumber(t2));
            }
        }

        for (int i = 0; i < t1.getChildCount(); i++) { // compare each branch
            ParseTree c1 = t1.getChild(i);
            if (isValidChild(c1)) { // is valid child
                int max = 0;
                ArrayList<ArrayList<Integer>> preLines = new ArrayList<>();
                preLines.add(new ArrayList<>());
                preLines.add(new ArrayList<>());

                for (int j = 0; j < t2.getChildCount(); j++) {
                    ParseTree c2 = t2.getChild(j);
                    if (isValidChild(c2)) { // get the max matched nodes between c1 and branches of t2
                        List<List<Integer>> tempLines = getMachedLines((RuleContext) c1, (RuleContext) c2);
                        int num = tempLines.get(0).size();
                        if (num > max) {
                            max = num;
                            preLines.get(0).clear();
                            preLines.get(1).clear();
                            preLines.get(0).addAll(tempLines.get(0));
                            preLines.get(1).addAll(tempLines.get(1));
                        }
                    }
                }
                lines.get(0).addAll(preLines.get(0));
                lines.get(1).addAll(preLines.get(1));
            }
        }

        // add to similarLines if satisfy conditions
        if (ruleName1.equals(ruleName2)
                && (ruleName1.equals("funcdef"))
                && getSimilarPercentage(t1, t2) > 90) {
            this.similarLines.get(0).addAll(lines.get(0));
            this.similarLines.get(1).addAll(lines.get(1));
        }
        return lines;
    }


    // previous methods, get percentage number of similarity
    /**
     * detect whether there is plagiarism by moving codes
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return A double number that indicated the percentage of plagiarism
     *        in these two trees
     */
    public double detect(RuleContext t1, RuleContext t2) {
        int totalNum = getTotalNodeNum(t1);
        int matchNum = getMachedNodeNum(t1, t2);
        if (totalNum == 0) return 0;
        else return ((double)matchNum / (double) totalNum)*100;
    }
    /**
     * get the number of matched nodes in given two trees
     * @param t1 A RuleContext(ast) that needs to detect
     * @param t2 A RuleContext(ast) that needs to compare with
     * @return
     */
    private int getMachedNodeNum(RuleContext t1, RuleContext t2) {
        int count = 0; // count of maximal matches

        if (!isWrapper(t1) && !isWrapper(t2)) { // if has no wrapper
            String ruleName1 = Python3Parser.ruleNames[t1.getRuleIndex()];
            String ruleName2 = Python3Parser.ruleNames[t2.getRuleIndex()];
            if (!ruleName1.equals(ruleName2)) // if have different rule name
                return 0;
            else {
                count++;
            }

        }

        for (int i = 0; i < t1.getChildCount(); i++) { // compare each branch
            ParseTree c1 = t1.getChild(i);
            if (isValidChild(c1)) { // is valid child
                int max = 0;
                for (int j = 0; j < t2.getChildCount(); j++) {
                    ParseTree c2 = t2.getChild(j);
                    if (isValidChild(c2)) { // get the max matched nodes between c1 and branches of t2
                        max = Math.max(max, getMachedNodeNum((RuleContext) c1, (RuleContext) c2));
                    }
                }
                count += max;
            }
        }

        return count;
    }

    /**
     * get the total number of nodes in the given tree
     * @param ctx A RuleContext(ast)
     * @return A int that indicates the total number of nodes in t
     */
    private int getTotalNodeNum(RuleContext ctx) {

        int count = 0; // should ignore wrapper
        if (!isWrapper(ctx)) {
            count++;
        }

        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (isValidChild(element)) { // only for child that is context
                count += getTotalNodeNum((RuleContext)element);
            }
        }

        return count;
    }

    /**
     * decide whether a given ctx is a wrapper
     * @param ctx
     * @return true if given ctx is a wrapper
     */
    private boolean isWrapper(RuleContext ctx) {
        return ctx.getChildCount() == 1
                && ctx.getChild(0) instanceof ParserRuleContext;
    }

    /**
     * decide whether given tree is RuleContext
     * @param t
     * @return true if given tree is RuleContext
     */
    private boolean isValidChild(ParseTree t) {
        return t instanceof RuleContext;
    }
    
    /**
     * get the detect result
     * @param f1 A file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a list of three string lists that contain the line numbers of
     *   similar codes in two files and the similar percentage
     */
    public List<List<String>> getDetectResult(String f1, String f2){
        return null;
    }

}
