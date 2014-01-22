package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import static de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig.CUSTOM_DB_PASSWORD;
import static de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig.CUSTOM_DB_USER;
import static de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig.KOCH_DB_URL;
import static de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig.UBY_PASSWORD;
import static de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig.UBY_USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class GamesAdapter
    implements Adapter
{

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
        final DbConfig ubyConfig = new DbConfig(DemonstratorConfig.UBY_URL, UBY_USER, UBY_PASSWORD);
        final DbConfig customDbConfig = new DbConfig(KOCH_DB_URL, CUSTOM_DB_USER,
                CUSTOM_DB_PASSWORD);

        final String gameName = (String) parameters.get(GAME_NAME);
        final Long randomSeed = (Long) parameters.get(RANDOM_SEED);
        final Integer sloganCount = (Integer) parameters.get(SLOGAN_COUNT);
        final Emotion emotion = (Emotion) parameters.get(EMOTION);

        final Map<Pattern, Double> patternWeights = this
                .parsePatternWeights((List<Double>) parameters.get(PATTERN_WEIGHTS));
        final Map<StylisticDevice, Double> styleDevWeights = this
                .parseStyleDevWeights((List<Double>) parameters.get(STYLISTIC_DEV_WEIGHTS));

        final Set<String> features = this.parseSetFromLines((String) parameters.get(FEATURES));
        final Set<String> alienFeatures = this.parseSetFromLines((String) parameters
                .get(ALIEN_FEATURES));

        final Integer minWordsForGeneration = (Integer) parameters
                .get(MIN_WORD_LIST_FOR_GENERATION);
        final Integer maxSynsetDepth = (Integer) parameters.get(MAX_SYNSET_DEPTH);
        
        final Integer maxWordListLength = (Integer) parameters.get(MAX_WORD_LIST_LENGTH);

        final Generator generator = new Generator(new Config(gameName, randomSeed, sloganCount,
                emotion, patternWeights, styleDevWeights, features, alienFeatures,
                minWordsForGeneration, maxSynsetDepth,maxWordListLength, ubyConfig, customDbConfig));

        LoggerFactory.getLogger(this.getClass()).info("Generating slogans...");
        final ArrayList<Slogan> slogans = new ArrayList<Slogan>();
        for (int i = 0; i < sloganCount; ++i) {
            LoggerFactory.getLogger(this.getClass()).info(
                    String.format("Slogan %d/%d...", i + 1, sloganCount));
            final Slogan slogan = new Slogan(generator.generateSlogan());
            slogans.add(slogan);
        }
        LoggerFactory.getLogger(this.getClass()).info("Generating slogans...Done");

        return slogans;
    }

    private Map<StylisticDevice, Double> parseStyleDevWeights(final List<Double> list)
    {
        final HashMap<StylisticDevice, Double> map = new HashMap<StylisticDevice, Double>();
        map.put(StylisticDevice.Alliteration, list.get(0));
        map.put(StylisticDevice.Metaphor, list.get(1));
        map.put(StylisticDevice.Oxymoron, list.get(2));
        map.put(StylisticDevice.Parallelism, list.get(3));
        map.put(StylisticDevice.None, list.get(4));
        return map;
    }

    private Map<Pattern, Double> parsePatternWeights(final List<Double> list)
    {
        final HashMap<Pattern, Double> map = new HashMap<Pattern, Double>();
        map.put(Pattern.JJNN, list.get(0));
        map.put(Pattern.JJNNJJNN, list.get(1));
        map.put(Pattern.VBVBVB, list.get(2));
        map.put(Pattern.NNVVN, list.get(3));
        return map;
    }

    private Set<String> parseSetFromLines(final String string)
    {
        final String[] split = string.split("\n");
        return new HashSet<String>(Arrays.asList(split));
    }
}
