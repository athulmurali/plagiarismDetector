package Tests;

import com.blacksheep.controller.ResultsController;
import com.blacksheep.parser.Matches;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ResultControllerTest {


    @Test(expected = IndexOutOfBoundsException.class)
    public void empty() {

        ResultsController rc = new ResultsController();
        List<String> l1 = new ArrayList<>();
        l1.add("1");

        List<List<String>> list1 = new ArrayList<>();
        list1.add(l1);

        List<Integer> listInt = new ArrayList<>();
        listInt.add(1);

        Matches m = new Matches("abc", listInt, listInt);

        List<Matches> matches = new ArrayList<>();
        matches.add(m);

        rc.createMatches(list1, "test", matches);
    }

    @Test
    public void Notempty() {
        ResultsController rc = new ResultsController();
        List<String> l1 = new ArrayList<>();
        l1.add("1");

        List<String> l2 = new ArrayList<>();
        l2.add("2");

        List<List<String>> list1 = new ArrayList<>();
        list1.add(l1);
        list1.add(l2);

        List<Integer> listInt = new ArrayList<>();
        listInt.add(1);


        Matches m = new Matches("abc", listInt, listInt);

        List<Matches> matches = new ArrayList<>();
        matches.add(m);

        assertEquals("All went fine",rc.createMatches(list1, "test", matches));

    }

    @Test
    public void CheckPercentage() {
        ResultsController rc = new ResultsController();

        double v1 = 40, v2 = 40;

        assertEquals(40.0,rc.calculateWeightedPercentage(v1,v2),0);

    }



}
