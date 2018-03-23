package com.blacksheep.strategy;

import java.io.File;
import java.util.List;

/**
 * This class contains the implementation for comparing the syntax tree of source and suspect to get the
 * plagiarism if the variable and methods names have been changed but code order is the same
 */
public class VariableChangeDetector implements Plagiarism{

    @Override
    public List<List<String>> getDetectResult(File f1, File f2) {
        return null;
    }

    @Override
    public double getSimilarPercentage(File f1, File f2) {
        return 0;
    }

    @Override
    public List<List<Integer>> getSimilarLines(File f1, File f2) {
        return null;
    }
}
