package de.koch.uim_project.generation;

import de.koch.uim_project.generation.exception.PatternNotInitializeableException;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.pattern.AbstractPattern;
import de.koch.uim_project.generation.pattern.PatternFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.koch.uim_project.main.Main;

import org.apache.log4j.Logger;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.database.UbyConnect;
import de.tudarmstadt.ukp.lmf.api.Uby;

/**
 * This class is the entry point to the generation package. {@link Generator}
 * manages slogan generation
 * 
 * @author Frerik Koch
 * 
 */
public class Generator {

	private Config config;
	private final Map<AbstractPattern, Double> patternsNormWeights = new HashMap<AbstractPattern, Double>();
	private Logger log = Logger.getRootLogger();
	private static Random rnd;
	private static BaseWordListGen globalWordListGen;
	private JdbcConnect customDb;
	private Uby uby;
	private static Generator instance;

	public static Generator getInstance(Config config) throws DbException {
		if (instance == null) {
			instance = new Generator(config);
			return instance;
		} else {
			if (config.equals(instance.config)) {
				return instance;
			} else {
				instance = new Generator(config);
			}
		}

		return instance;
	}

	/**
	 * Initializes a new {@link Generator} with the given {@link Config}
	 * Reuses an old {@link BaseWordListGen} if possible for performance reasons
	 * @param config
	 *            Config for the generation of this {@link Generator}
	 * @throws DbException
	 */
	private Generator(Config config) throws DbException {
		// Init fields
		this.config = config;
		this.customDb = new JdbcConnect(config.getCustomDbConfig());
		this.uby = UbyConnect.getUbyInstance(config.getUbyConfig());

		// Init random
		rnd = new Random(config.getRandomSeed());

		// Init new BaseWordListGen if necessary
		if (globalWordListGen == null || !globalWordListGen.generatesEqualList(config.getWordListGenConfig())) {
			globalWordListGen = new BaseWordListGen(config.getWordListGenConfig(), uby, customDb);
		}

		// Normalize pattern weights
		Map<Pattern, Double> normalizedPatternWeights = normalizeWeights(config.getPatternweights());
		for (Pattern pattern : Pattern.values()) {
			try {
				patternsNormWeights.put(PatternFactory.getInstance().createPattern(pattern, config, this), normalizedPatternWeights.get(pattern));
			} catch (PatternNotInitializeableException e) {
				log.error("Pattern not initializable: " + e.getPattern(), e);
				Main.writeToConsole("Pattern not initializable: " + e.getPattern());
			}
		}

	}

	/**
	 * Getter for global random generator.
	 * 
	 * @return
	 */
	public Random getRnd() {
		if (rnd == null) {
			rnd = new Random(config.getRandomSeed());
		}
		return rnd;
	}

	/**
	 * Generates one slogan. Pattern and {@link StylisticDevice} are chosen
	 * randomly according to given weights
	 * 
	 * @return Generated slogan
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	public String generateSlogan() throws DbException, SloganNotCreatedException {
		String result;
		AbstractPattern patternToGenerate;
		StylisticDevice sdToGenerate;

		patternToGenerate = choosePattern();
		sdToGenerate = chooseSD(patternToGenerate.getPossibleStylisticDevices());
		result = patternToGenerate.generateSlogan(sdToGenerate);
		Main.writeToConsole("Created: " + patternToGenerate + " With: " + sdToGenerate);

		return result;
	}

	/**
	 * This method chooses one of the available stylistic devices randomly
	 * 
	 * @param possibleStylisticDevices
	 *            Possible stylistic devices
	 * @return Chosen stylistic device
	 */
	private StylisticDevice chooseSD(StylisticDevice[] possibleStylisticDevices) {
		Double chooseSD = rnd.nextDouble();
		Map<StylisticDevice, Double> normalizedSD = new HashMap<StylisticDevice, Double>();
		for (StylisticDevice sd : possibleStylisticDevices) {
			normalizedSD.put(sd, config.getSdweights().get(sd));
		}
		normalizedSD = normalizeWeights(normalizedSD);
		Double count = 0.0;
		for (StylisticDevice sdNorm : normalizedSD.keySet()) {
			count += normalizedSD.get(sdNorm);
			if (count >= chooseSD) {
				return sdNorm;
			}
		}
		return possibleStylisticDevices[0];
	}

	/**
	 * This method chooeses a pattern randomly
	 * 
	 * @return Choosen pattern
	 */
	private AbstractPattern choosePattern() {
		Double choosenPattern = rnd.nextDouble();
		Double count = 0.0;
		for (AbstractPattern pattern : patternsNormWeights.keySet()) {
			count += patternsNormWeights.get(pattern);
			if (count >= choosenPattern) {
				return pattern;
			}
		}
		return patternsNormWeights.keySet().iterator().next(); // Shouldn't be
																// reached. If
																// so, the first
																// pattern is
																// returned.
	}

	/**
	 * This method is used to normalize weights so that the values sum up to 1.
	 * It works by multiplying each weight with 1/(sum of all weights). This
	 * results in the same weight distribution between 0 and 1.
	 * 
	 * @param toNormalize
	 *            Weights to normalize
	 * @return normalized weights
	 */
	public static <T> Map<T, Double> normalizeWeights(Map<T, Double> toNormalize) {
		Map<T, Double> result = new HashMap<T, Double>();
		Double sum = 0.0;
		for (T t : toNormalize.keySet()) {
			sum += toNormalize.get(t);
		}
		for (T t : toNormalize.keySet()) {
			result.put(t, (toNormalize.get(t) * (1 / sum)));
		}
		return result;
	}

	public BaseWordListGen getGlobalWordListGen() {
		return globalWordListGen;
	}

	public JdbcConnect getCustomDb() {
		return customDb;
	}

	public Uby getUby() {
		return uby;
	}

	/**
	 * Closes the DB connection. Should be called at the end of
	 * {@link Generator} usage.
	 */
	public void close() {
		customDb.closeConnection();
	}

}
