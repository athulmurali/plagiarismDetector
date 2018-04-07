package Tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.blacksheep.controller.GetCodeController;
import com.blacksheep.controller.UploadController;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;
import com.blacksheep.util.GetCodeJson;

public class GetCodeControllerTests {
	private static final String TESTUSER = "Test";

	@Test
	public void getDataTest() {
		GetCodeController controller = new GetCodeController();
		List<GetCodeJson> actual = controller.getCode(TESTUSER);
		
		List<GetCodeJson> expected = getExpectedOutput();
		
		assertEquals(expected.size(), actual.size());		
	}
	
	@Test
	public void exceptionTest() {
		GetCodeController controller = new GetCodeController();
		List<GetCodeJson> actual = controller.getCode(TESTUSER);
		
		List<GetCodeJson> expected = getExpectedOutput();
		
		assertEquals(expected.size(), actual.size());		
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
	
	private List<GetCodeJson> getExpectedOutput() {
		List<GetCodeJson> output = new ArrayList<>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Scanner scanner1 = null;
		Scanner scanner2 = null;
		
		try {
			File inputFile1 = new File(classloader.getResource("python/simple1.py").getFile());
			File inputFile2 = new File(classloader.getResource("python/simple4.py").getFile());
			
			InputStream in1 = new FileInputStream(inputFile1);
			InputStream in2 = new FileInputStream(inputFile2);

			scanner1 = new Scanner(in1);
			String code1 = scanner1.useDelimiter("\\A").next();
			
			scanner2 = new Scanner(in2);
			String code2 = scanner2.useDelimiter("\\A").next();

			GetCodeJson json = new GetCodeJson("Project1/simple1.py", code1);
			output.add(json);
			
			json = new GetCodeJson("Project1/simple4.py", code2);
			output.add(json);
			
			json = new GetCodeJson("Project2/simple1.py", code1);
			output.add(json);
			
			json = new GetCodeJson("Project2/simple4.py", code1);
			output.add(json);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if(scanner1 != null)
				scanner1.close();
			if(scanner2 != null)
				scanner2.close();
		}		
		
		return output;
	}
	
	@Test
	public void getJsonSetGetFileNameTest() {
		GetCodeJson json = new GetCodeJson("python1", "# no code");
		json.setFileName("NewPython1");
		assertEquals("NewPython1", json.getFileName());			
	}
	
	@Test
	public void getJsonSetGetCodeTest() {
		GetCodeJson json = new GetCodeJson("python1", "# no code");
		json.setCode("# still no code");
		assertEquals("# still no code", json.getCode());			
	}
	
}