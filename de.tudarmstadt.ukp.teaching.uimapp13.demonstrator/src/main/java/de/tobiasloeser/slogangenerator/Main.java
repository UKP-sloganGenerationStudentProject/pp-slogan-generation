package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Main
{
    private static SGConfig config;
    private static SloganGenerator generator;

    public static List<Slogan> main(final SGConfig sgc)
        throws UbyInvalidArgumentException
    {
        config = sgc;
        final List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
        templates.add(TemplateGenerator.getTemplateById(sgc.TemplateId));
        generator = new SloganGenerator(sgc.DBUrl, sgc.DBUser, sgc.DBPassword, sgc.EmotionPath, sgc.Web1TPath);
        
        for (final SloganTemplate t : templates) {
            for (final String word : config.WordList) {
                for (final String synset : generator.getSynsetByWord(word)) {
                    t.addSynset(synset);
                }
            }
        }
        
        final GoodLuck goodLuck = new GoodLuck(sgc.DBUrl, sgc.DBUser, sgc.DBPassword);
        if(sgc.GoodLuck)
        	goodLuck.AddMoreVerbs(templates);
        return generateSlogans(templates);

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
