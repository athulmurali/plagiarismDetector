package Tests;

import com.blacksheep.strategy.Context;
import com.blacksheep.strategy.NameChangePlagiarism;
import com.blacksheep.services.ParserFacade;
import org.antlr.v4.runtime.RuleContext;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * NameChangePlagiarismTests tests for the name change
 * code
 */
public class NameChangePlagiarismTest {

    private static String fileText1;
    private static String fileText2;
    private static String fileText3;

    /**
     * setupFiles, Sets up the files to test
     */
    @BeforeClass
    public static void setupFiles(){
        fileText1 = "def sum(a, b):\n" +
                "    mul(a,b,1)\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")";

        fileText2 = "def sum(a, b):\n" +
                "    mul(a,b,1)\n" +
                "    a+b\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c\n" +
                "\n" +
                "print(\"hello world\")";

        fileText3 = "print(\"hello world\")\n" +
                "\n" +
                "def sum(a, b):\n" +
                "    a+b\n" +
                "    mul(a,b,1)\n" +
                "\n" +
                "def mul(a,b,c):\n" +
                "    a*b*c";
    }


    /**
     * CheckPercentage, checks the List with 100 % match
     * @throws IOException
     */
    @Test
    public void CheckPercentage() throws IOException {

        ParserFacade parserFacade = new ParserFacade();

        List<String> l1 = new ArrayList<>();
        l1.add("1");
        l1.add("2");
        l1.add("3");
        l1.add("5");
        l1.add("6");
        l1.add("8");

        List<String> l2 = new ArrayList<>();
        l2.add("1");
        l2.add("2");
        l2.add("3");
        l2.add("5");
        l2.add("6");
        l2.add("8");

        List<String> l3 = new ArrayList<>();
        l3.add("100.0");

        List<List<String>> l4 = new ArrayList<>();
        l4.add(l1);
        l4.add(l2);
        l4.add(l3);


        RuleContext sourceContext = parserFacade.parse(new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8)));
        RuleContext suspectContext = parserFacade.parse(new ByteArrayInputStream(fileText2.getBytes(StandardCharsets.UTF_8)));

        Context c = new Context(new NameChangePlagiarism());
        assertEquals(l4,c.executeStrategy(sourceContext,suspectContext));


    }

    /**
     * CheckPercentageLessThan100, checks the List with less than 100 % match
     * @throws IOException
     */
    @Test
    public void CheckPercentageLessThan100() throws IOException {

        ParserFacade parserFacade = new ParserFacade();

        List<String> l1 = new ArrayList<>();
        l1.add("1");
        l1.add("3");


        List<String> l2 = new ArrayList<>();
        l2.add("1");
        l2.add("5");

        List<String> l3 = new ArrayList<>();
        l3.add("5.555555555555555");

        List<List<String>> l4 = new ArrayList<>();
        l4.add(l1);
        l4.add(l2);
        l4.add(l3);


        RuleContext sourceContext = parserFacade.parse(new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8)));
        RuleContext suspectContext = parserFacade.parse(new ByteArrayInputStream(fileText3.getBytes(StandardCharsets.UTF_8)));

        Context c = new Context(new NameChangePlagiarism());
        assertEquals(l4,c.executeStrategy(sourceContext,suspectContext));


    }
}
