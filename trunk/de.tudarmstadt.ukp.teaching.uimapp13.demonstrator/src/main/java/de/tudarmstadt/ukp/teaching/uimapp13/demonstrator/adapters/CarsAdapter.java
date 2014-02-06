package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

import de.tobiasloeser.slogangenerator.GoodLuck;
import de.tobiasloeser.slogangenerator.SloganGenerator;
import de.tobiasloeser.slogangenerator.SloganTemplate;
import de.tobiasloeser.slogangenerator.TemplateGenerator;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class CarsAdapter
    implements Adapter, Serializable
{
    private static final long serialVersionUID = -2271555874935381594L;

    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String SLOGAN_COUNT = "SLOGAN_COUNT";
    public static final String EMOTION = "EMOTION";
    public static final String TEMPLATE_ID = "TEMPLATE";
    public static final String GOOD_LUCK = "GOOD_LUCK";
    public static final String USE_PRODUCT_NAME_CREATIVELY = "USE_PRODUCT_NAME_CREATIVELY";
    public static final String SUGGESTED_WORDS = "SUGGESTED_WORDS";
    private SloganGenerator generator;

    public static final boolean DEFAULT_GOOD_LUCK = false;

    public static final boolean DEFAULT_USE_PRODUCT_NAME_CREATIVELY = true;

    public static final String DEFAULT_EMOTION = getAllEmotions().get(0);

    public static final Template DEFAULT_TEMPLATE = getAllTemplates().get(0);

    public static final String DEFAULT_SUGGESTED_WORDS = Joiner.on(",").join(
            Arrays.asList("fun", "speed", "family"));

    public static final String DEFAULT_PRODUCT_NAME = "MyCar";

    public static final int DEFAULT_SLOGAN_COUNT = 20;

    public static class Template
        implements Serializable
    {
        private static final long serialVersionUID = -5798199994348706917L;

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

    }

    @Override
    public void initialize(final Map<String, Object> parameters)
    {
        final DemonstratorConfig config = DemonstratorConfig.getInstance();
        this.generator = new SloganGenerator(config.getUbyUrl(), config.getUbyUser(),
                config.getUbyPassword(), config.getEmotionPath(), config.getWeb1TPath());
    }

    @Override
    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
        throws UbyInvalidArgumentException
    {
        final DemonstratorConfig config = DemonstratorConfig.getInstance();

        final int templateId = (Integer) parameters.get(TEMPLATE_ID);
        final SloganTemplate template = TemplateGenerator.getTemplateById(templateId);
        final List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
        templates.add(template);

        final String suggestedWords = (String) parameters.get(SUGGESTED_WORDS);
        for (final String word : suggestedWords.split(",")) {
            for (final String synset : this.generator.getSynsetByWord(word)) {
                template.addSynset(synset);
            }
        }

        final GoodLuck goodLuck = new GoodLuck(config.getUbyUrl(), config.getUbyUser(),
                config.getUbyPassword());
        final boolean isGoodLuck = (boolean) parameters.get(GOOD_LUCK);
        if (isGoodLuck) {
            goodLuck.AddMoreVerbs(templates);
        }

        final Integer sloganCount = (int) parameters.get(SLOGAN_COUNT);
        final String productName = (String) parameters.get(PRODUCT_NAME);
        final Boolean useProductName = (Boolean) parameters.get(USE_PRODUCT_NAME_CREATIVELY);
        final String emotion = (String) parameters.get(EMOTION);
        final List<de.tobiasloeser.slogangenerator.Slogan> generatedSlogans = this.generator
                .generateSlogans(template, sloganCount, productName, useProductName, emotion);

        final List<Slogan> outputSlogans = new ArrayList<>();
        for (final de.tobiasloeser.slogangenerator.Slogan generatedSlogan : generatedSlogans) {
            outputSlogans.add(new Slogan(generatedSlogan.toString()));
        }

        return outputSlogans;
    }

    public static List<Template> getAllTemplates()
    {
        return Arrays.asList(//
                new Template(1, "NC with Alliteration"), //
                new Template(2, "NC without Alliteration"), //
                new Template(3, "VC NC without Alliteration"), //
                new Template(4, "VC NC with Alliteration"), //
                new Template(5, "NC VC NC without Alliteration"), //
                new Template(6, "NC VC NC with Alliteration"), //
                new Template(7, "NC with Oxymoron"), //
                new Template(8, "NC PC NC without Alliteration"), //
                new Template(9, "NC PC NC with Alliteration"));
    }

    public static Template getTemplateById(final int templateId)
    {
        for (final Template template : getAllTemplates()) {
            if (templateId == template.getId()) {
                return template;
            }
        }
        return null;
    }

    public static List<String> getAllEmotions()
    {
        return Arrays.asList("positive", "negative", "anger", "anticipation", "disgust", "fear",
                "joy", "sadness", "surprise", "trust");
    }

    @Override
    public ProductDomain getDomain()
    {
        return ProductDomain.CARS;
    }

}
