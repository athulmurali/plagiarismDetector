package com.blacksheep.strategy;

import org.antlr.v4.runtime.RuleContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * factory class for creating different detector
 */
public class Context {

    private Plagiarism strategy;

    public Context(Plagiarism strategy){
        this.strategy = strategy;
    }

    public List<List<String>> executeStrategy(InputStream i1, InputStream i2) throws IOException{
        return strategy.getDetectResult(i1, i2);
    }

    public List<List<String>> executeStrategy(RuleContext i1, RuleContext i2) throws IOException{
        return strategy.getDetectResult(i1, i2);
    }

    public List<List<String>> executeStrategy(File i1, File i2) throws IOException{
        return strategy.getDetectResult(i1, i2);
    }
    
    public List<List<String>> executeStrategy(String i1, String i2){
        return strategy.getDetectResult(i1, i2);
    }
}
