package de.koch.uim_project.generation.pattern;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.exception.PatternNotInitializeableException;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;

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
