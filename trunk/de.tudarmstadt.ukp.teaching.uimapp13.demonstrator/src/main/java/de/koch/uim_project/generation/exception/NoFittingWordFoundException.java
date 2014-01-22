package de.koch.uim_project.generation.exception;

import de.koch.uim_project.generation.RandomUtil;

/**
 * This exception is thrown when {@link RandomUtil} is unable to find a word.
 * This can have multiple reasons, in most cases for the actual feature words
 * and min,max parameters no valid word can be found
 * 
 * @author Frerik Koch
 * 
 */
public class NoFittingWordFoundException extends Exception {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 2864948220849390356L;

	public NoFittingWordFoundException(String msg) {
		super(msg);
	}

	public NoFittingWordFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
