package com.blacksheep;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This class contains the implementation of Plagiarism check for comments in two source files
 */
public class CommentPlagiarism {
    /**
     * The threshold to acceptable plagiarism
     */
    private double threshold = 80;

    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(CommentPlagiarism.class);

    /**
     * List to store comments from the first source file
     */
    private List<String> comments1;

    /**
     * List to store comments from the second source file
     */
    private List<String> comments2;

    /**
     * List to store line numbers of comments from the first source file
     */
    private List<String> lineNumbers1;

    /**
     * List to store line numbers of comments from the second source file
     */
    private List<String> lineNumbers2;

    /**
     * Initializes the arrays that store the comments and the lines numbers of the source files
     */
    public CommentPlagiarism() {
        comments1 = new ArrayList<>();
        comments2 = new ArrayList<>();

        lineNumbers1 = new ArrayList<>();
        lineNumbers2 = new ArrayList<>();
    }

    /***
     * This method gets the comments from the 2 source files and returns the line numbers that match and the match
     * percentage
     * @param input1: Inputstream of the first source file
     * @param input2:  Inputstream of the second source file
     * @return The lines numbers that match in the two source files and match percent
     */
    public List<List<String>> getDetectResult(InputStream input1, InputStream input2) throws IOException {
        logger.debug("Comment plagiarism check started");

        List<String> visitedFile1 = new ArrayList<>();
        List<String> visitedFile2 = new ArrayList<>();

        double percentage = 0;
        List<Double> percentages = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());

        try {

            processInputStream(input1, input2);

            int comments1Size = comments1.size();
            int comments2Size = comments2.size();

            for (int i = 0; i < comments1Size; i++) {
                if(visitedFile1.contains(lineNumbers1.get(i))){
                    continue;
                }
                String comment1 = comments1.get(i);
                double comment1Length = comment1.length();

                for (int j = 0; j < comments2Size; j++) {

                    if(visitedFile2.contains(lineNumbers1.get(j))
                            || visitedFile1.contains(lineNumbers1.get(i))){
                        continue;
                    }

                    String comment2 = comments2.get(j);

                    double total = comment1Length + comment2.length();

                    CalculateEditDistance dist = new CalculateEditDistance();

                    double edit = dist.calculate(comment1, comment2);
                    double percent = ((total - edit) / total) * 100;

                    if (percent > threshold) {
                        percentages.add(percent);
                        result.get(0).add(lineNumbers1.get(i));
                        result.get(1).add(lineNumbers1.get(j));
                        visitedFile1.add(lineNumbers1.get(i));
                        visitedFile2.add(lineNumbers1.get(j));
                    }
                }
            }

            OptionalDouble average = percentages
                    .stream()
                    .mapToDouble(a -> a)
                    .average();

            percentage = average.isPresent() ? average.getAsDouble() : 0;
            result.get(2).add(percentage + "");
            logger.debug("Comment plagiarism check ended");
            return result;
        } catch (Exception e) {
            logger.error("Comment Plagiarism error", e);
            return result;
        }
        finally {
            input1.close();
            input2.close();
        }
    }

    /***
     * This method extracts the comments from the source file and stores them in a list
     * @param lines : Array containing all the lines of a source file
     * @param size : Size of the array
     * @param index : Line number of the current line of the source file
     * @param code : List containing all the comments
     * @param lineNum : List containing line numbers of all the comments
     * @param multipleCommentFound : Flag to check if multiline comment was found
     *
     * @return Returns whether multiline comment was found or not
     */
    private boolean getCommentsFromSource(String[] lines, int size, int index, List<String> code, List<String> lineNum,
                                       boolean multipleCommentFound) {
        if (size <= lines.length) {
            String codeLine = lines[index];

            if(multipleCommentFound){
                if(codeLine.contains("\"\"\"")){
                    multipleCommentFound = false;
                }
                else{
                    if(codeLine.length() > 0){
                        code.add(codeLine.trim());
                        lineNum.add(String.valueOf(index + 1));
                    }
                }
            }
            else{
                if (codeLine.contains("#")) {
                    code.add(codeLine.substring(codeLine.indexOf('#') + 1).trim());
                    lineNum.add(String.valueOf(index + 1));
                }
                else if(codeLine.contains("\"\"\"")){
                    multipleCommentFound = true;
                }
            }
        }
        return multipleCommentFound;
    }

    /***
     * This method takes the input streams from 2 files and converts them to strings which is then converted to array
     * of strings
     * @param input1 : InputStream of the first source file
     * @param input2 : InputStream of the second source file
     */
    private void processInputStream(InputStream input1, InputStream input2) {
        String s1 = new Scanner(input1).useDelimiter("\\A").next();
        String s2 = new Scanner(input2).useDelimiter("\\A").next();

        String[] codeLines1 = s1.split("\\r?\\n");
        String[] codeLines2 = s2.split("\\r?\\n");

        int size = codeLines1.length > codeLines2.length ? codeLines1.length : codeLines2.length;

        boolean multiLineFound1 = false;
        boolean multiLineFound2 = false;

        for (int i = 0; i < size; i++) {
            multiLineFound1 = getCommentsFromSource(codeLines1, size, i, comments1, lineNumbers1, multiLineFound1);
            multiLineFound2 = getCommentsFromSource(codeLines2, size, i, comments2, lineNumbers2, multiLineFound2);
        }
    }
}
