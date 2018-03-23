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

    private boolean ignoringWrappers = true;

    /**
     * Syntax tree in the form of a list
     */
    private List<RuleContext> ruleSyntaxTree = new ArrayList<>();

    /**
     * Returns the syntax tree in the list form
     *
     */
    public List<RuleContext> getRuleSyntaxTree() {
        return ruleSyntaxTree;
    }

    /**
     * Returns the syntax tree in the list form for the RuleContext provided
     *
     * @param ctx : Context of the source code
     *
     */
    public List<RuleContext> getRuleSyntaxTree(RuleContext ctx) {
        explore(ctx, 0);
        return ruleSyntaxTree;
    }

    /**
     * Takes the rule context and passes it to the explore method
     *
     * @param ctx : Rule context containing the syntax tree and other information related to the source code
     */
    public String print(RuleContext ctx) {

        explore(ctx, 0);

        System.out.println(ctx.getChild(0).getText());
        //explore(ctx, 0);

        return ctx.getChild(0).getText().trim();
    }

    /**
     * Takes the rule context of the current root, retrieves the rule names for the subtree
     *
     * @param ctx : Rule context of the of the current root
     *
     * @param indentation : Indentation of the current root
     */
    private void explore(RuleContext ctx, int indentation) {
        boolean toBeIgnored = ignoringWrappers
                && ctx.getChildCount() == 1
                && ctx.getChild(0) instanceof ParserRuleContext;
        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            for (int i = 0; i < indentation; i++) {

                System.out.print("  ");
            }
            ruleSyntaxTree.add(ctx);
            System.out.println(ruleName);
        }
        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext)element, indentation + (toBeIgnored ? 0 : 1));
            }

        }

    }

    /**
     * A method to ignore wrapper
     *
     * @param ignoringWrappers, true or false
     */
    public void setIgnoringWrappers(boolean ignoringWrappers) {
        this.ignoringWrappers = ignoringWrappers;
    }


}