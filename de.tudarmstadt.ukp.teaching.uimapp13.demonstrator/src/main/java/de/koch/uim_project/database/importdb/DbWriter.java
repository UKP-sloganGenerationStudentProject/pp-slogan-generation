package de.koch.uim_project.database.importdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;

/**
 * This class imports data into the DB. Please see {@link DbWriter#main(String[])} and edit constants accordingly
 * 
 * emotion import needs: EmotionLexicon as pdf. Its a specialized parser for the emotion Lexicon:
 * "NRC Emotion Lexicon,Saif M. Mohammad and Peter D. Turney,National Research Council Canada,Ottawa, Ontario, Canada, K1A 0R6,{saif.mohammad,peter.turney}@nrc-cnrc.gc.ca"
 *
 * function word import needs a txt with function words
 * 
 * stop word import needs a txt with stop words
 * 
 * game slogan import needs a csv with game slogans and the following fields: slogan(string),game name(String), year(int),source(string)
 * 
 * @author Frerik Koch
 * 
 */

public class DbWriter {

	private static Logger log = Logger.getRootLogger();
	private static DbWriter instance = null;
	private JdbcConnect jdbcCon;

	private DbWriter() throws DbException {
		jdbcCon = new JdbcConnect(new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER,
				Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS));
	}

	public static DbWriter getInstance() throws DbException {
		if (instance == null) {
			instance = new DbWriter();
		}
		return instance;
	}

	/**
	 * Inserts function words into DB
	 * @param file
	 * @throws DbException
	 */
	public void readFunctionWordsIntoTable(File file) throws DbException {
		fileToDbIgnoreDuplicates(file, "functionword", "word");

	}

	/**
	 * Inserts stop words into DB
	 * @param file
	 * @throws DbException
	 */
	public void readStopWordsIntoTable(File file) throws DbException {
		fileToDbIgnoreDuplicates(file, "stopword", "word");
	}

	/**
	 * Inserts a txt file in a DB table. Eg. stop words
	 * @param file
	 * @param insertTable
	 * @param insertField
	 * @throws DbException
	 */
	private void fileToDbIgnoreDuplicates(File file, String insertTable, String insertField) throws DbException {
		Connection con = jdbcCon.getConnection();
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(file));
			PreparedStatement pstmt = con.prepareStatement("INSERT IGNORE INTO " + insertTable + "(" + insertField + ") VALUES(?);");

			String line = br.readLine();
			while (line != null && line.trim() != "") {
				line = line.trim().toLowerCase();
				pstmt.setString(1, line);
				pstmt.executeUpdate();
				line = br.readLine();
			}

		} catch (SQLException | IOException e) {
			log.error("Failed to insert function word", e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("ROLLBACK FAILED!", e);
			}
			throw new DbException("Failed to insert function word", e);
		}
	}

	/**
	 * Inserts emotion words from given emotion lexicon as pdf into DB
	 * @param filePathToEmotionLexicon
	 */
	public void readEmotionWordsIntoTable(String filePathToEmotionLexicon) {
		try {
			File input = new File(filePathToEmotionLexicon);
			PDDocument pd = PDDocument.load(input);
			PDFTextStripper stripper = new PDFTextStripper();

			stripper.setStartPage(6);
			String extractedText = stripper.getText(pd);
			String[] rawText = extractedText.split("\n");
			ArrayList<Emotion> data = new ArrayList<Emotion>(rawText.length);

			rawText[0] = "remove";
			rawText[1] = "remove";
			for (String str : rawText) {
				String tmpStr = str.trim();
				if (tmpStr.length() < 10) {
					// drop strings < 10 (page numbers etc.)
					continue;
				} else {
					String[] tmpData = tmpStr.split("\\s");
					Emotion tmpEm = new Emotion(tmpData[0].trim().toLowerCase(), tmpData[1].trim().endsWith("1"), tmpData[2].trim().endsWith("1"),
							tmpData[3].trim().endsWith("1"), tmpData[4].trim().endsWith("1"), tmpData[5].trim().endsWith("1"), tmpData[6].trim()
									.endsWith("1"), tmpData[7].trim().endsWith("1"), tmpData[8].trim().endsWith("1"),
							tmpData[9].trim().endsWith("1"), tmpData[10].trim().endsWith("1"));

					data.add(tmpEm);

				}

			}

			for (Emotion emotion : data) {
				int wordId = insertEmotionWord(emotion.getWord());
				if (emotion.isPositive()) {
					insertEmotionWordRelation(wordId, 1);
				}
				if (emotion.isNegative()) {
					insertEmotionWordRelation(wordId, 2);
				}
				if (emotion.isAnger()) {
					insertEmotionWordRelation(wordId, 3);
				}
				if (emotion.isAnticipation()) {
					insertEmotionWordRelation(wordId, 4);
				}
				if (emotion.isDisgust()) {
					insertEmotionWordRelation(wordId, 5);
				}
				if (emotion.isFear()) {
					insertEmotionWordRelation(wordId, 6);
				}
				if (emotion.isJoy()) {
					insertEmotionWordRelation(wordId, 7);
				}
				if (emotion.isSadness()) {
					insertEmotionWordRelation(wordId, 8);
				}
				if (emotion.isSurprise()) {
					insertEmotionWordRelation(wordId, 9);
				}
				if (emotion.isTrust()) {
					insertEmotionWordRelation(wordId, 10);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts an emotion word into DB
	 * @param wordToAdd
	 * @return
	 * @throws DbException
	 */
	private int insertEmotionWord(String wordToAdd) throws DbException {
		try {
			Connection con = jdbcCon.getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT IGNORE INTO emotionword(word) VALUES (?);");
			pstmt.setString(1, wordToAdd);
			pstmt.executeUpdate();
			PreparedStatement pstmt2 = con.prepareStatement("SELECT idemotionword FROM emotionword WHERE word=?;");
			pstmt2.setString(1, wordToAdd);
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException("", e);
		}

	}

	/**
	 * Reads slogans from csv with correct format (see class description)
	 * @param gameSloganCsvPath
	 */
	private void readSlogans(String gameSloganCsvPath) {
		Connection connection = null;
		try {
			connection = jdbcCon.getConnection();
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO `uim_projekt`.`slogans` (`slogan`, `year`, `game_title`, `source`) VALUES (?, ?, ?, ?);");

			BufferedReader br = new BufferedReader(new FileReader(new File(gameSloganCsvPath)));
			String line = br.readLine();
			while (line != null) {
				String[] tmp = line.split(";");
				if (tmp.length == 4) {
					pstmt.setString(1, tmp[0].toLowerCase());
					if (tmp[2].equals("")) {
						pstmt.setNull(2, java.sql.Types.INTEGER);
					} else {
						pstmt.setInt(2, Integer.parseInt(tmp[2]));
					}
					pstmt.setString(3, tmp[1].toLowerCase());
					pstmt.setString(4, tmp[3].toLowerCase());
					pstmt.executeUpdate();
				}
				line = br.readLine();
			}

			pstmt.close();
			connection.close();
		} catch (Exception e) {
			try {
				connection.rollback();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	}

	/**
	 * Writes an emotion word relation into m:n table in DB
	 * @param emotionwordId
	 * @param emotionId
	 * @throws DbException
	 */
	private void insertEmotionWordRelation(int emotionwordId, int emotionId) throws DbException {
		try {
			Connection con = jdbcCon.getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT IGNORE INTO emotiontoemotionword(idemotion,idemotionword) VALUES (?,?)");
			pstmt.setInt(1, emotionId);
			pstmt.setInt(2, emotionwordId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException();
		}
	}

	/**
	 * Imports data into database. Please in/out comment any import you need.
	 * Set the constants correctly to point to the needed files.
	 * @param args
	 * @throws DbException
	 */
	public static void main(String[] args) throws DbException {
		final String emotionLexicionPath = "src/main/resources/sources/NRCemotionlexicon.pdf";
		final String functionWordsTxtPath = "src/main/resources/sources/functionWords.txt";
		final String stopWordsTxtPath = "src/main/resources/sources/stopwordlist.txt";
		final String gameSloganCsvPath = "files/GameSlogansS2Final.csv";
		DbWriter dbw = DbWriter.getInstance();
		dbw.readFunctionWordsIntoTable(new File(functionWordsTxtPath));
		dbw.readStopWordsIntoTable(new File(stopWordsTxtPath));
		dbw.readEmotionWordsIntoTable(emotionLexicionPath);
		dbw.readSlogans(gameSloganCsvPath);
	}
}
