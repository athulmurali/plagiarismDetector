package Tests;

import com.blacksheep.services.ParserFacade;
import com.blacksheep.strategy.CRCPlagiarism;
import com.blacksheep.strategy.Context;

import org.antlr.v4.runtime.RuleContext;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class CRCPlagiarismTests {

	/**
	 * test the same files with CRC
	 * @throws IOException
	 */
    @Test
    public void sameFiles() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile = new File(classloader.getResource("python/simple1.py").getFile());

        Context c = new Context(new CRCPlagiarism());
        InputStream stream = new FileInputStream(inputFile);
        
        Scanner scanner = new Scanner(stream);
		String s = scanner.useDelimiter("\\A").next();
        
        List<List<String>> result = c.executeStrategy(s, s);
        assertEquals(8, result.get(0).size());
    }

    /**
     * test two different files with CRC
     * @throws IOException
     */
    @Test
    public void differentFiles() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
    	File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());

        Context c = new Context(new CRCPlagiarism());
        InputStream stream1 = new FileInputStream(inputFile1);
        InputStream stream2 = new FileInputStream(inputFile2);
        
        Scanner scanner = new Scanner(stream1);
		String s1 = scanner.useDelimiter("\\A").next();      
        
		scanner = new Scanner(stream2);
		String s2 = scanner.useDelimiter("\\A").next(); 
        
        List<List<String>> result = c.executeStrategy(s1, s2);
        assertEquals(0, result.get(0).size());
    }
    
    @Test
    public void testFiles() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
    	File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());
    	Context c = new Context(new CRCPlagiarism());
    	assertEquals(new ArrayList<>(),c.executeStrategy(inputFile1,inputFile2));
    }
    
    @Test
    public void testRuleContext() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
    	File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());
    	
    	ParserFacade parser = new ParserFacade();
    	RuleContext ruleContext1 = parser.parse(inputFile1);
    	RuleContext ruleContext2 = parser.parse(inputFile2);
    	Context c = new Context(new CRCPlagiarism());
    	assertEquals(new ArrayList<>(),c.executeStrategy(ruleContext1,ruleContext2));
    }
    
    @Test
    public void exceptionTest() throws IOException {
    	try {
			List<List<String>> expected = new ArrayList<>();
			expected.add(new ArrayList<>());
			expected.add(new ArrayList<>());
			expected.add(new ArrayList<>());
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());

			Context c = new Context(new CRCPlagiarism());
			InputStream stream2 = new FileInputStream(inputFile2);
			List<List<String>> result = c.executeStrategy(null, stream2);
			assertEquals(new ArrayList<>(), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
