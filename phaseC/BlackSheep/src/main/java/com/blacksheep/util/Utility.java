package com.blacksheep.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.log4j.Logger;

/***
 * This class contains utility methods to be used in the application
 */
public class Utility {
    /***
     *
     * @param input : InputStream to be converted to ByteArrayOutputStream
     * @throws IOException
     */
    public static ByteArrayOutputStream backupInput(InputStream input) throws IOException{
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1 ) {
            byteArrayStream.write(buffer, 0, len);
        }
        byteArrayStream.flush();
        return byteArrayStream;
    }
    
    public static String streamToText(InputStream in) {
    	Scanner scanner = null;
    	try {
			scanner = new Scanner(in);
			return scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(Utility.class);
			logger.error("",e);
			return "";
		}
    	finally {
    		if(scanner != null) {
    			scanner.close();
    		}
    	}
    }
    
    public Utility() {
    	
    }
}
