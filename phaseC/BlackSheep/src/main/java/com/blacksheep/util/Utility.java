package com.blacksheep.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import com.blacksheep.models.FileStreams;
import com.blacksheep.models.SourceCodeList;

/***
 * This class contains utility methods to be used in the application
 */
public class Utility {
    private static final String END = " end\n";
	private static final String START = " start\n";
	private static final String FILESEPARATOR = "\n # @@TOPAATMABI@@ ";


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
			logger.error(e.getMessage(),e);
			return "";
		}
    	finally {
    		if(scanner != null) {
    			scanner.close();
    		}
    	}
    }
    
    /**
	 * @param submissionstreams
	 * @throws IOException
	 */
	public static SourceCodeList getFilesForDetection(Map<String, List<FileStreams>> submissionstreams) {
		Iterator<Entry<String, List<FileStreams>>> it = submissionstreams.entrySet().iterator();
		SourceCodeList codeList = new SourceCodeList();
		List<String> sourceCodes = new ArrayList<>();
		List<String> folderNames = new ArrayList<>();
		while (it.hasNext()) {
			Map.Entry<String, List<FileStreams>> pair = it.next();
			String projectName = pair.getKey();
			List<FileStreams> streams = pair.getValue();
			StringBuilder fileText = new StringBuilder();

			for (FileStreams file : streams) {
				fileText.append(FILESEPARATOR);
				fileText.append(file.getFileName());
				fileText.append(START);

				fileText.append(Utility.streamToText(file.getStream()));

				fileText.append(FILESEPARATOR);
				fileText.append(file.getFileName());
				fileText.append(END);
			}

			folderNames.add(projectName);
			sourceCodes.add(fileText.toString());
		}
		codeList.setFolderNames(folderNames);
		codeList.setSourceCodes(sourceCodes);
		return codeList;
	}
	
	/**
	 *
	 * @param config
	 *            : Configuration values selected by the user
	 * @throws MessagingException
	 */
	public static boolean[] getConfigFlags() throws SQLException, IOException {
		IDBConfigUtil dbConfigUtil = new DBConfigUtil();
		
		boolean[] flags = new boolean[3]; 

		String updateTableSQL = "SELECT * FROM configPlagiarism";

		try (Connection connection = DriverManager.getConnection(dbConfigUtil.getDbURL(), dbConfigUtil.getDbUser(),
				dbConfigUtil.getDbPass())) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL)) {

				try (ResultSet results = preparedStatement.executeQuery()) {
					while (results.next()) {
						if (results.getString("comment") != null)
							flags[0] = true;
						else
							flags[0] = false;

						if (results.getString("codeMovement") != null)
							flags[1] = true;
						else
							flags[1] = false;

						if (results.getString("structure") != null)
							flags[2] = true;
						else
							flags[2] = false;
					}
				}
			}
		}
		return flags;
	}

    
    public Utility() {
    	
    }
}
