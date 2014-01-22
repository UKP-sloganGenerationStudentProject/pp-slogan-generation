package de.koch.uim_project.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.koch.uim_project.generation.Generator;

/**
 * This class is a data container for config data. An instance of {@link Config} is needed for initializing {@link Generator}
 * @author Frerik Koch
 *
 */
public class Config
{

    /**
     * Name of the game to generated slogans for
     */
    private String gameName;
    /**
     * Random seed to use with random number generator
     */
    private Long randomSeed;
    /**
     * How much slogans should be generated(not used by {@link Generator})
     */
    private Integer sloganCount;
    /**
     * Emotion to filter words for
     */
    private Emotion emotion;
    /**
     * Randoms weights for pattern choosing
     */
    private Map<Pattern, Double> patternweights;
    /**
     * Stylistic device weights for stylistic device choosing
     */
    private Map<StylisticDevice, Double> sdweights;
    /**
     * Feature words for generation
     */
    private Set<String> featureList;
    /**
     * Alien feature words for metapher generation
     */
    private Set<String> alienFeatureList;
    /**
     * Minimal word list size from which words are choosen randomly. Low value leads to very similar slogans, high value might have lower semantic value or may not generate at all
     */
    private int minWordlistForGeneration;
    /**
     * Max synset depth is an indicator of how great the semantic distance of words for generation might be to the feature words.
     */
    private int maxSynsetDepth;
    
    /**
     * This limits the word generation to a given word maximum. 
     * This parameter can be used to limit execution time.
     */
    private int maxWordListLength;
    /**
     * Uby config data
     * ATTENTION: The uby url has to be different then for example an JDBC url, please see Uby documentation
     */
    private DbConfig ubyConfig;
    /**
     * JDBC config data for custom database
     * ATTENTION: Url has to be a valid jdbc url, which is different from uby urls
     */
    private DbConfig customDbConfig;

    public Config(final String gameName, final Long randomSeed, final Integer sloganCount,
            final Emotion emotion, final Map<Pattern, Double> patternweights,
            final Map<StylisticDevice, Double> sdweights, final Set<String> featureList,
            final Set<String> alienFeatureList, final int minWordlistForGeneration,
            final int maxSynsetDepth,final int maxWordListLength, final DbConfig ubyConfig, final DbConfig customDbConfig)
    {
        super();
        this.gameName = gameName;
        this.randomSeed = randomSeed;
        this.sloganCount = sloganCount;
        this.emotion = emotion;
        this.patternweights = patternweights;
        this.sdweights = sdweights;
        this.featureList = featureList;
        this.alienFeatureList = alienFeatureList;
        this.minWordlistForGeneration = minWordlistForGeneration;
        this.maxSynsetDepth = maxSynsetDepth;
        this.maxWordListLength = maxWordListLength;
        this.ubyConfig = ubyConfig;
        this.customDbConfig = customDbConfig;
    }

    public String getGameName()
    {
        return this.gameName;
    }

    public Long getRandomSeed()
    {
        return this.randomSeed;
    }

    public Integer getSloganCount()
    {
        return this.sloganCount;
    }

    public Emotion getEmotion()
    {
        return this.emotion;
    }

    public Map<Pattern, Double> getPatternweights()
    {
        return this.patternweights;
    }

    public Map<StylisticDevice, Double> getSdweights()
    {
        return this.sdweights;
    }

    public Set<String> getFeatureList()
    {
        return this.featureList;
    }

    public Set<String> getAlienFeatureList()
    {
        return this.alienFeatureList;
    }

    public int getMinWordlistForGeneration()
    {
        return this.minWordlistForGeneration;
    }

    public int getMaxSynsetDepth()
    {
        return this.maxSynsetDepth;
    }

    public int getMaxWordListLength() {
		return maxWordListLength;
	}

	public DbConfig getUbyConfig()
    {
        return this.ubyConfig;
    }

    public DbConfig getCustomDbConfig()
    {
        return this.customDbConfig;
    }

    public static Config getDefaultConfig()
    {
        final DbConfig config = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL,
                Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER,
                Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
        final DbConfig ubyConfig = new DbConfig(Constants.DATABASE.UBY.DEFAULT_UBY_URL,
                Constants.DATABASE.UBY.DEFAULT_UBY_LOGIN, Constants.DATABASE.UBY.DEFAULT_UBY_PASS);

        final String name = "No Name";
        final long randomSeed = System.currentTimeMillis();
        final int sloganCount = 20;
        final Emotion emotion = Emotion.POSITIVE;

        final Map<Pattern, Double> patternWeights = new HashMap<Pattern, Double>();
        for (final Pattern pattern : Pattern.values()) {
            patternWeights.put(pattern, 1.0);
        }

        final Map<StylisticDevice, Double> sdWeights = new HashMap<StylisticDevice, Double>();
        for (final StylisticDevice sd : StylisticDevice.values()) {
            sdWeights.put(sd, 1.0);
        }

        final int minWordlistForGeneration = 10;
        final int maxSynsetDepth = 4;
        final int maxWordsInBasicList = 1500;

        final Set<String> featureList = getDefaultFeatureList();
        final Set<String> alienFeatureList = getDefaultAlienFeatureList();

        return new Config(name, randomSeed, sloganCount, emotion, patternWeights, sdWeights,
                featureList, alienFeatureList, minWordlistForGeneration, maxSynsetDepth,maxWordsInBasicList, ubyConfig,
                config);
    }

    private static Set<String> getDefaultAlienFeatureList()
    {
        final Set<String> result = new HashSet<String>();
        result.add("life");
        result.add("epic");
        result.add("experience");
        result.add("achievement");
        result.add("legendary");
        result.add("might");
        result.add("new");
        result.add("spare time");
        result.add("challenge");
        result.add("dream");
        result.add("entertaining");
        result.add("imagination");
        result.add("smart");
        result.add("fantasy");
        result.add("violent");
        result.add("socializing");
        result.add("creepy");
        result.add("trendy");
        result.add("fascinating");
        result.add("exciting");
        return result;
    }

    private static Set<String> getDefaultFeatureList()
    {
        final Set<String> result = new HashSet<String>();
        result.add("battle");
        result.add("build");
        result.add("conquer");
        result.add("create");
        result.add("discover");
        result.add("empire");
        result.add("fast");
        result.add("fight");
        result.add("fun");
        result.add("funny");
        result.add("future");
        result.add("historic");
        result.add("immersion");
        result.add("interactive");
        result.add("kingdom");
        result.add("magic");
        result.add("real");
        result.add("realistic");
        result.add("shoot");
        result.add("simmulation");
        result.add("strategic");
        result.add("strategy");
        result.add("tactic");
        result.add("tactical");
        result.add("world");
        return result;
    }

}
