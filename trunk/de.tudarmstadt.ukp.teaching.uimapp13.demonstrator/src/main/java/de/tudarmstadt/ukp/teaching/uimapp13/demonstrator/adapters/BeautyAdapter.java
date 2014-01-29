package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class BeautyAdapter
    implements Adapter, Serializable
{
    private static final long serialVersionUID = -5583328894154375145L;

    PatternGenerator generator;
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String SUGGESTED_WORDS = "SUGGESTED_WORDS";
    public static final String BODY_PART = "BODY_PART";
    public static final String PATTERN = "PATTERN";
    public static final String SLOGAN_COUNT = "SLOGAN_COUNT";

    @Override
    public void initialize(final Map<String, Object> parameters)
        throws Exception
    {
        this.generator = new PatternGenerator();

        final DemonstratorConfig config = DemonstratorConfig.getInstance();

        this.generator.setWeb1TPathname(config.getWeb1TPathname());
        this.generator.setEmotionFilePath(DemonstratorConfig.getResourcePath()
                + "/NRCemotionlexicon.pdf");
        this.generator.setSloganBasePath(DemonstratorConfig.getResourcePath()
                + "/beautySlogans.txt");
        this.generator.setUbyDBData(config.getUbyUrl(), config.getJdbcDriver(),
                config.getJdbcDriverName(), config.getUbyUser(), config.getUbyPassword());

        this.generator.init();

    }

    @Override
    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
    {

        final String productName = (String) parameters.get(PRODUCT_NAME);
        final String suggestedWords = (String) parameters.get(SUGGESTED_WORDS);
        final String bodyPart = (String) parameters.get(BODY_PART);
        final String pattern = (String) parameters.get(PATTERN);
        final Integer sloganCount = (Integer) parameters.get(SLOGAN_COUNT);

        this.generator.setProductName(productName);

        this.generator.setSuggestedWords(suggestedWords);

        this.generator.selectPartOfBody(bodyPart);
        this.generator.selectPattern(pattern);

        // TODO generatePatterns should take the number of slogans to generate as parameters
        final List<String> generatedSlogans = this.generator.generateSlogans(sloganCount);

        final int returnedSloganCount = Math.min(generatedSlogans.size(), sloganCount);

        final List<Slogan> slogans = new ArrayList<Slogan>();

        for (int i = 0; i < returnedSloganCount; ++i) {
            final Slogan slogan = new Slogan(generatedSlogans.get(i));
            slogans.add(slogan);
        }

        return slogans;
    }

    @Override
    public ProductDomain getDomain()
    {
        return ProductDomain.BEAUTY;
    }
}