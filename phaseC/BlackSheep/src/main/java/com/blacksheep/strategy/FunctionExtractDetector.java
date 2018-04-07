package com.blacksheep.strategy;

import com.blacksheep.services.ParserFacade;
import com.blacksheep.parser.Python3Parser;
import com.blacksheep.strategy.CodeMoveDetector;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.*;

public class FunctionExtractDetector {
    // map that saves the name of a function and all its RuleContext children
    private Map<String, String> functionMap = new HashMap();


    // set of names of functions that need to be removed
    private Set<String> functionRemoveSet = new HashSet<>();


    /**
     * get the percentage of similarity of two files
     * @param f1 a file that needs to detect
     * @param f2 A file that needs to compare with
     * @return a double that indicates the percentage of similarity
     */
    public double getSimilarPercentage(File f1, File f2) throws IOException {
        CodeMoveDetector detector = new CodeMoveDetector();
        ParserFacade parserFacade = new ParserFacade();
        RuleContext t1 = parserFacade.parse(f1); // build ast1
        RuleContext t2 = parserFacade.parse(f2);
        return detector.getSimilarPercentage(t1,t2);
    }


    /**
     * change the tree structure by replace function call with its body
     * @param ctx a RuleContext represents the tree that needs to change
     */
    public void changeTreeStructure(RuleContext ctx, File file) throws IOException {
        getFunctionNameAndBody(ctx);
        replaceFunctionCall(file);
    }

    /**
     * iterate the tree with dfs, save all function names and function's body
     * @param ctx A RuleContext(ast)
     **/
    private void getFunctionNameAndBody(RuleContext ctx) {
        if (!isWrapper(ctx) && Python3Parser.ruleNames[ctx.getRuleIndex()].equals("funcdef"))
        {
            // get name of the function
            String functionName = ctx.getChild(1).getText().trim();
            System.out.println("\nfunction: " + functionName);

            // put all its function name and body into map
            //String body = ctx.getChild(4).getText(); // suite(body) of function
            String bodyString = "";
            RuleContext body = (RuleContext) ctx.getChild(4);
            System.out.println("childCOutn: " + body.getChildCount());
            for (int i=0;i<body.getChildCount();i++) {
                if (isValidChild(body.getChild(i))) {
                    System.out.println("child" + i + ": " + body.getChild(i).getText());
                    bodyString += "  " + body.getChild(i).getText() + "\n";
                }
            }
            functionMap.put(functionName, bodyString);
        }

        // dfs apply to its child
        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (isValidChild(element)) { // only for child that is context
                getFunctionNameAndBody((RuleContext)element);
            }
        }
    }

    /**
     * repalce all the function call in the file with the actual function body
     * @param file / change to inpuStream represents the original code
     * @return a string/ inputStream represents the new code
     */
    private String replaceFunctionCall(File file) throws IOException {
        InputStream codeStream = new FileInputStream(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(codeStream));
        String line = null;

        StringBuilder newCode = new StringBuilder();
        while((line = in.readLine()) != null) {
            if(isFunctionCall(line)) {
                String functionName = extractFunctionCallName(line);
                System.out.println("function name: " + functionName);
                newCode.append(this.functionMap.get(functionName) + "\n");
            }
            else {
                newCode.append(line + "\n");
            }
        }
        System.out.println("new code: \n" + newCode);
        return newCode.toString();
    }


    /**
     * decide whether given rule is a function call
     * @param text a string represents a statement
     * @return true if given rule is a function call
     */
    public Boolean isFunctionCall(String text) {
        return  text.indexOf("(") > 0 &&
                text.indexOf("def") == -1 &&
                text.indexOf("print") != 0;
    }

    /**
     * extract the function name from given string, which is a function call
     * @param s a string that contains function name and body
     * @return a string which is the name of given function
     */
    private String extractFunctionCallName(String s) {
        return s.substring(0, s.indexOf("(")).trim();
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