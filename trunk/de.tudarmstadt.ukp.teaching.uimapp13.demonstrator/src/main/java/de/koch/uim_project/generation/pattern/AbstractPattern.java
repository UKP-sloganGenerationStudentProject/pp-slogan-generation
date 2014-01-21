package de.koch.uim_project.generation.pattern;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.exception.FilterException;
import de.koch.uim_project.generation.exception.NoFittingWordFoundException;
import de.koch.uim_project.generation.exception.PatternNotInitializeableException;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.database.DbException;

/**
 * This class represents a slogan pattern
 * 
 * @author Frerik Koch
 * 
 */
public abstract class AbstractPattern {

	protected Config config;
	protected Generator gen;
	
	public AbstractPattern(Config config,Generator gen){
		this.config = config;
		this.gen = gen;
	}
	
	/**
	 * This method generates a slogan for this pattern
	 * 
	 * @return Generated slogan
	 * @throws StylisticDeviceNotSupportedException
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 * @throws NoFittingWordFoundException 
	 * @throws PatternNotInitializeableException
	 * @throws FilterException
	 */
	public abstract String generateSlogan(StylisticDevice stylisticDevice) throws DbException,
			SloganNotCreatedException;

	/**
	 * Getter for possible stylistic devices
	 * 
	 * @return Stylistic devices possible for this pattern
	 */
	public abstract StylisticDevice[] getPossibleStylisticDevices();
	
	public abstract Pattern getPatternType();

}
