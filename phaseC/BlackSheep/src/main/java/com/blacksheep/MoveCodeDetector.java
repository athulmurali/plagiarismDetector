package com.blacksheep;

import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * a detector to detect plagiarism by moving code around
 * only if the hierarchy of code (like abstract codes into another
 *   method), plagiarism can be detected
 */
public class MoveCodeDetector {

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
        return (double)matchNum / (double) totalNum;
    }

    /**
     * get the number of matched nodes in given two trees
     * @param t1
     * @param t2
     * @return
     */
    private int getMachedNodeNum(RuleContext t1, RuleContext t2) {
        int count = 0; // count of maximal matches

        if (!isWrapper(t1) && !isWrapper(t2)) { // if has no wrapper
            String ruleName1 = Python3Parser.ruleNames[t1.getRuleIndex()];
            String ruleName2 = Python3Parser.ruleNames[t2.getRuleIndex()];
            if (!ruleName1.equals(ruleName2)) // if have different rule name
                return 0;
            else
                count++;
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
}
