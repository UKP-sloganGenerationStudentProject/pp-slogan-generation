package de.koch.uim_project.generation;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.database.UbyConnect;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;
import de.koch.uim_project.util.Config;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class TestBaseWordListGenerator
{

    @Test
    public void testWordListGenerator()
        throws DbException
    {
        final DemonstratorConfig demoConfig = DemonstratorConfig.getInstance();
        final Config config = Config.getDefaultConfig();

        final BaseWordListGen wordGen = new BaseWordListGen(config.getWordListGenConfig(),
                UbyConnect.getUbyInstance(demoConfig.createUbyDbConfig()), new JdbcConnect(
                        demoConfig.createKochsCustomDbConfig()));

        int synsetDepth = 0;
        this.testWordList(wordGen.getInitialSet());
        this.testFeature(wordGen.getInitialSet());
        try {
            while (synsetDepth < config.getMaxSynsetDepth()) {
                synsetDepth++;
                this.testWordList(wordGen.getSynsetDepthWords(synsetDepth));

            }
        }
        catch (final NoMorGenerationPossibleException e) {
            assertTrue(wordGen.getWordCount() > config.getMaxWordListLength());
        }
    }

    private void testFeature(final Set<Word> initialSet)
    {
        for (final Word word : initialSet) {
            assertTrue(word.isFeature());
        }

    }

    private void testWordList(final Set<Word> wordList)
    {
        for (final Word word : wordList) {
            assertTrue(word != null);
            assertTrue(word.isEmotionChecked());
            assertTrue(word.getEmotions() != null);
            assertTrue(word.getLemma() != null);
            assertTrue(word.getPos() != null);
            assertTrue(word.getPos() == EPartOfSpeech.verb || word.getPos() == EPartOfSpeech.noun
                    || word.getPos() == EPartOfSpeech.adjective);

        }
    }

}
