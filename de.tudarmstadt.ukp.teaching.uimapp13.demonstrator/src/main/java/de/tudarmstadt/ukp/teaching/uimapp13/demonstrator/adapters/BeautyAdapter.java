package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class BeautyAdapter
    implements Adapter
{
    PatternGenerator generator;
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String SUGGESTED_WORDS = "SUGGESTED_WORDS";
    public static final String BODY_PART = "BODY_PART";
    public static final String PATTERN = "PATTERN";
    public static final String SLOGAN_COUNT = "SLOGAN_COUNT";

    public void initialize(final Map<String, Object> parameters) throws Exception
    {
        generator = new PatternGenerator();

        generator.setWeb1TPathname(System.getenv("DKPRO_HOME") + "/web1t/ENGLISH");
        generator
                .setEmotionFilePath("/s21/studium/11_semester/master_thesis/workspace/de.tudarmstadt.ukp.teaching.uimapp13.demonstrator/src/main/resources/NRCemotionlexicon.pdf");
        generator
                .setSloganBasePath("/s21/studium/11_semester/master_thesis/workspace/de.tudarmstadt.ukp.teaching.uimapp13.demonstrator/src/main/resources/beautySlogans.txt");
        generator.setUbyDBData(DemonstratorConfig.UBY_URL, DemonstratorConfig.JDBC_DRIVER,
                DemonstratorConfig.JDBC_DRIVER_NAME, DemonstratorConfig.UBY_USER,
                DemonstratorConfig.UBY_PASSWORD);


     generator.init();

    }

    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
    {

        final String productName = (String) parameters.get(PRODUCT_NAME);
        final String suggestedWords = (String) parameters.get(SUGGESTED_WORDS);
        final String bodyPart = (String) parameters.get(BODY_PART);
        final String pattern = (String) parameters.get(PATTERN);
        final Integer sloganCount = (Integer) parameters.get(SLOGAN_COUNT);

        generator.setProductName(productName);

        generator.setSuggestedWords(suggestedWords);

        generator.selectPartOfBody(bodyPart);
        generator.selectPattern(pattern);

        //TODO generatePatterns should take the number of slog to generate as parameters
        List<String> generatedSlogans = generator.generatePatterns();

        final int returnedSloganCount = Math.min(generatedSlogans.size(), sloganCount);

        List<Slogan> slogans = new ArrayList<Slogan>();

        for (int i = 0; i < returnedSloganCount; ++i)
        {
            final Slogan slogan = new Slogan(generatedSlogans.get(i));
            slogans.add(slogan);
        }

        return slogans;
    }
}
