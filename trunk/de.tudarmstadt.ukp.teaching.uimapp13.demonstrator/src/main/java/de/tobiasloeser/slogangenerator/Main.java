package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Main {

	/**
	 * Requirements
	 *  
	 * 	4 Templates:	- NC PC NC
	 * 					- NC +			CHECK
	 * 					- VC NC			CHECK
	 * 					- NC VC NC		CHECK
	 * 
	 * 	Optional product name 			CHECK
	 * 
	 * 	Creative use of product name 	CHECK
	 * 
	 * 	Possibility to choose emotion	CHECK
	 * 
	 * 	More verbs with semantic label
	 * 
	 *  Oxymoron/Antonym with synsetRelation	CHECK (next word is antonym)
	 *  
	 *  More Synsets through words from User	CHECK
	 *  
	 *  More Synsets from analyse
	 *  
	 *  Config-Objekt + Pfad zu Web1T übergeben lassen
	 *      
	 * @param args
	 * @throws UbyInvalidArgumentException 
	 */
	
	private static SGConfig config;
	private static SloganGenerator generator;
	
	public static void main(SGConfig sgc) throws UbyInvalidArgumentException {
		config = sgc;
		List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
		templates.add(TemplateGenerator.getTemplateById(sgc.TemplateId));
		generator = new SloganGenerator(sgc.DBUrl, sgc.DBUser, sgc.DBPassword, sgc.EmotionPath);
		GoodLuck goodLuck = new GoodLuck(sgc.DBUrl, sgc.DBUser, sgc.DBPassword);
		for(SloganTemplate t : templates)
		{		
			for(String word : config.WordList)
			{
				for(String synset : generator.getSynsetByWord(word))
					t.addSynset(synset);
			}
		}
		goodLuck.AddMoreVerbs(templates);
		generateSlogans(templates);
		
		//displaySloganTemplates(templates);
	}
	
	public static void displaySloganTemplates(List<SloganTemplate> templates)
	{
		for(SloganTemplate template : templates)
		{
			System.out.println(template.toString());
		}
	}
	
	public static void generateSlogans(List<SloganTemplate> templates)
	{
		List<Slogan> slogans = new ArrayList<Slogan>();
		for(SloganTemplate template : templates)
		{
			slogans.addAll(generator.generateSlogans(template, config.SloganCount, config.ProductName, config.UseProductNameCreative, config.Emotion));
		}
		for(Slogan slogan : slogans)
		{
			System.out.println(slogan.toString());
		}
	}

}
