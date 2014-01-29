package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.tobiasloeser.slogangenerator.SloganGenerator;
import de.tobiasloeser.slogangenerator.SloganTemplate;
import de.tobiasloeser.slogangenerator.TemplateGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class CarsAdapter
    implements Adapter
{
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String SLOGAN_COUNT = "SLOGAN_COUNT";
    public static final String EMOTION = "EMOTION";
    public static final String TEMPLATE = "TEMPLATE";
    public static final String GOOD_LUCK = "GOOD_LUCK";
    public static final String USE_PRODUCT_NAME_CREATIVELY = "USE_PRODUCT_NAME_CREATIVELY";
    public static final String SUGGESTED_WORDS = "SUGGESTED_WORDS";
    private SloganGenerator generator;

    public static class Emotion
    {
        public static List<String> getAllEmotions()
        {
            return Arrays.asList("positive", "negative", "anger", "anticipation", "disgust",
                    "fear", "joy", "sadness", "surprise", "trust");
        }
    }

    public static class Template
    {
        private final int id;
        private final String name;

        private Template(final int id, final String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getId()
        {
            return this.id;
        }

        public String getName()
        {
            return this.name;
        }

        public static List<Template> getAllTemplates()
        {
            return Arrays.asList(new Template(1, "NC with Alliteration"), //
                    new Template(2, "NC without Alliteration"), //
                    new Template(3, "VC NC without Alliteration"), //
                    new Template(4, "VC NC with Alliteration"), //
                    new Template(5, "NC VC NC without Alliteration"), //
                    new Template(6, "NC VC NC with Alliteration"), //
                    new Template(7, "NC with Oxymoron"), //
                    new Template(8, "?"));
        }
    }

    @Override
    public void initialize(final Map<String, Object> parameters)
    {
        final DemonstratorConfig config = DemonstratorConfig.getInstance();
        generator = new SloganGenerator(config.getUbyUrl(),
                config.getUbyUser(), config.getUbyPassword(), config.getEmotionPath());
    }

    @Override
    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
    {

        final List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
        final int templateId = ((Template) parameters.get(TEMPLATE)).getId();
        templates.add(TemplateGenerator.getTemplateById(templateId));

        return Arrays.asList(new Slogan("Testslogan for Cars"));
    }

}
