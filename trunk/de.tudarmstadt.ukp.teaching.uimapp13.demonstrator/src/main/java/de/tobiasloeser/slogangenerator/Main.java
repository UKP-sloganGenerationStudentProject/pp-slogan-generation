package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Main
{
    /**
     * possible start point for the slogan generation
     * @param sgc config object for the slogan generator
     * @return a list of slogans
     * @throws UbyInvalidArgumentException
     */
    public static List<Slogan> main(final SGConfig sgc)
        throws UbyInvalidArgumentException
    {
        final List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
        templates.add(TemplateGenerator.getTemplateById(sgc.TemplateId));
        SloganGenerator generator = new SloganGenerator(sgc);
        
        final List<Slogan> slogans = generator.generateSlogans(sgc);
        for (final Slogan slogan : slogans) {
            System.out.println(slogan.toString());
        }
        return slogans;
    }
}
