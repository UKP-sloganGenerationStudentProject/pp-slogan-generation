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
public class Config {

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
	 * Uby config data
	 * ATTENTION: The uby url has to be different then for example an JDBC url, please see Uby documentation
	 */
	private DbConfig ubyConfig;
	/**
	 * JDBC config data for custom database
	 * ATTENTION: Url has to be a valid jdbc url, which is different from uby urls
	 */
	private DbConfig customDbConfig;
	
	public Config(String gameName, Long randomSeed, Integer sloganCount, Emotion emotion, Map<Pattern, Double> patternweights,
			Map<StylisticDevice, Double> sdweights, Set<String> featureList, Set<String> alienFeatureList, int minWordlistForGeneration,
			int maxSynsetDepth, DbConfig ubyConfig, DbConfig customDbConfig) {
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
		this.ubyConfig = ubyConfig;
		this.customDbConfig = customDbConfig;
	}

	public String getGameName() {
		return gameName;
	}

	public Long getRandomSeed() {
		return randomSeed;
	}

	public Integer getSloganCount() {
		return sloganCount;
	}

	public Emotion getEmotion() {
		return emotion;
	}

	public Map<Pattern, Double> getPatternweights() {
		return patternweights;
	}

	public Map<StylisticDevice, Double> getSdweights() {
		return sdweights;
	}

	public Set<String> getFeatureList() {
		return featureList;
	}

	public Set<String> getAlienFeatureList() {
		return alienFeatureList;
	}

	public int getMinWordlistForGeneration() {
		return minWordlistForGeneration;
	}

	public int getMaxSynsetDepth() {
		return maxSynsetDepth;
	}

	public DbConfig getUbyConfig() {
		return ubyConfig;
	}

	public DbConfig getCustomDbConfig() {
		return customDbConfig;
	}

	public static Config getDefaultConfig() {
		DbConfig config = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
		DbConfig ubyConfig = new DbConfig(Constants.DATABASE.UBY.DEFAULT_UBY_URL, Constants.DATABASE.UBY.DEFAULT_UBY_LOGIN, Constants.DATABASE.UBY.DEFAULT_UBY_PASS);
		
		String name = "No Name";
		long randomSeed = System.currentTimeMillis();
		int sloganCount = 20;
		Emotion emotion = Emotion.POSITIVE;
		
		
		Map<Pattern,Double> patternWeights = new HashMap<Pattern,Double>();
		for(Pattern pattern : Pattern.values()){
			patternWeights.put(pattern, 1.0);
		}
		
		Map<StylisticDevice,Double> sdWeights = new HashMap<StylisticDevice,Double>();
		for(StylisticDevice sd : StylisticDevice.values()){
			sdWeights.put(sd, 1.0);
		}
		
		int minWordlistForGeneration = 10;
		int maxSynsetDepth = 4;
		 
		Set<String> featureList = getDefaultFeatureList();
		Set<String> alienFeatureList = getDefaultAlienFeatureList();
		
		return new Config(name, randomSeed, sloganCount, emotion, patternWeights, sdWeights, featureList, alienFeatureList, minWordlistForGeneration, maxSynsetDepth, ubyConfig, config);
	}

	private static Set<String> getDefaultAlienFeatureList() {
		Set<String> result = new HashSet<String>();
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

	private static Set<String> getDefaultFeatureList() {
		Set<String> result = new HashSet<String>();
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
