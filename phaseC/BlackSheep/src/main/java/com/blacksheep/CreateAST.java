package com.blacksheep;

import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the implementation for getting the syntax tree of the source code
 */
public class CreateAST {

    /**
     * Syntax tree in the form of a list
     */
    private List<String> ruleSyntaxTree = new ArrayList<>();

    /**
     * Returns the syntax tree in the list form
     *
     */
    public List<String> getRuleSyntaxTree() {
        return ruleSyntaxTree;
    }

    /**
     * Returns the syntax tree in the list form for the RuleContext provided
     *
     * @param ctx : Context of the source code
     *
     */
    public List<String> getRuleSyntaxTree(RuleContext ctx) {
        explore(ctx, 0);
        return ruleSyntaxTree;
    }

    /**
     * Takes the rule context and passes it to the explore method
     *
     * @param ctx : Rule context containing the syntax tree and other information related to the source code
     */
    public void print(RuleContext ctx) {

        explore(ctx, 0);
    }

    /**
     * Takes the rule context of the current root, retrieves the rule names for the subtree
     *
     * @param ctx : Rule context of the of the current root
     *
     * @param indentation : Indentation of the current root
     */
    private void explore(RuleContext ctx, int indentation) {
        boolean toBeIgnored =  ctx.getChildCount() == 1
                && ctx.getChild(0) instanceof ParserRuleContext;

        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            ruleSyntaxTree.add(ruleName);
        }

        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext)element, indentation + (toBeIgnored ? 0 : 1));
            }
        }
    }
}