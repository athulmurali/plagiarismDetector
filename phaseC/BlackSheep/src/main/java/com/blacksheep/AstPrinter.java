package com.blacksheep;

import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class AstPrinter {

    private List<String> arr = new ArrayList<>();

    public List<String>  getArr() {

        int size = arr.size();
        return arr;
    }

    public void print2(){
       // System.out.println(arr);
    }

    private boolean ignoringWrappers = true;

    public void setIgnoringWrappers(boolean ignoringWrappers) {
        this.ignoringWrappers = ignoringWrappers;
    }

    public void print(RuleContext ctx) {

        System.out.println(ctx.getChild(0).getText());
        //explore(ctx, 0);
    }

    private void explore(RuleContext ctx, int indentation) {
        boolean toBeIgnored = ignoringWrappers
                && ctx.getChildCount() == 1
                && ctx.getChild(0) instanceof ParserRuleContext;
        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            String ruleName1 = ctx.getText();
            int line = ctx.getChildCount();
            for (int i = 0; i < indentation; i++) {
                System.out.print("  ");
            }
            arr.add(ruleName);
               System.out.println(line);
        }
        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext)element, indentation + (toBeIgnored ? 0 : 1));
            }
        }

    }

}