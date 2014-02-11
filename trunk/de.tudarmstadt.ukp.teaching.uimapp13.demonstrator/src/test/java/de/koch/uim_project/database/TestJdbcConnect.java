package de.koch.uim_project.database;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import de.koch.uim_project.generation.Word;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class TestJdbcConnect
{

    private static Logger log = Logger.getRootLogger();
    private DbConfig customDbConfig = new DbConfig(
            Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL,
            Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER,
            Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);;

    @Before
    public void setUp()
    {
        this.customDbConfig = DemonstratorConfig.getInstance().createKochsCustomDbConfig();
    }

    @Test
    public void testGetSlogans()
    {
        ArrayList<String> slogans = null;
        try {
            final JdbcConnect jdbcCon = new JdbcConnect(this.customDbConfig);
            slogans = jdbcCon.getSlogans();

        }
        catch (final Exception e) {
            assert (false);
            log.error("Exception during JdbcConnect test.", e);
        }

        assertTrue(slogans != null && slogans.size() > 0);

    }

    @Test
    public void tetsGetEmotionsBatch()
        throws DbException
    {
        final Set<Word> wordList = new HashSet<Word>();

        final JdbcConnect jdbcCon = new JdbcConnect(this.customDbConfig);
        final Word agile = new Word("agile", EPartOfSpeech.adjective);
        final Word abolish = new Word("abolish", EPartOfSpeech.verb);
        final Word buck = new Word("shoot down", EPartOfSpeech.verb);
        wordList.add(agile);
        wordList.add(abolish);
        wordList.add(buck);
        try {
            final Set<Word> result = jdbcCon.getEmotionBatch(wordList);
            for (final Word word : result) {
                switch (word.getLemma()) {
                case "abolish":
                    assertTrue(word.getEmotions().contains(Emotion.ANGER));
                    assertTrue(word.getEmotions().contains(Emotion.NEGATIVE));
                    break;

                case "agile":
                    assertTrue(word.getEmotions().contains(Emotion.POSITIVE));
                    break;
                case "shoot down":
                    assertTrue(word.getEmotions().size() == 0);
                    break;
                }
            }
        }
        catch (final DbException e) {
            assertTrue(false);
            log.error("Exception during batch emotion test", e);
        }

    }
}
