package de.koch.uim_project.generation.pattern;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.exception.PatternNotInitializeableException;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;

/**
 * {@link PatternFactory} allows convenient instantiation of {@link AbstractPattern} subclasses
 * It implements the factory pattern
 * @author Frerik Koch
 *
 */
public class PatternFactory {

	private static PatternFactory instance;

	private PatternFactory() {

	}

	public static PatternFactory getInstance() {
		if (instance == null) {
			instance = new PatternFactory();
		}
		return instance;
	}

	/**
	 * Instantiates a pattern with given parameters
	 * @param pattern {@link Pattern} to initialize
	 * @param config {@link Config} for generation
	 * @param gen {@link Generator} requesting an instantiation
	 * @return Instantiated subclass of {@link AbstractPattern}
	 * @throws DbException
	 * @throws PatternNotInitializeableException
	 */
	public AbstractPattern createPattern(Pattern pattern,Config config,Generator gen) throws DbException, PatternNotInitializeableException {
		switch (pattern) {
		case JJNN:
			return new PatternJJNN(config,gen);
		case JJNNJJNN:
			return new PatternJJNNJJNN(config,gen);
		case NNVVN:
			return new PatternNNVVN(config, gen);
		case VBVBVB:
			return new PatternVBVBVB(config,gen);
		default:
			throw new PatternNotInitializeableException("Pattern not recognized",null);
		}
	}
}
