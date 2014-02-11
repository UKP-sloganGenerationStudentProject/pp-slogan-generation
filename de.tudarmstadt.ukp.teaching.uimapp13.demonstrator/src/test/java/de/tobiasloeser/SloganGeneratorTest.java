package de.tobiasloeser.slogangenerator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class SloganGeneratorTest {
	
	
	@Test
	public void testSloganGenerator() {

		String[] config = new String[5];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		assertEquals("Database has not been saved", config[0], sg.database);
		assertEquals("DB User has not been saved", config[1], sg.dbuser);
		assertEquals("DB Password has not been saved", config[2], sg.dbpassword);
		assertEquals("EmotionPath has not been saved", config[3], sg.emotionPath);
		assertEquals("Web1TPath has not been saved", config[4], sg.web1TPath);
	}

	
	@Test
	public void testGetSynsetByWord() {
		String[] config = new String[8];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		config[5] = "test";
		config[6] = "bird";
		config[7] = null;
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		List<String> synsets;
		
		try {
			synsets = sg.getSynsetByWord(config[5]);
			assertNotNull("No Synsets found", synsets);
			assertTrue("Synset List is empty", synsets.size() > 0);
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			fail(e.toString());
		}
		
		try {
			synsets = sg.getSynsetByWord(config[6]);
			assertNotNull("No Synsets found", synsets);
			assertTrue("Synset List is empty", synsets.size() > 0);
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			fail(e.toString());
		}
		

		try {
			synsets = sg.getSynsetByWord(config[7]);
			assertNull("Synsets found, but that should not be", synsets);
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			fail(e.toString());
		}
	}
	

	@Test
	public void testGenerateSlogansSloganTemplateInt() {
		String[] config = new String[5];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		List<Slogan> slogans = sg.generateSlogans(TemplateGenerator.getTemplateById(1), 5);
		assertNotNull("No slogans were generated", slogans);
		assertEquals("Not the expected count of generated slogans", slogans.size(), 5);
		for(Slogan slogan : slogans)
		{
			assertTrue("The slogan has no part", slogan.getSloganParts().size() > 0);
			assertNotNull("The slogan is null", slogan.toString());
			assertTrue("The slogan is empty", slogan.toString().length() > 0);
		}
	}
	

	@Test
	public void testGenerateSlogansSloganTemplateIntStringBooleanString() {
		String[] config = new String[5];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		List<Slogan> slogans = sg.generateSlogans(TemplateGenerator.getTemplateById(2), 5, "Toyota", true, "positive");
		assertNotNull("No slogans were generated", slogans);
		assertEquals("Not the expected count of generated slogans", slogans.size(), 5);
		for(Slogan slogan : slogans)
		{
			assertNotNull("Slogan is null", slogan);
			assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
			assertNotNull("Slogan as String is null", slogan.toString());
			assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
		}
	}

	@Test
	public void testGenerateSlogan() {
		String[] config = new String[5];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		Slogan slogan = sg.generateSlogan(TemplateGenerator.getTemplateById(3));
		assertNotNull("Slogan is null", slogan);
		assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
		assertNotNull("Slogan as String is null", slogan.toString());
		assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
	}
	

	@Test
	public void testAllTemplates()
	{
		String[] config = new String[5];
		config[0] = "localhost/uby_medium_0_3_0";
		config[1] = "root";
		config[2] = "";
		config[3] = "";
		config[4] = "";
		
		SloganGenerator sg = new SloganGenerator(config[0], config[1], config[2], config[3], config[4]);
		assertNotNull("SloganGenerator Instance is null", sg);
		for(int i = 1; i < 10; i++)
		{
			Slogan slogan = sg.generateSlogan(TemplateGenerator.getTemplateById(i));
			assertNotNull("Slogan is null", slogan);
			assertTrue("Slogan has no parts", slogan.getSloganParts().size() > 0);
			assertNotNull("Slogan as String is null", slogan.toString());
			assertTrue("Slogan as String is empty", slogan.toString().length() > 0);
		}
	}

}
