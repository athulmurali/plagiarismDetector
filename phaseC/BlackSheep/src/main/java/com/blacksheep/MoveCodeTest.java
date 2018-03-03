package com.blacksheep;

import org.antlr.v4.runtime.RuleContext;
import java.io.File;
import java.io.IOException;

public class MoveCodeTest {

    public static void main(String[] args) throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        ParserFacade parserFacade = new ParserFacade();

        // build ast from file
        File file = new File(classloader.getResource("python/simple1.py").getFile());
        RuleContext ast1 = parserFacade.parse(file); // build ast1

        file = new File(classloader.getResource("python/simple2.py").getFile());
        RuleContext ast2 = parserFacade.parse(file); // build ast2

        file = new File(classloader.getResource("python/simple3.py").getFile());
        RuleContext ast3 = parserFacade.parse(file);

        file = new File(classloader.getResource("python/simple4.py").getFile());
        RuleContext ast4 = parserFacade.parse(file);

        file = new File(classloader.getResource("python/simple5.py").getFile());
        RuleContext ast5 = parserFacade.parse(file);

        file = new File(classloader.getResource("python/simple6.py").getFile());
        RuleContext ast6 = parserFacade.parse(file);

        // detect plagiarism
        // simple 1,2,3 are the same, just change the order of codes
        // simple 4 is part of simple 1
        // simple 5 has part of code from simple 1, and part of extra code
        // simple 6 is totally different from previous simples
        MoveCodeDetector d = new MoveCodeDetector();
        System.out.println("\nmachedRate 1 2: " + d.detect(ast1, ast2));
        System.out.println("\nmachedRate 1 2: " + d.detect(ast1, ast2));
        System.out.println("\nmachedRate 1 3: " + d.detect(ast1, ast3));
        System.out.println("\nmachedRate 2 3: " + d.detect(ast2, ast3));
        System.out.println("\nmachedRate 4 1: " + d.detect(ast4, ast1));
        System.out.println("\nmachedRate 4 2: " + d.detect(ast4, ast2));
        System.out.println("\nmachedRate 5 1: " + d.detect(ast5, ast1));
        System.out.println("\nmachedRate 6 1: " + d.detect(ast6, ast1));
    }
}
