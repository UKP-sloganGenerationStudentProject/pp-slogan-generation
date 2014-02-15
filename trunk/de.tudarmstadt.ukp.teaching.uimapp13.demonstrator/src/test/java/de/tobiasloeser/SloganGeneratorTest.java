package de.tobiasloeser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.tobiasloeser.slogangenerator.Slogan;
import de.tobiasloeser.slogangenerator.SloganGenerator;
import de.tobiasloeser.slogangenerator.TemplateGenerator;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class SloganGeneratorTest
{

    private String[] config;
    private SloganGenerator sg;

    @Before
    public void setUp()
    {
        this.config = new String[8];
        this.config[0] = "localhost/uby_medium_0_3_0";
        this.config[1] = "root"; // user
        this.config[2] = ""; // password
        this.config[3] = ""; // emotionsPath
        this.config[4] = ""; // web1Tpath

        if (DemonstratorConfig.canGenerateInstance()) {
            final DemonstratorConfig demoConfig = DemonstratorConfig.getInstance();
            this.config[0] = demoConfig.getUbyUrl();
            this.config[1] = demoConfig.getUbyUser();
            this.config[2] = demoConfig.getUbyPassword();
            this.config[3] = demoConfig.getEmotionPath();
            this.config[4] = demoConfig.getWeb1TPath();
        }

        this.sg = new SloganGenerator(this.config[0], this.config[1], this.config[2],
                this.config[3], this.config[4]);
    }

	/**
	 * tests the initialization of the slogan generator
	 */
    @Test
    public void testSloganGenerator()
    {

        assertEquals("Database has not been saved", this.config[0], this.sg.database);
        assertEquals("DB User has not been saved", this.config[1], this.sg.dbuser);
        assertEquals("DB Password has not been saved", this.config[2], this.sg.dbpassword);
        assertEquals("EmotionPath has not been saved", this.config[3], this.sg.emotionPath);
        assertEquals("Web1TPath has not been saved", this.config[4], this.sg.web1TPath);
    }

	/**
	 * tests the method, which searches synsets for given words
	 */
    @Test
    public void testGetSynsetByWord()
    {
        this.config[5] = "test";
        this.config[6] = "bird";
        this.config[7] = null;

        List<String> synsets;

        try {
            synsets = this.sg.getSynsetByWord(this.config[5]);
            assertNotNull("No Synsets found", synsets);
            assertTrue("Synset List is empty", synsets.size() > 0);
        }
        catch (final UbyInvalidArgumentException e) {
            fail(e.toString());
        }

        try {
            synsets = this.sg.getSynsetByWord(this.config[6]);
            assertNotNull("No Synsets found", synsets);
            assertTrue("Synset List is empty", synsets.size() > 0);
        }
        catch (final UbyInvalidArgumentException e) {
            fail(e.toString());
        }

        try {
            synsets = this.sg.getSynsetByWord(this.config[7]);
            assertNull("Synsets found, but that should not be", synsets);
        }
        catch (final UbyInvalidArgumentException e) {
            fail(e.toString());
        }
    }

	/**
	 * tests the slogan generation with the constructor, which takes just the needed settings.
	 */
    @Test
    public void testGenerateSlogansSloganTemplateInt()
    {
        final List<Slogan> slogans = this.sg.generateSlogans(TemplateGenerator.getTemplateById(1),
                5);
        assertNotNull("No slogans were generated", slogans);
        assertEquals("Not the expected count of generated slogans", slogans.size(), 5);
        for (final Slogan slogan : slogans) {
            assertTrue("The slogan has no part", slogan.getSloganParts().size() > 0);
            assertNotNull("The slogan is null", slogan.toString());
            assertTrue("The slogan is empty", slogan.toString().length() > 0);
        }
    }

	/**
	 * tests slogan generation with the constructor, which takes manual settings.
	 */
    @Test
    public void testGenerateSlogansSloganTemplateIntStringBooleanString()
    {
        final List<Slogan> slogans = this.sg.generateSlogans(TemplateGenerator.getTemplateById(2),
                5, "Toyota", true, "positive", false);
        assertNotNull("No slogans were generated", slogans);
        assertEquals("Not the expected count of generated slogans", slogans.size(), 5);
        for (final Slogan slogan : slogans) {
            assertNotNull("Slogan is null", slogan);
            assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
            assertNotNull("Slogan as String is null", slogan.toString());
            assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
        }
    }

	/**
	 * tests a simple slogan generation
	 */
    @Test
    public void testGenerateSlogan()
    {
        final Slogan slogan = this.sg.generateSlogan(TemplateGenerator.getTemplateById(3));
        assertNotNull("Slogan is null", slogan);
        assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
        assertNotNull("Slogan as String is null", slogan.toString());
        assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
    }

	/**
	 * tests all templates
	 */
    @Test
    public void testAllTemplates()
    {
        for (int i = 1; i < 10; i++) {
            final Slogan slogan = this.sg.generateSlogan(TemplateGenerator.getTemplateById(i));
            assertNotNull("Slogan is null", slogan);
            assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
            assertNotNull("Slogan as String is null", slogan.toString());
            assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
        }
    }
    
	/**
	 * This is a test for the use of the new constructor and generateSlogans method which takes the SGConfig object
	 */
	@Test
	public void testSGConfig()
	{
        SGConfig sgc = new SGConfig();
        sgc.DBUrl = "localhost/uby_medium_0_3_0";
        sgc.DBUser = "root";
        sgc.DBPassword = "";
        sgc.TemplateId = 9;
        sgc.SloganCount = 10;
        sgc.Web1TPath = "";
        sgc.EmotionPath = "";
        SloganGenerator sg = new SloganGenerator(sgc);
        assertNotNull("SloganGenerator Instance is null", sg);
        assertNotNull("SloganGenerator Instance is null", sg);
		assertEquals("Database has not been saved", sgc.DBUrl, sg.database);
		assertEquals("DB User has not been saved", sgc.DBUser, sg.dbuser);
		assertEquals("DB Password has not been saved", sgc.DBPassword, sg.dbpassword);
		assertEquals("EmotionPath has not been saved", sgc.EmotionPath, sg.emotionPath);
		assertEquals("Web1TPath has not been saved", sgc.Web1TPath, sg.web1TPath);
		List<Slogan> slogans = sg.generateSlogans(sgc);
		for(Slogan slogan : slogans)
		{
			assertNotNull("Slogan is null", slogan);
			assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
			assertNotNull("Slogan as String is null", slogan.toString());
			assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
		}
	}

}
