package de.koch.uim_project.database;

import static org.junit.Assert.assertTrue;
import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class TestJdbcConnect {

	private static Logger log = Logger.getRootLogger();

	@Test
	public void testGetSlogans() {
		ArrayList<String> slogans = null;
		try {
			DbConfig config = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
			JdbcConnect jdbcCon = new JdbcConnect(config);
			slogans = jdbcCon.getSlogans();

		} catch (Exception e) {
			assert (false);
			log.error("Exception during JdbcConnect test.", e);
		}

		assertTrue(slogans != null && slogans.size() > 0);

	}

	@Test
	public void tetsGetEmotionsBatch() throws DbException {
		Set<Word> wordList = new HashSet<Word>();
		DbConfig config = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
		JdbcConnect jdbcCon = new JdbcConnect(config);
		Word agile = new Word("agile", EPartOfSpeech.adjective);
		Word abolish = new Word("abolish", EPartOfSpeech.verb); 
		Word buck = new Word("shoot down",EPartOfSpeech.verb);
		wordList.add(agile);
		wordList.add(abolish);
		wordList.add(buck);
		try {
			Set<Word> result = jdbcCon.getEmotionBatch(wordList);
			for(Word word : result){
				switch(word.getLemma()){
				case  "abolish" :
					assertTrue(word.getEmotions().contains(Emotion.ANGER));
					assertTrue(word.getEmotions().contains(Emotion.NEGATIVE));
					break;
				
				case "agile" :
					assertTrue(word.getEmotions().contains(Emotion.POSITIVE));
					break;
				case "shoot down" :
					assertTrue(word.getEmotions().size() == 0);
					break;								
				}
			}
		} catch (DbException e) {
			assertTrue(false);
			log.error("Exception during batch emotion test", e);
		}

	}
}
