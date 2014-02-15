package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates the templates for the slogans. 
 * 
 * @author tobias
 *
 */
public class TemplateGenerator {
	
	/**
	 * returns all possible templates
	 * @return list of slogan templates
	 */
	public static List<SloganTemplate> getAllTemplates()
	{
		List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
		templates.add(createTemplate_NC_WITH_ALLITERATION());
		templates.add(createTemplate_VC_NC_WITHOUT_ALLITERATION());
		templates.add(createTemplate_VC_NC_WITH_ALLITERATION());
		templates.add(createTemplate_NC_VC_NC_WITHOUT_ALLITERATION());
		templates.add(createTemplate_NC_WITHOUT_ALLITERATION());
		return templates;
	}
	
	/**
	 * returns the slogan template with the given id
	 * @param id id of the template
	 * @return slogan template with given id
	 */
	public static SloganTemplate getTemplateById(int id)
	{
		switch(id)
		{
			case 1: return createTemplate_NC_WITH_ALLITERATION();
			case 2: return createTemplate_NC_WITHOUT_ALLITERATION();
			case 3: return createTemplate_VC_NC_WITHOUT_ALLITERATION();
			case 4: return createTemplate_VC_NC_WITH_ALLITERATION();
			case 5: return createTemplate_NC_VC_NC_WITHOUT_ALLITERATION();
			case 6: return createTemplate_NC_VC_NC_WITH_ALLITERATION();
			case 7: return createTemplate_NC_WITH_OXYMORON();
			case 8: return createTemplate_NC_PC_NC_WITHOUT_ALLITERATION();
			case 9: return createTemplate_NC_PC_NC_WITH_ALLITERATION();
		}
		return null;
	}

	/**
	 * Creates the slogan template NC with Oxymoron
	 * @return slogan template with id 2
	 */
	private static SloganTemplate createTemplate_NC_WITH_OXYMORON() {
		SloganTemplate template = new SloganTemplate("2", "NC", false, true, true, ",");
		TemplatePart part = new TemplatePart(template, 0, "NC", false, 3);
		for(String synset : getNounSynsets())
		{
			part.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part.addSynset(synset);
		}
		template.addTemplatePart(part);
		return template;	
	}

	/**
	 * Creates the slogan template NC VC NC
	 * @return slogan template with id 5
	 */
	private static SloganTemplate createTemplate_NC_VC_NC_WITHOUT_ALLITERATION()
	{
		SloganTemplate template = new SloganTemplate("5", "VC NC", false, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part1.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part1.addSynset(synset);
		}
		template.addTemplatePart(part1);
		TemplatePart part2 = new TemplatePart(template, 1, "VC", false, 1);
		for(String synset : getVerbSynsets())
		{
			part2.addSynset(synset);
		}
		template.addTemplatePart(part2);
		TemplatePart part3 = new TemplatePart(template, 2, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part3.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part3.addSynset(synset);
		}
		template.addTemplatePart(part3);
		return template;
	}
	
	/**
	 * Creates the slogan template NC VC NC with alliteration
	 * @return slogan template with id 6
	 */
	private static SloganTemplate createTemplate_NC_VC_NC_WITH_ALLITERATION()
	{
		SloganTemplate template = new SloganTemplate("6", "VC NC", true, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part1.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part1.addSynset(synset);
		}
		template.addTemplatePart(part1);
		TemplatePart part2 = new TemplatePart(template, 1, "VC", false, 1);
		for(String synset : getVerbSynsets())
		{
			part2.addSynset(synset);
		}
		template.addTemplatePart(part2);
		TemplatePart part3 = new TemplatePart(template, 2, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part3.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part3.addSynset(synset);
		}
		template.addTemplatePart(part3);
		return template;
	}
	
	/**
	 * Creates the slogan template NC with alliteration
	 * @return slogan template with id 1
	 */
	private static SloganTemplate createTemplate_NC_WITH_ALLITERATION()
	{
		SloganTemplate template = new SloganTemplate("1", "NC", true, false, true, ",");
		TemplatePart part = new TemplatePart(template, 0, "NC", false, 3);
		for(String synset : getNounSynsets())
		{
			part.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part.addSynset(synset);
		}
		template.addTemplatePart(part);
		return template;
	}
	
	/**
	 * Creates the slogan template NC
	 * @return slogan template with id 2
	 */
	private static SloganTemplate createTemplate_NC_WITHOUT_ALLITERATION()
	{
		SloganTemplate template = new SloganTemplate("2", "NC", false, false, true, ",");
		TemplatePart part = new TemplatePart(template, 0, "NC", false, 3);
		for(String synset : getNounSynsets())
		{
			part.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part.addSynset(synset);
		}
		template.addTemplatePart(part);
		return template;		
	}
	
	/**
	 * Creates the slogan template NC VC
	 * @return slogan template with id 3
	 */
	private static SloganTemplate createTemplate_VC_NC_WITHOUT_ALLITERATION()
	{
		SloganTemplate template = new SloganTemplate("3", "VC NC", false, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "VC", false, 1);
		for(String synset : getVerbSynsets())
		{
			part1.addSynset(synset);
		}
		template.addTemplatePart(part1);
		TemplatePart part2 = new TemplatePart(template, 1, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part2.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part2.addSynset(synset);
		}
		template.addTemplatePart(part2);
		return template;
	}
	
	/**
	 * Creates the slogan template VC NC with alliteration
	 * @return slogan template with id 4
	 */
	private static SloganTemplate createTemplate_VC_NC_WITH_ALLITERATION() {
		SloganTemplate template = new SloganTemplate("4", "VC NC", true, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "VC", false, 1);
		for(String synset : getVerbSynsets())
		{
			part1.addSynset(synset);
		}
		template.addTemplatePart(part1);
		TemplatePart part2 = new TemplatePart(template, 1, "NC", false, 1);
		for(String synset : getNounSynsets())
		{
			part2.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part2.addSynset(synset);
		}
		template.addTemplatePart(part2);
		return template;
	}
	
	/**
	 * Creates the slogan template NC PC NC
	 * @return slogan template with id 7
	 */
	private static SloganTemplate createTemplate_NC_PC_NC_WITHOUT_ALLITERATION() {
		SloganTemplate template = new SloganTemplate("7", "NC PC NC", false, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "NC", false, 1);
		//TemplatePart part2 = new TemplatePart(template, 1, "PC", false, 1);
		TemplatePart part3 = new TemplatePart(template, 1, "PCNC", false, 1);
		for(String synset : getNounSynsets())
		{
			part1.addSynset(synset);
			part3.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part1.addSynset(synset);
			part3.addSynset(synset);
		}
		template.addTemplatePart(part1);
		//template.addTemplatePart(part2);
		template.addTemplatePart(part3);
		return template;	
	}
	
	/**
	 * Creates the slogan template NC PC NC with alliteration
	 * @return slogan template with id 8
	 */
	private static SloganTemplate createTemplate_NC_PC_NC_WITH_ALLITERATION() {
		SloganTemplate template = new SloganTemplate("8", "NC PC NC", true, false, false, "");
		TemplatePart part1 = new TemplatePart(template, 0, "NC", false, 1);
		//TemplatePart part2 = new TemplatePart(template, 1, "PC", false, 1);
		TemplatePart part3 = new TemplatePart(template, 1, "PCNC", false, 1);
		for(String synset : getNounSynsets())
		{
			part1.addSynset(synset);
			part3.addSynset(synset);
		}
		for(String synset : getAdjectiveSynsets())
		{
			part1.addSynset(synset);
			part3.addSynset(synset);
		}
		template.addTemplatePart(part1);
		//template.addTemplatePart(part2);
		template.addTemplatePart(part3);
		return template;	
	}
	
	/**
	 * generates a list of string with synsets of nouns
	 * @return list of string with synsets
	 */
	private static List<String> getNounSynsets()
	{
		List<String> synsets = new ArrayList<String>();
		synsets.add("WN_Synset_15951"); // car
		synsets.add("WN_Synset_781"); // road
		synsets.add("WN_Synset_5485"); // pleasure
		synsets.add("WN_Synset_22615"); // ride
		synsets.add("WN_Synset_92610"); // travel
		synsets.add("WN_Synset_87863"); // power
		synsets.add("WN_Synset_50822"); // world
		synsets.add("WN_Synset_32018"); // way
		synsets.add("WN_Synset_85537"); // design
		synsets.add("WN_Synset_77795"); // feel, look, feeling, flavor, tone, smell, spirit
		synsets.add("WN_Synset_40769"); // exhilaration, excitement
		synsets.add("WN_Synset_1218"); // perfection
		synsets.add("WN_Synset_39561"); // movement, motion
		synsets.add("WN_Synset_74760"); // aliveness, living, life, animation
		synsets.add("WN_Synset_27341"); // manner, mode, style, fashion, way
		synsets.add("WN_Synset_31662"); // dream, dreaming
		synsets.add("WN_Synset_81926"); // beginning, showtime, start, outset, get-go, kickoff, commencement, offset, first, starting time
		synsets.add("WN_Synset_27728"); // build
		synsets.add("WN_Synset_26090"); // shape, form
		synsets.add("WN_Synset_31309"); // confidence, self-confidence, sureness, self-assurance
		synsets.add("WN_Synset_55"); // accomplishment, achievement
		synsets.add("WN_Synset_74600"); // confidence, trust
		synsets.add("WN_Synset_74133"); // first, number one
		synsets.add("WN_Synset_43514"); // people
		synsets.add("WN_Synset_31614"); // experience
		return synsets;
	}
	
	/**
	 * generates a list of string with synsets of adjectives
	 * @return list of string with synsets
	 */
	private static List<String> getAdjectiveSynsets()
	{
		List<String> synsets = new ArrayList<String>();
		synsets.add("WN_Synset_102939"); // future, next, succeeding
		synsets.add("WN_Synset_100395"); // new, modern
		synsets.add("WN_Synset_101016"); // raw, new
		synsets.add("WN_Synset_105120"); // fresh, novel, new
		//synsets.add("WN_Synset_96816"); // expectant, heavy, enceinte, large, big, gravid, with child, great
		synsets.add("WN_Synset_103411"); // large, big
		synsets.add("WN_Synset_98639"); // different
		synsets.add("WN_Synset_109542"); // dependable, honest, true, reliable
		synsets.add("WN_Synset_100612"); //pure
		synsets.add("WN_Synset_104421"); // more
		synsets.add("WN_Synset_104405"); // some
		synsets.add("WN_Synset_105487"); // consummate, complete
		synsets.add("WN_Synset_108187"); // accomplished, complete
		synsets.add("WN_Synset_100022"); // serious
		synsets.add("WN_Synset_106178"); // forwand-moving, advancing, forward
		return synsets;
	}
	
	/**
	 * generates a list of string with synsets of verbs
	 * @return list of string with synsets
	 */
	private static List<String> getVerbSynsets()
	{
		List<String> synsets = new ArrayList<String>();
		synsets.add("WN_Synset_87046");	// get, drive, aim
		synsets.add("WN_Synset_91207");	// enjoy, love
		synsets.add("WN_Synset_91235");	// travel, locomote, move, go
		synsets.add("WN_Synset_95720");	// drive, ride
		synsets.add("WN_Synset_95135");	// come, follow
		synsets.add("WN_Synset_92639"); // experience, get, have, receive
		synsets.add("WN_Synset_92103"); // get, arrive, come
		synsets.add("WN_Synset_85082"); // experience, know, live
		synsets.add("WN_Synset_95080"); // be, live
		synsets.add("WN_Synset_90279");	// make, create
		synsets.add("WN_Synset_90391");	// make, fix, ready, cook, prepare
		synsets.add("WN_Synset_82797");	// get, go, become
		synsets.add("WN_Synset_91360");	// get going, go, start
		synsets.add("WN_Synset_95421");	// run, extend, pass, lead, go
		synsets.add("WN_Synset_85241");	// suppose, opine, think, imagine, guess, reckon
		synsets.add("WN_Synset_85498");	// believe, consider, think, conceive
		synsets.add("WN_Synset_90932"); // experience, feel
		synsets.add("WN_Synset_85261"); // build
		synsets.add("WN_Synset_90348"); // establish
		synsets.add("WN_Synset_95236"); // merit, deserve
		synsets.add("WN_Synset_95264"); // keep
		synsets.add("WN_Synset_85136"); // call up, think, recall, retrieve, remember, call back, recollect
		synsets.add("WN_Synset_87871"); // fulfill, fill, satisfy, fulfil, meet
		synsets.add("WN_Synset_91143"); // gratify, satisfy
		synsets.add("WN_Synset_87045"); // touch, advert
		synsets.add("WN_Synset_90928"); // stir, touch
		return synsets;
	}
	

}
