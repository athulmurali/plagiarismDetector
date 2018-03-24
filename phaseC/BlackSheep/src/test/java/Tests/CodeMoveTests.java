package Tests;

import com.blacksheep.ParserFacade;
import com.blacksheep.controller.ResultsController;
import com.blacksheep.parser.Matches;
import com.blacksheep.strategy.*;
import org.junit.Assert;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CodeMoveTests {

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    DetectorFactory f = new DetectorFactory();

    /**
     * test when two files are exactly the same
     */
    @Test
    public void exactlySame() throws IOException {
        Plagiarism detector = f.createCodeMoveDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple1.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("codeMove exact test fail", percentage, 100.0, 1);
    }


    /**
     * test when two files change the order of codes
     */
    @Test
    public void changeOrder() throws IOException {
        Plagiarism detector = f.createCodeMoveDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple2.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("codeMove change order test fail", percentage, 100.0, 1);
    }

    /**
     * test when one file is a part of another file
     */
    @Test
    public void partOf() throws IOException {
        Plagiarism detector = f.createCodeMoveDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple4.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("codeMove part of test fail", percentage, 98.0, 1);
    }

    /**
     * test when two totally different files
     */
    @Test
    public void totalDifferent() throws IOException {
        Plagiarism detector = f.createCodeMoveDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple6.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("codeMove total different test fail", percentage, 5.0, 1);
    }

    /**
     * test when two partly different files
     */
    @Test
    public void partDifferent() throws IOException {
        Plagiarism detector = f.createCodeMoveDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple5.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("codeMove part different test fail", percentage, 80.0, 1);
    }
}
