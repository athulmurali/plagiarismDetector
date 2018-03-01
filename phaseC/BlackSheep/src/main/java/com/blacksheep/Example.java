package com.blacksheep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Example {

    //static AstPrinter as = new AstPrinter();

    public static void main(String[] args) throws IOException {

       // AstPrinter as = new AstPrinter();

        ParserFacade parserFacade = new ParserFacade();
        AstPrinter astPrinter = new AstPrinter();
        AstPrinter astPrinter1 = new AstPrinter();
        astPrinter.print(parserFacade.parse(new File("/Users/prashanthavanagi/IdeaProjects/BlackSheep/src/main/java/com/blacksheep/controller/simple.py")));
        astPrinter1.print(parserFacade.parse(new File("/Users/prashanthavanagi/IdeaProjects/BlackSheep/src/main/java/com/blacksheep/controller/simple1.py")));
        //System.out.print(astPrinter.getArr());

        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();

        l1 = astPrinter.getArr();
        l2 = astPrinter1.getArr();

        checkPlagiarism check = new checkPlagiarism();
        check.check(l1,l2);
    }
}
