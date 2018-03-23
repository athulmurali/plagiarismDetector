package com.blacksheep;

import com.blacksheep.util.Utility;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.CRC32;

/**
 * This class contains the implementation for MD5 algorithm to check if the two source files are exactly the same
 */
public class CRCPlagiarism {
    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(CommentPlagiarism.class);

    /***
     *
     * @param input1 : InputStream of source file 1
     * @param input2 : InputStream of source file 2
     * @return true if both the files have same CRC. False otherwise
     */
    public List<List<String>> getDetectResult(InputStream input1, InputStream input2) throws IOException {
        logger.debug("CRC plagiarism check started");

        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        try {
            ByteArrayOutputStream baos = Utility.backupInput(input1);
            InputStream crcStream = new ByteArrayInputStream(baos.toByteArray());
            InputStream crcStream1 = new ByteArrayInputStream(baos.toByteArray());

            CRC32 crcMaker1 = new CRC32();
            CRC32 crcMaker2 = new CRC32();

            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];

            int bytesRead1;
            int bytesRead2;

            while ((bytesRead1 = crcStream1.read(buffer1)) != -1) {
                crcMaker1.update(buffer1, 0, bytesRead1);
            }

            long crc1 = crcMaker1.getValue(); // This is your error checking code

            while ((bytesRead2 = input2.read(buffer2)) != -1) {
                crcMaker2.update(buffer2, 0, bytesRead2);
            }

            long crc2 = crcMaker2.getValue();

            if(crc1 == crc2){

                String s1 = new Scanner(crcStream).useDelimiter("\\A").next();
                String[] codeLines1 = s1.split("\\r?\\n");

                for(int i = 0; i < codeLines1.length; i++){
                    result.get(0).add(i+1 + "");
                    result.get(1).add(i+1 + "");
                }
                result.get(2).add(100.00 + "");
                logger.debug("CRC plagiarism check ended");
                return result;
            }
            else{
                logger.debug("CRC plagiarism check ended");
                return result;
            }

        } catch (IOException e) {
            logger.error("CRC check error", e);
            return result;
        }
        finally {
            input1.close();
            input2.close();
        }
    }
}

