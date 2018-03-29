package com.blacksheep.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blacksheep.*;
import com.blacksheep.parser.CreateJson;
import com.blacksheep.parser.Matches;
import com.blacksheep.parser.ParserFacade;
import com.blacksheep.strategy.*;
import com.blacksheep.util.AWSConfigUtil;
import com.blacksheep.util.Utility;
import org.antlr.v4.runtime.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ResultsController {



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
    private final Logger logger = LoggerFactory.getLogger(ResultsController.class);


    private boolean structure = false;
    private boolean comment = false;
    private boolean codemove = false;

    /**
     * Controller to take values of the optional Strategy
     * @param c, Types JSON
     * @return a list of JSON Objects
     */
    @RequestMapping(
            value = "/PostChoices",
            method = RequestMethod.POST)
    public List<CreateJson> postChoices(@RequestBody Types c){

        try {

            structure = false;
            comment = false;
            codemove = false;

            if (c.getC1() != null)
                structure = true;
            if (c.getC2() != null)
                codemove = true;
            if (c.getC3() != null)
                comment = true;


            return inputStream();
        }
        catch (Exception e){
            logger.error("Error",e);
        }

        return new ArrayList<>();

    }


    /**
     * An API to send the eventual results in form of a JSON
     * @return List of CreateJson
     */
    @RequestMapping("/getResults3")
    public List<CreateJson> inputStream() {

         ByteArrayOutputStream baos1;
         ByteArrayOutputStream baos2;
        List<CreateJson> ljson = new ArrayList<>();

        try {
            AWSCredentials credentials = null;
            credentials = new BasicAWSCredentials("AKIAJOOF3REKFDUYKJJQ", "KsxOKXynTIgJSlQxkeUADrHMh6VV7OoK5PsoqPV6");

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("us-east-2")
                    .build();

            String bucketName = "black-sheep-file-upload";

            // create folder into bucket
            String studentOneFolder = USERID + SUFFIX + "student1";
            String studentTwoFolder = USERID + SUFFIX + "student2";

            List student1List =
                    s3.listObjects(bucketName, studentOneFolder).getObjectSummaries();

            List student2List =
                    s3.listObjects(bucketName, studentTwoFolder).getObjectSummaries();

            student1List.remove(student1List.get(0));
            student2List.remove(student2List.get(0));

            ParserFacade parserFacade = new ParserFacade();

            CreateJson cj1;

            for (Object file1 : student1List) {

                S3ObjectSummary f1 = (S3ObjectSummary) file1;
                S3Object object1 = s3.getObject(new GetObjectRequest(bucketName, f1.getKey()));
                String last1 = f1.getKey().substring(f1.getKey().lastIndexOf('/') + 1);
                InputStream stream1 = object1.getObjectContent();
                baos1 = Utility.backupInput(stream1);
                InputStream parserStream1 = new ByteArrayInputStream(baos1.toByteArray());
                RuleContext sourceContext1 = parserFacade.parse(parserStream1);

                for (Object file2 : student2List) {

                    S3ObjectSummary f2 = (S3ObjectSummary) file2;
                    S3Object object2 = s3.getObject(new GetObjectRequest(bucketName, f2.getKey()));

                    String last2 = f2.getKey().substring(f2.getKey().lastIndexOf('/') + 1);

                    InputStream stream2 = object2.getObjectContent();
                    baos2 = Utility.backupInput(stream2);

                    InputStream parserStream2 = new ByteArrayInputStream(baos2.toByteArray());
                    RuleContext sourceContext2 = parserFacade.parse(parserStream2);

                    List<Matches> listmatches = new ArrayList<>();

                    InputStream crcStream1 = new ByteArrayInputStream(baos1.toByteArray());
                    InputStream crcStream2 = new ByteArrayInputStream(baos2.toByteArray());

                    double percentage = 0;

                    Context context = new Context(new CRCPlagiarism());
                    List<List<String>> list4 = context.executeStrategy(crcStream1, crcStream2);

                    if(!list4.get(0).isEmpty()){
                        percentage = 100.0;
                        createMatches(list4,"CRC Match", listmatches);
                    }
                    else{
                        List<List<String>> list2 = new ArrayList<>();
                        list2.add(new ArrayList<>());
                        list2.add(new ArrayList<>());
                        list2.add(new ArrayList<>());
                        list2.get(2).add(0.0+"");

                        List<List<String>> list3 = new ArrayList<>();
                        list3.add(new ArrayList<>());
                        list3.add(new ArrayList<>());
                        list3.add(new ArrayList<>());
                        list3.get(2).add(0.0+"");

                        List<List<String>> list1 = new ArrayList<>();
                        list1.add(new ArrayList<>());
                        list1.add(new ArrayList<>());
                        list1.add(new ArrayList<>());
                        list1.get(2).add(0.0+"");

                        if(structure){
                            context = new Context(new NameChangePlagiarism());
                            list2 = context.executeStrategy(sourceContext1, sourceContext2);
                            createMatches(list2,"Structure Match", listmatches);
                        }

                        if(codemove){
                            context = new Context(new CodeMoveDetector());
                            list1 = context.executeStrategy(sourceContext1, sourceContext2);
                            createMatches(list1,"CodeMovement Match", listmatches);
                        }

                        if(comment) {
                            context = new Context(new CommentPlagiarism());
                            InputStream commentStream1 = new ByteArrayInputStream(baos1.toByteArray());
                            InputStream commentStream2 = new ByteArrayInputStream(baos2.toByteArray());
                            list3 = context.executeStrategy(commentStream1, commentStream2);
                            createMatches(list3, "Comments Match", listmatches);
                        }

                        percentage = calculateWeightedPercentage(Double.parseDouble(list1.get(2).get(0)),
                                Double.parseDouble(list1.get(2).get(0)),Double.parseDouble(list3.get(2).get(0)));
                    }

                    cj1 = new CreateJson(last1,last2,percentage,listmatches);
                    ljson.add(cj1);

                }

            }

            return ljson;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ljson;
        }

    }

    /**
     * Helper Method to Create Match Array in JSON
     * @param list1 An List of List containing line matches and percentage match
     * @param type The type of match
     * @param matches The List of matches passed as reference
     */
    public String createMatches(List<List<String>> list1,String type, List<Matches> matches){

        try {
            List<String> l1 = list1.get(0);
            List<Integer> intl1 = new ArrayList<>();

            for (String s : l1)
                intl1.add(Integer.valueOf(s));

            List<String> l2 = list1.get(1);
            List<Integer> intl2 = new ArrayList<>();

            for (String s : l2)
                intl2.add(Integer.valueOf(s));

            Matches match1 = new Matches(type, intl1, intl2);

            matches.add(match1);
            return "All went fine";
        }
        catch (ArrayIndexOutOfBoundsException exception){

            logger.info ("Error:", exception);
            throw exception;

        }

    }

    /**
     * calculateWeightedPercentage calcultes the weighted percentage
     * @param value1 the percentage from first comparison strategy
     * @param value2 the percentage from second comparison strategy
     * @return double, returns the weighted percentage
     */
    public double calculateWeightedPercentage(double value1,double value2, double value3){


        return (0.33 * value1) +(0.33*value2 ) +(0.33 * value3);

    }
}