package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

public class TemplateGenerator {
	
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
		}
		return null;
	}

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
		return synsets;
	}
	
	private static List<String> getAdjectiveSynsets()
	{
		List<String> synsets = new ArrayList<String>();
		synsets.add("WN_Synset_102939"); // future, next, succeeding
		synsets.add("WN_Synset_100395"); // new, modern
		synsets.add("WN_Synset_101016"); // raw, new
		synsets.add("WN_Synset_105120"); // fresh, novel, new
		synsets.add("WN_Synset_96816"); // expectant, heavy, enceinte, large, big, gravid, with child, great
		synsets.add("WN_Synset_98639"); // different
		synsets.add("WN_Synset_109542"); // dependable, honest, true, reliable
		return synsets;
	}
	
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
		return synsets;
	}
	

}