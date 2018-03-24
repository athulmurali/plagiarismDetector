package com.blacksheep.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.blacksheep.parser.ParserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains the implementation for the file upload use case
 */
@RestController
public class GetFileController {

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
    private final Logger logger = LoggerFactory.getLogger(GetFileController.class);


    @RequestMapping(method = RequestMethod.POST, value = "/getfile")
    public ResponseEntity uploadFileSource() {
        return uploadFiles("student1", "student2");
    }

    private ResponseEntity<?> uploadFiles(String name1, String name2) {
        try {
            AWSCredentials credentials = null;
            try {
                credentials = new BasicAWSCredentials("AKIAJOOF3REKFDUYKJJQ", "KsxOKXynTIgJSlQxkeUADrHMh6VV7OoK5PsoqPV6");  //new ProfileCredentialsProvider().getCredentials();
            } catch (Exception e) {
                throw new AmazonClientException(
                        "Cannot load the credentials from the credential profiles file. " +
                                "Please make sure that your credentials file is at the correct " +
                                "location (~/.aws/credentials), and is in valid format.",
                        e);
            }

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("us-east-2")
                    .build();

            String bucketName = "black-sheep-file-upload";

            // create folder into bucket
            String Student1Folder = USERID + SUFFIX + name1;
            String Student2Folder = USERID + SUFFIX + name2;

            List student1List =
                    s3.listObjects(bucketName, Student1Folder).getObjectSummaries();

            List student2List =
                    s3.listObjects(bucketName, Student2Folder).getObjectSummaries();

            student1List.remove(student1List.get(0));
            student2List.remove(student2List.get(0));

            ParserFacade parserFacade = new ParserFacade();

            for (Object list1 : student1List) {
                for (Object list2 : student2List){

                    S3ObjectSummary f1 = (S3ObjectSummary) list1;
                    S3ObjectSummary f2 = (S3ObjectSummary) list2;

                    if(f1.getKey() != Student1Folder+SUFFIX && f2.getKey() != Student2Folder+SUFFIX) {
                        S3Object object1 = s3.getObject(new GetObjectRequest(bucketName, f1.getKey()));
                        S3Object object2 = s3.getObject(new GetObjectRequest(bucketName, f2.getKey()));

                        InputStream objectData1 = object1.getObjectContent();
                        InputStream objectData2 = object2.getObjectContent();



//                        RuleContext ast1 = parserFacade.parse(objectData1);
//                        RuleContext ast5 = parserFacade.parse(objectData2);

                        String s1 = new Scanner( objectData1).useDelimiter("\\A").next();
                        String s2 = new Scanner( objectData2).useDelimiter("\\A").next();

                        return new ResponseEntity<>(s2, HttpStatus.OK);
                    }
                }
            }

        } catch (Exception e) {
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
