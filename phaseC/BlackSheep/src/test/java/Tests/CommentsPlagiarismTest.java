package Tests;

import com.blacksheep.strategy.CommentPlagiarism;
import com.blacksheep.strategy.Context;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        List<List<String>> result = c.executeStrategy(stream1, stream2);
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
        List<List<String>> result = c.executeStrategy(stream1, stream2);
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
        InputStream stream1 = new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8));
        InputStream stream2 = new ByteArrayInputStream(fileText2.getBytes(StandardCharsets.UTF_8));
        List<List<String>> result = c.executeStrategy(stream1, stream2);
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
        InputStream stream1 = new ByteArrayInputStream(fileText1.getBytes(StandardCharsets.UTF_8));
        InputStream stream2 = new ByteArrayInputStream(fileText2.getBytes(StandardCharsets.UTF_8));
        List<List<String>> result = c.executeStrategy(stream1, stream2);
        assertEquals(100.0, Double.valueOf(result.get(2).get(0)), 2);
    }
}
