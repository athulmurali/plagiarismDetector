package Tests;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.controller.UploadController;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.AWSConnection;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * This class tests login and createAST
 */
public class UploadTests {
	private static final String TEST = "test";

	@Test
	public void testFolderCreate() {
		AWSConfigUtil util = new AWSConfigUtil();
		AmazonS3 s3 = createAWSClient(util);

		String bucketName = util.getAwsBucketName();
		AWSConnection.createFolder(bucketName, TEST, s3);

		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, TEST)
				.getObjectSummaries();
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary file : fileList) {
			keys.add(file.getKey());
		}
		assertEquals(true, keys.contains(TEST));
	}

	@Test
	public void testFolderDelete() {
		AWSConfigUtil util = new AWSConfigUtil();
		AmazonS3 s3 = createAWSClient(util);

		String bucketName = util.getAwsBucketName();
		AWSConnection.deleteFolder(bucketName, TEST, s3);

		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, TEST)
				.getObjectSummaries();
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary file : fileList) {
			keys.add(file.getKey());
		}
		assertEquals(false, keys.contains(TEST));
	}

	@Test
	public void testUploadSourceFiles() throws FileNotFoundException, IOException {
		AWSConfigUtil util = new AWSConfigUtil();
		AmazonS3 s3 = createAWSClient(util);

		UploadController uploadController = new UploadController();
		String bucketName = util.getAwsBucketName();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		byte[] array = null;

		File inputFile = new File(classloader.getResource("python/simple1.py").getFile());
		try {
			array = Files.readAllBytes(inputFile.toPath());
		} catch (final IOException e) {
		}

		MultipartFile multipartFile = new MockMultipartFile("simple1.py", "simple1.py",
				"text/plain", array);

		MultipartFile[] files = { multipartFile };

		uploadController.uploadFileSource(TEST, "Project1", files);

		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, TEST)
				.getObjectSummaries();
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary file : fileList) {
			keys.add(file.getKey());
		}
		assertEquals(new ResponseEntity<>(HttpStatus.OK),
				uploadController.uploadFileSource(TEST, "Project1", files));
	}

	@Test
	public void testUploadEmptyFiles() throws FileNotFoundException, IOException {
		AWSConfigUtil util = new AWSConfigUtil();
		AmazonS3 s3 = createAWSClient(util);

		UploadController uploadController = new UploadController();
		String bucketName = util.getAwsBucketName();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		byte[] array = null;

		File inputFile = new File(classloader.getResource("python/empty.py").getFile());
		try {
			array = Files.readAllBytes(inputFile.toPath());
		} catch (final IOException e) {
		}

		MultipartFile multipartFile = new MockMultipartFile("simple1.py", "simple1.py",
				"text/plain", array);

		MultipartFile[] files = { multipartFile };

		uploadController.uploadFileSource(TEST, "Project1", files);

		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, TEST)
				.getObjectSummaries();
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary file : fileList) {
			keys.add(file.getKey());
		}
		assertEquals(new ResponseEntity<>(HttpStatus.OK),
				uploadController.uploadFileSource(TEST, "Project1", files));
	}

	@Test
	public void testUploadNoFiles() throws FileNotFoundException, IOException {
		AWSConfigUtil util = new AWSConfigUtil();
		AmazonS3 s3 = createAWSClient(util);

		UploadController uploadController = new UploadController();
		String bucketName = util.getAwsBucketName();

		MultipartFile[] files = {};

		uploadController.uploadFileSource(TEST, "Project1", files);

		List<S3ObjectSummary> fileList = s3.listObjects(bucketName, TEST)
				.getObjectSummaries();
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary file : fileList) {
			keys.add(file.getKey());
		}
		assertEquals(
				new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST),
				uploadController.uploadFileSource(TEST, "Project1", files));
	}

	/**
	 * @param util
	 * @return
	 */
	private AmazonS3 createAWSClient(AWSConfigUtil util) {
		AWSCredentials credentials = new BasicAWSCredentials(util.getAwsAccessKey(),
				util.getAwsSecretKey());

		AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("us-east-2").build();
		return s3;
	}

}