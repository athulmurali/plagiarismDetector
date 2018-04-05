package Tests;

import com.amazonaws.services.s3.AmazonS3;
import com.blacksheep.Types;
import com.blacksheep.controller.ResultsController;
import com.blacksheep.controller.UploadController;
import com.blacksheep.parser.CreateJson;
import com.blacksheep.parser.Matches;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ResultControllerTest {
	
	private static final String TESTUSER = "Test";

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

		assertEquals("All went fine", rc.createMatches(list1, "test", matches));

	}

	@Test
	public void CheckPercentage() {
		ResultsController rc = new ResultsController();

		double v1 = 40, v2 = 40, v3 = 40;

		assertEquals(39.6, rc.calculateWeightedPercentage(v1, v2, v3), 0);

	}

	@Test
	public void TestGetterSetterCreateJson() {

		List<String> l1 = new ArrayList<>();
		l1.add("1");

		List<List<String>> list1 = new ArrayList<>();
		list1.add(l1);

		List<Integer> listInt = new ArrayList<>();
		listInt.add(1);

		Matches m = new Matches("abc", listInt, listInt);

		List<Matches> matches = new ArrayList<>();
		matches.add(m);

		CreateJson cj = new CreateJson("file1", "file2", 80.0, matches);
		cj.setFile1("file1");
		cj.setFile2("file2");
		cj.setPercentage(100.0);
		cj.setMatches(matches);

		assertEquals("file1", cj.getFile1());
		assertEquals("file2", cj.getFile2());
		assertEquals(100.0, cj.getPercentage(), 0);
		assertEquals(matches, cj.getMatches());

	}

	@Test
	public void TestGetterSetterMatches() {

		List<String> l1 = new ArrayList<>();
		l1.add("1");

		List<List<String>> list1 = new ArrayList<>();
		list1.add(l1);

		List<Integer> listInt = new ArrayList<>();
		listInt.add(1);

		Matches m = new Matches("Structure Match ", listInt, listInt);

		List<Matches> matches = new ArrayList<>();
		matches.add(m);

		m.setType("Structure Match");
		m.setFile1MatchLines(listInt);
		m.setFile2MatchLines(listInt);

		assertEquals("Structure Match", m.getType());
		assertEquals(listInt, m.getFile1MatchLines());
		assertEquals(listInt, m.getFile2MatchLines());

	}

	@Test
	public void TestTypes() {

		Types t = new Types();

		t.setC1("CodeMovement");
		assertEquals("CodeMovement", t.getC1());

		t.setC2("StructureChange");
		assertEquals("StructureChange", t.getC2());

		t.setC3("CommentChange");
		assertEquals("CommentChange", t.getC3());

	}

	@Test
	public void TestPostChoices() {

		Types t = new Types();

		t.setC1("CodeMovement");
		assertEquals("CodeMovement", t.getC1());

		t.setC2("StructureChange");
		assertEquals("StructureChange", t.getC2());

		t.setC3("CommentChange");
		assertEquals("CommentChange", t.getC3());

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

		CreateJson cj = new CreateJson("file1", "file2", 80.0, matches);

		List<CreateJson> lcj = new ArrayList<>();
		lcj.add(cj);
		
		rc.initPlagiarismDetection("Mike");

		// assertEquals(lcj.toString(),rc.PostChoices(t).toString());

	}

	@Test
	public void TestPostChoices1() {
		Types t = new Types();
		
		t.setC2("StructureChange");
		assertEquals("StructureChange", t.getC2());

		t.setC3("CommentChange");
		assertEquals("CommentChange", t.getC3());

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

		CreateJson cj = new CreateJson("file1", "file2", 80.0, matches);

		List<CreateJson> lcj = new ArrayList<>();
		lcj.add(cj);
		
		rc.initPlagiarismDetection(TESTUSER);

		// assertEquals(lcj.toString(),rc.PostChoices(t).toString());

	}
	
	@Test
	public void TestPostChoices2() {

		Types t = new Types();

		t.setC1("CodeMovement");
		assertEquals("CodeMovement", t.getC1());
		
		t.setC3("CommentChange");
		assertEquals("CommentChange", t.getC3());

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

		CreateJson cj = new CreateJson("file1", "file2", 80.0, matches);

		List<CreateJson> lcj = new ArrayList<>();
		lcj.add(cj);
		
		rc.initPlagiarismDetection(TESTUSER);

		// assertEquals(lcj.toString(),rc.PostChoices(t).toString());

	}
	
	@Test
	public void TestPostChoices3() {

		Types t = new Types();

		t.setC1("CodeMovement");
		assertEquals("CodeMovement", t.getC1());

		t.setC2("StructureChange");
		assertEquals("StructureChange", t.getC2());

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

		CreateJson cj = new CreateJson("file1", "file2", 80.0, matches);

		List<CreateJson> lcj = new ArrayList<>();
		lcj.add(cj);
		
		rc.initPlagiarismDetection(TESTUSER);

		// assertEquals(lcj.toString(),rc.PostChoices(t).toString());

	}

	@BeforeClass
	public static void setUp() throws FileNotFoundException, IOException {
		UploadController uploadController = new UploadController();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		byte[] array1 = null;
		byte[] array2 = null;

		File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
		File inputFile2 = new File(classloader.getResource("python/simple4.py").getFile());
		try {
			array1 = Files.readAllBytes(inputFile1.toPath());
			array2 = Files.readAllBytes(inputFile2.toPath());
		} catch (final IOException e) {
		}

		MultipartFile multipartFile1 = new MockMultipartFile("simple1.py", "simple1.py", "text/plain", array1);

		MultipartFile multipartFile2 = new MockMultipartFile("simple4.py", "simple4.py", "text/plain", array2);

		MultipartFile[] files1 = { multipartFile1, multipartFile2 };
		MultipartFile[] files2 = { multipartFile2, multipartFile1 };

		uploadController.uploadFileSource(TESTUSER, "Project1", files1);
		uploadController.uploadFileSource(TESTUSER, "Project2", files2);
	}
	
	@AfterClass
	public static void cleaup() throws FileNotFoundException, IOException {
		AWSConfigUtil config = new AWSConfigUtil();
		AmazonS3 s3 = AWSConnection.getS3Client();
		AWSConnection.deleteFolder(config.getAwsBucketName(), TESTUSER, s3);
	}
}