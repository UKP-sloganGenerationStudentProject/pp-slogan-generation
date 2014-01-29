package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Main
{

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
     *  Config-Objekt + Pfad zu Web1T �bergeben lassen
     *      
     * @param args
     * @throws UbyInvalidArgumentException 
     */

    private static SGConfig config;
    private static SloganGenerator generator;

    public static void main(final SGConfig sgc)
        throws UbyInvalidArgumentException
    {
        config = sgc;
        final List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
        templates.add(TemplateGenerator.getTemplateById(sgc.TemplateId));
        generator = new SloganGenerator(sgc.DBUrl, sgc.DBUser, sgc.DBPassword, sgc.EmotionPath);
        final GoodLuck goodLuck = new GoodLuck(sgc.DBUrl, sgc.DBUser, sgc.DBPassword);
        for (final SloganTemplate t : templates) {
            for (final String word : config.WordList) {
                for (final String synset : generator.getSynsetByWord(word)) {
                    t.addSynset(synset);
                }
            }
        }
        goodLuck.AddMoreVerbs(templates);
        generateSlogans(templates);

        // displaySloganTemplates(templates);
    }

    public static void displaySloganTemplates(final List<SloganTemplate> templates)
    {
        for (final SloganTemplate template : templates) {
            System.out.println(template.toString());
        }
    }

    public static List<Slogan> generateSlogans(final List<SloganTemplate> templates)
    {
        final List<Slogan> slogans = new ArrayList<Slogan>();
        for (final SloganTemplate template : templates) {
            slogans.addAll(generator.generateSlogans(template, config.SloganCount,
                    config.ProductName, config.UseProductNameCreative, config.Emotion));
        }
        for (final Slogan slogan : slogans) {
            System.out.println(slogan.toString());
        }
        return slogans;
    }

}
