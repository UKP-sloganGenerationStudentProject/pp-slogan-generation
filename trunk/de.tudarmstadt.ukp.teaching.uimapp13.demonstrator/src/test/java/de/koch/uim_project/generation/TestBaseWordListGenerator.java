package de.koch.uim_project.generation;

import static org.junit.Assert.*;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;

import java.util.Set;

import org.junit.Test;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.database.UbyConnect;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.koch.uim_project.util.Config;

public class TestBaseWordListGenerator {

	@Test
	public void testWordListGenerator() throws DbException {
		Config config = Config.getDefaultConfig();

		BaseWordListGen wordGen = new BaseWordListGen(config.getWordListGenConfig(), UbyConnect.getUbyInstance(config.getUbyConfig()),
				new JdbcConnect(config.getCustomDbConfig()));

		int synsetDepth = 0;
		testWordList(wordGen.getInitialSet());
		testFeature(wordGen.getInitialSet());
		try {
			while (synsetDepth < config.getMaxSynsetDepth()) {
				synsetDepth++;
				testWordList(wordGen.getMore(synsetDepth));

			}
		} catch (NoMorGenerationPossibleException e) {
			assertTrue(wordGen.getWordCount() > config.getMaxWordListLength());
		}
	}

	private void testFeature(Set<Word> initialSet) {
		for (Word word : initialSet) {
			assertTrue(word.isFeature());
		}

	}

	private void testWordList(Set<Word> wordList) {
		for (Word word : wordList) {
			assertTrue(word != null);
			assertTrue(word.isEmotionChecked());
			assertTrue(word.getEmotions() != null);
			assertTrue(word.getLemma() != null);
			assertTrue(word.getPos() != null);
			assertTrue(word.getPos() == EPartOfSpeech.verb || word.getPos() == EPartOfSpeech.noun || word.getPos() == EPartOfSpeech.adjective);

		}
	}

}
