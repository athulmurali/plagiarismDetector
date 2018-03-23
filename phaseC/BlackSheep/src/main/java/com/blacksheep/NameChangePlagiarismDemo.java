package com.blacksheep;

import org.antlr.v4.runtime.RuleContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class contains the driver method to get the match percent between two files
 */
public class NameChangePlagiarismDemo {

    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        ParserFacade parserFacade = new ParserFacade();
//        CreateAST sourceAST = new CreateAST();
//        CreateAST suspectAST = new CreateAST();


        RuleContext sourceContext = parserFacade.parse(new File(classloader.getResource("python/simple1.py").getFile()));
        RuleContext suspectContext = parserFacade.parse(new File(classloader.getResource("python/simple2.py").getFile()));

//        List<String> sourceTree = sourceAST.getRuleSyntaxTree(sourceContext);
//        List<String> suspectTree = suspectAST.getRuleSyntaxTree(suspectContext);

        NameChangePlagiarism check = new NameChangePlagiarism();
        check.check(sourceContext,suspectContext);
    }
}
