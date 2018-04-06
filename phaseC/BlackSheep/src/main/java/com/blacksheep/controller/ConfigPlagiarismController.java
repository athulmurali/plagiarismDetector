/**
 * This class contains the implementation for Plagiarism Configuration
 */
package com.blacksheep.controller;

import com.blacksheep.DBConfigUtil;
import com.blacksheep.IDBConfigUtil;
import com.blacksheep.Types;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class ConfigPlagiarismController {

	private final Logger logger = Logger.getLogger(ConfigPlagiarismController.class);

	/**
	 *
	 * @param config
	 *            contains the percentage to be set for the user account user and
	 *            percentage must be present in the json data of request
	 * @return a response entity object representing success or failure
	 * @throws SQLException
	 */
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public ResponseEntity<Object> configPercentageController(
			@RequestBody Types config) {
		try {
			logger.info("Config update start");

			updateConfig(config);

			logger.info("Config update end");
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (SQLException | IOException e) {
			logger.error("", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 *
	 * @param config
	 *            : Configuration values selected by the user
	 * @throws MessagingException
	 */
	private void updateConfig(Types config) throws SQLException, IOException {
		IDBConfigUtil dbConfigUtil = new DBConfigUtil();

		String updateTableSQL = "UPDATE configPlagiarism SET COMMENT = ?, STRUCTURE = ?, CODEMOVEMENT = ?, PERCENT = ?";

		try (Connection connection = DriverManager.getConnection(dbConfigUtil.getDbURL(),
				dbConfigUtil.getDbUser(), dbConfigUtil.getDbPass())) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement(updateTableSQL)) {
				preparedStatement.setString(1, config.getC3());
				preparedStatement.setString(2, config.getC1());
				preparedStatement.setString(3, config.getC2());
				preparedStatement.setInt(4, config.getPercentage());
				preparedStatement.executeUpdate();
			}
		}
	}
}
