package Tests;


import com.blacksheep.strategy.Context;
import com.blacksheep.strategy.FunctionExtractDetector;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * test the function extraction detector
 */
public class FunctionExtractTests {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    /**
     * test when two files are exactly the same
     */
    @Test
    public void exactlySame() throws IOException {
        FunctionExtractDetector detector =  new FunctionExtractDetector();
        File f1 = new File(classloader.getResource("python/simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/simple4.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("functionExtract exact test fail", percentage, 100.0, 10);
    }

    /**
     * test when one file extract statements into a new function
     */
    @Test
    public void extractionExist() throws IOException {
        FunctionExtractDetector detector =  new FunctionExtractDetector();
        File f1 = new File(classloader.getResource("python/ef_simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/ef_simple1.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("functionExtract exist test fail", percentage, 100.0, 1);
    }

    /**
     * test when one no function extraction exist
     */
    @Test
    public void extractionNotExist() throws IOException {
        FunctionExtractDetector detector =  new FunctionExtractDetector();
        File f1 = new File(classloader.getResource("python/ef_simple1.py").getFile());
        File f2 = new File(classloader.getResource("python/ef_simple3.py").getFile());
        double percentage = detector.getSimilarPercentage(f1, f2);
        assertEquals("functionExtract not exist test fail", percentage, 0.0, 15);
    }
}