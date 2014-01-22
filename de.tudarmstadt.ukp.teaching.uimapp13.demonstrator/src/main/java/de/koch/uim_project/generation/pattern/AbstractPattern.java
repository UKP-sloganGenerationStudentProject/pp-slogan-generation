package de.koch.uim_project.generation.pattern;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;

/**
 * This class represents a pattern
 * 
 * @author Frerik Koch
 * 
 */
public abstract class AbstractPattern {

	/**
	 * {@link Config} for generation
	 */
	protected Config config;
	/**
	 * {@link Generator} generating this pattern
	 */
	protected Generator gen;
	
	/**
	 * @param config {@link Config} for generation
	 * @param gen {@link Generator} generating this pattern
	 */
	public AbstractPattern(Config config,Generator gen){
		this.config = config;
		this.gen = gen;
	}
	
	/**
	 * This method generates a slogan for this pattern
	 * 
	 * @return Generated slogan
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	public abstract String generateSlogan(StylisticDevice stylisticDevice) throws DbException,
			SloganNotCreatedException;

	/**
	 * Getter for possible stylistic devices
	 * 
	 * @return Stylistic devices possible for this pattern
	 */
	public abstract StylisticDevice[] getPossibleStylisticDevices();
	
	/**
	 * @return {@link Pattern} of this pattern
	 */
	public abstract Pattern getPatternType();

}
