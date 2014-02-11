package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class GamesAdapter
    implements Adapter
{

    private static final long serialVersionUID = -8502811331633647842L;
    public static final String DEFAULT_GAME_NAME = "My Game";
    public static final int DEFAULT_NUM_SLOGANS = 40;

    public static final String EMOTION = "Emotion";
    public static final String GAME_NAME = "GameName";
    public static final String RANDOM_SEED = "RandomSeed";
    public static final String SLOGAN_COUNT = "SloganCount";

    public static final String MAX_SYNSET_DEPTH = "MaxSynsetDepth";
    public static final String MIN_WORD_LIST_FOR_GENERATION = "MinWordListForGeneration";
    public static final String FEATURES = "ProductFeatures";
    public static final String ALIEN_FEATURES = "AlienFeatures";
    public static final String STYLISTIC_DEV_WEIGHTS = "StylisticDeviceWeights";
    public static final String PATTERN_WEIGHTS = "PatternWeights";
    public static final String MAX_WORD_LIST_LENGTH = "MaxWordListLength";

    @Override
    public void initialize(final Map<String, Object> parameters)
    {

    }

    @Override
    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
        throws Exception
    {
        final DemonstratorConfig config = DemonstratorConfig.getInstance();
        final DbConfig ubyConfig = new DbConfig(config.getUbyUrl(), config.getUbyUser(),
                config.getUbyPassword());
        final DbConfig customDbConfig = new DbConfig(config.getKochDbUrl(),
                config.getKochDbUser(), config.getKochDbPassword());

        final String gameName = (String) parameters.get(GAME_NAME);
        final Long randomSeed = (Long) parameters.get(RANDOM_SEED);
        final Integer sloganCount = (Integer) parameters.get(SLOGAN_COUNT);
        final Emotion emotion = Emotion.valueOf((String) parameters.get(EMOTION));

        final Map<Pattern, Double> patternWeights = this.parsePatternWeights(parameters
                .get(PATTERN_WEIGHTS));
        final Map<StylisticDevice, Double> styleDevWeights = this.parseStyleDevWeights(parameters
                .get(STYLISTIC_DEV_WEIGHTS));

        final Set<String> features = this.parseSet((String) parameters.get(FEATURES));
        final Set<String> alienFeatures = this.parseSet((String) parameters
                .get(ALIEN_FEATURES));

        final Integer minWordsForGeneration = (Integer) parameters
                .get(MIN_WORD_LIST_FOR_GENERATION);
        final Integer maxSynsetDepth = (Integer) parameters.get(MAX_SYNSET_DEPTH);

        final Integer maxWordListLength = (Integer) parameters.get(MAX_WORD_LIST_LENGTH);

        final Generator generator = Generator
                .getInstance(new Config(gameName, randomSeed, sloganCount, emotion, patternWeights,
                        styleDevWeights, features, alienFeatures, minWordsForGeneration,
                        maxSynsetDepth, maxWordListLength, ubyConfig, customDbConfig));

        final Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Generating slogans...");
        final ArrayList<Slogan> slogans = new ArrayList<Slogan>();
        for (int i = 0; i < sloganCount; ++i) {
            logger.info(String.format("Slogan %d/%d...", i + 1, sloganCount));
            final Slogan slogan = new Slogan(generator.generateSlogan());
            slogans.add(slogan);
        }
        logger.info("Generating slogans...Done");

        return slogans;
    }

    private Map<StylisticDevice, Double> parseStyleDevWeights(final Object input)
    {
        @SuppressWarnings("unchecked")
        final List<Double> sdWeights = (List<Double>) input;

        final HashMap<StylisticDevice, Double> map = new HashMap<StylisticDevice, Double>();
        map.put(StylisticDevice.Alliteration, sdWeights.get(0));
        map.put(StylisticDevice.Metaphor, sdWeights.get(1));
        map.put(StylisticDevice.Oxymoron, sdWeights.get(2));
        map.put(StylisticDevice.Parallelism, sdWeights.get(3));
        map.put(StylisticDevice.None, sdWeights.get(4));
        return map;
    }

    private Map<Pattern, Double> parsePatternWeights(final Object input)
    {
        @SuppressWarnings("unchecked")
        final List<Double> patternWeights = (List<Double>) input;

        final HashMap<Pattern, Double> map = new HashMap<Pattern, Double>();
        map.put(Pattern.JJNN, patternWeights.get(0));
        map.put(Pattern.JJNNJJNN, patternWeights.get(1));
        map.put(Pattern.VBVBVB, patternWeights.get(2));
        map.put(Pattern.NNVVN, patternWeights.get(3));
        return map;
    }

    private Set<String> parseSet(final String string)
    {
        final String[] split = string.split(",");
        return new HashSet<String>(Arrays.asList(split));
    }

    @Override
    public ProductDomain getDomain()
    {
        return ProductDomain.GAMES;
    }

}
