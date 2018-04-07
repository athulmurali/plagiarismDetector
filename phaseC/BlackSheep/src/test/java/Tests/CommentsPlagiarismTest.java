package Tests;

import com.blacksheep.services.ParserFacade;
import com.blacksheep.strategy.CommentPlagiarism;
import com.blacksheep.strategy.Context;

import org.antlr.v4.runtime.RuleContext;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class CommentsPlagiarismTest {

	/**
	 * test when there is only one line comment in each file
	 * @throws IOException
	 */
    @Test
    public void oneSingleLineComment() throws IOException {
        String fileText1 = "# change code order compare to 2 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    mul(a,b,1)\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")\n";

        String fileText2 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    a+b\n" +
                "    mul(a,b,1)\n" +
                "\n" +
                "print(\"hello world\")\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c";

        List<List<String>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        expected.get(0).add(1 + "");
        expected.get(1).add(1 + "");
        expected.get(1).add(1 + "");


        Context c = new Context(new CommentPlagiarism());
        InputStream stream1 = new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8));
        InputStream stream2 = new ByteArrayInputStream(fileText2.getBytes(StandardCharsets.UTF_8));
        
        Scanner scanner = new Scanner(stream1);
		String s1 = scanner.useDelimiter("\\A").next();
		
		scanner = new Scanner(stream2);
		String s2 = scanner.useDelimiter("\\A").next();
        
        List<List<String>> result = c.executeStrategy(s1, s2);
        assertEquals(1, result.get(0).size());
    }

    /**
     * test when there are two lines of comments in each file
     * @throws IOException
     */
    @Test
    public void twoSingleLineComments() throws IOException {
        String fileText1 = "# change code order compare to 2 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    mul(a,b,1) # new comment\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")\n";

        String fileText2 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    a+b\n" +
                "    mul(a,b,1)\n" +
                "\n" +
                "print(\"hello world\") # new comment\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c";

        Context c = new Context(new CommentPlagiarism());
        InputStream stream1 = new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8));
        InputStream stream2 = new ByteArrayInputStream(fileText2.getBytes(StandardCharsets.UTF_8));
        
        Scanner scanner = new Scanner(stream1);
		String s1 = scanner.useDelimiter("\\A").next();
		
		scanner = new Scanner(stream2);
		String s2 = scanner.useDelimiter("\\A").next();
        
        List<List<String>> result = c.executeStrategy(s1, s2);
        assertEquals(2, result.get(0).size());
    }

    /**
     * test when similar pencentage is less than 100
     * @throws IOException
     */
    @Test
    public void percentageLessThan100() throws IOException {
        String fileText1 = "# change code order compare to 2 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    mul(a,b,1) # new comment\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")\n";

        String fileText2 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    a+b\n" +
                "    mul(a,b,1)\n" +
                "\n" +
                "print(\"hello world\") # new comment\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c";

        Context c = new Context(new CommentPlagiarism()); 
        
        List<List<String>> result = c.executeStrategy(fileText1, fileText2);
        assertEquals(99.3, Double.valueOf(result.get(2).get(0)), 2);
    }

    /**
     * test when have the exactly same comment
     * @throws IOException
     */
    @Test
    public void percentage100() throws IOException {
        String fileText1 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    mul(a,b,1)\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")\n";

        String fileText2 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    a+b\n" +
                "    mul(a,b,1)\n" +
                "\n" +
                "print(\"hello world\")\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c";

        Context c = new Context(new CommentPlagiarism());
        
        List<List<String>> result = c.executeStrategy(fileText1, fileText2);
        assertEquals(100.0, Double.valueOf(result.get(2).get(0)), 2);
    }
    
    @Test
    public void testFiles() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
    	File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());
    	Context c = new Context(new CommentPlagiarism());
    	assertEquals(null,c.executeStrategy(inputFile1,inputFile2));
    }
    
    @Test
    public void testRuleContext() throws IOException {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
    	File inputFile2 = new File(classloader.getResource("python/simple5.py").getFile());
    	
    	ParserFacade parser = new ParserFacade();
    	RuleContext ruleContext1 = parser.parse(inputFile1);
    	RuleContext ruleContext2 = parser.parse(inputFile2);
    	Context c = new Context(new CommentPlagiarism());
    	assertEquals(null,c.executeStrategy(ruleContext1,ruleContext2));
    }
    
    @Test
    public void multiLineComment() throws IOException {
        String fileText1 = "# change code order compare to 1 and 3\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    mul(a,b,1)\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")\n";

        String fileText2 = "\"\"\"\r\n" + 
        		"totally different from previous simples\r\n" + 
        		"No match\r\n" + 
        		"\"\"\"\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112:\r\n" + 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        Context c = new Context(new CommentPlagiarism()); 
        
        List<List<String>> result = c.executeStrategy(fileText1, fileText2);
        assertEquals(0.0, Double.valueOf(result.get(2).get(0)), 2);
    }
    
    @Test
    public void sameCommentTwice1() throws IOException {
        String fileText1 = "# totally different from previous simples\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112: "+ 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        String fileText2 = "# totally different from previous simples\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112: # totally different from previous simples\r\n" + 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        Context c = new Context(new CommentPlagiarism());
        
        List<List<String>> result = c.executeStrategy(fileText1, fileText2);
        assertEquals(100.0, Double.valueOf(result.get(2).get(0)), 2);
    }
    
    @Test
    public void sameCommentTwice2() throws IOException {
        String fileText2 = "# totally different from previous simples\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112: "+ 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        String fileText1 = "# totally different from previous simples\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112: # totally different from previous simples\r\n" + 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        Context c = new Context(new CommentPlagiarism());
        
        List<List<String>> result = c.executeStrategy(fileText2, fileText1);
        assertEquals(100.0, Double.valueOf(result.get(2).get(0)), 2);
    }
    
    @Test
    public void exceptionTest() throws IOException {
    	List<List<String>> expected = new ArrayList<>();
    	expected.add(new ArrayList<>());
    	expected.add(new ArrayList<>());
    	expected.add(new ArrayList<>());

        String fileText1 = "# totally different from previous simples\r\n" + 
        		"temperature = 115\r\n" + 
        		"while temperature > 112: # totally different from previous simples\r\n" + 
        		"    print(temperature)\r\n" + 
        		"    temperature = temperature - 1";

        Context c = new Context(new CommentPlagiarism());
        
        List<List<String>> result = c.executeStrategy(fileText1, null);
        assertEquals(expected, result);
    }
}
