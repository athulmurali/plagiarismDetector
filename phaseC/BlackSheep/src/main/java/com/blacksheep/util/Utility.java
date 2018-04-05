package com.blacksheep.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    
    private Utility() {
    	
    }
}
