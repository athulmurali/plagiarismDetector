package com.blacksheep.strategy;

import java.io.File;
import java.util.List;

public class CRCDetector implements Plagiarism{
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
