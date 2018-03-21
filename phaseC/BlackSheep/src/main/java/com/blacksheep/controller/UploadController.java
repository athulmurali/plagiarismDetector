package com.blacksheep.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.util.AWSConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class contains the implementation for the file upload use case
 */
@RestController
public class UploadController {

    /**
     * Id of the logged in user
     */
    private String USERID = "Mike";

    /**
     * Suffix for the folder names
     */
    private static final String SUFFIX = "/";

    /**
     * Logger instance
     */
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * Receives the student1 files and sends them to the upload method
     *
     * @param files : source files to be uploaded
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload/student1")
    public ResponseEntity uploadFileSource(@RequestParam("files") MultipartFile[] files) {

        if(files.length == 0)
            return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);

        return uploadFiles("student1", files);
    }

    /**
     * Receives the student2 files and sends them to the upload method
     *
     * @param files : suspect files to be uploaded
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload/student2")
    public ResponseEntity uploadFileSuspect(@RequestParam("files") MultipartFile[] files) {

        if(files.length == 0)
            return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);

        return uploadFiles("student2", files);
    }

    /**
     * Uploads the files to the AWS S3 instance
     *
     * @param name :
     *                   prefix of the folder on AWS
     *
     * @param files :
     *              files to the saved
     */
    private ResponseEntity<?> uploadFiles(String name, MultipartFile[] files) {
        try {
            AWSConfigUtil util = new AWSConfigUtil();
            AWSCredentials credentials = new BasicAWSCredentials(util.getAwsAccessKey(),
                    util.getAwsSecretKey());

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("us-east-2")
                    .build();

            String bucketName = util.getAwsBucketName();

            // create folder into bucket
            String folder = USERID + SUFFIX + name;

            deleteFolder(bucketName, folder, s3);
            createFolder(bucketName, folder, s3);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                // upload file to folder and set it to public
                String fileName = folder + SUFFIX + file.getOriginalFilename();
                ObjectMetadata metaData = new ObjectMetadata();
                byte[] bytes = file.getBytes();
                metaData.setContentLength(bytes.length);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metaData).withCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(putObjectRequest);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Upload success");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * This method first creates the folder with the specified name in the specified AWS bucket
     *
     * @param bucketName:
     *                  Name of the AWS bucket
     * @param folderName :
     *                   Name of the prefix in the AWS bucket
     * @param client:
     *              AWS S3 client
     */
    public void createFolder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + SUFFIX, emptyContent, metadata);

        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }

    /**
     * This method first deletes all the files in given folder and than the
     * folder itself
     *
     * @param bucketName:
     *                  Name of the AWS bucket
     * @param folderName :
     *                   Name of the prefix in the AWS bucket
     * @param client:
     *              AWS S3 client
     */
    public void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
        List fileList =
                client.listObjects(bucketName, folderName).getObjectSummaries();
        for (Object file : fileList) {
            S3ObjectSummary f = (S3ObjectSummary) file;
            client.deleteObject(bucketName, f.getKey());
        }
        client.deleteObject(bucketName, folderName);
    }
}
