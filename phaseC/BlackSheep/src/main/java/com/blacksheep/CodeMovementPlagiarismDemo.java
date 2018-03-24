package com.blacksheep;

import com.blacksheep.parser.ParserFacade;
import com.blacksheep.strategy.NameChangePlagiarism;
import org.antlr.v4.runtime.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class contains the driver method to get the match percent between two files
 */
public class CodeMovementPlagiarismDemo {

    public static void main(String[] args) throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        ParserFacade parserFacade = new ParserFacade();

        // build ast from file
        File file = new File(classloader.getResource("python/simple1.py").getFile());
        RuleContext ast1 = parserFacade.parse(file); // build ast1



        file = new File(classloader.getResource("python/simple5.py").getFile());
        RuleContext ast5 = parserFacade.parse(file);

        // detect plagiarism
        // simple 1,2,3 are the same, just change the order of codes
        // simple 4 is part of simple 1
        // simple 5 has part of code from simple 1, and part of extra code
        // simple 6 is totally different from previous simples
        CodeMovementPlagiarism d = new CodeMovementPlagiarism();
        Logger logger = LoggerFactory.getLogger(NameChangePlagiarism.class);

        logger.info("\nmachedRate 5 1: " + d.detect(ast5, ast1));


        List<List<String>> result = d.getDetectResult(ast1, ast5);
        logger.info("\nsimiliar lines");
        System.out.println("\nfile1: ");
        for (String i : result.get(0)) {
            System.out.print(i + ",");
        }
        System.out.println("\nfile2: ");
        for (String i : result.get(1)) {
            System.out.print(i + ",");
        }
        System.out.println("\npercentage: " + result.get(2).get(0));
    }
}