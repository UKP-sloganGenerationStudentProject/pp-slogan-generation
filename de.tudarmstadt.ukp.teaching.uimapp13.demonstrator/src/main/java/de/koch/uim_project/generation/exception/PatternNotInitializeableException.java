package de.koch.uim_project.generation.exception;

import de.koch.uim_project.util.Pattern;

/**
 * Thrown if a pattern is not initializable. This will mostly happen because of
 * configuration witch leads to a very small basic word list
 * 
 * @author Frerik Koch
 * 
 */
public class PatternNotInitializeableException extends Exception {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 1920606114394906967L;

	private Pattern pattern;

	/**
	 * @param msg
	 *            Message for exception
	 */
	public PatternNotInitializeableException(String msg, Pattern pattern) {
		super(msg);
	}

	/**
	 * @param msg
	 *            Message for exception
	 * @param cause
	 *            Cause for exception
	 */
	public PatternNotInitializeableException(String msg, Pattern pattern, Throwable cause) {
		super(msg, cause);
	}

	public Pattern getPattern() {
		return pattern;
	}

}
