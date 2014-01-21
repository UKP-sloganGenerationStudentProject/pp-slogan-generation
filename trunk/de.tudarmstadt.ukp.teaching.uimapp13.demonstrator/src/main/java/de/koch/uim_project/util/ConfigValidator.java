package de.koch.uim_project.util;

/**
 * This class is to validate a {@link Config} object.
 * @author Frerik Koch
 *
 */
public class ConfigValidator {

	private static ConfigValidator instance = null;
	
	private ConfigValidator(){
		
	}
	
	public static ConfigValidator getInstance(){
		if(instance == null){
			instance = new ConfigValidator();
		}
		return instance;
	}
	
	public boolean validate(Config config){
		return true; //TODO Implement!
	}
}
