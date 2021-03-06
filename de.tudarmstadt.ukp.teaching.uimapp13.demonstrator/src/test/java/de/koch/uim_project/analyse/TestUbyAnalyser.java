package de.koch.uim_project.analyse;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.UbyConnect;
import de.koch.uim_project.generation.Word;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class TestUbyAnalyser
{

    private DbConfig defaultConfig = new DbConfig(Constants.DATABASE.UBY.DEFAULT_UBY_URL,
            Constants.DATABASE.UBY.DEFAULT_UBY_LOGIN, Constants.DATABASE.UBY.DEFAULT_UBY_PASS);

    @Before
    public void setUp()
    {
        this.defaultConfig = DemonstratorConfig.getInstance().createUbyDbConfig();
    }

    @Test
    public void testGetRelatedWo2rdsSense()
        throws DbException
    {
        final Word hot = new Word("hot", EPartOfSpeech.adjective);

        final List<String> relNames = new ArrayList<String>();
        final List<ERelTypeSemantics> relTypes = new ArrayList<ERelTypeSemantics>();
        relNames.add("antonym");
        relTypes.add(ERelTypeSemantics.complementary);
        final Set<Word> oxymorons = UbyAnalyser.getInstance().getRelatedWordsSense(hot, relNames,
                relTypes, UbyConnect.getUbyInstance(this.defaultConfig));
        assertTrue(oxymorons.size() > 0);
    }

}
